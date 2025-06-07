package com.kehutanan.ppth.mataair.serahterima.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.ppth.mataair.serahterima.dto.BastPageDTO;
import com.kehutanan.ppth.mataair.serahterima.model.Bast;
import com.kehutanan.ppth.mataair.serahterima.model.dto.BastDTO;

public interface BastService {
    
    List<Bast> findAll();
    
    BastDTO findDTOById(Long id);

    Bast findById(Long id);
    
    Bast save(Bast bast);
    
    Bast update(Long id, Bast bast);
    
    void deleteById(Long id);
    
    Bast uploadBastPdf(Long id, List<MultipartFile> pdf);

    Bast deleteBastPdf(Long id, List<String> uuidPdf);

    BastPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    BastPageDTO findByFiltersWithCache(String nomor, String kontrak, List<String> bpdas,
            Pageable pageable, String baseUrl);

    BastPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}