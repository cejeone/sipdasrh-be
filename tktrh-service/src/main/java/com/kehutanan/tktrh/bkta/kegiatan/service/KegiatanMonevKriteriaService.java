package com.kehutanan.tktrh.bkta.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.bkta.kegiatan.dto.KegiatanMonevKriteriaPageDTO;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanMonevKriteria;
import com.kehutanan.tktrh.bkta.kegiatan.model.dto.KegiatanMonevKriteriaDTO;

public interface KegiatanMonevKriteriaService {
    
    List<KegiatanMonevKriteria> findAll();
    
    KegiatanMonevKriteriaDTO findDTOById(Long id);

    KegiatanMonevKriteria findById(Long id);
    
    KegiatanMonevKriteria save(KegiatanMonevKriteria kriteria);
    
    KegiatanMonevKriteria update(Long id, KegiatanMonevKriteria kriteria);
    
    void deleteById(Long id);

    KegiatanMonevKriteriaPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanMonevKriteriaPageDTO findByFiltersWithCache(Long kegiatanMonevId, String aktivitas, String target, List<String> realisasi,
            Pageable pageable, String baseUrl);

    KegiatanMonevKriteriaPageDTO searchWithCache(Long kegiatanMonevId, String keyWord, Pageable pageable, String baseUrl);
}