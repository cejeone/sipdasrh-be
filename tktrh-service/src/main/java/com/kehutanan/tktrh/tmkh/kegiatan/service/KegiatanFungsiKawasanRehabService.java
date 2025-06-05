package com.kehutanan.tktrh.tmkh.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanFungsiKawasanRehabPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanFungsiKawasanRehab;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanFungsiKawasanRehabDTO;

public interface KegiatanFungsiKawasanRehabService {
    
    List<KegiatanFungsiKawasanRehab> findAll();
    
    KegiatanFungsiKawasanRehabDTO findDTOById(Long id);

    KegiatanFungsiKawasanRehab findById(Long id);
    
    KegiatanFungsiKawasanRehab save(KegiatanFungsiKawasanRehab kegiatanFungsiKawasanRehab);
    
    KegiatanFungsiKawasanRehab update(Long id, KegiatanFungsiKawasanRehab kegiatanFungsiKawasanRehab);
    
    void deleteById(Long id);

    KegiatanFungsiKawasanRehabPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanFungsiKawasanRehabPageDTO findByFiltersWithCache(Long kegiatanId, String fungsiKawasan, String keterangan, List<String> statusList,
            Pageable pageable, String baseUrl);

    KegiatanFungsiKawasanRehabPageDTO searchWithCache(Long kegiatanId, String keyWord, Pageable pageable, String baseUrl);
}
