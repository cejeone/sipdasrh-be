package com.kehutanan.rm.dokumen.service;

import com.kehutanan.rm.dokumen.dto.DokumenDTO;
import com.kehutanan.rm.dokumen.model.Dokumen;
import com.kehutanan.rm.dokumen.model.DokumenFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface DokumenService {
    
    Page<Dokumen> findAll(Pageable pageable);
    
    List<Dokumen> findAll();
    
    DokumenDTO findDTOById(Long id);

    Dokumen findById(Long id);
    
    Dokumen save(Dokumen dokumen);
    
    Dokumen update(Long id, Dokumen dokumen);
    
    void deleteById(Long id);
    
    Page<Dokumen> findByFilters(String namaDokumen, Long tipeId, Long statusId, Pageable pageable);

    Dokumen uploadDokumenFiles(Long id, List<MultipartFile> files);

    Dokumen deleteDokumenFiles(Long id, List<String> uuidFiles);
}