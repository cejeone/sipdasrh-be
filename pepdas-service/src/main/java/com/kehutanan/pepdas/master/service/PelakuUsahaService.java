package com.kehutanan.pepdas.master.service;

import com.kehutanan.pepdas.master.model.PelakuUsaha;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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