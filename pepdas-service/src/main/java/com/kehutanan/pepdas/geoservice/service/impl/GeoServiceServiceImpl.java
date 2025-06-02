package com.kehutanan.pepdas.geoservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import com.kehutanan.pepdas.geoservice.dto.GeoServiceDTO;
import com.kehutanan.pepdas.geoservice.dto.GeoServicePageDTO;
import com.kehutanan.pepdas.geoservice.model.GeoService;
import com.kehutanan.pepdas.geoservice.repository.GeoServiceRepository;
import com.kehutanan.pepdas.geoservice.service.GeoServiceService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeoServiceServiceImpl implements GeoServiceService {

    private final GeoServiceRepository repository;

    @Autowired
    public GeoServiceServiceImpl(GeoServiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<GeoService> findAll() {
        return repository.findAll();
    }

    @Override
    public GeoServiceDTO findDTOById(Long id) {
        GeoService geoService = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("GeoService not found with id: " + id));
        return new GeoServiceDTO(geoService);
    }

    @Override
    public GeoService findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("GeoService not found with id: " + id));
    }

    @Override
    public GeoService save(GeoService geoService) {
        return repository.save(geoService);
    }

    @Override
    public GeoService update(Long id, GeoService geoService) {
        geoService.setId(id);
        return repository.save(geoService);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public GeoServicePageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<GeoService> page = repository.findAll(pageable);

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());

        List<Link> links = buildPageLinks(baseUrl, page);

        return new GeoServicePageDTO(page, pageMetadata, links);
    }

    @Override
    public GeoServicePageDTO findByFiltersWithCache(String service, List<String> bpdas, Pageable pageable, String baseUrl) {
        Specification<GeoService> spec = Specification.where(null);

        if (service != null && !service.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("service")), "%" + service.toLowerCase() + "%"));
        }
        if (bpdas != null && !bpdas.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("bpdasId").get("id").in(bpdas));
        }

        Page<GeoService> page = repository.findAll(spec, pageable);

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());

        String filterBaseUrl = buildFilterBaseUrl(baseUrl, service, bpdas);

        List<Link> links = buildPageLinks(filterBaseUrl, page);

        return new GeoServicePageDTO(page, pageMetadata, links);
    }

    @Override
    public GeoServicePageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl) {
        Specification<GeoService> spec = Specification.where(null);

        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("service")), searchPattern),
                    cb.like(cb.lower(root.get("url")), searchPattern)
            ));
        }

        Page<GeoService> page = repository.findAll(spec, pageable);

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());

        String searchBaseUrl = buildSearchBaseUrl(baseUrl, keyWord);

        List<Link> links = buildPageLinks(searchBaseUrl, page);

        return new GeoServicePageDTO(page, pageMetadata, links);
    }

    private List<Link> buildPageLinks(String baseUrl, Page<?> page) {
        List<Link> links = new ArrayList<>();
        links.add(Link.of(createPageUrl(baseUrl, page.getNumber(), page.getSize()), "self"));
        links.add(Link.of(createPageUrl(baseUrl, 0, page.getSize()), "first"));
        if (page.getNumber() > 0) {
            links.add(Link.of(createPageUrl(baseUrl, page.getNumber() - 1, page.getSize()), "prev"));
        }
        if (page.getNumber() < page.getTotalPages() - 1) {
            links.add(Link.of(createPageUrl(baseUrl, page.getNumber() + 1, page.getSize()), "next"));
        }
        if (page.getTotalPages() > 0) {
            links.add(Link.of(createPageUrl(baseUrl, page.getTotalPages() - 1, page.getSize()), "last"));
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

    private String buildFilterBaseUrl(String baseUrl, String service, List<String> bpdas) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        if (service != null && !service.isEmpty()) {
            builder.queryParam("service", service);
        }
        if (bpdas != null && !bpdas.isEmpty()) {
            for (String bpdasId : bpdas) {
                builder.queryParam("bpdas", bpdasId);
            }
        }
        return builder.build().toUriString();
    }

    private String buildSearchBaseUrl(String baseUrl, String keyWord) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        if (keyWord != null && !keyWord.isEmpty()) {
            builder.queryParam("keyWord", keyWord);
        }
        return builder.build().toUriString();
    }
}
