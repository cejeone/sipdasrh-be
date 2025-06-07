package com.kehutanan.tktrh.bkta.kegiatan.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.kehutanan.tktrh.bkta.kegiatan.dto.KegiatanMonevKriteriaPageDTO;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanMonev;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanMonevKriteria;
import com.kehutanan.tktrh.bkta.kegiatan.model.dto.KegiatanMonevKriteriaDTO;
import com.kehutanan.tktrh.bkta.kegiatan.repository.KegiatanMonevKriteriaRepository;
import com.kehutanan.tktrh.bkta.kegiatan.repository.KegiatanMonevRepository;
import com.kehutanan.tktrh.bkta.kegiatan.service.KegiatanMonevKriteriaService;

import jakarta.persistence.EntityNotFoundException;

@Service("bktaKegiatanMonevKriteriaServiceImpl")
public class KegiatanMonevKriteriaServiceImpl implements KegiatanMonevKriteriaService {
    
    private final KegiatanMonevKriteriaRepository repository;
    private final KegiatanMonevRepository kegiatanMonevRepository;
    
    @Autowired
    public KegiatanMonevKriteriaServiceImpl(KegiatanMonevKriteriaRepository repository, 
                                           KegiatanMonevRepository kegiatanMonevRepository) {
        this.repository = repository;
        this.kegiatanMonevRepository = kegiatanMonevRepository;
    }

    @Override
    public List<KegiatanMonevKriteria> findAll() {
        return repository.findAll();
    }
    
    @Override
    public KegiatanMonevKriteriaDTO findDTOById(Long id) {
        KegiatanMonevKriteria kriteria = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanMonevKriteria not found with id: " + id));
                
        return new KegiatanMonevKriteriaDTO(kriteria);
    }

