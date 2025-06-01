package com.kehutanan.rh.master.service;

import com.kehutanan.rh.master.model.Kecamatan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KecamatanService {
    
    Page<Kecamatan> findAll(Pageable pageable);
    
    List<Kecamatan> findAll();
    
    Kecamatan findById(Long id);
    
    Kecamatan save(Kecamatan kecamatan);
    
    Kecamatan update(Long id, Kecamatan kecamatan);
    
    void deleteById(Long id);
    
    Page<Kecamatan> findByFilters(String kecamatan, String kodeDepdagri, Long kabupatenKotaId, Pageable pageable);
}