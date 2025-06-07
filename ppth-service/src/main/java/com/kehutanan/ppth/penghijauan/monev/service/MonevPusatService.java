package com.kehutanan.ppth.penghijauan.monev.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.ppth.penghijauan.monev.dto.MonevPusatPageDTO;
import com.kehutanan.ppth.penghijauan.monev.model.MonevPusat;
import com.kehutanan.ppth.penghijauan.monev.model.dto.MonevPusatDTO;

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