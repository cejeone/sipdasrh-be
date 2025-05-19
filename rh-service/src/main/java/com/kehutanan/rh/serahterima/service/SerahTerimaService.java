package com.kehutanan.rh.serahterima.service;

import com.kehutanan.rh.serahterima.model.SerahTerima;
import com.kehutanan.rh.serahterima.repository.SerahTerimaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SerahTerimaService {

    @Autowired
    private SerahTerimaRepository serahTerimaRepository;

    public Page<SerahTerima> findAll(Pageable pageable) {
        return serahTerimaRepository.findAll(pageable);
    }

    public Optional<SerahTerima> findById(UUID id) {
        return serahTerimaRepository.findById(id);
    }

    public SerahTerima save(SerahTerima serahTerima) {
        return serahTerimaRepository.save(serahTerima);
    }

    public void deleteById(UUID id) {
        serahTerimaRepository.deleteById(id);
    }

    public Page<SerahTerima> findByFilters(String program, Pageable pageable) {
        Specification<SerahTerima> spec = (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("program")),
                "%" + program.toLowerCase() + "%");

        return serahTerimaRepository.findAll(spec, pageable);
    }
}