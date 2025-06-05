package com.kehutanan.rm.kegiatan.service.impl;

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

import com.kehutanan.rm.kegiatan.dto.KegiatanPemeliharaanSulamanPageDTO;
import com.kehutanan.rm.kegiatan.model.KegiatanPemeliharaanSulaman;
import com.kehutanan.rm.kegiatan.model.dto.KegiatanPemeliharaanSulamanDTO;
import com.kehutanan.rm.kegiatan.repository.KegiatanPemeliharaanSulamanRepository;
import com.kehutanan.rm.kegiatan.repository.KegiatanRepository;
import com.kehutanan.rm.kegiatan.service.KegiatanPemeliharaanSulamanService;
import com.kehutanan.rm.master.repository.LovRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class KegiatanPemeliharaanSulamanServiceImpl implements KegiatanPemeliharaanSulamanService {
    private final KegiatanPemeliharaanSulamanRepository repository;
    private final KegiatanRepository kegiatanRepository;
    private final LovRepository lovRepository;

    @Autowired
    public KegiatanPemeliharaanSulamanServiceImpl(
            KegiatanPemeliharaanSulamanRepository repository,
            KegiatanRepository kegiatanRepository,
            LovRepository lovRepository) {
        this.repository = repository;
        this.kegiatanRepository = kegiatanRepository;
        this.lovRepository = lovRepository;
    }

    @Override
    public List<KegiatanPemeliharaanSulaman> findAll() {
        return repository.findAll();
    }

    @Override
    public KegiatanPemeliharaanSulamanDTO findDTOById(Long id) {
        KegiatanPemeliharaanSulaman sulaman = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanPemeliharaanSulaman not found with id: " + id));

        KegiatanPemeliharaanSulamanDTO sulamanDTO = new KegiatanPemeliharaanSulamanDTO(sulaman);
        return sulamanDTO;
    }

    @Override
    public KegiatanPemeliharaanSulaman findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanPemeliharaanSulaman not found with id: " + id));
    }

    @Override
    public KegiatanPemeliharaanSulaman save(KegiatanPemeliharaanSulaman sulaman) {
        return repository.save(sulaman);
    }

    @Override
    public KegiatanPemeliharaanSulaman update(Long id, KegiatanPemeliharaanSulaman sulaman) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("KegiatanPemeliharaanSulaman not found with id: " + id);
        }
        return repository.save(sulaman);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("KegiatanPemeliharaanSulaman not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public KegiatanPemeliharaanSulamanPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<KegiatanPemeliharaanSulaman> page = repository.findAll(pageable);

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

        return new KegiatanPemeliharaanSulamanPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanPemeliharaanSulamanPageDTO findByFiltersWithCache(
            Long kegiatanId,
            String tahun,
            List<String> sumberBibit,
            Pageable pageable,
            String baseUrl) {

        // Create specification based on filters
        Specification<KegiatanPemeliharaanSulaman> spec = Specification.where(null);

        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("kegiatan").get("id"),
                    kegiatanId));
        }

        // Execute query with filters
        Page<KegiatanPemeliharaanSulaman> page = repository.findAll(spec, pageable);

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

        return new KegiatanPemeliharaanSulamanPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanPemeliharaanSulamanPageDTO searchWithCache(Long kegiatanId, String keyWord, Pageable pageable,
            String baseUrl) {
        Specification<KegiatanPemeliharaanSulaman> spec = Specification.where(null);

        String searchPattern = "%" + keyWord.toLowerCase() + "%";
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.join("kegiatan").get("namaKegiatan")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.join("namaBibitId").get("name")), searchPattern)));

        Page<KegiatanPemeliharaanSulaman> page = repository.findAll(spec, pageable);

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

        return new KegiatanPemeliharaanSulamanPageDTO(page, pageMetadata, links);
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