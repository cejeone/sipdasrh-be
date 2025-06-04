package com.kehutanan.tktrh.bkta.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.tktrh.bkta.kegiatan.dto.KegiatanLokusDTO;
import com.kehutanan.tktrh.bkta.kegiatan.dto.KegiatanLokusPageDTO;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanLokus;

public interface KegiatanLokusService {
    
    List<KegiatanLokus> findAll();
    
    KegiatanLokusDTO findDTOById(Long id);

    KegiatanLokus findById(Long id);
    
    KegiatanLokus save(KegiatanLokus kegiatanLokus);
    
    KegiatanLokus update(Long id, KegiatanLokus kegiatanLokus);
    
    void deleteById(Long id);
    
    KegiatanLokus uploadProposalPdf(Long id, List<MultipartFile> pdf);
    
    KegiatanLokus uploadLokasiPdf(Long id, List<MultipartFile> pdf);
    
    KegiatanLokus uploadBangunanPdf(Long id, List<MultipartFile> pdf);

    KegiatanLokus deleteProposalPdf(Long id, List<String> uuidPdf);
    
    KegiatanLokus deleteLokasiPdf(Long id, List<String> uuidPdf);
    
    KegiatanLokus deleteBangunanPdf(Long id, List<String> uuidPdf);

    KegiatanLokusPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanLokusPageDTO findByFiltersWithCache(Long kegiatanId, String provinsi, String catatan, 
            List<String> status, Pageable pageable, String baseUrl);

    KegiatanLokusPageDTO searchWithCache(Long kegiatanId, String keyWord, Pageable pageable, String baseUrl);
}