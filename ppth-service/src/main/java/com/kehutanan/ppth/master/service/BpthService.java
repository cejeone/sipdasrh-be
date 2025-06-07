package com.kehutanan.ppth.master.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kehutanan.ppth.master.model.Bpth;

import java.util.List;

public interface BpthService {
    
    Page<Bpth> findAll(Pageable pageable);
    
    List<Bpth> findAll();
    
    Bpth findById(Long id);
    
    Bpth save(Bpth bpth);
    
    Bpth update(Long id, Bpth bpth);
    
    void deleteById(Long id);
    
    Page<Bpth> findByFilters(String namaBpth, Long provinsiId, Long kabupatenKotaId, Pageable pageable);
}