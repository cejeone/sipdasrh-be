package com.kehutanan.tktrh.tmkh.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.Kegiatan;

public interface KegiatanService {
    
    List<Kegiatan> findAll();
    
    KegiatanDTO findDTOById(Long id);

    Kegiatan findById(Long id);
    
    Kegiatan save(Kegiatan kegiatan);
    
    Kegiatan update(Long id, Kegiatan kegiatan);
    
    void deleteById(Long id);
    
    // Perijinan file operations
    Kegiatan uploadPerijinanPdf(Long id, List<MultipartFile> pdf);
    Kegiatan uploadRiwayatSk(Long id, List<MultipartFile> sk);
    
    // PBAK file operations
    Kegiatan uploadPbakPdfShp(Long id, List<MultipartFile> pdfShp);
    
    // Lahan Pengganti file operations
    Kegiatan uploadFungsiKawasanLahanPengganti(Long id, List<MultipartFile> lahanPengganti);
    
    // Rehabilitasi file operations
    Kegiatan uploadFungsiKawasanRehab(Long id, List<MultipartFile> fungsiKawasanRehab);
    Kegiatan uploadRehabPdf(Long id, List<MultipartFile> rehabPdf);
    
    // Kinerja file operations
    Kegiatan uploadRealisasiReboisasi(Long id, List<MultipartFile> realisasi);
    
    // Monev file operations
    Kegiatan uploadMonev(Long id, List<MultipartFile> monev);
    
    // BAST file operations
    Kegiatan uploadBastRehabDas(Long id, List<MultipartFile> bastRehab);
    Kegiatan uploadBastZip(Long id, List<MultipartFile> bastZip);
    
    // Delete file operations
    Kegiatan deletePerijinanPdf(Long id, List<String> uuidPdf);
    Kegiatan deleteRiwayatSk(Long id, List<String> uuidSk);
    Kegiatan deletePbakPdfShp(Long id, List<String> uuidPdfShp);
    Kegiatan deleteFungsiKawasanLahanPengganti(Long id, List<String> uuidLahanPengganti);
    Kegiatan deleteFungsiKawasanRehab(Long id, List<String> uuidFungsiRehab);
    Kegiatan deleteRehabPdf(Long id, List<String> uuidRehabPdf);
    Kegiatan deleteRealisasiReboisasi(Long id, List<String> uuidRealisasi);
    Kegiatan deleteMonev(Long id, List<String> uuidMonev);
    Kegiatan deleteBastRehabDas(Long id, List<String> uuidBastRehab);
    Kegiatan deleteBastZip(Long id, List<String> uuidBastZip);
    
    // Paged and cached operations
    KegiatanPageDTO findAllWithCache(Pageable pageable, String baseUrl);
    
    KegiatanPageDTO findByFiltersWithCache(String namaKegiatan, String status, List<String> bpdasList,
            Pageable pageable, String baseUrl);
    
    KegiatanPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}
