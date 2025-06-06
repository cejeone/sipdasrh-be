package com.kehutanan.tktrh.tmkh.kegiatan.service.impl;

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

import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanBastRehabDasPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanBastRehabDas;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanBastRehabDasDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.repository.KegiatanBastRehabDasRepository;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanBastRehabDasService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class KegiatanBastRehabDasServiceImpl implements KegiatanBastRehabDasService {
    
    private final KegiatanBastRehabDasRepository repository;

    @Autowired
    public KegiatanBastRehabDasServiceImpl(KegiatanBastRehabDasRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<KegiatanBastRehabDas> findAll() {
        return repository.findAll();
    }

    @Override
    public KegiatanBastRehabDasDTO findDTOById(Long id) {
        KegiatanBastRehabDas kegiatanBastRehabDas = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanBastRehabDas not found with id: " + id));

        KegiatanBastRehabDasDTO kegiatanBastRehabDasDTO = new KegiatanBastRehabDasDTO(kegiatanBastRehabDas);

        return kegiatanBastRehabDasDTO;
    }

    @Override
    public KegiatanBastRehabDas findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanBastRehabDas not found with id: " + id));
    }

    @Override
    @Transactional
    public KegiatanBastRehabDas save(KegiatanBastRehabDas kegiatanBastRehabDas) {
        return repository.save(kegiatanBastRehabDas);
    }

    @Override
    @Transactional
    public KegiatanBastRehabDas update(Long id, KegiatanBastRehabDas kegiatanBastRehabDas) {
        // Ensure the entity exists
        findById(id);
        
        // Set the ID to ensure we're updating the existing record
        kegiatanBastRehabDas.setId(id);
        
        return repository.save(kegiatanBastRehabDas);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Check if entity exists before deletion
        findById(id);
        repository.deleteById(id);
    }

    @Override
    public KegiatanBastRehabDasPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<KegiatanBastRehabDas> page = repository.findAll(pageable);

        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());

        // Create links for pagination
        List<Link> links = createPaginationLinks(baseUrl, page);

        return new KegiatanBastRehabDasPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanBastRehabDasPageDTO findByFiltersWithCache(Long kegiatanId, String keterangan,
            List<String> statusList, Pageable pageable, String baseUrl) {
        
        // Create specification based on filters
        Specification<KegiatanBastRehabDas> spec = Specification.where(null);

        // Add filter for kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("kegiatan").get("id"), kegiatanId));
        }

        // Add case-insensitive LIKE filter for keterangan if provided
        if (keterangan != null && !keterangan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("keterangan")),
                    "%" + keterangan.toLowerCase() + "%"));
        }

        // Add filter for statusList if provided
        if (statusList != null && !statusList.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                root.get("statusSerahTerima").get("id").in(statusList));
        }

        // Execute query with filters
        Page<KegiatanBastRehabDas> page = repository.findAll(spec, pageable);

        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());

        // Build URL with filters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);

        // Add all filter parameters to URL
        if (kegiatanId != null) {
            builder.queryParam("kegiatanId", kegiatanId);
        }
        if (keterangan != null && !keterangan.isEmpty()) {
            builder.queryParam("keterangan", keterangan);
        }
        if (statusList != null && !statusList.isEmpty()) {
            for (String status : statusList) {
                builder.queryParam("statusList", status);
            }
        }

        String filterBaseUrl = builder.build().toUriString();
        
        // Create links with filters
        List<Link> links = createFilterPaginationLinks(filterBaseUrl, page);

        return new KegiatanBastRehabDasPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanBastRehabDasPageDTO searchWithCache(Long kegiatanId, String keyWord, Pageable pageable, String baseUrl) {
        Specification<KegiatanBastRehabDas> spec = Specification.where(null);

        // Filter by kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("kegiatan").get("id"), kegiatanId));
        }

        // Search across multiple fields if keyword is provided
        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("statusSerahTerima").get("name")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.function("cast", String.class, root.get("targetLuas")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.function("cast", String.class, root.get("tahunId")), searchPattern)
            ));
        }

        Page<KegiatanBastRehabDas> page = repository.findAll(spec, pageable);

        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());

        // Build URL with search parameter
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);

        // Add search parameters to URL
        if (kegiatanId != null) {
            builder.queryParam("kegiatanId", kegiatanId);
        }
        if (keyWord != null && !keyWord.isEmpty()) {
            builder.queryParam("keyWord", keyWord);
        }

        String searchBaseUrl = builder.build().toUriString();

        // Create links with search parameters
        List<Link> links = createFilterPaginationLinks(searchBaseUrl, page);

        return new KegiatanBastRehabDasPageDTO(page, pageMetadata, links);
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
    
    // Helper method for filtered pagination links
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

    // Helper method to create page URL
    private String createPageUrl(String baseUrl, int page, int size) {
        return UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("page", page)
                .queryParam("size", size)
                .build()
                .toUriString();
    }

    // Helper method to create filter page URL
    private String createFilterPageUrl(String filterBaseUrl, int page, int size) {
        // Check if the URL already has query parameters
        String connector = filterBaseUrl.contains("?") ? "&" : "?";

        return filterBaseUrl + connector + "page=" + page + "&size=" + size;
    }
}