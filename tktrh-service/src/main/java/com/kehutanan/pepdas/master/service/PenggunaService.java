package com.kehutanan.pepdas.master.service;

import com.kehutanan.pepdas.master.model.Pengguna;
import com.kehutanan.pepdas.master.model.PenggunaFoto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PenggunaService {
    
    Page<Pengguna> findAll(Pageable pageable);
    
    List<Pengguna> findAll();
    
    Pengguna findById(Long id);
    
    Pengguna save(Pengguna pengguna);
    
    Pengguna update(Long id, Pengguna pengguna);
    
    void deleteById(Long id);
    
    Page<Pengguna> findByFilters(String username, String namaLengkap, String email, Long peranId, Long statusId, Pageable pageable);

    Pengguna uploadPenggunaFoto(Long id, List<MultipartFile> foto_profile);

    Pengguna deletePenggunaFoto(Long id, List<String> uuidFotoProfiles);
}