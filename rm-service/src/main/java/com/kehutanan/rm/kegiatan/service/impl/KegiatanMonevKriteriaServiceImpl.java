package com.kehutanan.rm.kegiatan.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.kehutanan.rm.kegiatan.dto.KegiatanMonevKriteriaPageDTO;
import com.kehutanan.rm.kegiatan.model.KegiatanMonevKriteria;
import com.kehutanan.rm.kegiatan.model.dto.KegiatanMonevKriteriaDTO;
import com.kehutanan.rm.kegiatan.repository.KegiatanMonevKriteriaRepository;
import com.kehutanan.rm.kegiatan.service.KegiatanMonevKriteriaService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class KegiatanMonevKriteriaServiceImpl implements KegiatanMonevKriteriaService {
    
    private final KegiatanMonevKriteriaRepository repository;
    
    @Autowired
    public KegiatanMonevKriteriaServiceImpl(KegiatanMonevKriteriaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<KegiatanMonevKriteria> findAll() {
        return repository.findAll();
    }
    
    @Override
    public KegiatanMonevKriteriaDTO findDTOById(Long id) {
        KegiatanMonevKriteria kegiatanMonevKriteria = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanMonevKriteria not found with id: " + id));
                
        return new KegiatanMonevKriteriaDTO(kegiatanMonevKriteria);
    }

    @Override
    public KegiatanMonevKriteria findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanMonevKriteria not found with id: " + id));
    }
    
    @Override
    public KegiatanMonevKriteria save(KegiatanMonevKriteria kegiatanMonevKriteria) {
        return repository.save(kegiatanMonevKriteria);
    }
    
    @Override
    @Transactional
    public KegiatanMonevKriteria update(Long id, KegiatanMonevKriteria kegiatanMonevKriteria) {
        // Make sure the entity exists
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("KegiatanMonevKriteria not found with id: " + id);
        }
        
        // Ensure the ID is set correctly
        kegiatanMonevKriteria.setId(id);
        
        return repository.save(kegiatanMonevKriteria);
    }
    
    @Override
    @Transactional
    public void deleteById(Long id) {
        // Check if entity exists
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("KegiatanMonevKriteria not found with id: " + id);
        }
        
        repository.deleteById(id);
    }
    
    @Override
    public List<KegiatanMonevKriteria> findByKegiatanMonevId(Long kegiatanMonevId) {
        return repository.findByKegiatanMonevId(kegiatanMonevId);
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
        
        return new KegiatanMonevKriteriaPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanMonevKriteriaPageDTO findByFiltersWithCache(
            Long kegiatanMonevId, String namaAktivitas, Pageable pageable, String baseUrl) {
        
        // Create specification based on filters
        Specification<KegiatanMonevKriteria> spec = Specification.where(null);
        
        if (kegiatanMonevId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("kegiatanMonev").get("id"), kegiatanMonevId));
        }
        
        if (namaAktivitas != null && !namaAktivitas.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("aktivitasId").get("nama")), 
                    "%" + namaAktivitas.toLowerCase() + "%"));
        }
        
        // Execute query with filters
        Page<KegiatanMonevKriteria> page = repository.findAll(spec, pageable);
        
        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());
        
        // Create links for pagination with filters
        List<Link> links = new ArrayList<>();
        
        // Base URL with filters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        
        // Add all filter parameters to URL
        if (kegiatanMonevId != null) {
            builder.queryParam("kegiatanMonevId", kegiatanMonevId);
        }
        
        if (namaAktivitas != null && !namaAktivitas.isEmpty()) {
            builder.queryParam("namaAktivitas", namaAktivitas);
        }
        
        String filterBaseUrl = builder.build().toUriString();
        
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
        
        return new KegiatanMonevKriteriaPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanMonevKriteriaPageDTO searchWithCache(Long kegiatanMonevId, String keyWord, Pageable pageable, String baseUrl) {
        Specification<KegiatanMonevKriteria> spec = Specification.where(null);
        
        // Filter by kegiatanMonevId if provided
        if (kegiatanMonevId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("kegiatanMonev").get("id"), kegiatanMonevId));
        }
        
        // Add search criteria if keyword is provided
        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("aktivitasId").get("nama")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("catatan")), searchPattern)
            ));
        }
        
        // Execute the search query
        Page<KegiatanMonevKriteria> page = repository.findAll(spec, pageable);
        
        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());
        
        // Create links for pagination
        List<Link> links = new ArrayList<>();
        
        // Base URL with search parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        
        // Add search keyword parameter to URL
        if (kegiatanMonevId != null) {
            builder.queryParam("kegiatanMonevId", kegiatanMonevId);
        }
        
        if (keyWord != null && !keyWord.isEmpty()) {
            builder.queryParam("keyWord", keyWord);
        }
        
        String searchBaseUrl = builder.build().toUriString();
        
        // Self link
        links.add(Link.of(createFilterPageUrl(searchBaseUrl, page.getNumber(), page.getSize()), "self"));
        
        // First page link
        links.add(Link.of(createFilterPageUrl(searchBaseUrl, 0, page.getSize()), "first"));
        
        // Previous page link (if not first page)
        if (page.getNumber() > 0) {
            links.add(Link.of(createFilterPageUrl(searchBaseUrl, page.getNumber() - 1, page.getSize()), "prev"));
        }
        
        // Next page link (if not last page)
        if (page.getNumber() < page.getTotalPages() - 1) {
            links.add(Link.of(createFilterPageUrl(searchBaseUrl, page.getNumber() + 1, page.getSize()), "next"));
        }
        
        // Last page link
        if (page.getTotalPages() > 0) {
            links.add(Link.of(createFilterPageUrl(searchBaseUrl, page.getTotalPages() - 1, page.getSize()), "last"));
        }
        
        return new KegiatanMonevKriteriaPageDTO(page, pageMetadata, links);
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