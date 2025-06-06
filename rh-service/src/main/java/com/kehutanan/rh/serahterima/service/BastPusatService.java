package com.kehutanan.rh.serahterima.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.rh.serahterima.dto.BastPusatPageDTO;
import com.kehutanan.rh.serahterima.model.BastPusat;
import com.kehutanan.rh.serahterima.model.dto.BastPusatDTO;

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