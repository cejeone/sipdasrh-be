package com.kehutanan.tktrh.tmkh.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanBastRehabDasPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanBastRehabDas;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanBastRehabDasDTO;

public interface KegiatanBastRehabDasService {
    
    List<KegiatanBastRehabDas> findAll();
    
    KegiatanBastRehabDasDTO findDTOById(Long id);

    KegiatanBastRehabDas findById(Long id);
    
    KegiatanBastRehabDas save(KegiatanBastRehabDas kegiatanBastRehabDas);
    
    KegiatanBastRehabDas update(Long id, KegiatanBastRehabDas kegiatanBastRehabDas);
    
    void deleteById(Long id);
    
    KegiatanBastRehabDasPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanBastRehabDasPageDTO findByFiltersWithCache(Long kegiatanId , String keterangan, 
            List<String> statusList, Pageable pageable, String baseUrl);

    KegiatanBastRehabDasPageDTO searchWithCache(Long kegiatanId, String keyWord, Pageable pageable, String baseUrl);
}
