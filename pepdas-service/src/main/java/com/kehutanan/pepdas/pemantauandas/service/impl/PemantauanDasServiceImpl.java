package com.kehutanan.pepdas.pemantauandas.service.impl;

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

import com.kehutanan.pepdas.pemantauandas.dto.PemantauanDasDTO;
import com.kehutanan.pepdas.pemantauandas.dto.PemantauanDasPageDTO;
import com.kehutanan.pepdas.pemantauandas.model.PemantauanDas;
import com.kehutanan.pepdas.pemantauandas.repository.PemantauanDasRepository;
import com.kehutanan.pepdas.pemantauandas.service.PemantauanDasService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PemantauanDasServiceImpl implements PemantauanDasService {
    private final PemantauanDasRepository repository;

    @Autowired
    public PemantauanDasServiceImpl(PemantauanDasRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PemantauanDas> findAll() {
        return repository.findAll();
    }

    @Override
    public PemantauanDasDTO findDTOById(Long id) {
        PemantauanDas pemantauanDas = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PemantauanDas not found with id: " + id));

        return new PemantauanDasDTO(pemantauanDas);
    }

    @Override
    public PemantauanDas findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PemantauanDas not found with id: " + id));
    }

    @Override
    @Transactional
    public PemantauanDas save(PemantauanDas pemantauanDas) {
        return repository.save(pemantauanDas);
    }

    @Override
    @Transactional
    public PemantauanDas update(Long id, PemantauanDas pemantauanDas) {
        // Ensure the entity exists
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("PemantauanDas not found with id: " + id);
        }
        
        // Set the ID to make sure we're updating the right entity
        pemantauanDas.setId(id);
        return repository.save(pemantauanDas);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("PemantauanDas not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public PemantauanDasPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<PemantauanDas> page = repository.findAll(pageable);

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

        return new PemantauanDasPageDTO(page, pageMetadata, links);
    }

    @Override
    public PemantauanDasPageDTO findByFiltersWithCache(String das, String spasId, List<String> bpdas,
            Pageable pageable, String baseUrl) {

        // Create specification based on filters
        Specification<PemantauanDas> spec = Specification.where(null);

        // Add LIKE filter for das name if provided
        if (das != null && !das.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("das").get("namaDas")),
                    "%" + das.toLowerCase() + "%"));
        }

        // Add exact match for spas ID if provided
        if (spasId != null && !spasId.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("spas").get("id"), Long.parseLong(spasId)));
        }

        // Add filter for bpdas list if provided
        if (bpdas != null && !bpdas.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("bpdas").get("id").in(bpdas));
        }

        // Execute query with filters
        Page<PemantauanDas> page = repository.findAll(spec, pageable);

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
        if (das != null && !das.isEmpty()) {
            builder.queryParam("das", das);
        }
        if (spasId != null && !spasId.isEmpty()) {
            builder.queryParam("spasId", spasId);
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

        return new PemantauanDasPageDTO(page, pageMetadata, links);
    }

    @Override
    public PemantauanDasPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl) {
        Specification<PemantauanDas> spec = Specification.where(null);

        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("bpdas").get("namaBpdas")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("das").get("namaDas")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("spas").get("namaSpas")), searchPattern)));
        }

        Page<PemantauanDas> page = repository.findAll(spec, pageable);

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

        return new PemantauanDasPageDTO(page, pageMetadata, links);
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
