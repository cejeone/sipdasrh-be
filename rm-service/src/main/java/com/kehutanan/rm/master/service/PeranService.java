package com.kehutanan.rm.master.service;

import com.kehutanan.rm.master.model.Peran;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PeranService {
    
    Page<Peran> findAll(Pageable pageable);
    
    List<Peran> findAll();
    
    Peran findById(Long id);
    
    Peran save(Peran peran);
    
    Peran update(Long id, Peran peran);
    
    void deleteById(Long id);
    
    Page<Peran> findByFilters(String nama, String deskripsi, Long statusId, Pageable pageable);
}