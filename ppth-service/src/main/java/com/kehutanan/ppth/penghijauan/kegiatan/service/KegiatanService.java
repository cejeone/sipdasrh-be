package com.kehutanan.ppth.penghijauan.kegiatan.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.ppth.penghijauan.kegiatan.dto.KegiatanPageDTO;
import com.kehutanan.ppth.penghijauan.kegiatan.model.Kegiatan;
import com.kehutanan.ppth.penghijauan.kegiatan.model.dto.KegiatanDTO;

public interface KegiatanService {
    
    List<Kegiatan> findAll();
    
    KegiatanDTO findDTOById(Long id);

    Kegiatan findById(Long id);
    
    Kegiatan save(Kegiatan kegiatan);
    
    Kegiatan update(Long id, Kegiatan kegiatan);
    
    void deleteById(Long id);
    
    // File upload methods
    Kegiatan uploadRancanganTeknisPdf(Long id, List<MultipartFile> pdf);
    
    Kegiatan uploadRancanganTeknisFoto(Long id, List<MultipartFile> foto);
    
    Kegiatan uploadRancanganTeknisVideo(Long id, List<MultipartFile> video);
    
    Kegiatan uploadKontrakPdf(Long id, List<MultipartFile> pdf);
    
    Kegiatan uploadDokumentasiFoto(Long id, List<MultipartFile> foto);
    
    Kegiatan uploadDokumentasiVideo(Long id, List<MultipartFile> video);
    Kegiatan uploadLokusShp(Long id, List<MultipartFile> lokusShpFiles);

    // File deletion methods
    Kegiatan deleteRancanganTeknisPdf(Long id, List<String> uuidPdf);
    
    Kegiatan deleteRancanganTeknisFoto(Long id, List<String> uuidFoto);
    
    Kegiatan deleteRancanganTeknisVideo(Long id, List<String> uuidVideo);
    
    Kegiatan deleteKontrakPdf(Long id, List<String> uuidPdf);
    
    Kegiatan deleteDokumentasiFoto(Long id, List<String> uuidFoto);
    
    Kegiatan deleteDokumentasiVideo(Long id, List<String> uuidVideo);
    Kegiatan deleteLokusShp(Long id, List<Long> lokusShpIds);

    // Search and filter methods with cache
    KegiatanPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KegiatanPageDTO findByFiltersWithCache( String namaProgram, String jenisKegiatan, List<String> jenis,
            Pageable pageable, String baseUrl);

    KegiatanPageDTO searchWithCache( String keyWord, Pageable pageable, String baseUrl);

    
}