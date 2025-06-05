package com.kehutanan.tktrh.tmkh.serahterima.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.tktrh.tmkh.serahterima.dto.BastPusatDTO;
import com.kehutanan.tktrh.tmkh.serahterima.dto.BastPusatPageDTO;
import com.kehutanan.tktrh.tmkh.serahterima.model.BastPusat;

public interface BastPusatService {
    
    List<BastPusat> findAll();
    
    BastPusatDTO findDTOById(Long id);

    BastPusat findById(Long id);
    
    BastPusat save(BastPusat bastPusat);
    
    BastPusat update(Long id, BastPusat bastPusat);
    
    void deleteById(Long id);
    
    // Optional file handling methods if needed in the future
    // BastPusat uploadDocuments(Long id, List<MultipartFile> documents);
    // BastPusat deleteDocuments(Long id, List<String> documentIds);

    BastPusatPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    BastPusatPageDTO findByFiltersWithCache(String programName, String keterangan, List<String> bpdasList,
            Pageable pageable, String baseUrl);

    BastPusatPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}
