package com.kehutanan.tktrh.ppkh.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.ppkh.kegiatan.dto.KegiatanRencanaRealisasiPageDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRencanaRealisasi;
import com.kehutanan.tktrh.ppkh.kegiatan.model.dto.KegiatanRencanaRealisasiDTO;

public interface KegiatanRencanaRealisasiService {
    
    List<KegiatanRencanaRealisasi> findAll();
    
    KegiatanRencanaRealisasiDTO findDTOById(Long id);

    KegiatanRencanaRealisasi findById(Long id);
    
    KegiatanRencanaRealisasi save(KegiatanRencanaRealisasi kegiatanRencanaRealisasi);
    
    KegiatanRencanaRealisasi update(Long id, KegiatanRencanaRealisasi kegiatanRencanaRealisasi);
    
    void deleteById(Long id);
    
    KegiatanRencanaRealisasiPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanRencanaRealisasiPageDTO findByFiltersWithCache(Long kegiatanId,String fungsiKawasan, String keterangan, List<String> status,
            Pageable pageable, String baseUrl);

    KegiatanRencanaRealisasiPageDTO searchWithCache(Long kegiatanId,String keyWord, Pageable pageable, String baseUrl);
}