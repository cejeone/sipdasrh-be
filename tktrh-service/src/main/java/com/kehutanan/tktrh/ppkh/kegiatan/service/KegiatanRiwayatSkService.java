package com.kehutanan.tktrh.ppkh.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.tktrh.ppkh.kegiatan.dto.KegiatanRiwayatSkPageDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRiwayatSk;
import com.kehutanan.tktrh.ppkh.kegiatan.model.dto.KegiatanRiwayatSkDTO;

public interface KegiatanRiwayatSkService {
    
    List<KegiatanRiwayatSk> findAll();
    
    KegiatanRiwayatSkDTO findDTOById(Long id);

    KegiatanRiwayatSk findById(Long id);
    
    KegiatanRiwayatSk save(KegiatanRiwayatSk kegiatanRiwayatSk);
    
    KegiatanRiwayatSk update(Long id, KegiatanRiwayatSk kegiatanRiwayatSk);
    
    void deleteById(Long id);
    
    KegiatanRiwayatSk uploadKegiatanRiwayatSkShp(Long id, List<MultipartFile> shp);

    KegiatanRiwayatSk deleteKegiatanRiwayatSkShp(Long id, List<String> uuidShp);

    KegiatanRiwayatSkPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanRiwayatSkPageDTO findByFiltersWithCache(Long kegiatanId,String jenisPerubahan, String keterangan, List<String> status,
            Pageable pageable, String baseUrl);

    KegiatanRiwayatSkPageDTO searchWithCache(Long kegiatanId,String keyWord, Pageable pageable, String baseUrl);
}