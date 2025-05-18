package com.kehutanan.rh.bimtek.service;

import com.kehutanan.rh.bimtek.model.Bimtek;
import com.kehutanan.rh.bimtek.repository.BimtekRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BimtekService {
    
    private final BimtekRepository bimtekRepository;

    public Page<Bimtek> findAll(Pageable pageable) {
        return bimtekRepository.findAll(pageable);
    }
    
    public Page<Bimtek> search(String query, Pageable pageable) {
        return bimtekRepository.findByNamaBimtekContainingIgnoreCase(query, pageable);
    }

    public List<Bimtek> findAll() {
        return bimtekRepository.findAll();
    }

    public Bimtek findById(UUID id) {
        return bimtekRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Bimtek not found with id: " + id));
    }

    @Transactional
    public Bimtek create(Bimtek bimtek) {
        return bimtekRepository.save(bimtek);
    }

    @Transactional
    public Bimtek update(UUID id, Bimtek bimtek) {
        Bimtek existing = findById(id);
        
        existing.setNamaBimtek(bimtek.getNamaBimtek());
        existing.setSubjek(bimtek.getSubjek());
        existing.setProgram(bimtek.getProgram());
        existing.setBpdas(bimtek.getBpdas());
        existing.setTempat(bimtek.getTempat());
        existing.setTanggal(bimtek.getTanggal());
        existing.setAudience(bimtek.getAudience());
        existing.setEvaluasi(bimtek.getEvaluasi());
        existing.setKeterangan(bimtek.getKeterangan());
        
        return bimtekRepository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        bimtekRepository.deleteById(id);
    }
}
