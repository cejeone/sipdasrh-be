package com.kehutanan.pepdas.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.pepdas.kegiatan.dto.KegiatanPageDTO;
import com.kehutanan.pepdas.kegiatan.model.Kegiatan;
import com.kehutanan.pepdas.kegiatan.model.dto.KegiatanDTO;

public interface KegiatanService {
    
    List<Kegiatan> findAll();
    
    KegiatanDTO findDTOById(Long id);

    Kegiatan findById(Long id);
    
    Kegiatan save(Kegiatan kegiatan);
    
    Kegiatan update(Long id, Kegiatan kegiatan);
    
    void deleteById(Long id);
    
    // Upload methods for various file types
    Kegiatan uploadRancanganTeknisPdf(Long id, List<MultipartFile> files);
    
    Kegiatan uploadRancanganTeknisFoto(Long id, List<MultipartFile> files);
    
    Kegiatan uploadRancanganTeknisVideo(Long id, List<MultipartFile> files);
    
    Kegiatan uploadKontrakPdf(Long id, List<MultipartFile> files);
    
    Kegiatan uploadDokumentasiFoto(Long id, List<MultipartFile> files);
    
    Kegiatan uploadDokumentasiVideo(Long id, List<MultipartFile> files);
    
    Kegiatan uploadLokusShp(Long id, List<MultipartFile> files);
    
    // Delete methods for various file types
    Kegiatan deleteRancanganTeknisPdf(Long id, List<String> fileIds);
    
    Kegiatan deleteRancanganTeknisFoto(Long id, List<String> fileIds);
    
    Kegiatan deleteRancanganTeknisVideo(Long id, List<String> fileIds);
    
    Kegiatan deleteKontrakPdf(Long id, List<String> fileIds);
    
    Kegiatan deleteDokumentasiFoto(Long id, List<String> fileIds);
    
    Kegiatan deleteDokumentasiVideo(Long id, List<String> fileIds);
    
    Kegiatan deleteLokusShp(Long id, List<String> fileIds);

    // Cached methods for pagination and filtering
    KegiatanPageDTO findAllWithCache(Pageable pageable, String baseUrl);
    
    KegiatanPageDTO findByFiltersWithCache(String program, String kegiatan, List<String> bpdasList,
            Pageable pageable, String baseUrl);
    
    KegiatanPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}