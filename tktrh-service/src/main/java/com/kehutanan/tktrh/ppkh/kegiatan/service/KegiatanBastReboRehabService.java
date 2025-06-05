package com.kehutanan.tktrh.ppkh.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.ppkh.kegiatan.dto.KegiatanBastReboRehabPageDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanBastReboRehab;
import com.kehutanan.tktrh.ppkh.kegiatan.model.dto.KegiatanBastReboRehabDTO;

public interface KegiatanBastReboRehabService {
    
    List<KegiatanBastReboRehab> findAll();
    
    KegiatanBastReboRehabDTO findDTOById(Long id);

    KegiatanBastReboRehab findById(Long id);
    
    KegiatanBastReboRehab save(KegiatanBastReboRehab kegiatanBastReboRehab);
    
    KegiatanBastReboRehab update(Long id, KegiatanBastReboRehab kegiatanBastReboRehab);
    
    void deleteById(Long id);
    
    KegiatanBastReboRehabPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanBastReboRehabPageDTO findByFiltersWithCache(Long kegiatanId,String tahun, String keterangan, 
            List<String> statusList, Pageable pageable, String baseUrl);

    KegiatanBastReboRehabPageDTO searchWithCache(Long kegiatanId,String keyWord, Pageable pageable, String baseUrl);
}
