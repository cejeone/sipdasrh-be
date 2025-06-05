package com.kehutanan.tktrh.tmkh.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanMonevKriteriaPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanMonevKriteria;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanMonevKriteriaDTO;

public interface KegiatanMonevKriteriaService {
    
    List<KegiatanMonevKriteria> findAll();
    
    KegiatanMonevKriteriaDTO findDTOById(Long id);

    KegiatanMonevKriteria findById(Long id);
    
    KegiatanMonevKriteria save(KegiatanMonevKriteria kegiatanMonevKriteria);
    
    KegiatanMonevKriteria update(Long id, KegiatanMonevKriteria kegiatanMonevKriteria);
    
    void deleteById(Long id);
    
    KegiatanMonevKriteriaPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanMonevKriteriaPageDTO findByFiltersWithCache(Long monevId, String catatan, List<String> aktivitasList,
            Pageable pageable, String baseUrl);

    KegiatanMonevKriteriaPageDTO searchWithCache(Long monevId, String keyWord, Pageable pageable, String baseUrl);
}
