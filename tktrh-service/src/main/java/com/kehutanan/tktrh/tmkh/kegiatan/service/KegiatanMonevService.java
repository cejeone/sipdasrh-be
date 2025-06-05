package com.kehutanan.tktrh.tmkh.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanMonevPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanMonev;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanMonevDTO;

public interface KegiatanMonevService {
    
    List<KegiatanMonev> findAll();
    
    KegiatanMonevDTO findDTOById(Long id);

    KegiatanMonev findById(Long id);
    
    KegiatanMonev save(KegiatanMonev kegiatanMonev);
    
    KegiatanMonev update(Long id, KegiatanMonev kegiatanMonev);
    
    void deleteById(Long id);
    
    KegiatanMonev uploadKegiatanMonevPdf(Long id, List<MultipartFile> pdf);

    KegiatanMonev deleteKegiatanMonevPdf(Long id, List<String> uuidPdf);

    KegiatanMonevPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanMonevPageDTO findByFiltersWithCache(Long kegiatanId, String nomor, String deskripsi, List<String> statuList,
            Pageable pageable, String baseUrl);

    KegiatanMonevPageDTO searchWithCache(Long kegiatanId, String keyWord, Pageable pageable, String baseUrl);
}
