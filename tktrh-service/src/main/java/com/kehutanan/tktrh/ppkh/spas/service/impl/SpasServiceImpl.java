package com.kehutanan.tktrh.ppkh.spas.service.impl;

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

import com.kehutanan.tktrh.ppkh.spas.dto.SpasPageDTO;
import com.kehutanan.tktrh.ppkh.spas.model.Spas;
import com.kehutanan.tktrh.ppkh.spas.model.dto.SpasDTO;
import com.kehutanan.tktrh.ppkh.spas.repository.SpasRepository;
import com.kehutanan.tktrh.ppkh.spas.service.SpasService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SpasServiceImpl implements SpasService {
    
    private final SpasRepository repository;

    @Autowired
    public SpasServiceImpl(SpasRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Spas> findAll() {
        return repository.findAll();
    }

    @Override
    public SpasDTO findDTOById(Long id) {
        Spas spas = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SPAS not found with id: " + id));
        return new SpasDTO(spas);
    }

    @Override
    public Spas findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SPAS not found with id: " + id));
    }

    @Override
    @Transactional
    public Spas save(Spas spas) {
        return repository.save(spas);
    }

    @Override
    @Transactional
    public Spas update(Long id, Spas spas) {
        // Ensure the entity exists
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("SPAS not found with id: " + id);
        }
        // Set the ID explicitly to ensure we're updating the right entity
        spas.setId(id);
        return repository.save(spas);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Check if entity exists before attempting to delete
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("SPAS not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public SpasPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<Spas> page = repository.findAll(pageable);
        
        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());
        
        // Create links for pagination
        List<Link> links = createPaginationLinks(baseUrl, page);
        
        return new SpasPageDTO(page, pageMetadata, links);
    }

    @Override
    public SpasPageDTO findByFiltersWithCache(
            String spasId, 
            String namaDas, 
            List<String> bpdasList, 
            Pageable pageable, 
            String baseUrl) {
        
        // Create specification based on filters
        Specification<Spas> spec = Specification.where(null);
        
        // Add filter for SPAS ID if provided
        if (spasId != null && !spasId.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("spasId").get("name")), 
                    "%" + spasId.toLowerCase() + "%"));
        }
        
        // Add filter for DAS name if provided
        if (namaDas != null && !namaDas.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("das").get("namaDas")), 
                    "%" + namaDas.toLowerCase() + "%"));
        }
        
        // Add filter for BPDAS list if provided
        if (bpdasList != null && !bpdasList.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                root.get("bpdas").get("id").in(bpdasList));
        }
        
        // Execute query with filters
        Page<Spas> page = repository.findAll(spec, pageable);
        
        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());
        
        // Build base URL with filters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        
        // Add filter parameters to URL
        if (spasId != null && !spasId.isEmpty()) {
            builder.queryParam("spasId", spasId);
        }
        if (namaDas != null && !namaDas.isEmpty()) {
            builder.queryParam("namaDas", namaDas);
        }
        if (bpdasList != null && !bpdasList.isEmpty()) {
            for (String bpdasId : bpdasList) {
                builder.queryParam("bpdasList", bpdasId);
            }
        }
        
        String filterBaseUrl = builder.build().toUriString();
        
        // Create links for pagination
        List<Link> links = createFilterPageLinks(filterBaseUrl, page);
        
        return new SpasPageDTO(page, pageMetadata, links);
    }
    
    @Override
    public SpasPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl) {
        Specification<Spas> spec = Specification.where(null);
        
        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("spasId").get("name")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("das").get("namaDas")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("bpdas").get("namaBpdas")), searchPattern)
            ));
        }
        
        Page<Spas> page = repository.findAll(spec, pageable);
        
        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());
        
        // Base URL with search parameter
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        
        // Add search keyword parameter to URL
        if (keyWord != null && !keyWord.isEmpty()) {
            builder.queryParam("keyWord", keyWord);
        }
        
        String searchBaseUrl = builder.build().toUriString();
        
        // Create links for pagination
        List<Link> links = createFilterPageLinks(searchBaseUrl, page);
        
        return new SpasPageDTO(page, pageMetadata, links);
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
    
    // Helper method to create filter pagination links
    private List<Link> createFilterPageLinks(String filterBaseUrl, Page<?> page) {
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