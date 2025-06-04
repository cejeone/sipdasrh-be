package com.kehutanan.rm.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.rm.kegiatan.dto.KegiatanPemeliharaanSulamanDTO;
import com.kehutanan.rm.kegiatan.dto.KegiatanPemeliharaanSulamanPageDTO;
import com.kehutanan.rm.kegiatan.model.KegiatanPemeliharaanSulaman;

public interface KegiatanPemeliharaanSulamanService {
    
    List<KegiatanPemeliharaanSulaman> findAll();
    
    KegiatanPemeliharaanSulamanDTO findDTOById(Long id);

    KegiatanPemeliharaanSulaman findById(Long id);
    
    KegiatanPemeliharaanSulaman save(KegiatanPemeliharaanSulaman kegiatanPemeliharaanSulaman);
    
    KegiatanPemeliharaanSulaman update(Long id, KegiatanPemeliharaanSulaman kegiatanPemeliharaanSulaman);
    
    void deleteById(Long id);
    
    KegiatanPemeliharaanSulamanPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanPemeliharaanSulamanPageDTO findByFiltersWithCache(
        Long kegiatanId, 
        String keterangan, 
        List<String> kategori,
        Pageable pageable, 
        String baseUrl);

    KegiatanPemeliharaanSulamanPageDTO searchWithCache(
        Long kegiatanId, 
        String keyWord, 
        Pageable pageable, 
        String baseUrl);
}