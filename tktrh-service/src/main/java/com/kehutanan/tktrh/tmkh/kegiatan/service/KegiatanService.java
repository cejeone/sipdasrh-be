package com.kehutanan.tktrh.tmkh.kegiatan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanDTO;

public interface KegiatanService {
    
    List<Kegiatan> findAll();
    
    KegiatanDTO findDTOById(Long id);

    Kegiatan findById(Long id);
    
    Kegiatan save(Kegiatan kegiatan);
    
    Kegiatan update(Long id, Kegiatan kegiatan);
    
    void deleteById(Long id);
    
    // Perijinan file operations
    Kegiatan uploadPerijinanPdf(Long id, List<MultipartFile> pdf);
    
    // PBAK file operations
    Kegiatan uploadPbakPdfShp(Long id, List<MultipartFile> pdfShp);
    


    Kegiatan uploadRehabPdf(Long id, List<MultipartFile> rehabPdf);


    // BAST file operations
    Kegiatan uploadBastZip(Long id, List<MultipartFile> bastZip);
    
    // Delete file operations
    Kegiatan deletePerijinanPdf(Long id, List<String> uuidPdf);
    Kegiatan deletePbakPdfShp(Long id, List<String> uuidPdfShp);
    Kegiatan deleteRehabPdf(Long id, List<String> uuidRehabPdf);
    Kegiatan deleteBastZip(Long id, List<String> uuidBastZip);
    
    // Paged and cached operations
    KegiatanPageDTO findAllWithCache(Pageable pageable, String baseUrl);
    
    KegiatanPageDTO findByFiltersWithCache(String namaKegiatan, String status, List<String> bpdasList,
            Pageable pageable, String baseUrl);
    
    KegiatanPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}
