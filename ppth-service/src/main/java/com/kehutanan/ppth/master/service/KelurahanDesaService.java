package com.kehutanan.ppth.master.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kehutanan.ppth.master.model.KelurahanDesa;

import java.util.List;

public interface KelurahanDesaService {
    
    Page<KelurahanDesa> findAll(Pageable pageable);
    
    List<KelurahanDesa> findAll();
    
    KelurahanDesa findById(Long id);
    
    KelurahanDesa save(KelurahanDesa kelurahanDesa);
    
    KelurahanDesa update(Long id, KelurahanDesa kelurahanDesa);
    
    void deleteById(Long id);
    
    Page<KelurahanDesa> findByFilters(String kelurahan, Long kecamatanId, Pageable pageable);
}