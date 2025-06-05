package com.kehutanan.tktrh.ppkh.serahterima.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.ppkh.serahterima.dto.BastPusatPageDTO;
import com.kehutanan.tktrh.ppkh.serahterima.model.BastPusat;
import com.kehutanan.tktrh.ppkh.serahterima.model.dto.BastPusatDTO;

public interface BastPusatService {
    
    List<BastPusat> findAll();
    
    BastPusatDTO findDTOById(Long id);

    BastPusat findById(Long id);
    
    BastPusat save(BastPusat bastPusat);
    
    BastPusat update(Long id, BastPusat bastPusat);
    
    void deleteById(Long id);

    BastPusatPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    BastPusatPageDTO findByFiltersWithCache(String keterangan, List<String> bpdasList,
            Pageable pageable, String baseUrl);

    BastPusatPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}