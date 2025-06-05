package com.kehutanan.tktrh.tmkh.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanRiwayatSkDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanRiwayatSkPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanRiwayatSk;

public interface KegiatanRiwayatSkService {
    
    List<KegiatanRiwayatSk> findAll();
    
    KegiatanRiwayatSkDTO findDTOById(Long id);

    KegiatanRiwayatSk findById(Long id);
    
    KegiatanRiwayatSk save(KegiatanRiwayatSk riwayatSk);
    
    KegiatanRiwayatSk update(Long id, KegiatanRiwayatSk riwayatSk);
    
    void deleteById(Long id);
    
    KegiatanRiwayatSk uploadShp(Long id, List<MultipartFile> shp);

    KegiatanRiwayatSk deleteShp(Long id, List<String> uuidShp);

    KegiatanRiwayatSkPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanRiwayatSkPageDTO findByFiltersWithCache(Long kegiatanId, String skPenetapan, String keterangan, List<String> statusList,
            Pageable pageable, String baseUrl);

    KegiatanRiwayatSkPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}
