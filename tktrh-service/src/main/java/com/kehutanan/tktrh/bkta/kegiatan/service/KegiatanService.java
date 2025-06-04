package com.kehutanan.tktrh.bkta.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.tktrh.bkta.kegiatan.dto.KegiatanDTO;
import com.kehutanan.tktrh.bkta.kegiatan.dto.KegiatanPageDTO;
import com.kehutanan.tktrh.bkta.kegiatan.model.Kegiatan;

public interface KegiatanService {
    
    List<Kegiatan> findAll();
    
    KegiatanDTO findDTOById(Long id);

    Kegiatan findById(Long id);
    
    Kegiatan save(Kegiatan kegiatan);
    
    Kegiatan update(Long id, Kegiatan kegiatan);
    
    void deleteById(Long id);
    
    // Upload methods for various file types
    Kegiatan uploadRancanganTeknisPdf(Long id, List<MultipartFile> pdf);
    
    Kegiatan uploadRancanganTeknisFoto(Long id, List<MultipartFile> foto);
    
    Kegiatan uploadRancanganTeknisShp(Long id, List<MultipartFile> shp);
    
    Kegiatan uploadKontrakPdf(Long id, List<MultipartFile> pdf);
    
    Kegiatan uploadDokumentasiFoto(Long id, List<MultipartFile> foto);
    
    Kegiatan uploadDokumentasiVideo(Long id, List<MultipartFile> video);

    // Delete methods for various file types
    Kegiatan deleteRancanganTeknisPdf(Long id, List<String> uuidPdf);
    
    Kegiatan deleteRancanganTeknisFoto(Long id, List<String> uuidFoto);
    
    Kegiatan deleteRancanganTeknisShp(Long id, List<String> uuidShp);
    
    Kegiatan deleteKontrakPdf(Long id, List<String> uuidPdf);
    
    Kegiatan deleteDokumentasiFoto(Long id, List<String> uuidFoto);
    
    Kegiatan deleteDokumentasiVideo(Long id, List<String> uuidVideo);

    // Paged queries with cache
    KegiatanPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanPageDTO findByFiltersWithCache(String namaProgram, String namaKegiatan, List<String> programList,
            Pageable pageable, String baseUrl);

    KegiatanPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}