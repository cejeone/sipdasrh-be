package com.kehutanan.tktrh.tmkh.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanFungsiKawasanLahanPenggantiPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanFungsiKawasanLahanPengganti;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanFungsiKawasanLahanPenggantiDTO;

public interface KegiatanFungsiKawasanLahanPenggantiService {
    
    List<KegiatanFungsiKawasanLahanPengganti> findAll();
    
    KegiatanFungsiKawasanLahanPenggantiDTO findDTOById(Long id);

    KegiatanFungsiKawasanLahanPengganti findById(Long id);
    
    KegiatanFungsiKawasanLahanPengganti save(KegiatanFungsiKawasanLahanPengganti kegiatanFungsiKawasanLahanPengganti);
    
    KegiatanFungsiKawasanLahanPengganti update(Long id, KegiatanFungsiKawasanLahanPengganti kegiatanFungsiKawasanLahanPengganti);
    
    void deleteById(Long id);
    
    KegiatanFungsiKawasanLahanPenggantiPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanFungsiKawasanLahanPenggantiPageDTO findByFiltersWithCache(Long kegiatanId, String fungsiKawasan, String keterangan, List<String> statusList,
            Pageable pageable, String baseUrl);

    KegiatanFungsiKawasanLahanPenggantiPageDTO searchWithCache(Long kegiatanId, String keyWord, Pageable pageable, String baseUrl);
}
