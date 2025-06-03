package com.kehutanan.tktrh.master.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.master.model.Das;

import java.util.List;

public interface DasService {
    
    Page<Das> findAll(Pageable pageable);
    
    List<Das> findAll();
    
    Das findById(Long id);
    
    Das save(Das das);
    
    Das update(Long id, Das das);
    
    void deleteById(Long id);
    
    Page<Das> findByFilters(String namaDas, Long bpdasId, Pageable pageable);
}