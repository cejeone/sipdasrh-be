package com.kehutanan.ppth.master.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.ppth.master.dto.UptdDTO;
import com.kehutanan.ppth.master.dto.UptdPageDTO;
import com.kehutanan.ppth.master.model.Uptd;

public interface UptdService {
    
    
    List<Uptd> findAll();
    
    UptdDTO findDTOById(Long id);

    Uptd findById(Long id);
    
    Uptd save(Uptd uptd);
    
    Uptd update(Long id, Uptd uptd);
    
    void deleteById(Long id);
    

    Uptd uploadUptdFoto(Long id, List<MultipartFile> foto);
    
    Uptd uploadUptdPdf(Long id, List<MultipartFile> pdf);
    
    Uptd uploadUptdVideo(Long id, List<MultipartFile> video);
    
    Uptd uploadUptdShp(Long id, List<MultipartFile> shp);

    Uptd deleteUptdFoto(Long id, List<String> uuidFoto);
    
    Uptd deleteUptdPdf(Long id, List<String> uuidPdf);
    
    Uptd deleteUptdVideo(Long id, List<String> uuidVideo);
    
    Uptd deleteUptdShp(Long id, List<String> uuidShp);

    UptdPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    UptdPageDTO findByFiltersWithCache(String namaBpdas, String namaUptd, List<String> bpdasList,
            Pageable pageable, String baseUrl);

    UptdPageDTO searchWithCache(String keyWord, Pageable pageable, String string);
}