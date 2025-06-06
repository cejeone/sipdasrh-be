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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanRealisasiReboisasiPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanRealisasiReboisasi;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanRealisasiReboisasiDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.repository.KegiatanRealisasiReboisasiRepository;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanRealisasiReboisasiService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class KegiatanRealisasiReboisasiServiceImpl implements KegiatanRealisasiReboisasiService {
    
    private final KegiatanRealisasiReboisasiRepository repository;
    
    @Autowired
    public KegiatanRealisasiReboisasiServiceImpl(KegiatanRealisasiReboisasiRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<KegiatanRealisasiReboisasi> findAll() {
        return repository.findAll();
    }

    @Override
    public KegiatanRealisasiReboisasiDTO findDTOById(Long id) {
        KegiatanRealisasiReboisasi realisasi = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan Realisasi Reboisasi not found with id: " + id));
        
        return new KegiatanRealisasiReboisasiDTO(realisasi);
    }

    @Override
    public KegiatanRealisasiReboisasi findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan Realisasi Reboisasi not found with id: " + id));
    }

    @Override
    @Transactional
    public KegiatanRealisasiReboisasi save(KegiatanRealisasiReboisasi kegiatanRealisasiReboisasi) {
        return repository.save(kegiatanRealisasiReboisasi);
    }

    @Override
    @Transactional
    public KegiatanRealisasiReboisasi update(Long id, KegiatanRealisasiReboisasi kegiatanRealisasiReboisasi) {
        KegiatanRealisasiReboisasi existing = findById(id);
        // Set ID to ensure we're updating the existing record
        kegiatanRealisasiReboisasi.setId(id);
        return repository.save(kegiatanRealisasiReboisasi);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Check if entity exists before deletion
        findById(id);
        repository.deleteById(id);
    }

    @Override
    public KegiatanRealisasiReboisasiPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<KegiatanRealisasiReboisasi> page = repository.findAll(pageable);

        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());

        // Create links for pagination
        List<Link> links = createPaginationLinks(baseUrl, page);

        return new KegiatanRealisasiReboisasiPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanRealisasiReboisasiPageDTO findByFiltersWithCache(Long kegiatanId, String fungsiKawasan, 
            String keterangan, List<String> status, Pageable pageable, String baseUrl) {
        
        // Create specification based on filters
        Specification<KegiatanRealisasiReboisasi> spec = Specification.where(null);

        // Add filter for kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("kegiatan").get("id"), kegiatanId));
        }

        // Add case-insensitive LIKE filter for fungsiKawasan if provided
        if (fungsiKawasan != null && !fungsiKawasan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("fungsiKawasanId").get("name")),
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
                    root.get("statusId").get("code").in(status));
        }

        // Execute query with filters
        Page<KegiatanRealisasiReboisasi> page = repository.findAll(spec, pageable);

        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());

        // Create URL with filters for pagination links
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
        
        // Create pagination links
        List<Link> links = createFilterPaginationLinks(filterBaseUrl, page);

        return new KegiatanRealisasiReboisasiPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanRealisasiReboisasiPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl) {
        Specification<KegiatanRealisasiReboisasi> spec = Specification.where(null);

        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("fungsiKawasanId").get("name")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("kegiatan").get("namaKegiatan")), searchPattern)
            ));
        }

        Page<KegiatanRealisasiReboisasi> page = repository.findAll(spec, pageable);

        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());

        // Create URL with search parameter
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);

        // Add search keyword parameter to URL
        if (keyWord != null && !keyWord.isEmpty()) {
            builder.queryParam("keyWord", keyWord);
        }

        String searchBaseUrl = builder.build().toUriString();

        // Create pagination links
        List<Link> links = createFilterPaginationLinks(searchBaseUrl, page);

        return new KegiatanRealisasiReboisasiPageDTO(page, pageMetadata, links);
    }
    
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
    
    private List<Link> createFilterPaginationLinks(String filterBaseUrl, Page<?> page) {
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