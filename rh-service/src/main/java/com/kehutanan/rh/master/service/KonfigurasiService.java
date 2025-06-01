package com.kehutanan.rh.master.service;

import com.kehutanan.rh.master.model.Konfigurasi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KonfigurasiService {
    
    Page<Konfigurasi> findAll(Pageable pageable);
    
    List<Konfigurasi> findAll();
    
    Konfigurasi findById(Long id);
    
    Konfigurasi save(Konfigurasi konfigurasi);
    
    Konfigurasi update(Long id, Konfigurasi konfigurasi);
    
    void deleteById(Long id);
    
    Page<Konfigurasi> findByFilters(Integer key, String value, String deskripsi, Long tipeId, Pageable pageable);
}