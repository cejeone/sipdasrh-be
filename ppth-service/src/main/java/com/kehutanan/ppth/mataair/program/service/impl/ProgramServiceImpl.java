package com.kehutanan.ppth.mataair.program.service.impl;

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

import com.kehutanan.ppth.master.model.Lov;
import com.kehutanan.ppth.mataair.program.model.dto.ProgramDTO;
import com.kehutanan.ppth.mataair.program.dto.ProgramPageDTO;
import com.kehutanan.ppth.mataair.program.model.Program;
import com.kehutanan.ppth.mataair.program.repository.ProgramRepository;
import com.kehutanan.ppth.mataair.program.service.ProgramService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

@Service("penghijauanProgramService")
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
    @Cacheable(value = "rh_programCache", key = "#id")
    public ProgramDTO findDTOById(Long id) {
        Program program = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Program not found with id: " + id));

        ProgramDTO programDTO = new ProgramDTO(program);

        return programDTO;
    }

    @Override
    public Program findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Program not found with id: " + id));
    }

    @Override
    @CacheEvict(value = { "rh_programCache", "rh_programPageCache", "rh_programFilterCache",
            "rh_programSearchCache" }, allEntries = true, beforeInvocation = true)
    public Program save(Program program) {
        return repository.save(program);
    }

    @Override
    @CachePut(value = "rh_programCache", key = "#id")
    public Program update(Long id, Program program) {
        return repository.save(program);
    }

    @Override
    @CacheEvict(value = { "rh_programCache", "rh_programPageCache", "rh_programFilterCache",
            "rh_programSearchCache" }, allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Cacheable(value = "rh_programPageCache", key = "#pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
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
    @Cacheable(value = "rh_programFilterCache", key = "{#nama, #eselon, #pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    public ProgramPageDTO findByFiltersWithCache(String nama, List<String> eselon,
            Pageable pageable, String baseUrl) {

        // Create specification based on filters
        Specification<Program> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for nama if provided
        if (nama != null && !nama.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nama")),
                    "%" + nama.toLowerCase() + "%"));
        }

        if (eselon != null && !eselon.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                // Use the path expression directly without join
                return root.get("eselon2").get("nama").in(eselon);
            });
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
        if (eselon != null && !eselon.isEmpty()) {
            for (String esl : eselon) {
                builder.queryParam("eselon", esl);
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
    @Cacheable(value = "rh_programSearchCache", key = "{#keyWord, #pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    public ProgramPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl) {
        Specification<Program> spec = Specification.where(null);

        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nama")), searchPattern));
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

    @Override
    public Page<Program> findAll(Pageable pageable) {
       return repository.findAll(pageable);
    }
}