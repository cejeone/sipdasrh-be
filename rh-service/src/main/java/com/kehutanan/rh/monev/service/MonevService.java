package com.kehutanan.rh.monev.service;

import com.kehutanan.rh.monev.model.Monev;
import com.kehutanan.rh.monev.repository.MonevRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;

@Service
public class MonevService {

    private final MonevRepository monevRepository;

    @Autowired
    public MonevService(MonevRepository monevRepository) {
        this.monevRepository = monevRepository;
    }

    public Page<Monev> findAll(Pageable pageable) {
        return monevRepository.findAll(pageable);
    }

    public Page<Monev> findByFilters(String program, List<String> bpdasList, Pageable pageable) {
        Specification<Monev> spec = Specification.where(null);

        if (bpdasList != null && !bpdasList.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("bpdas").in(bpdasList));
        }

        if (program != null && !program.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("program")),
                    "%" + program.toLowerCase() + "%"));
        }

        return monevRepository.findAll(spec, pageable);
    }

    public Monev findById(UUID id) {
        return monevRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Monev not found with id: " + id));
    }

    public Monev create(Monev monev) {
        return monevRepository.save(monev);
    }

    public Monev update(UUID id, Monev monev) {
        if (!monevRepository.existsById(id)) {
            throw new EntityNotFoundException("Monev not found with id: " + id);
        }
        monev.setId(id);
        return monevRepository.save(monev);
    }

    public void delete(UUID id) {
        if (!monevRepository.existsById(id)) {
            throw new EntityNotFoundException("Monev not found with id: " + id);
        }
        monevRepository.deleteById(id);
    }
}