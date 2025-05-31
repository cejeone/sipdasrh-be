package com.kehutanan.superadmin.master.service;

import com.kehutanan.superadmin.master.model.Uptd;
import com.kehutanan.superadmin.master.model.UptdFoto;
import com.kehutanan.superadmin.master.model.UptdPdf;
import com.kehutanan.superadmin.master.model.UptdVideo;
import com.kehutanan.superadmin.master.model.UptdShp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UptdService {
    
    Page<Uptd> findAll(Pageable pageable);
    
    List<Uptd> findAll();
    
    Uptd findById(Long id);
    
    Uptd save(Uptd uptd);
    
    Uptd update(Long id, Uptd uptd);
    
    void deleteById(Long id);
    
    Page<Uptd> findByFilters(String namaUptd, Long bpdasId, Long provinsiId, Long kabupatenKotaId, Long kecamatanId, Long kelurahanDesaId, Pageable pageable);

    Uptd uploadUptdFoto(Long id, List<MultipartFile> foto);
    
    Uptd uploadUptdPdf(Long id, List<MultipartFile> pdf);
    
    Uptd uploadUptdVideo(Long id, List<MultipartFile> video);
    
    Uptd uploadUptdShp(Long id, List<MultipartFile> shp);

    Uptd deleteUptdFoto(Long id, List<String> uuidFoto);
    
    Uptd deleteUptdPdf(Long id, List<String> uuidPdf);
    
    Uptd deleteUptdVideo(Long id, List<String> uuidVideo);
    
    Uptd deleteUptdShp(Long id, List<String> uuidShp);
}