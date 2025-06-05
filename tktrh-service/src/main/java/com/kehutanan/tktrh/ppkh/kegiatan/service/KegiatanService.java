package com.kehutanan.tktrh.ppkh.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.tktrh.ppkh.kegiatan.dto.KegiatanDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.dto.KegiatanPageDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.model.Kegiatan;

public interface KegiatanService {
    
    List<Kegiatan> findAll();
    
    KegiatanDTO findDTOById(Long id);

    Kegiatan findById(Long id);
    
    Kegiatan save(Kegiatan kegiatan);
    
    Kegiatan update(Long id, Kegiatan kegiatan);
    
    void deleteById(Long id);
    
    // File upload methods
    Kegiatan uploadPerijinanPdf(Long id, List<MultipartFile> perijinanPdf);
    
    Kegiatan uploadRiwayatSk(Long id, List<MultipartFile> riwayatSk);
    
    Kegiatan uploadPakPdfShp(Long id, List<MultipartFile> pakPdfShp);
    
    Kegiatan uploadFungsiKawasan(Long id, List<MultipartFile> fungsiKawasan);
    
    Kegiatan uploadRantekPdf(Long id, List<MultipartFile> rantekPdf);
    
    Kegiatan uploadRencanaRealisasi(Long id, List<MultipartFile> rencanaRealisasi);
    
    Kegiatan uploadBastReboRehab(Long id, List<MultipartFile> bastReboRehab);
    
    Kegiatan uploadBastZip(Long id, List<MultipartFile> bastZip);
    
    // File deletion methods
    Kegiatan deletePerijinanPdf(Long id, List<String> uuidPerijinanPdf);
    
    Kegiatan deleteRiwayatSk(Long id, List<String> uuidRiwayatSk);
    
    Kegiatan deletePakPdfShp(Long id, List<String> uuidPakPdfShp);
    
    Kegiatan deleteFungsiKawasan(Long id, List<String> uuidFungsiKawasan);
    
    Kegiatan deleteRantekPdf(Long id, List<String> uuidRantekPdf);
    
    Kegiatan deleteRencanaRealisasi(Long id, List<String> uuidRencanaRealisasi);
    
    Kegiatan deleteBastReboRehab(Long id, List<String> uuidBastReboRehab);
    
    Kegiatan deleteBastZip(Long id, List<String> uuidBastZip);
    
    // Pagination and search methods
    KegiatanPageDTO findAllWithCache(Pageable pageable, String baseUrl);
    
    KegiatanPageDTO findByFiltersWithCache(String namaKegiatan, String peruntukan, List<String> tahunList,
            Pageable pageable, String baseUrl);
    
    KegiatanPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}
