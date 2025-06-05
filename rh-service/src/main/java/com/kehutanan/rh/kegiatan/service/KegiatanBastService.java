package com.kehutanan.rh.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.rh.kegiatan.dto.KegiatanBastPageDTO;
import com.kehutanan.rh.kegiatan.model.KegiatanBast;
import com.kehutanan.rh.kegiatan.model.dto.KegiatanBastDTO;

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

    KegiatanBastPageDTO findByFiltersWithCache(Long kegiatanId, String keterangan, List<String> jenis,
            Pageable pageable, String baseUrl);

    KegiatanBastPageDTO searchWithCache(Long kegiatanId,String keyWord, Pageable pageable, String baseUrl);
}