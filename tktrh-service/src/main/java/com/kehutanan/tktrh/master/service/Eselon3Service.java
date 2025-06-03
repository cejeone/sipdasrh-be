package com.kehutanan.tktrh.master.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.master.model.Eselon3;

import java.util.List;

public interface Eselon3Service {
    
    Page<Eselon3> findAll(Pageable pageable);
    
    List<Eselon3> findAll();
    
    Eselon3 findById(Long id);
    
    Eselon3 save(Eselon3 eselon3);
    
    Eselon3 update(Long id, Eselon3 eselon3);
    
    void deleteById(Long id);
    
    Page<Eselon3> findByFilters(String nama, String pejabat, Long eselon2Id, Pageable pageable);
}