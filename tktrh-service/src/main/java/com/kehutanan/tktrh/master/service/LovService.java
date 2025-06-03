package com.kehutanan.tktrh.master.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.master.model.Lov;

import java.util.List;

public interface LovService {
    
    Page<Lov> findAll(Pageable pageable);
    
    List<Lov> findAll();
    
    Lov findById(Long id);
    
    Lov save(Lov lov);
    
    Lov update(Long id, Lov lov);
    
    void deleteById(Long id);
    
    Page<Lov> findByFilters(String namaKategori, String nilai, List<String> status, Pageable pageable);
}