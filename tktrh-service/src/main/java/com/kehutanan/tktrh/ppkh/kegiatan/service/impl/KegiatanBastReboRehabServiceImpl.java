package com.kehutanan.tktrh.ppkh.kegiatan.service.impl;

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

import com.kehutanan.tktrh.ppkh.kegiatan.dto.KegiatanBastReboRehabPageDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanBastReboRehab;
import com.kehutanan.tktrh.ppkh.kegiatan.model.dto.KegiatanBastReboRehabDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.repository.KegiatanBastReboRehabRepository;
import com.kehutanan.tktrh.ppkh.kegiatan.service.KegiatanBastReboRehabService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class KegiatanBastReboRehabServiceImpl implements KegiatanBastReboRehabService {
    
    private final KegiatanBastReboRehabRepository repository;

    @Autowired
    public KegiatanBastReboRehabServiceImpl(KegiatanBastReboRehabRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<KegiatanBastReboRehab> findAll() {
        return repository.findAll();
    }

    @Override
    public KegiatanBastReboRehabDTO findDTOById(Long id) {
        KegiatanBastReboRehab kegiatanBastReboRehab = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanBastReboRehab not found with id: " + id));
        
        KegiatanBastReboRehabDTO kegiatanBastReboRehabDTO = new KegiatanBastReboRehabDTO(kegiatanBastReboRehab);
        
        return kegiatanBastReboRehabDTO;
    }

    @Override
    public KegiatanBastReboRehab findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanBastReboRehab not found with id: " + id));
    }

    @Override
    @Transactional
    public KegiatanBastReboRehab save(KegiatanBastReboRehab kegiatanBastReboRehab) {
        return repository.save(kegiatanBastReboRehab);
    }

    @Override
    @Transactional
    public KegiatanBastReboRehab update(Long id, KegiatanBastReboRehab kegiatanBastReboRehab) {
        // Ensure the entity exists
        findById(id);
        return repository.save(kegiatanBastReboRehab);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Ensure the entity exists
        findById(id);
        repository.deleteById(id);
    }

    @Override
    public KegiatanBastReboRehabPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<KegiatanBastReboRehab> page = repository.findAll(pageable);

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

        return new KegiatanBastReboRehabPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanBastReboRehabPageDTO findByFiltersWithCache(Long kegiatanId, String tahun, String keterangan, 
            List<String> statusList, Pageable pageable, String baseUrl) {

        // Create specification based on filters
        Specification<KegiatanBastReboRehab> spec = Specification.where(null);

        // Add filter for kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("kegiatan").get("id"), kegiatanId));
        }

        // Add case-insensitive LIKE filter for tahun if provided
        if (tahun != null && !tahun.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.like(
                    criteriaBuilder.function("CAST", String.class, root.get("tahunId")),
                    "%" + tahun + "%"
                ));
        }

        // Add case-insensitive LIKE filter for keterangan if provided
        if (keterangan != null && !keterangan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("keterangan")),
                    "%" + keterangan.toLowerCase() + "%"
                ));
        }

        // Add filter for status list if provided
        if (statusList != null && !statusList.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                root.get("statusSerahTerima").get("id").in(statusList));
        }

        // Execute query with filters
        Page<KegiatanBastReboRehab> page = repository.findAll(spec, pageable);

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
        if (tahun != null && !tahun.isEmpty()) {
            builder.queryParam("tahun", tahun);
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

        return new KegiatanBastReboRehabPageDTO(page, pageMetadata, links);
    }

    @Override
    public KegiatanBastReboRehabPageDTO searchWithCache(Long kegiatanId, String keyWord, Pageable pageable, String baseUrl) {
        Specification<KegiatanBastReboRehab> spec = Specification.where(null);

        // Add filter for kegiatanId if provided
        if (kegiatanId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("kegiatan").get("id"), kegiatanId));
        }

        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.function("CAST", String.class, root.get("tahunId")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("statusSerahTerima").get("name")), searchPattern)));
        }

        Page<KegiatanBastReboRehab> page = repository.findAll(spec, pageable);

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

        return new KegiatanBastReboRehabPageDTO(page, pageMetadata, links);
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