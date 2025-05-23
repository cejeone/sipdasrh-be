package com.kehutanan.rh.monev.service;

import com.kehutanan.rh.monev.dto.MonevDto;
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

    public Monev create(MonevDto monevDto) {
        Monev newMonev = new Monev();
        
        newMonev.setProgram(monevDto.getProgram());
        newMonev.setBpdas(monevDto.getBpdas());
        newMonev.setKeterangan(monevDto.getKeterangan());
        
        // Set numerical fields
        newMonev.setTotalTarget(monevDto.getTotalTarget());
        newMonev.setTotalRealisasi(monevDto.getTotalRealisasi());
        
        newMonev.setTotalT1(monevDto.getTotalT1());
        newMonev.setRealisasiT1(monevDto.getRealisasiT1());
        
        newMonev.setTotalP0(monevDto.getTotalP0());
        newMonev.setRealisasiP0(monevDto.getRealisasiP0());
        
        newMonev.setTotalP1(monevDto.getTotalP1());
        newMonev.setRealisasiP1(monevDto.getRealisasiP1());
        
        newMonev.setTotalP2(monevDto.getTotalP2());
        newMonev.setRealisasiP2(monevDto.getRealisasiP2());
        
        newMonev.setTotalBast(monevDto.getTotalBast());
        newMonev.setRealisasiBast(monevDto.getRealisasiBast());

        return monevRepository.save(newMonev);
    }

    public Monev update(UUID id, MonevDto monevDto) {
        if (!monevRepository.existsById(id)) {
            throw new EntityNotFoundException("Monev not found with id: " + id);
        }
        
        Monev existingMonev = monevRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Monev not found with id: " + id));
        
        // Update fields from DTO to entity
        existingMonev.setProgram(monevDto.getProgram());
        existingMonev.setBpdas(monevDto.getBpdas());
        existingMonev.setKeterangan(monevDto.getKeterangan());
        // Update numerical fields
        existingMonev.setTotalTarget(monevDto.getTotalTarget());
        existingMonev.setTotalRealisasi(monevDto.getTotalRealisasi());
        
        existingMonev.setTotalT1(monevDto.getTotalT1());
        existingMonev.setRealisasiT1(monevDto.getRealisasiT1());
        
        existingMonev.setTotalP0(monevDto.getTotalP0());
        existingMonev.setRealisasiP0(monevDto.getRealisasiP0());
        
        existingMonev.setTotalP1(monevDto.getTotalP1());
        existingMonev.setRealisasiP1(monevDto.getRealisasiP1());
        
        existingMonev.setTotalP2(monevDto.getTotalP2());
        existingMonev.setRealisasiP2(monevDto.getRealisasiP2());
        
        existingMonev.setTotalBast(monevDto.getTotalBast());
        existingMonev.setRealisasiBast(monevDto.getRealisasiBast());
        
        return monevRepository.save(existingMonev);
    }

    public void delete(UUID id) {
        if (!monevRepository.existsById(id)) {
            throw new EntityNotFoundException("Monev not found with id: " + id);
        }
        monevRepository.deleteById(id);
    }
}