    @Override
    public KegiatanMonevKriteria findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanMonevKriteria not found with id: " + id));
    }
    
    @Override
    public KegiatanMonevKriteria save(KegiatanMonevKriteria kriteria) {
        return repository.save(kriteria);
    }
    
    @Override
    public KegiatanMonevKriteria update(Long id, KegiatanMonevKriteria kriteria) {
        // Ensure the entity exists
        findById(id);
        // Set the ID to ensure update and not insert
        kriteria.setId(id);
        return repository.save(kriteria);
    }
    
    @Override
    public void deleteById(Long id) {
        // Check if exists
        findById(id);
        repository.deleteById(id);
    }

    @Override
    public KegiatanMonevKriteriaPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<KegiatanMonevKriteria> page = repository.findAll(pageable);
        
        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());
                
        // Create links for pagination
        List<Link> links = createPaginationLinks(baseUrl, page);
        
        return new KegiatanMonevKriteriaPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanMonevKriteriaPageDTO findByFiltersWithCache(Long kegiatanMonevId, String aktivitas, 
            String target, List<String> realisasi, Pageable pageable, String baseUrl) {
        
        Specification<KegiatanMonevKriteria> spec = Specification.where(null);
        
        // Add filter for kegiatanMonevId if provided
        if (kegiatanMonevId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("monitoringEvaluasi").get("id"), kegiatanMonevId));
        }
        
        // Add case-insensitive LIKE filter for aktivitas if provided
        if (aktivitas != null && !aktivitas.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("aktivitas")), 
                    "%" + aktivitas.toLowerCase() + "%"));
        }
        
        // Add case-insensitive LIKE filter for target if provided
        if (target != null && !target.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("target")), 
                    "%" + target.toLowerCase() + "%"));
        }
        
        // Execute query with filters
        Page<KegiatanMonevKriteria> page = repository.findAll(spec, pageable);
        
        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());
                
        // Create base URL with filters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        
        // Add filter parameters to URL
        if (kegiatanMonevId != null) {
            builder.queryParam("kegiatanMonevId", kegiatanMonevId);
        }
        if (aktivitas != null && !aktivitas.isEmpty()) {
            builder.queryParam("aktivitas", aktivitas);
        }
        if (target != null && !target.isEmpty()) {
            builder.queryParam("target", target);
        }
        if (realisasi != null && !realisasi.isEmpty()) {
            for (String r : realisasi) {
                builder.queryParam("realisasi", r);
            }
        }
        
        String filterBaseUrl = builder.build().toUriString();
        
        // Create links for pagination with filters
        List<Link> links = createFilterPaginationLinks(filterBaseUrl, page);
        
        return new KegiatanMonevKriteriaPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanMonevKriteriaPageDTO searchWithCache(Long kegiatanMonevId, String keyWord, 
            Pageable pageable, String baseUrl) {
        
        Specification<KegiatanMonevKriteria> spec = Specification.where(null);
        
        // Add filter for kegiatanMonevId if provided
        if (kegiatanMonevId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("monitoringEvaluasi").get("id"), kegiatanMonevId));
        }
        
        // Add search term if provided
        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("aktivitas")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("target")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("realisasi")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("catatan")), searchPattern)
            ));
        }
        
        // Execute search query
        Page<KegiatanMonevKriteria> page = repository.findAll(spec, pageable);
        
        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());
                
        // Create base URL with search params
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        
        // Add search parameters
        if (kegiatanMonevId != null) {
            builder.queryParam("kegiatanMonevId", kegiatanMonevId);
        }
        if (keyWord != null && !keyWord.isEmpty()) {
            builder.queryParam("keyWord", keyWord);
        }
        
        String searchBaseUrl = builder.build().toUriString();
        
        // Create links for pagination with search
        List<Link> links = createFilterPaginationLinks(searchBaseUrl, page);
        
        return new KegiatanMonevKriteriaPageDTO(page, pageMetadata, links);
    }
    
    // Helper method to create pagination links
    private List<Link> createPaginationLinks(String baseUrl, Page<?> page) {
        List<Link> links = new ArrayList<>();
        
        // Self link
        links.add(Link.of(createPageUrl(baseUrl, page.getNumber(), page.getSize()), "self"));
        
        // First page link
        links.add(Link.of(createPageUrl(baseUrl, 0, page.getSize()), "first"));
        
        // Previous page link (if not first page)
        if (page.getNumber() > 0) {
            links.add(Link.of(createPageUrl(baseUrl, page.getNumber() - 1, page.getSize()), "prev"));
        }
        
        // Next page link (if not last page)
        if (page.getNumber() < page.getTotalPages() - 1) {
            links.add(Link.of(createPageUrl(baseUrl, page.getNumber() + 1, page.getSize()), "next"));
        }
        
        // Last page link
        if (page.getTotalPages() > 0) {
            links.add(Link.of(createPageUrl(baseUrl, page.getTotalPages() - 1, page.getSize()), "last"));
        }
        
        return links;
    }
    
    // Helper method to create pagination links with filters
    private List<Link> createFilterPaginationLinks(String filterBaseUrl, Page<?> page) {
        List<Link> links = new ArrayList<>();
        
        // Self link
        links.add(Link.of(createFilterPageUrl(filterBaseUrl, page.getNumber(), page.getSize()), "self"));
        
        // First page link
        links.add(Link.of(createFilterPageUrl(filterBaseUrl, 0, page.getSize()), "first"));
        
        // Previous page link (if not first page)
        if (page.getNumber() > 0) {
            links.add(Link.of(createFilterPageUrl(filterBaseUrl, page.getNumber() - 1, page.getSize()), "prev"));
        }
        
        // Next page link (if not last page)
        if (page.getNumber() < page.getTotalPages() - 1) {
            links.add(Link.of(createFilterPageUrl(filterBaseUrl, page.getNumber() + 1, page.getSize()), "next"));
        }
        
        // Last page link
        if (page.getTotalPages() > 0) {
            links.add(Link.of(createFilterPageUrl(filterBaseUrl, page.getTotalPages() - 1, page.getSize()), "last"));
        }
        
        return links;
    }
    
    private String createPageUrl(String baseUrl, int page, int size) {
        return UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("page", page)
                .queryParam("size", size)
                .build()
                .toUriString();
    }
    
    private String createFilterPageUrl(String filterBaseUrl, int page, int size) {
        // Check if the URL already has query parameters
        String connector = filterBaseUrl.contains("?") ? "&" : "?";
        
        return filterBaseUrl + connector + "page=" + page + "&size=" + size;
    }
}