package com.kehutanan.tktrh.ppkh.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.tktrh.ppkh.kegiatan.dto.KegiatanPageDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.ppkh.kegiatan.model.dto.KegiatanDTO;

public interface KegiatanService {
    
    List<Kegiatan> findAll();
    
    KegiatanDTO findDTOById(Long id);

    Kegiatan findById(Long id);
    
    Kegiatan save(Kegiatan kegiatan);
    
    Kegiatan update(Long id, Kegiatan kegiatan);
    
    void deleteById(Long id);
    
    // File upload methods
    Kegiatan uploadPerijinanPdf(Long id, List<MultipartFile> perijinanPdf);
    
    Kegiatan uploadPakPdfShp(Long id, List<MultipartFile> pakPdfShp);
    
    Kegiatan uploadRantekPdf(Long id, List<MultipartFile> rantekPdf);
    
    Kegiatan uploadBastZip(Long id, List<MultipartFile> bastZip);
    
    // File deletion methods
    Kegiatan deletePerijinanPdf(Long id, List<String> uuidPerijinanPdf);
    
    Kegiatan deletePakPdfShp(Long id, List<String> uuidPakPdfShp);
    
    Kegiatan deleteRantekPdf(Long id, List<String> uuidRantekPdf);
    
    Kegiatan deleteBastZip(Long id, List<String> uuidBastZip);
    
    // Pagination and search methods
    KegiatanPageDTO findAllWithCache(Pageable pageable, String baseUrl);
    
    KegiatanPageDTO findByFiltersWithCache(String namaKegiatan, String peruntukan, List<String> tahunList,
            Pageable pageable, String baseUrl);
    
    KegiatanPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}
