package com.kehutanan.pepdas.master.service;

import com.kehutanan.pepdas.master.model.Provinsi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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