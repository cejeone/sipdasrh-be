package com.kehutanan.pepdas.program.service.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

import com.kehutanan.pepdas.program.dto.ProgramDTO;
import com.kehutanan.pepdas.program.dto.ProgramPageDTO;
import com.kehutanan.pepdas.program.model.Program;
import com.kehutanan.pepdas.program.repository.ProgramRepository;
import com.kehutanan.pepdas.program.service.ProgramService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProgramServiceImpl implements ProgramService {
    
    private final ProgramRepository repository;
    
    @Autowired
    public ProgramServiceImpl(ProgramRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Program> findAll() {
        return repository.findAll();
    }

    @Override
    public ProgramDTO findDTOById(Long id) {
        Program program = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Program not found with id: " + id));
        
        return new ProgramDTO(program);
    }

    @Override
    public Program findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Program not found with id: " + id));
    }

    @Override
    @Transactional
    public Program save(Program program) {
        return repository.save(program);
    }

    @Override
    @Transactional
    public Program update(Long id, Program program) {
        // Ensure the entity exists
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Program not found with id: " + id);
        }
        
        return repository.save(program);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Check if exists
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Program not found with id: " + id);
        }
        
        repository.deleteById(id);
    }

    @Override
    public ProgramPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<Program> page = repository.findAll(pageable);
        
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
        
        return new ProgramPageDTO(page, pageMetadata, links);
    }

    @Override
    public ProgramPageDTO findByFiltersWithCache(String nama, String anggaran, List<String> bpdas, 
            Pageable pageable, String baseUrl) {
        
        // Create specification based on filters
        Specification<Program> spec = Specification.where(null);
        
        // Add case-insensitive LIKE filter for nama if provided
        if (nama != null && !nama.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nama")),
                    "%" + nama.toLowerCase() + "%"));
        }
        
        // Add filter for anggaran if provided
        if (anggaran != null && !anggaran.isEmpty()) {
            try {
                Double anggaranValue = Double.parseDouble(anggaran);
                spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                        root.get("totalAnggaran"), anggaranValue));
            } catch (NumberFormatException e) {
                // Handle case where anggaran is not a valid number
                // Could log this, or ignore silently
            }
        }
        
        // Add filter for bpdas if provided
        if (bpdas != null && !bpdas.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("eselon1").get("id").in(bpdas));
        }
        
        // Execute query with filters
        Page<Program> page = repository.findAll(spec, pageable);
        
        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());
        
        // Create links for pagination
        List<Link> links = new ArrayList<>();
        
        // Base URL with filters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        
        // Add all filter parameters to URL
        if (nama != null && !nama.isEmpty()) {
            builder.queryParam("nama", nama);
        }
        if (anggaran != null && !anggaran.isEmpty()) {
            builder.queryParam("anggaran", anggaran);
        }
        if (bpdas != null && !bpdas.isEmpty()) {
            for (String bpdasId : bpdas) {
                builder.queryParam("bpdas", bpdasId);
            }
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
        
        return new ProgramPageDTO(page, pageMetadata, links);
    }

    @Override
    public ProgramPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl) {
        Specification<Program> spec = Specification.where(null);
        
        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("nama")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("kategori").get("nama")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("eselon1").get("nama")), searchPattern)
            ));
        }
        
        Page<Program> page = repository.findAll(spec, pageable);
        
        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());
        
        // Create links for pagination
        List<Link> links = new ArrayList<>();
        
        // Base URL with search parameter
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        
        // Add search keyword parameter to URL
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
        
        return new ProgramPageDTO(page, pageMetadata, links);
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