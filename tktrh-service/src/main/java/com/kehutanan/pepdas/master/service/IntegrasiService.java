package com.kehutanan.pepdas.master.service;

import com.kehutanan.pepdas.master.model.Integrasi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IntegrasiService {
    
    Page<Integrasi> findAll(Pageable pageable);
    
    List<Integrasi> findAll();
    
    Integrasi findById(Long id);
    
    Integrasi save(Integrasi integrasi);
    
    Integrasi update(Long id, Integrasi integrasi);
    
    void deleteById(Long id);
    
    Page<Integrasi> findByFilters(String url, String tipe, String deskripsi, Long statusId, Pageable pageable);
}