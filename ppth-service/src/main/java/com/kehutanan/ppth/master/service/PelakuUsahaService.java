package com.kehutanan.ppth.master.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kehutanan.ppth.master.model.PelakuUsaha;

import java.util.List;

public interface PelakuUsahaService {
    
    Page<PelakuUsaha> findAll(Pageable pageable);
    
    List<PelakuUsaha> findAll();
    
    PelakuUsaha findById(Long id);
    
    PelakuUsaha save(PelakuUsaha pelakuUsaha);
    
    PelakuUsaha update(Long id, PelakuUsaha pelakuUsaha);
    
    void deleteById(Long id);
    
    Page<PelakuUsaha> findByFilters(
        String namaBadanUsaha, 
        String nomorIndukBerusahaNib, 
        String ruangLingkupUsaha, 
        String namaDirektur,
        Long kategoriPelakuUsahaId, 
        Long provinsiId, 
        Long kabupatenKotaId, 
        Long kecamatanId, 
        Long kelurahanDesaId,
        Pageable pageable);
}