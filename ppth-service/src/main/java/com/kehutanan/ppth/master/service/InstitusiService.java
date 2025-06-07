package com.kehutanan.ppth.master.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kehutanan.ppth.master.model.Institusi;

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