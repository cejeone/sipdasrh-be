package com.kehutanan.tktrh.master.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.master.model.Provinsi;

import java.util.List;

public interface ProvinsiService {
    
    Page<Provinsi> findAll(Pageable pageable);
    
    List<Provinsi> findAll();
    
    Provinsi findById(Long id);
    
    Provinsi save(Provinsi provinsi);
    
    Provinsi update(Long id, Provinsi provinsi);
    
    void deleteById(Long id);
    
    Page<Provinsi> findByFilters(String namaProvinsi, String kodeDepdagri, Pageable pageable);
}