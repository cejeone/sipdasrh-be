package com.kehutanan.rm.master.service;

import com.kehutanan.rm.master.model.KabupatenKota;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KabupatenKotaService {
    
    Page<KabupatenKota> findAll(Pageable pageable);
    
    List<KabupatenKota> findAll();
    
    KabupatenKota findById(Long id);
    
    KabupatenKota save(KabupatenKota kabupatenKota);
    
    KabupatenKota update(Long id, KabupatenKota kabupatenKota);
    
    void deleteById(Long id);
    
    Page<KabupatenKota> findByFilters(String kabupatenKota, String kodeDepdagri, Long provinsiId, Pageable pageable);
}