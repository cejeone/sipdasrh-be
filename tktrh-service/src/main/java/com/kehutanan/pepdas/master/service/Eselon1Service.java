package com.kehutanan.pepdas.master.service;

import com.kehutanan.pepdas.master.model.Eselon1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface Eselon1Service {
    
    Page<Eselon1> findAll(Pageable pageable);
    
    List<Eselon1> findAll();
    
    Eselon1 findById(Long id);
    
    Eselon1 save(Eselon1 eselon1);
    
    Eselon1 update(Long id, Eselon1 eselon1);
    
    void deleteById(Long id);
    
    Page<Eselon1> findByFilters(String nama, String pejabat, Pageable pageable);
}