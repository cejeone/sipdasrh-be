package com.kehutanan.tktrh.master.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.master.model.Bpdas;

import java.util.List;

public interface BpdasService {
    
    Page<Bpdas> findAll(Pageable pageable);
    
    List<Bpdas> findAll();
    
    Bpdas findById(Long id);
    
    Bpdas save(Bpdas bpdas);
    
    Bpdas update(Long id, Bpdas bpdas);
    
    void deleteById(Long id);
    
    Page<Bpdas> findByFilters(String namaBpdas, Long provinsiId, Long kabupatenKotaId, Pageable pageable);
}