package com.kehutanan.rh.bimtek.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.rh.bimtek.dto.BimtekDTO;
import com.kehutanan.rh.bimtek.dto.BimtekPageDTO;
import com.kehutanan.rh.bimtek.model.Bimtek;

public interface BimtekService {
    
    List<Bimtek> findAll();
    
    BimtekDTO findDTOById(Long id);

    Bimtek findById(Long id);
    
    Bimtek save(Bimtek bimtek);
    
    Bimtek update(Long id, Bimtek bimtek);
    
    void deleteById(Long id);
    
    Bimtek uploadBimtekFoto(Long id, List<MultipartFile> foto);
    
    Bimtek uploadBimtekPdf(Long id, List<MultipartFile> pdf);
    
    Bimtek uploadBimtekVideo(Long id, List<MultipartFile> video);

    Bimtek deleteBimtekFoto(Long id, List<String> uuidFoto);
    
    Bimtek deleteBimtekPdf(Long id, List<String> uuidPdf);
    
    Bimtek deleteBimtekVideo(Long id, List<String> uuidVideo);

    BimtekPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    BimtekPageDTO findByFiltersWithCache(String namaBimtek, List<String> bpdasList,
            Pageable pageable, String baseUrl);

    BimtekPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}