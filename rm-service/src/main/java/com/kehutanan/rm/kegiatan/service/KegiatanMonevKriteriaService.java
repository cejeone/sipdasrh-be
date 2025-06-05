package com.kehutanan.rm.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.rm.kegiatan.dto.KegiatanMonevKriteriaPageDTO;
import com.kehutanan.rm.kegiatan.model.KegiatanMonevKriteria;
import com.kehutanan.rm.kegiatan.model.dto.KegiatanMonevKriteriaDTO;

public interface KegiatanMonevKriteriaService {
    
    List<KegiatanMonevKriteria> findAll();
    
    KegiatanMonevKriteriaDTO findDTOById(Long id);

    KegiatanMonevKriteria findById(Long id);
    
    KegiatanMonevKriteria save(KegiatanMonevKriteria kegiatanMonevKriteria);
    
    KegiatanMonevKriteria update(Long id, KegiatanMonevKriteria kegiatanMonevKriteria);
    
    void deleteById(Long id);
    
    List<KegiatanMonevKriteria> findByKegiatanMonevId(Long kegiatanMonevId);
    
    KegiatanMonevKriteriaPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanMonevKriteriaPageDTO findByFiltersWithCache(
            Long kegiatanMonevId, String namaAktivitas, Pageable pageable, String baseUrl);

    KegiatanMonevKriteriaPageDTO searchWithCache(Long kegiatanMonevId,String keyWord, Pageable pageable, String baseUrl);
}