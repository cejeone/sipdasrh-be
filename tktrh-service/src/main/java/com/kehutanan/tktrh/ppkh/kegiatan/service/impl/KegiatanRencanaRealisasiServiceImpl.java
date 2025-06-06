package com.kehutanan.tktrh.ppkh.kegiatan.service.impl;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.kehutanan.tktrh.ppkh.kegiatan.dto.KegiatanRencanaRealisasiPageDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRencanaRealisasi;
import com.kehutanan.tktrh.ppkh.kegiatan.model.dto.KegiatanRencanaRealisasiDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.repository.KegiatanRencanaRealisasiRepository;
import com.kehutanan.tktrh.ppkh.kegiatan.service.KegiatanRencanaRealisasiService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class KegiatanRencanaRealisasiServiceImpl implements KegiatanRencanaRealisasiService {
    
    private final KegiatanRencanaRealisasiRepository repository;

    @Autowired
    public KegiatanRencanaRealisasiServiceImpl(KegiatanRencanaRealisasiRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<KegiatanRencanaRealisasi> findAll() {
        return repository.findAll();
    }

    @Override
    public KegiatanRencanaRealisasiDTO findDTOById(Long id) {
        KegiatanRencanaRealisasi kegiatanRencanaRealisasi = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanRencanaRealisasi not found with id: " + id));

        KegiatanRencanaRealisasiDTO kegiatanRencanaRealisasiDTO = new KegiatanRencanaRealisasiDTO(kegiatanRencanaRealisasi);

        return kegiatanRencanaRealisasiDTO;
    }

    @Override
    public KegiatanRencanaRealisasi findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanRencanaRealisasi not found with id: " + id));
    }

    @Override
    @Transactional
    public KegiatanRencanaRealisasi save(KegiatanRencanaRealisasi kegiatanRencanaRealisasi) {
        return repository.save(kegiatanRencanaRealisasi);
    }

    @Override
    @Transactional
    public KegiatanRencanaRealisasi update(Long id, KegiatanRencanaRealisasi kegiatanRencanaRealisasi) {
        // Check if entity exists
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("KegiatanRencanaRealisasi not found with id: " + id);
        }
        return repository.save(kegiatanRencanaRealisasi);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Check if entity exists
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("KegiatanRencanaRealisasi not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public KegiatanRencanaRealisasiPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<KegiatanRencanaRealisasi> page = repository.findAll(pageable);

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

        return new KegiatanRencanaRealisasiPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanRencanaRealisasiPageDTO findByFiltersWithCache(Long kegiatanId, String fungsiKawasan, 
            String keterangan, List<String> status, Pageable pageable, String baseUrl) {
        
        // Create specification based on filters
        Specification<KegiatanRencanaRealisasi> spec = Specification.where(null);

        // Add filter for kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("kegiatan").get("id"), kegiatanId));
        }

        // Add case-insensitive LIKE filter for fungsiKawasan if provided
        if (fungsiKawasan != null && !fungsiKawasan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("fungsiKawasanId").get("nilai")),
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
        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                root.get("statusId").get("nilai").in(status));
        }

        // Execute query with filters
        Page<KegiatanRencanaRealisasi> page = repository.findAll(spec, pageable);

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
        
        if (status != null && !status.isEmpty()) {
            for (String s : status) {
                builder.queryParam("status", s);
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

        return new KegiatanRencanaRealisasiPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanRencanaRealisasiPageDTO searchWithCache(Long kegiatanId, String keyWord, Pageable pageable, String baseUrl) {
        Specification<KegiatanRencanaRealisasi> spec = Specification.where(null);

        // Add filter for kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("kegiatan").get("id"), kegiatanId));
        }

        // Perform keyword search across relevant fields
        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("fungsiKawasanId").get("nilai")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("statusId").get("nilai")), searchPattern)
            ));
        }

        Page<KegiatanRencanaRealisasi> page = repository.findAll(spec, pageable);

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
        
        if (kegiatanId != null) {
            builder.queryParam("kegiatanId", kegiatanId);
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

        return new KegiatanRencanaRealisasiPageDTO(page, pageMetadata, links);
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