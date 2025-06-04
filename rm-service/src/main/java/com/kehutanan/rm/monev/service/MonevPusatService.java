package com.kehutanan.rm.monev.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.rm.monev.dto.MonevPusatDTO;
import com.kehutanan.rm.monev.dto.MonevPusatPageDTO;
import com.kehutanan.rm.monev.model.MonevPusat;

public interface MonevPusatService {
    
    List<MonevPusat> findAll();
    
    MonevPusatDTO findDTOById(Long id);

    MonevPusat findById(Long id);
    
    MonevPusat save(MonevPusat monevPusat);
    
    MonevPusat update(Long id, MonevPusat monevPusat);
    
    void deleteById(Long id);
    
    MonevPusatPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    MonevPusatPageDTO findByFiltersWithCache(String keterangan, List<String> bpdasList,
            Pageable pageable, String baseUrl);

    MonevPusatPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}