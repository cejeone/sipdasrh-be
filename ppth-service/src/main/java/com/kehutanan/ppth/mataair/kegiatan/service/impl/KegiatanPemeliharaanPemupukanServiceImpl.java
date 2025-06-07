package com.kehutanan.ppth.mataair.kegiatan.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.kehutanan.ppth.mataair.kegiatan.dto.KegiatanPemeliharaanPemupukanPageDTO;
import com.kehutanan.ppth.mataair.kegiatan.model.KegiatanPemeliharaanPemupukan;
import com.kehutanan.ppth.mataair.kegiatan.model.dto.KegiatanPemeliharaanPemupukanDTO;
import com.kehutanan.ppth.mataair.kegiatan.repository.KegiatanPemeliharaanPemupukanRepository;
import com.kehutanan.ppth.mataair.kegiatan.service.KegiatanPemeliharaanPemupukanService;

import jakarta.persistence.EntityNotFoundException;

@Service("mataAirKegiatanPemeliharaanPemupukanService")
public class KegiatanPemeliharaanPemupukanServiceImpl implements KegiatanPemeliharaanPemupukanService {
    private final KegiatanPemeliharaanPemupukanRepository repository;

    @Autowired
    public KegiatanPemeliharaanPemupukanServiceImpl(KegiatanPemeliharaanPemupukanRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<KegiatanPemeliharaanPemupukan> findAll() {
        return repository.findAll();
    }

    @Override
    public KegiatanPemeliharaanPemupukanDTO findDTOById(Long id) {
        KegiatanPemeliharaanPemupukan pemupukan = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan Pemeliharaan Pemupukan not found with id: " + id));

        return new KegiatanPemeliharaanPemupukanDTO(pemupukan);
    }

    @Override
    public KegiatanPemeliharaanPemupukan findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan Pemeliharaan Pemupukan not found with id: " + id));
    }

    @Override
    @CacheEvict(value = "pemupukanCache", allEntries = true)
    public KegiatanPemeliharaanPemupukan save(KegiatanPemeliharaanPemupukan pemupukan) {
        return repository.save(pemupukan);
    }

    @Override
    @CacheEvict(value = "pemupukanCache", allEntries = true)
    public KegiatanPemeliharaanPemupukan update(Long id, KegiatanPemeliharaanPemupukan pemupukan) {
        // Check if exists
        findById(id);
        return repository.save(pemupukan);
    }

    @Override
    @CacheEvict(value = "pemupukanCache", allEntries = true)
    public void deleteById(Long id) {
        // Check if exists
        findById(id);
        repository.deleteById(id);
    }

    @Override
    public KegiatanPemeliharaanPemupukanPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<KegiatanPemeliharaanPemupukan> page = repository.findAll(pageable);

        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());

        // Create links for pagination
        List<Link> links = createPaginationLinks(baseUrl, page);

        return new KegiatanPemeliharaanPemupukanPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanPemeliharaanPemupukanPageDTO findByFiltersWithCache(Long kegiatanId, String keterangan,
            List<String> jenis, Pageable pageable, String baseUrl) {
        
        // Create specification based on filters
        Specification<KegiatanPemeliharaanPemupukan> spec = Specification.where(null);

        // Filter by kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("kegiatan").get("id"), kegiatanId));
        }

        // Filter by keterangan if provided (case-insensitive LIKE)
        if (keterangan != null && !keterangan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("keterangan")), 
                    "%" + keterangan.toLowerCase() + "%"));
        }

        // Filter by jenis if provided
        if (jenis != null && !jenis.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                root.get("jenisId").get("id").in(jenis));
        }

        // Execute query with filters
        Page<KegiatanPemeliharaanPemupukan> page = repository.findAll(spec, pageable);

        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());

        // Build URL with filters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        
        if (kegiatanId != null) {
            builder.queryParam("kegiatanId", kegiatanId);
        }
        if (keterangan != null && !keterangan.isEmpty()) {
            builder.queryParam("keterangan", keterangan);
        }
        if (jenis != null && !jenis.isEmpty()) {
            for (String j : jenis) {
                builder.queryParam("jenis", j);
            }
        }
        
        String filterBaseUrl = builder.build().toUriString();
        List<Link> links = createFilterPaginationLinks(filterBaseUrl, page);

        return new KegiatanPemeliharaanPemupukanPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanPemeliharaanPemupukanPageDTO searchWithCache(Long kegiatanId, String keyWord, Pageable pageable, String baseUrl) {
        Specification<KegiatanPemeliharaanPemupukan> spec = Specification.where(null);

        // Filter by kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("kegiatan").get("id"), kegiatanId));
        }

        // Search by keyword if provided
        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("jumlahPupuk")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("jenisId").get("name")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("satuanId").get("name")), searchPattern)
            ));
        }

        Page<KegiatanPemeliharaanPemupukan> page = repository.findAll(spec, pageable);

        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());

        // Build URL with search parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        
        if (kegiatanId != null) {
            builder.queryParam("kegiatanId", kegiatanId);
        }
        if (keyWord != null && !keyWord.isEmpty()) {
            builder.queryParam("keyWord", keyWord);
        }
        
        String searchBaseUrl = builder.build().toUriString();
        List<Link> links = createFilterPaginationLinks(searchBaseUrl, page);

        return new KegiatanPemeliharaanPemupukanPageDTO(page, pageMetadata, links);
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
        
        // Check if the URL already has query parameters
        String connector = filterBaseUrl.contains("?") ? "&" : "?";

        // Self link
        links.add(Link.of(filterBaseUrl + connector + "page=" + page.getNumber() + "&size=" + page.getSize(), "self"));

        // First page link
        links.add(Link.of(filterBaseUrl + connector + "page=0&size=" + page.getSize(), "first"));

        // Previous page link (if not first page)
        if (page.getNumber() > 0) {
            links.add(Link.of(filterBaseUrl + connector + "page=" + (page.getNumber() - 1) + "&size=" + page.getSize(), "prev"));
        }

        // Next page link (if not last page)
        if (page.getNumber() < page.getTotalPages() - 1) {
            links.add(Link.of(filterBaseUrl + connector + "page=" + (page.getNumber() + 1) + "&size=" + page.getSize(), "next"));
        }

        // Last page link
        if (page.getTotalPages() > 0) {
            links.add(Link.of(filterBaseUrl + connector + "page=" + (page.getTotalPages() - 1) + "&size=" + page.getSize(), "last"));
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
}