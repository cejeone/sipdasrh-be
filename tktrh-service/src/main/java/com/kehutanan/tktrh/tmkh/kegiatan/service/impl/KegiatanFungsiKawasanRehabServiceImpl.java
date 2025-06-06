package com.kehutanan.tktrh.tmkh.kegiatan.service.impl;

import java.io.UnsupportedEncodingException;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanFungsiKawasanRehabPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanFungsiKawasanRehab;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanFungsiKawasanRehabDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.repository.KegiatanFungsiKawasanRehabRepository;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanFungsiKawasanRehabService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class KegiatanFungsiKawasanRehabServiceImpl implements KegiatanFungsiKawasanRehabService {

    private final KegiatanFungsiKawasanRehabRepository repository;

    @Autowired
    public KegiatanFungsiKawasanRehabServiceImpl(KegiatanFungsiKawasanRehabRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<KegiatanFungsiKawasanRehab> findAll() {
        return repository.findAll();
    }

    @Override
    public KegiatanFungsiKawasanRehabDTO findDTOById(Long id) {
        KegiatanFungsiKawasanRehab kegiatanFungsiKawasanRehab = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanFungsiKawasanRehab not found with id: " + id));

        return new KegiatanFungsiKawasanRehabDTO(kegiatanFungsiKawasanRehab);
    }

    @Override
    public KegiatanFungsiKawasanRehab findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanFungsiKawasanRehab not found with id: " + id));
    }

    @Override
    public KegiatanFungsiKawasanRehab save(KegiatanFungsiKawasanRehab kegiatanFungsiKawasanRehab) {
        return repository.save(kegiatanFungsiKawasanRehab);
    }

    @Override
    public KegiatanFungsiKawasanRehab update(Long id, KegiatanFungsiKawasanRehab kegiatanFungsiKawasanRehab) {
        // Verify the entity exists
        findById(id);
        return repository.save(kegiatanFungsiKawasanRehab);
    }

    @Override
    public void deleteById(Long id) {
        // Verify the entity exists
        findById(id);
        repository.deleteById(id);
    }

    @Override
    public KegiatanFungsiKawasanRehabPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<KegiatanFungsiKawasanRehab> page = repository.findAll(pageable);

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

        return new KegiatanFungsiKawasanRehabPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanFungsiKawasanRehabPageDTO findByFiltersWithCache(Long kegiatanId, String fungsiKawasan, String keterangan,
            List<String> statusList, Pageable pageable, String baseUrl) {

        // Create specification based on filters
        Specification<KegiatanFungsiKawasanRehab> spec = Specification.where(null);

        // Add filter for kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("kegiatan").get("id"), kegiatanId));
        }

        // Add case-insensitive LIKE filter for fungsiKawasan if provided
        if (fungsiKawasan != null && !fungsiKawasan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("fungsiKawasanId").get("nama")),
                    "%" + fungsiKawasan.toLowerCase() + "%"));
        }

        // Add case-insensitive LIKE filter for keterangan if provided
        if (keterangan != null && !keterangan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("keterangan")),
                    "%" + keterangan.toLowerCase() + "%"));
        }

        // Add filter for status if provided
        if (statusList != null && !statusList.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                root.get("statusId").get("id").in(statusList));
        }

        // Execute query with filters
        Page<KegiatanFungsiKawasanRehab> page = repository.findAll(spec, pageable);

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
        if (kegiatanId != null) {
            builder.queryParam("kegiatanId", kegiatanId);
        }
        if (fungsiKawasan != null && !fungsiKawasan.isEmpty()) {
            builder.queryParam("fungsiKawasan", fungsiKawasan);
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

        return new KegiatanFungsiKawasanRehabPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanFungsiKawasanRehabPageDTO searchWithCache(Long kegiatanId, String keyWord, Pageable pageable, String baseUrl) {
        Specification<KegiatanFungsiKawasanRehab> spec = Specification.where(null);

        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("kegiatan").get("id"), kegiatanId));
        }

        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("fungsiKawasanId").get("nama")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("statusId").get("nama")), searchPattern)
            ));
        }

        Page<KegiatanFungsiKawasanRehab> page = repository.findAll(spec, pageable);

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

        return new KegiatanFungsiKawasanRehabPageDTO(page, pageMetadata, links);
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