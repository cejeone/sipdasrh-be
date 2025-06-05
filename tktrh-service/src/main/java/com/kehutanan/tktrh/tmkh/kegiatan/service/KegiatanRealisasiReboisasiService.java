package com.kehutanan.tktrh.tmkh.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanRealisasiReboisasiDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanRealisasiReboisasiPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanRealisasiReboisasi;

public interface KegiatanRealisasiReboisasiService {
    
    List<KegiatanRealisasiReboisasi> findAll();
    
    KegiatanRealisasiReboisasiDTO findDTOById(Long id);

    KegiatanRealisasiReboisasi findById(Long id);
    
    KegiatanRealisasiReboisasi save(KegiatanRealisasiReboisasi kegiatanRealisasiReboisasi);
    
    KegiatanRealisasiReboisasi update(Long id, KegiatanRealisasiReboisasi kegiatanRealisasiReboisasi);
    
    void deleteById(Long id);
    
    KegiatanRealisasiReboisasiPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanRealisasiReboisasiPageDTO findByFiltersWithCache(Long kegiatanId, String fungsiKawasan, String keterangan, 
            List<String> status, Pageable pageable, String baseUrl);

    KegiatanRealisasiReboisasiPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}
