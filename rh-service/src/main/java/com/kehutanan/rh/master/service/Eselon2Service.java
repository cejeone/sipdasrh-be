package com.kehutanan.rh.master.service;

import com.kehutanan.rh.master.model.Eselon2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface Eselon2Service {
    
    Page<Eselon2> findAll(Pageable pageable);
    
    List<Eselon2> findAll();
    
    Eselon2 findById(Long id);
    
    Eselon2 save(Eselon2 eselon2);
    
    Eselon2 update(Long id, Eselon2 eselon2);
    
    void deleteById(Long id);
    
    Page<Eselon2> findByFilters(String nama, String pejabat, Long eselon1Id, Pageable pageable);
}