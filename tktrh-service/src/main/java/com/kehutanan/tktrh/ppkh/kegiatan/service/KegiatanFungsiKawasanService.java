package com.kehutanan.tktrh.ppkh.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.ppkh.kegiatan.dto.KegiatanFungsiKawasanDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.dto.KegiatanFungsiKawasanPageDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanFungsiKawasan;

public interface KegiatanFungsiKawasanService {
    
    List<KegiatanFungsiKawasan> findAll();
    
    KegiatanFungsiKawasanDTO findDTOById(Long id);

    KegiatanFungsiKawasan findById(Long id);
    
    KegiatanFungsiKawasan save(KegiatanFungsiKawasan kegiatanFungsiKawasan);
    
    KegiatanFungsiKawasan update(Long id, KegiatanFungsiKawasan kegiatanFungsiKawasan);
    
    void deleteById(Long id);
    
    KegiatanFungsiKawasanPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanFungsiKawasanPageDTO findByFiltersWithCache(Long kegiatanId,String fungsiKawasan, String keterangan, List<String> statusList,
            Pageable pageable, String baseUrl);

    KegiatanFungsiKawasanPageDTO searchWithCache(Long kegiatanId,String keyWord, Pageable pageable, String baseUrl);
}
