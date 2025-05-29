package com.kehutanan.superadmin.master.service;

import com.kehutanan.superadmin.master.model.Bpth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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