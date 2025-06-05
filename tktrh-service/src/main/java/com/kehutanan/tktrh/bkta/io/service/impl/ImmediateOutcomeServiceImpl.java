package com.kehutanan.tktrh.bkta.io.service.impl;

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

import com.kehutanan.tktrh.bkta.io.dto.ImmediateOutcomePageDTO;
import com.kehutanan.tktrh.bkta.io.model.ImmediateOutcome;
import com.kehutanan.tktrh.bkta.io.model.dto.ImmediateOutcomeDTO;
import com.kehutanan.tktrh.bkta.io.repository.ImmediateOutcomeRepository;
import com.kehutanan.tktrh.bkta.io.service.ImmediateOutcomeService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ImmediateOutcomeServiceImpl implements ImmediateOutcomeService {

    private final ImmediateOutcomeRepository repository;

    @Autowired
    public ImmediateOutcomeServiceImpl(ImmediateOutcomeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ImmediateOutcome> findAll() {
        return repository.findAll();
    }

    @Override
    public ImmediateOutcomeDTO findDTOById(Long id) {
        ImmediateOutcome immediateOutcome = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ImmediateOutcome not found with id: " + id));

        ImmediateOutcomeDTO immediateOutcomeDTO = new ImmediateOutcomeDTO(immediateOutcome);
        return immediateOutcomeDTO;
    }

    @Override
    public ImmediateOutcome findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ImmediateOutcome not found with id: " + id));
    }

    @Override
    public ImmediateOutcome save(ImmediateOutcome immediateOutcome) {
        return repository.save(immediateOutcome);
    }

    @Override
    public ImmediateOutcome update(Long id, ImmediateOutcome immediateOutcome) {
        // Ensure entity exists
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("ImmediateOutcome not found with id: " + id);
        }
        return repository.save(immediateOutcome);
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("ImmediateOutcome not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public ImmediateOutcomePageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<ImmediateOutcome> page = repository.findAll(pageable);
        
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

        return new ImmediateOutcomePageDTO(page, pageMetadata, links);
    }

    @Override
    public ImmediateOutcomePageDTO findByFiltersWithCache(String kegiatan, Integer tahun, List<String> bpdas,
            Pageable pageable, String baseUrl) {
        
        // Create specification based on filters
        Specification<ImmediateOutcome> spec = Specification.where(null);

        // Add case-insensitive LIKE filter for namaKegiatan if provided
        if (kegiatan != null && !kegiatan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaKegiatan")),
                    "%" + kegiatan.toLowerCase() + "%"));
        }

        // Add filter for tahunKegiatan if provided
        if (tahun != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("tahunKegiatan"), tahun));
        }

        // Add filter for BPDAS if provided
        if (bpdas != null && !bpdas.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("bpdas").get("id").in(bpdas));
        }

        // Execute query with filters
        Page<ImmediateOutcome> page = repository.findAll(spec, pageable);

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
        if (kegiatan != null && !kegiatan.isEmpty()) {
            builder.queryParam("kegiatan", kegiatan);
        }
        if (tahun != null) {
            builder.queryParam("tahun", tahun);
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

        return new ImmediateOutcomePageDTO(page, pageMetadata, links);
    }

    @Override
    public ImmediateOutcomePageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl) {
        Specification<ImmediateOutcome> spec = Specification.where(null);

        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("namaKegiatan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("dta")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("bpdas").get("name")), searchPattern)));
        }

        Page<ImmediateOutcome> page = repository.findAll(spec, pageable);

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

        return new ImmediateOutcomePageDTO(page, pageMetadata, links);
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
