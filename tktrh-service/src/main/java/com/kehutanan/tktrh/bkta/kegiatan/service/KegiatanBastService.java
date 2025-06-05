package com.kehutanan.tktrh.bkta.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.tktrh.bkta.kegiatan.dto.KegiatanBastPageDTO;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanBast;
import com.kehutanan.tktrh.bkta.kegiatan.model.dto.KegiatanBastDTO;

public interface KegiatanBastService {
    
    List<KegiatanBast> findAll();
    
    KegiatanBastDTO findDTOById(Long id);

    KegiatanBast findById(Long id);
    
    KegiatanBast save(KegiatanBast kegiatanBast);
    
    KegiatanBast update(Long id, KegiatanBast kegiatanBast);
    
    void deleteById(Long id);
    
    KegiatanBast uploadKegiatanBastPdf(Long id, List<MultipartFile> pdf);
    
    KegiatanBast deleteKegiatanBastPdf(Long id, List<String> uuidPdf);

    KegiatanBastPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanBastPageDTO findByFiltersWithCache(Long kegiatanId, String identitasBkta, String jenisBkta, 
            List<String> status, Pageable pageable, String baseUrl);

    KegiatanBastPageDTO searchWithCache(Long kegiatanId, String keyWord, Pageable pageable, String baseUrl);
}