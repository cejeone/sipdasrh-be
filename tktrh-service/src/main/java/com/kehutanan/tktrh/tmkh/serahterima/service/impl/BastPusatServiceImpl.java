package com.kehutanan.tktrh.tmkh.serahterima.service.impl;

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

import com.kehutanan.tktrh.tmkh.serahterima.dto.BastPusatPageDTO;
import com.kehutanan.tktrh.tmkh.serahterima.model.BastPusat;
import com.kehutanan.tktrh.tmkh.serahterima.model.dto.BastPusatDTO;
import com.kehutanan.tktrh.tmkh.serahterima.repository.BastPusatRepository;
import com.kehutanan.tktrh.tmkh.serahterima.service.BastPusatService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BastPusatServiceImpl implements BastPusatService {
    
    private final BastPusatRepository repository;

    @Autowired
    public BastPusatServiceImpl(BastPusatRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<BastPusat> findAll() {
        return repository.findAll();
    }

    @Override
    public BastPusatDTO findDTOById(Long id) {
        BastPusat bastPusat = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BAST Pusat not found with id: " + id));

        return new BastPusatDTO(bastPusat);
    }

    @Override
    public BastPusat findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BAST Pusat not found with id: " + id));
    }

    @Override
    @Transactional
    public BastPusat save(BastPusat bastPusat) {
        return repository.save(bastPusat);
    }

    @Override
    @Transactional
    public BastPusat update(Long id, BastPusat bastPusat) {
        // Ensure the entity exists
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("BAST Pusat not found with id: " + id);
        }
        
        return repository.save(bastPusat);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Ensure the entity exists
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("BAST Pusat not found with id: " + id);
        }
        
        repository.deleteById(id);
    }

    @Override
    public BastPusatPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<BastPusat> page = repository.findAll(pageable);
        
        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());
        
        // Create links for pagination
        List<Link> links = createPaginationLinks(baseUrl, page);
        
        return new BastPusatPageDTO(page, pageMetadata, links);
    }

    @Override
    public BastPusatPageDTO findByFiltersWithCache(String programName, String keterangan, List<String> bpdasList,
            Pageable pageable, String baseUrl) {
        
        // Create specification based on filters
        Specification<BastPusat> spec = Specification.where(null);
        
        // Add filter for programName if provided
        if (programName != null && !programName.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("programId").get("namaProgramKegiatan")),
                    "%" + programName.toLowerCase() + "%"));
        }
        
        // Add filter for keterangan if provided
        if (keterangan != null && !keterangan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("keterangan")),
                    "%" + keterangan.toLowerCase() + "%"));
        }
        
        // Add filter for BPDAS list if provided
        if (bpdasList != null && !bpdasList.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                root.get("bpdasId").get("id").in(bpdasList));
        }
        
        // Execute query with filters
        Page<BastPusat> page = repository.findAll(spec, pageable);
        
        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());
        
        // Create filter URL
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        
        // Add filter parameters
        if (programName != null && !programName.isEmpty()) {
            builder.queryParam("programName", programName);
        }
        if (keterangan != null && !keterangan.isEmpty()) {
            builder.queryParam("keterangan", keterangan);
        }
        if (bpdasList != null && !bpdasList.isEmpty()) {
            for (String bpdasId : bpdasList) {
                builder.queryParam("bpdasList", bpdasId);
            }
        }
        
        String filterBaseUrl = builder.build().toUriString();
        
        // Create links for pagination
        List<Link> links = createFilterPageLinks(filterBaseUrl, page);
        
        return new BastPusatPageDTO(page, pageMetadata, links);
    }

    @Override
    public BastPusatPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl) {
        Specification<BastPusat> spec = Specification.where(null);
        
        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("programId").get("namaProgramKegiatan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("bpdasId").get("namaBpdas")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("provinsiId").get("namaProvinsi")), searchPattern)
            ));
        }
        
        Page<BastPusat> page = repository.findAll(spec, pageable);
        
        // Create PageMetadata for HATEOAS
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages());
        
        // Create search URL with parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        if (keyWord != null && !keyWord.isEmpty()) {
            builder.queryParam("keyWord", keyWord);
        }
        
        String searchBaseUrl = builder.build().toUriString();
        
        // Create links for pagination
        List<Link> links = createFilterPageLinks(searchBaseUrl, page);
        
        return new BastPusatPageDTO(page, pageMetadata, links);
    }
    
    // Helper methods for pagination
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
    
    private List<Link> createFilterPageLinks(String filterBaseUrl, Page<?> page) {
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