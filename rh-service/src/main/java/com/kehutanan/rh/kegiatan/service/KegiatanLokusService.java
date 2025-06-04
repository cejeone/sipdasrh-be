package com.kehutanan.rh.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.rh.kegiatan.dto.KegiatanLokusPageDTO;
import com.kehutanan.rh.kegiatan.model.KegiatanLokus;
import com.kehutanan.rh.kegiatan.model.dto.KegiatanLokusDTO;

public interface KegiatanLokusService {
    
    List<KegiatanLokus> findAll();
    
    KegiatanLokusDTO findDTOById(Long id);

    KegiatanLokus findById(Long id);
    
    KegiatanLokus save(KegiatanLokus kegiatanLokus);
    
    KegiatanLokus update(Long id, KegiatanLokus kegiatanLokus);
    
    void deleteById(Long id);
    
    KegiatanLokus uploadKegiatanLokusShp(Long id, List<MultipartFile> shp);

    KegiatanLokus deleteKegiatanLokusShp(Long id, List<String> uuidShp);

    KegiatanLokusPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanLokusPageDTO findByFiltersWithCache(Long kegiatanId, String keterangan, List<String> provinsi,
            Pageable pageable, String baseUrl);

    KegiatanLokusPageDTO searchWithCache(Long kegiatanId, String keyWord, Pageable pageable, String baseUrl);
}