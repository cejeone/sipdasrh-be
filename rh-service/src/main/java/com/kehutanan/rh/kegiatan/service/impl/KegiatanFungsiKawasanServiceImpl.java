package com.kehutanan.rh.kegiatan.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.kehutanan.rh.kegiatan.dto.KegiatanFungsiKawasanDTO;
import com.kehutanan.rh.kegiatan.dto.KegiatanFungsiKawasanPageDTO;
import com.kehutanan.rh.kegiatan.model.KegiatanFungsiKawasan;
import com.kehutanan.rh.kegiatan.repository.KegiatanFungsiKawasanRepository;
import com.kehutanan.rh.kegiatan.service.KegiatanFungsiKawasanService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class KegiatanFungsiKawasanServiceImpl implements KegiatanFungsiKawasanService {
    private final KegiatanFungsiKawasanRepository repository;

    @Autowired
    public KegiatanFungsiKawasanServiceImpl(KegiatanFungsiKawasanRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<KegiatanFungsiKawasan> findAll() {
        return repository.findAll();
    }

    @Override
    public KegiatanFungsiKawasanDTO findDTOById(Long id) {
        KegiatanFungsiKawasan kegiatanFungsiKawasan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanFungsiKawasan not found with id: " + id));
        return new KegiatanFungsiKawasanDTO(kegiatanFungsiKawasan);
    }

    @Override
    public KegiatanFungsiKawasan findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanFungsiKawasan not found with id: " + id));
    }

    @Override
    @Transactional
    public KegiatanFungsiKawasan save(KegiatanFungsiKawasan kegiatanFungsiKawasan) {
        return repository.save(kegiatanFungsiKawasan);
    }

    @Override
    @Transactional
    public KegiatanFungsiKawasan update(Long id, KegiatanFungsiKawasan kegiatanFungsiKawasan) {
        // Verify entity exists
        findById(id);
        return repository.save(kegiatanFungsiKawasan);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Verify entity exists
        findById(id);
        repository.deleteById(id);
    }

    @Override
    public KegiatanFungsiKawasanPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<KegiatanFungsiKawasan> page = repository.findAll(pageable);

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

        return new KegiatanFungsiKawasanPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanFungsiKawasanPageDTO findByFiltersWithCache(
            Long kegiatanId,
            String keterangan,
            List<String> fungsiKawasan,
            Pageable pageable,
            String baseUrl) {

        // Create specification based on filters
        Specification<KegiatanFungsiKawasan> spec = Specification.where(null);

        // Add filter for kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("kegiatan").get("id"), kegiatanId));
        }

        // Add case-insensitive LIKE filter for keterangan if provided
        if (keterangan != null && !keterangan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")),
                    "%" + keterangan.toLowerCase() + "%"));
        }

        // Add filter for fungsiKawasan if provided
        if (fungsiKawasan != null && !fungsiKawasan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                root.get("fungsiKawasan").in(fungsiKawasan));
        }

        // Execute query with filters
        Page<KegiatanFungsiKawasan> page = repository.findAll(spec, pageable);

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
        if (kegiatanId != null) {
            builder.queryParam("kegiatanId", kegiatanId);
        }
        if (keterangan != null && !keterangan.isEmpty()) {
            builder.queryParam("keterangan", keterangan);
        }
        if (fungsiKawasan != null && !fungsiKawasan.isEmpty()) {
            for (String fk : fungsiKawasan) {
                builder.queryParam("fungsiKawasan", fk);
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

        return new KegiatanFungsiKawasanPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanFungsiKawasanPageDTO searchWithCache(
            Long kegiatanId,
            String keyWord,
            Pageable pageable,
            String baseUrl) {
        
        Specification<KegiatanFungsiKawasan> spec = Specification.where(null);

        // Add filter for kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("kegiatan").get("id"), kegiatanId));
        }

        // Add search condition if keyword is provided
        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("fungsiKawasan")), searchPattern)
                )
            );
        }

        Page<KegiatanFungsiKawasan> page = repository.findAll(spec, pageable);

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

        if (kegiatanId != null) {
            builder.queryParam("kegiatanId", kegiatanId);
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

        return new KegiatanFungsiKawasanPageDTO(page, pageMetadata, links);
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