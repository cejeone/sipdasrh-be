package com.kehutanan.rh.bimtek.service;

import com.kehutanan.rh.bimtek.model.Bimtek;
import com.kehutanan.rh.bimtek.model.BimtekFoto;
import com.kehutanan.rh.bimtek.repository.BimtekFotoRepository;
import com.kehutanan.rh.bimtek.repository.BimtekRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BimtekService {
    
    private final BimtekRepository bimtekRepository;
    private final BimtekFotoRepository bimtekFotoRepository;
    private final MinioBimtekService minioBimtekService;

    public Page<Bimtek> findAll(Pageable pageable) {
        return bimtekRepository.findAll(pageable);
    }
    
    public Page<Bimtek> search(String query, Pageable pageable) {
        return bimtekRepository.findByNamaBimtekContainingIgnoreCase(query, pageable);
    }

    public List<Bimtek> findAll() {
        return bimtekRepository.findAll();
    }

    public Bimtek findById(UUID id) {
        return bimtekRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Bimtek not found with id: " + id));
    }

    @Transactional
    public Bimtek create(Bimtek bimtek) {
        return bimtekRepository.save(bimtek);
    }

    @Transactional
    public Bimtek update(UUID id, Bimtek bimtek) {
        Bimtek existing = findById(id);
        
        existing.setNamaBimtek(bimtek.getNamaBimtek());
        existing.setSubjek(bimtek.getSubjek());
        existing.setProgram(bimtek.getProgram());
        existing.setBpdas(bimtek.getBpdas());
        existing.setTempat(bimtek.getTempat());
        existing.setTanggal(bimtek.getTanggal());
        existing.setAudience(bimtek.getAudience());
        existing.setEvaluasi(bimtek.getEvaluasi());
        existing.setKeterangan(bimtek.getKeterangan());
        
        return bimtekRepository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        bimtekRepository.deleteById(id);
    }

    @Transactional
    public List<BimtekFoto> uploadFotos(UUID bimtekId, List<MultipartFile> files) throws Exception {
        Bimtek bimtek = bimtekRepository.findById(bimtekId)
                .orElseThrow(() -> new EntityNotFoundException("Bimtek tidak ditemukan dengan id: " + bimtekId));
        
        List<BimtekFoto> uploadedFotos = new ArrayList<>();
        
        for (MultipartFile file : files) {
            String fileName =  UUID.randomUUID() + "_" + file.getOriginalFilename();
            
            minioBimtekService.uploadFile(fileName, file.getInputStream(), file.getContentType());
            
            BimtekFoto bimtekFoto = new BimtekFoto();
            bimtekFoto.setBimtek(bimtek);
            bimtekFoto.setNamaFile(fileName);
            bimtekFoto.setNamaAsli(file.getOriginalFilename());
            bimtekFoto.setUkuranMb((double) file.getSize() / (1024 * 1024));
            bimtekFoto.setContentType(file.getContentType());
            bimtekFoto.setUploadedAt(LocalDateTime.now());
            
            uploadedFotos.add(bimtekFotoRepository.save(bimtekFoto));
        }
        
        return uploadedFotos;
    }

    @Transactional
    public Bimtek deleteFotos(UUID bimtekId, List<UUID> fileIds) throws Exception {
        Bimtek bimtek = bimtekRepository.findById(bimtekId)
                .orElseThrow(() -> new EntityNotFoundException("Bimtek tidak ditemukan dengan id: " + bimtekId));
        
        List<BimtekFoto> fotosToDelete = bimtekFotoRepository.findAllById(fileIds);
        
        for (BimtekFoto foto : fotosToDelete) {
            if (foto.getBimtek().getId().equals(bimtekId)) {
                minioBimtekService.deleteFile(foto.getNamaFile());
                bimtekFotoRepository.delete(foto);
            }
        }
        
        return bimtekRepository.save(bimtek);
    }
}
