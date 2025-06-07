package com.kehutanan.tktrh.ppkh.program.service.impl;

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

import com.kehutanan.tktrh.ppkh.program.dto.ProgramPaguAnggaranPageDTO;
import com.kehutanan.tktrh.ppkh.program.model.ProgramPaguAnggaran;
import com.kehutanan.tktrh.ppkh.program.model.dto.ProgramPaguAnggaranDTO;
import com.kehutanan.tktrh.ppkh.program.repository.ProgramPaguAnggaranRepository;
import com.kehutanan.tktrh.ppkh.program.service.ProgramPaguAnggaranService;

import jakarta.persistence.EntityNotFoundException;

@Service("ppkhProgramPaguAnggaranServiceImpl")
public class ProgramPaguAnggaranServiceImpl implements ProgramPaguAnggaranService {
    
    private final ProgramPaguAnggaranRepository repository;

    @Autowired
    public ProgramPaguAnggaranServiceImpl(ProgramPaguAnggaranRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProgramPaguAnggaran> findAll() {
        return repository.findAll();
    }

    @Override
    public ProgramPaguAnggaranDTO findDTOById(Long id) {
        ProgramPaguAnggaran programPaguAnggaran = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Program Pagu Anggaran not found with id: " + id));
        return new ProgramPaguAnggaranDTO(programPaguAnggaran);
    }

    @Override
    public ProgramPaguAnggaran findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Program Pagu Anggaran not found with id: " + id));
    }

    @Override
    @Transactional
    public ProgramPaguAnggaran save(ProgramPaguAnggaran programPaguAnggaran) {
        return repository.save(programPaguAnggaran);
    }

    @Override
    @Transactional
    public ProgramPaguAnggaran update(Long id, ProgramPaguAnggaran programPaguAnggaran) {
        // Ensure the entity exists
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Program Pagu Anggaran not found with id: " + id);
        }
        // Set the ID explicitly to ensure we're updating the right entity
        programPaguAnggaran.setId(id);
        return repository.save(programPaguAnggaran);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Check if entity exists before attempting to delete
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Program Pagu Anggaran not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public ProgramPaguAnggaranPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<ProgramPaguAnggaran> page = repository.findAll(pageable);
        
        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());
        
        // Create links for pagination
        List<Link> links = createPaginationLinks(baseUrl, page);
        
        return new ProgramPaguAnggaranPageDTO(page, pageMetadata, links);
    }

    @Override
    public ProgramPaguAnggaranPageDTO findByFiltersWithCache(
            Long programId, 
            String sumberAnggaran, 
            String keterangan, 
            List<String> status, 
            Pageable pageable, 
            String baseUrl) {
        
        // Create specification based on filters
        Specification<ProgramPaguAnggaran> spec = Specification.where(null);
        
        // Add filter for programId if provided
        if (programId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("program").get("id"), programId));
        }
        
        // Add case-insensitive LIKE filter for sumberAnggaran if provided
        if (sumberAnggaran != null && !sumberAnggaran.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("sumberAnggaran").get("name")), 
                    "%" + sumberAnggaran.toLowerCase() + "%"));
        }
        
        // Add case-insensitive LIKE filter for keterangan if provided
        if (keterangan != null && !keterangan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("keterangan")), 
                    "%" + keterangan.toLowerCase() + "%"));
        }
        
        // Add filter for status if provided
        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                root.get("status").get("code").in(status));
        }
        
        // Execute query with filters
        Page<ProgramPaguAnggaran> page = repository.findAll(spec, pageable);
        
        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());
        
        // Build base URL with filters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        
        // Add filter parameters to URL
        if (programId != null) {
            builder.queryParam("programId", programId);
        }
        if (sumberAnggaran != null && !sumberAnggaran.isEmpty()) {
            builder.queryParam("sumberAnggaran", sumberAnggaran);
        }
        if (keterangan != null && !keterangan.isEmpty()) {
            builder.queryParam("keterangan", keterangan);
        }
        if (status != null && !status.isEmpty()) {
            for (String statusCode : status) {
                builder.queryParam("status", statusCode);
            }
        }
        
        String filterBaseUrl = builder.build().toUriString();
        
        // Create links for pagination
        List<Link> links = createFilterPageLinks(filterBaseUrl, page);
        
        return new ProgramPaguAnggaranPageDTO(page, pageMetadata, links);
    }
    
    @Override
    public ProgramPaguAnggaranPageDTO searchWithCache(
            Long programId,
            String keyWord, 
            Pageable pageable, 
            String baseUrl) {
        
        Specification<ProgramPaguAnggaran> spec = Specification.where(null);
        
        // Add filter for programId if provided
        if (programId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("program").get("id"), programId));
        }
        
        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("sumberAnggaran").get("name")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("status").get("name")), searchPattern)
            ));
        }
        
        Page<ProgramPaguAnggaran> page = repository.findAll(spec, pageable);
        
        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());
        
        // Base URL with search parameter
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        
        // Add search parameters to URL
        if (programId != null) {
            builder.queryParam("programId", programId);
        }
        if (keyWord != null && !keyWord.isEmpty()) {
            builder.queryParam("keyWord", keyWord);
        }
        
        String searchBaseUrl = builder.build().toUriString();
        
        // Create links for pagination
        List<Link> links = createFilterPageLinks(searchBaseUrl, page);
        
        return new ProgramPaguAnggaranPageDTO(page, pageMetadata, links);
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