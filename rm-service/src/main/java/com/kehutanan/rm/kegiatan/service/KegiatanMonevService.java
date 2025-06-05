package com.kehutanan.rm.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.rm.kegiatan.dto.KegiatanMonevPageDTO;
import com.kehutanan.rm.kegiatan.model.KegiatanMonev;
import com.kehutanan.rm.kegiatan.model.dto.KegiatanMonevDTO;

public interface KegiatanMonevService {
    
    List<KegiatanMonev> findAll();
    
    KegiatanMonevDTO findDTOById(Long id);

    KegiatanMonev findById(Long id);
    
    KegiatanMonev save(KegiatanMonev kegiatanMonev);
    
    KegiatanMonev update(Long id, KegiatanMonev kegiatanMonev);
    
    void deleteById(Long id);
    
    KegiatanMonev uploadPdf(Long id, List<MultipartFile> pdf);

    KegiatanMonev deletePdf(Long id, List<String> uuidPdf);

    KegiatanMonevPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanMonevPageDTO findByFiltersWithCache(Long kegiatanId, String kegiatan, List<String> statusId, 
            Pageable pageable, String baseUrl);

    KegiatanMonevPageDTO searchWithCache(Long kegiatanId, String keyWord, Pageable pageable, String baseUrl);
}