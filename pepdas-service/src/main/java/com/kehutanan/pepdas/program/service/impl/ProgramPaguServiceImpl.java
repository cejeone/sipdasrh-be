package com.kehutanan.pepdas.program.service.impl;

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

import com.kehutanan.pepdas.program.dto.ProgramPaguDTO;
import com.kehutanan.pepdas.program.dto.ProgramPaguPageDTO;
import com.kehutanan.pepdas.program.model.ProgramPagu;
import com.kehutanan.pepdas.program.repository.ProgramPaguRepository;
import com.kehutanan.pepdas.program.service.ProgramPaguService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProgramPaguServiceImpl implements ProgramPaguService {
    
    private final ProgramPaguRepository repository;
    
    @Autowired
    public ProgramPaguServiceImpl(ProgramPaguRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProgramPagu> findAll() {
        return repository.findAll();
    }

    @Override
    public ProgramPaguDTO findDTOById(Long id) {
        ProgramPagu programPagu = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Program Pagu not found with id: " + id));
        
        return new ProgramPaguDTO(programPagu);
    }

    @Override
    public ProgramPagu findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Program Pagu not found with id: " + id));
    }

    @Override
    @Transactional
    public ProgramPagu save(ProgramPagu programPagu) {
        return repository.save(programPagu);
    }

    @Override
    @Transactional
    public ProgramPagu update(Long id, ProgramPagu programPagu) {
        // Ensure the entity exists
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Program Pagu not found with id: " + id);
        }
        
        return repository.save(programPagu);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Check if exists
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Program Pagu not found with id: " + id);
        }
        
        repository.deleteById(id);
    }

    @Override
    public ProgramPaguPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<ProgramPagu> page = repository.findAll(pageable);
        
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
        
        return new ProgramPaguPageDTO(page, pageMetadata, links);
    }

    @Override
    public ProgramPaguPageDTO findByFiltersWithCache(Long programId, String kategori, String sumberAnggaran, 
            List<String> bpdas, Pageable pageable, String baseUrl) {
        
        // Create specification based on filters
        Specification<ProgramPagu> spec = Specification.where(null);
        
        // Add filter by programId if provided
        if (programId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("program").get("id"), programId));
        }
        
        // Add case-insensitive LIKE filter for kategori if provided
        if (kategori != null && !kategori.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("kategori").get("nama")),
                    "%" + kategori.toLowerCase() + "%"));
        }
        
        // Add case-insensitive LIKE filter for sumberAnggaran if provided
        if (sumberAnggaran != null && !sumberAnggaran.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("sumberAnggaran").get("nama")),
                    "%" + sumberAnggaran.toLowerCase() + "%"));
        }
        
        // Add filter for bpdas if provided - assuming program links to eselon1 which has bpdas
        if (bpdas != null && !bpdas.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("program").get("eselon1").get("id").in(bpdas));
        }
        
        // Execute query with filters
        Page<ProgramPagu> page = repository.findAll(spec, pageable);
        
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
        if (programId != null) {
            builder.queryParam("programId", programId);
        }
        if (kategori != null && !kategori.isEmpty()) {
            builder.queryParam("kategori", kategori);
        }
        if (sumberAnggaran != null && !sumberAnggaran.isEmpty()) {
            builder.queryParam("sumberAnggaran", sumberAnggaran);
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
        
        return new ProgramPaguPageDTO(page, pageMetadata, links);
    }

    @Override
    public ProgramPaguPageDTO searchWithCache(Long programId, String keyWord, Pageable pageable, String baseUrl) {
        Specification<ProgramPagu> spec = Specification.where(null);
        
        // Add filter by programId if provided
        if (programId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("program").get("id"), programId));
        }
        
        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("kategori").get("nama")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("sumberAnggaran").get("nama")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("status").get("nama")), searchPattern)
            ));
        }
        
        Page<ProgramPagu> page = repository.findAll(spec, pageable);
        
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
        
        // Add program ID if provided
        if (programId != null) {
            builder.queryParam("programId", programId);
        }
        
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
        
        return new ProgramPaguPageDTO(page, pageMetadata, links);
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