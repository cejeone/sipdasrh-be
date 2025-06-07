package com.kehutanan.ppth.perijinan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.ppth.perijinan.dto.PerijinanPageDTO;
import com.kehutanan.ppth.perijinan.model.Perijinan;
import com.kehutanan.ppth.perijinan.model.dto.PerijinanDTO;

public interface PerijinanService {
    
    List<Perijinan> findAll();
    
    PerijinanDTO findDTOById(Long id);

    Perijinan findById(Long id);
    
    Perijinan save(Perijinan perijinan);
    
    Perijinan update(Long id, Perijinan perijinan);
    
    void deleteById(Long id);
    
    Perijinan uploadDokumenAwalPdf(Long id, List<MultipartFile> pdf);
    
    Perijinan uploadDokumenBastPdf(Long id, List<MultipartFile> pdf);

    Perijinan deleteDokumenAwalPdf(Long id, List<String> uuidPdf);
    
    Perijinan deleteDokumenBastPdf(Long id, List<String> uuidPdf);

    PerijinanPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    PerijinanPageDTO findByFiltersWithCache(String pelakuUsaha, List<String> bpdas, Pageable pageable, String baseUrl);

    PerijinanPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}