package com.kehutanan.rm.kegiatan.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.rm.kegiatan.dto.KegiatanPemeliharaanPemupukanDTO;
import com.kehutanan.rm.kegiatan.dto.KegiatanPemeliharaanPemupukanPageDTO;
import com.kehutanan.rm.kegiatan.model.KegiatanPemeliharaanPemupukan;

public interface KegiatanPemeliharaanPemupukanService {
    
    List<KegiatanPemeliharaanPemupukan> findAll();
    
    KegiatanPemeliharaanPemupukanDTO findDTOById(Long id);

    KegiatanPemeliharaanPemupukan findById(Long id);
    
    KegiatanPemeliharaanPemupukan save(KegiatanPemeliharaanPemupukan pemupukan);
    
    KegiatanPemeliharaanPemupukan update(Long id, KegiatanPemeliharaanPemupukan pemupukan);
    
    void deleteById(Long id);

    KegiatanPemeliharaanPemupukanPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanPemeliharaanPemupukanPageDTO findByFiltersWithCache(Long kegiatanId, String keterangan,List<String> jenis, Pageable pageable, String baseUrl);

    KegiatanPemeliharaanPemupukanPageDTO searchWithCache(Long kegiatanId, String keyWord, Pageable pageable, String baseUrl);
}