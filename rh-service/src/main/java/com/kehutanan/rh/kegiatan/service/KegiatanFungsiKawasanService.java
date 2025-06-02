package com.kehutanan.rh.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.rh.kegiatan.dto.KegiatanFungsiKawasanDTO;
import com.kehutanan.rh.kegiatan.dto.KegiatanFungsiKawasanPageDTO;
import com.kehutanan.rh.kegiatan.model.KegiatanFungsiKawasan;

public interface KegiatanFungsiKawasanService {
    
    List<KegiatanFungsiKawasan> findAll();
    
    KegiatanFungsiKawasanDTO findDTOById(Long id);

    KegiatanFungsiKawasan findById(Long id);
    
    KegiatanFungsiKawasan save(KegiatanFungsiKawasan kegiatanFungsiKawasan);
    
    KegiatanFungsiKawasan update(Long id, KegiatanFungsiKawasan kegiatanFungsiKawasan);
    
    void deleteById(Long id);

    KegiatanFungsiKawasanPageDTO findAllWithCache(Pageable pageable, String baseUrl);
    
    /**
     * Find KegiatanFungsiKawasan by filters with cache
     * 
     * @param kegiatanId Kegiatan ID filter
     * @param keterangan Keterangan filter
     * @param jenis List of types filter
     * @param pageable Pagination information
     * @param baseUrl Base URL for HATEOAS links
     * @return Paged results of KegiatanFungsiKawasan
     */
    KegiatanFungsiKawasanPageDTO findByFiltersWithCache(
        Long kegiatanId, 
        String keterangan, 
        List<String> fungsiKawasan, 
        Pageable pageable, 
        String baseUrl
    );
    
    /**
     * Search KegiatanFungsiKawasan by keyword with cache
     * 
     * @param kegiatanId Kegiatan ID filter
     * @param keyWord Search keyword
     * @param pageable Pagination information
     * @param baseUrl Base URL for HATEOAS links
     * @return Paged results of KegiatanFungsiKawasan
     */
    KegiatanFungsiKawasanPageDTO searchWithCache(
        Long kegiatanId, 
        String keyWord, 
        Pageable pageable, 
        String baseUrl
    );
}