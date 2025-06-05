package com.kehutanan.rm.master.service;

import com.kehutanan.rm.master.model.Spas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SpasService {
    
    Page<Spas> findAll(Pageable pageable);
    
    List<Spas> findAll();
    
    Spas findById(Long id);
    
    Spas save(Spas spas);
    
    Spas update(Long id, Spas spas);
    
    void deleteById(Long id);
    
    Page<Spas> findByFilters(String spas, Long bpdasId, Long dasId, Long provinsiId, 
                            Long kabupatenKotaId, Long tipeSpasId, Long statusId, Pageable pageable);
}