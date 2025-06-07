package com.kehutanan.ppth.penghijauan.program.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.kehutanan.ppth.penghijauan.program.dto.ProgramJenisBibitPageDTO;
import com.kehutanan.ppth.penghijauan.program.model.ProgramJenisBibit;
import com.kehutanan.ppth.penghijauan.program.model.dto.ProgramJenisBibitDTO;
import com.kehutanan.ppth.penghijauan.program.repository.ProgramJenisBibitRepository;
import com.kehutanan.ppth.penghijauan.program.service.ProgramJenisBibitService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;

@Service("penghijauanProgramJenisBibitServiceImpl")
public class ProgramJenisBibitServiceImpl implements ProgramJenisBibitService {

    private final ProgramJenisBibitRepository repository;

    @Autowired
    public ProgramJenisBibitServiceImpl(ProgramJenisBibitRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProgramJenisBibit> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "rh_programJenisBibitCache", key = "#id")
    public ProgramJenisBibitDTO findDTOById(Long id) {
        ProgramJenisBibit programJenisBibit = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProgramJenisBibit not found with id: " + id));

        return new ProgramJenisBibitDTO(programJenisBibit);
    }

    @Override
    public ProgramJenisBibit findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProgramJenisBibit not found with id: " + id));
    }

    @Override
    @CacheEvict(value = { "rh_programJenisBibitCache", "rh_programJenisBibitPageCache",
            "rh_programJenisBibitFilterCache",
            "rh_programJenisBibitSearchCache" ,"rh_programCache", "rh_programPageCache", "rh_programFilterCache",
            "rh_programSearchCache" }, allEntries = true, beforeInvocation = true)
    public ProgramJenisBibit save(ProgramJenisBibit programJenisBibit) {
        return repository.save(programJenisBibit);
    }

    @Override
    @CachePut(value = "rh_programJenisBibitCache", key = "#id")
    public ProgramJenisBibit update(Long id, ProgramJenisBibit programJenisBibit) {
        return repository.save(programJenisBibit);
    }

    @Override
    @CacheEvict(value = { "rh_programJenisBibitCache", "rh_programJenisBibitPageCache",
            "rh_programJenisBibitFilterCache",
            "rh_programJenisBibitSearchCache","rh_programCache", "rh_programPageCache", "rh_programFilterCache",
            "rh_programSearchCache" }, allEntries = true, beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Cacheable(value = "rh_programJenisBibitPageCache", key = "#pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    public ProgramJenisBibitPageDTO findAllWithCache(Pageable pageable, String baseUrl) {
        Page<ProgramJenisBibit> page = repository.findAll(pageable);

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

        return new ProgramJenisBibitPageDTO(page, pageMetadata, links);
    }

    @Override
    @Cacheable(value = "rh_programJenisBibitFilterCache", key = "{#programId, #namaBibit, #kategori, #pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    public ProgramJenisBibitPageDTO findByFiltersWithCache(Long programId, String namaBibit, List<String> kategori,
            Pageable pageable, String baseUrl) {

        // Create specification based on filters
        Specification<ProgramJenisBibit> spec = Specification.where(null);

        // Add filter for program_id if provided
        if (programId != null) {
            spec = spec.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("program").get("id"), programId));
        }

        // Add case-insensitive LIKE filter for namaBibit if provided
        if (namaBibit != null && !namaBibit.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaBibitId").get("nama")),
                    "%" + namaBibit.toLowerCase() + "%"));
        }

        // Add filter for kategori if provided
        if (kategori != null && !kategori.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                for (String kategoriId : kategori) {
                    predicates.add(cb.equal(root.get("kategoriId").get("id"), Long.valueOf(kategoriId)));
                }
                return cb.or(predicates.toArray(new Predicate[0]));
            });
        }

        // Execute query with filters
        Page<ProgramJenisBibit> page = repository.findAll(spec, pageable);

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
        if (programId != null) {
            builder.queryParam("programId", programId);
        }
        if (namaBibit != null && !namaBibit.isEmpty()) {
            builder.queryParam("namaBibit", namaBibit);
        }
        if (kategori != null && !kategori.isEmpty()) {
            for (String kategoriId : kategori) {
                builder.queryParam("kategori", kategoriId);
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

        return new ProgramJenisBibitPageDTO(page, pageMetadata, links);
    }

    @Override
    @Cacheable(value = "rh_programJenisBibitSearchCache", key = "{#programId, #keyWord, #pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    public ProgramJenisBibitPageDTO searchWithCache(Long programId, String keyWord, Pageable pageable, String baseUrl) {
        Specification<ProgramJenisBibit> spec = Specification.where(null);

        // Add filter for program_id if provided
        if (programId != null) {
            spec = spec.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("program").get("id"), programId));
        }

        // Add search by keterangan
        if (keyWord != null && !keyWord.isEmpty()) {
            String searchPattern = "%" + keyWord.toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keterangan")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("namaBibitId").get("nama")), searchPattern)));
        }

        Page<ProgramJenisBibit> page = repository.findAll(spec, pageable);

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

        // Add program ID parameter to URL if provided
        if (programId != null) {
            builder.queryParam("programId", programId);
        }

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

        return new ProgramJenisBibitPageDTO(page, pageMetadata, links);
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