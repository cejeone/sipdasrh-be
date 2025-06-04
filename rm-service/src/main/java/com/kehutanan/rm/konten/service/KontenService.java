package com.kehutanan.rm.konten.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.rm.konten.dto.KontenDTO;
import com.kehutanan.rm.konten.dto.KontenPageDTO;
import com.kehutanan.rm.konten.model.Konten;

public interface KontenService {
    
    List<Konten> findAll();
    
    KontenDTO findDTOById(Long id);

    Konten findById(Long id);
    
    Konten save(Konten konten);
    
    Konten update(Long id, Konten konten);
    
    void deleteById(Long id);
    
    Konten uploadKontenGambar(Long id, List<MultipartFile> gambar);
    
    Konten uploadKontenGambarUtama(Long id, List<MultipartFile> gambarUtama);

    Konten deleteKontenGambar(Long id, List<String> uuidGambar);
    
    Konten deleteKontenGambarUtama(Long id, List<String> uuidGambarUtama);

    KontenPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    KontenPageDTO findByFiltersWithCache(String judul, List<String> tipe,
            Pageable pageable, String baseUrl);

    KontenPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}