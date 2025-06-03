package com.kehutanan.pepdas.master.service;

import com.kehutanan.pepdas.master.model.Institusi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InstitusiService {
    
    Page<Institusi> findAll(Pageable pageable);
    
    List<Institusi> findAll();
    
    Institusi findById(Long id);
    
    Institusi save(Institusi institusi);
    
    Institusi update(Long id, Institusi institusi);
    
    void deleteById(Long id);
    
    Page<Institusi> findByFilters(String nama, String email, Long tipeInstitusiId, Long provinsiId, Long kabupatenKotaId, Pageable pageable);
}