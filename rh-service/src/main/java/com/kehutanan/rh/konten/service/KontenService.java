package com.kehutanan.rh.konten.service;

import com.kehutanan.rh.bimtek.model.BimtekFoto;
import com.kehutanan.rh.generic.GenericFileService;
import com.kehutanan.rh.generic.MinioGenericService;
import com.kehutanan.rh.konten.dto.KontenDto;
import com.kehutanan.rh.konten.model.Konten;
import com.kehutanan.rh.konten.model.KontenGambar;
import com.kehutanan.rh.konten.model.KontenGambarUtama;
import com.kehutanan.rh.konten.repository.KontenRepository;
import com.kehutanan.rh.util.FileValidationUtil;
import com.kehutanan.rh.konten.repository.KontenGambarRepository;
import com.kehutanan.rh.konten.repository.KontenGambarUtamaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KontenService {

    private final MinioKontenService minioKontenService;
    private final FileValidationUtil fileValidationUtil;

    private final KontenRepository kontenRepository;
    private final KontenGambarRepository kontenGambarRepository;
    private final KontenGambarUtamaRepository kontenGambarUtamaRepository;

    public Page<Konten> findAll(Pageable pageable) {
        return kontenRepository.findAll(pageable);
    }

    public Konten findById(UUID id) {
        return kontenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Konten tidak ditemukan dengan id: " + id));
    }

    @Transactional
    public Konten create(KontenDto kontenDto) {
        Konten konten = new Konten();
        konten.setTipe(kontenDto.getTipe());
        konten.setJudul(kontenDto.getJudul());
        konten.setKonten(kontenDto.getKonten());
        konten.setKataKunci(kontenDto.getKataKunci());
        konten.setWaktuAwalTayang(kontenDto.getWaktuAwalTayang());
        konten.setWaktuAkhirTayang(kontenDto.getWaktuAkhirTayang());
        konten.setStatus(kontenDto.getStatus());
        
        return kontenRepository.save(konten);
    }

    @Transactional
    public Konten update(UUID id, KontenDto kontenDto) {
        Konten existing = kontenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Konten tidak ditemukan dengan id: " + id));

        existing.setTipe(kontenDto.getTipe());
        existing.setJudul(kontenDto.getJudul());
        existing.setKonten(kontenDto.getKonten());
        existing.setKataKunci(kontenDto.getKataKunci());
        existing.setWaktuAwalTayang(kontenDto.getWaktuAwalTayang());
        existing.setWaktuAkhirTayang(kontenDto.getWaktuAkhirTayang());
        existing.setStatus(kontenDto.getStatus());

        return kontenRepository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        Konten konten = findById(id);
        kontenRepository.deleteById(id);
    }

    @Transactional
    public List<KontenGambar> uploadGambars(UUID kontenId, List<MultipartFile> files) throws Exception {
        Konten konten = kontenRepository.findById(kontenId)
                .orElseThrow(() -> new EntityNotFoundException("Konten tidak ditemukan dengan id: " + kontenId));

        List<KontenGambar> uploadedGambars = new ArrayList<>();

        for (MultipartFile file : files) {

            fileValidationUtil.validateFileType(file, "image");
            fileValidationUtil.validateFileSize(file);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioKontenService.uploadFile(fileName, file.getInputStream(), file.getContentType());

            KontenGambar kontenGambar = new KontenGambar();
            kontenGambar.setKonten(konten);
            kontenGambar.setNamaFile(fileName);
            kontenGambar.setNamaAsli(file.getOriginalFilename());
            kontenGambar.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kontenGambar.setContentType(file.getContentType());
            kontenGambar.setUploadedAt(LocalDateTime.now());

            uploadedGambars.add(kontenGambarRepository.save(kontenGambar));
        }

        return uploadedGambars;
    }

    @Transactional
    public Konten deleteGambars(UUID kontenId, List<UUID> fileIds) throws Exception {
        Konten konten = kontenRepository.findById(kontenId)
                .orElseThrow(() -> new EntityNotFoundException("Konten tidak ditemukan dengan id: " + kontenId));

        List<KontenGambar> gambarToDelete = kontenGambarRepository.findAllById(fileIds);

        for (KontenGambar gambar : gambarToDelete) {
            if (gambar.getKonten().getId().equals(kontenId)) {
                minioKontenService.deleteFile(gambar.getNamaFile());
                kontenGambarRepository.delete(gambar);
            }
        }
        return kontenRepository.save(konten);
    }

    @Transactional
    public List<KontenGambarUtama> uploadGambarUtamas(UUID kontenId, List<MultipartFile> files) throws Exception {
        Konten konten = kontenRepository.findById(kontenId)
                .orElseThrow(() -> new EntityNotFoundException("Konten tidak ditemukan dengan id: " + kontenId));

        List<KontenGambarUtama> uploadedGambars = new ArrayList<>();

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "image");
            fileValidationUtil.validateFileSize(file);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioKontenService.uploadFile(fileName, file.getInputStream(), file.getContentType());

            KontenGambarUtama kontenGambarUtama = new KontenGambarUtama();
            kontenGambarUtama.setKonten(konten);
            kontenGambarUtama.setNamaFile(fileName);
            kontenGambarUtama.setNamaAsli(file.getOriginalFilename());
            kontenGambarUtama.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kontenGambarUtama.setContentType(file.getContentType());
            kontenGambarUtama.setUploadedAt(LocalDateTime.now());

            uploadedGambars.add(kontenGambarUtamaRepository.save(kontenGambarUtama));
        }

        return uploadedGambars;
    }

    @Transactional
    public Konten deleteGambarUtamas(UUID kontenId, List<UUID> fileIds) throws Exception {
        Konten konten = kontenRepository.findById(kontenId)
                .orElseThrow(() -> new EntityNotFoundException("Konten tidak ditemukan dengan id: " + kontenId));

        List<KontenGambar> gambarToDelete = kontenGambarRepository.findAllById(fileIds);

        for (KontenGambar gambar : gambarToDelete) {
            if (gambar.getKonten().getId().equals(kontenId)) {
                minioKontenService.deleteFile(gambar.getNamaFile());
                kontenGambarRepository.delete(gambar);
            }
        }
        return kontenRepository.save(konten);
    }

    public byte[] viewGambar(UUID kontenId, UUID gambarId) {
        // Ambil data foto dari database
        KontenGambar gambar = kontenGambarRepository.findById(gambarId)
                .orElseThrow(() -> new EntityNotFoundException("Foto tidak ditemukan dengan id: " + gambarId));

        // Validasi gambar tersebut memang milik konten yang dimaksud
        if (!gambar.getKonten().getId().equals(kontenId)) {
            throw new EntityNotFoundException("Gambar tidak terkait dengan Konten yang dimaksud");
        }

        try {
            // Ambil data file dari penyimpanan (Minio atau sistem file)
            return minioKontenService.getFileData(gambar.getNamaFile());
        } catch (Exception e) {
            log.error("Gagal mengambil data gambar: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal mengambil data gambar: " + e.getMessage(), e);
        }
    }

    public KontenGambar getGambarById(UUID gambarId) {
        return kontenGambarRepository.findById(gambarId)
                .orElseThrow(() -> new EntityNotFoundException("Gambar tidak ditemukan dengan id: " + gambarId));
    }

    public byte[] viewGambarUtama(UUID kontenId, UUID gambarUtamaId) {
        KontenGambarUtama gambarUtama = kontenGambarUtamaRepository.findById(gambarUtamaId)
                .orElseThrow(() -> new EntityNotFoundException("Foto tidak ditemukan dengan id: " + gambarUtamaId));

        // Validasi gambar Utama tersebut memang milik konten yang dimaksud
        if (!gambarUtama.getKonten().getId().equals(kontenId)) {
            throw new EntityNotFoundException("Gambar Utama tidak terkait dengan Konten yang dimaksud");
        }

        try {
            // Ambil data file dari penyimpanan (Minio atau sistem file)
            return minioKontenService.getFileData(gambarUtama.getNamaFile());
        } catch (Exception e) {
            log.error("Gagal mengambil data gambar Utama: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal mengambil data gambar Utama: " + e.getMessage(), e);
        }
    }

    public KontenGambarUtama getGambarUtamaById(UUID gambarUtamaId) {
        return kontenGambarUtamaRepository.findById(gambarUtamaId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Gambar Utama tidak ditemukan dengan id: " + gambarUtamaId));
    }

    public Page<Konten> findByFilters(String judul, Pageable pageable) {
        Specification<Konten> spec = (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("judul")),
                "%" + judul.toLowerCase() + "%");

        return kontenRepository.findAll(spec, pageable);
    }
}
