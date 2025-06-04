package com.kehutanan.rh.program.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.kehutanan.rh.program.dto.ProgramSkemaPageDTO;
import com.kehutanan.rh.program.model.ProgramSkema;
import com.kehutanan.rh.program.model.dto.ProgramSkemaDTO;
import com.kehutanan.rh.program.repository.ProgramSkemaRepository;
import com.kehutanan.rh.program.service.ProgramSkemaService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProgramSkemaServiceImpl implements ProgramSkemaService {
    
    private final ProgramSkemaRepository repository;
    
    @Autowired
    public ProgramSkemaServiceImpl(ProgramSkemaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProgramSkema> findAll() {
        return repository.findAll();
    }

    @Override
    public ProgramSkemaDTO findDTOById(Long id) {
        ProgramSkema programSkema = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProgramSkema not found with id: " + id));

        return new ProgramSkemaDTO(programSkema);
    }

    @Override
    public ProgramSkema findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProgramSkema not found with id: " + id));
    }

    @Override
    public ProgramSkema save(ProgramSkema programSkema) {
        return repository.save(programSkema);
    }

    @Override
    public ProgramSkema update(Long id, ProgramSkema programSkema) {
        return repository.save(programSkema);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public ProgramSkemaPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<ProgramSkema> page = repository.findAll(pageable);

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

        return new ProgramSkemaPageDTO(page, pageMetadata, links);
    }

    @Override
    public ProgramSkemaPageDTO findByFiltersWithCache(Long programId, String nama, List<String> kategori,
            Pageable pageable, String baseUrl) {

        // Create specification based on filters
        Specification<ProgramSkema> spec = Specification.where(null);

        // Add filter for program_id if provided
        if (programId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("program").get("id"), programId));
        }

        // Add case-insensitive filter for skema if provided
        if (nama != null && !nama.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("skema").as(String.class)),
                    "%" + nama.toLowerCase() + "%"));
        }

        // Add filter for category if provided
        if (kategori != null && !kategori.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("kategoriId").get("id").in(kategori));
        }

        // Execute query with filters
        Page<ProgramSkema> page = repository.findAll(spec, pageable);

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
        if (nama != null && !nama.isEmpty()) {
            builder.queryParam("nama", nama);
        }
        if (kategori != null && !kategori.isEmpty()) {
            for (String kategoriId : kategori) {
                builder.queryParam("kategori", kategoriId);
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

        return new ProgramSkemaPageDTO(page, pageMetadata, links);
    }

    @Override
    public ProgramSkemaPageDTO searchWithCache(Long programId, String keyWord, Pageable pageable, String baseUrl) {
        Specification<ProgramSkema> spec = Specification.where(null);

        // Add filter for program_id if provided
        if (programId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("program").get("id"), programId));
        }

        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("skema").as(String.class)), searchPattern)));
        }

        Page<ProgramSkema> page = repository.findAll(spec, pageable);

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
        if (programId != null) {
            builder.queryParam("programId", programId);
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

        return new ProgramSkemaPageDTO(page, pageMetadata, links);
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