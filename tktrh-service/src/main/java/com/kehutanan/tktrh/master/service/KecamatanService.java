package com.kehutanan.tktrh.master.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.master.model.Kecamatan;

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