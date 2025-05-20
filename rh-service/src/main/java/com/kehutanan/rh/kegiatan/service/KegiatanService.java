package com.kehutanan.rh.kegiatan.service;

import com.kehutanan.rh.bimtek.model.BimtekPdf;
import com.kehutanan.rh.kegiatan.dto.KegiatanDto;
import com.kehutanan.rh.kegiatan.dto.KegiatanDtoDetail;
import com.kehutanan.rh.kegiatan.dto.KegiatanFileDto;
import com.kehutanan.rh.kegiatan.model.Kegiatan;
import com.kehutanan.rh.kegiatan.model.KegiatanDokumentasiFoto;
import com.kehutanan.rh.kegiatan.model.KegiatanDokumentasiVideo;
import com.kehutanan.rh.kegiatan.model.KegiatanKontrakPdf;
import com.kehutanan.rh.kegiatan.model.KegiatanRancanganTeknisFoto;
import com.kehutanan.rh.kegiatan.model.KegiatanRancanganTeknisPdf;
import com.kehutanan.rh.kegiatan.model.KegiatanRancanganTeknisVideo;
import com.kehutanan.rh.kegiatan.model.KegiatanSerahTerimaPdf;
import com.kehutanan.rh.kegiatan.repository.KegiatanRancanganTeknisVideoRepository;
import com.kehutanan.rh.kegiatan.repository.KegiatanRepository;
import com.kehutanan.rh.program.model.Program;
import com.kehutanan.rh.program.repository.ProgramRepository;
import com.kehutanan.rh.kegiatan.repository.KegiatanDokumentasiFotoRepository;
import com.kehutanan.rh.kegiatan.repository.KegiatanDokumentasiVideoRepository;
import com.kehutanan.rh.kegiatan.repository.KegiatanKontrakPdfRepository;
import com.kehutanan.rh.kegiatan.repository.KegiatanRancanganTeknisFotoRepository;
import com.kehutanan.rh.kegiatan.repository.KegiatanRancanganTeknisPdfRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class KegiatanService {

    private final KegiatanRancanganTeknisVideoRepository kegiatanRancanganTeknisVideoRepository;

    private final ProgramRepository programRepository;
    private final KegiatanRepository kegiatanRepository;
    private final KegiatanRancanganTeknisFotoRepository kegiatanRancanganTeknisFotoRepository;
    private final KegiatanRancanganTeknisPdfRepository kegiatanRancanganTeknisPdfRepository;
    private final MinioKegiatanService minioKegiatanService;
    private final KegiatanKontrakPdfRepository kegiatanKontrakPdfRepository;
    private final KegiatanDokumentasiFotoRepository kegiatanDokumentasiFotoRepository;
    private final KegiatanDokumentasiVideoRepository kegiatanDokumentasiVideoRepository;

    public Page<KegiatanDto> findAll(Pageable pageable, String programName, String namaKegiatan) {
        Specification<Kegiatan> spec = Specification.where(null);

        // Add filter for programName if provided
        if (programName != null && !programName.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                Join<Kegiatan, Program> programJoin = root.join("program", JoinType.LEFT);
                return criteriaBuilder.like(
                        criteriaBuilder.lower(programJoin.get("nama")),
                        "%" + programName.toLowerCase() + "%");
            });
        }

        // Add filter for namaKegiatan if provided
        if (namaKegiatan != null && !namaKegiatan.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("namaKegiatan")),
                    "%" + namaKegiatan.toLowerCase() + "%"));
        }

        // Execute the query with the specifications
        Page<Kegiatan> kegiatanPage = kegiatanRepository.findAll(spec, pageable);

        // Convert entities to DTOs
        List<KegiatanDto> dtoList = kegiatanPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, kegiatanPage.getTotalElements());
    }

    // Keep the existing method for backward compatibility
    public Page<KegiatanDto> findAll(Pageable pageable) {
        return findAll(pageable, null, null);
    }

    public Kegiatan findById(UUID id) {
        return kegiatanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan tidak ditemukan dengan id: " + id));
    }

    @Transactional(readOnly = true)
    public KegiatanDtoDetail findByIdDtoMin(UUID id) {
        Kegiatan kegiatan = kegiatanRepository.findByIdAllChild(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan not found with id: " + id));

        // Collections will be initialized due to @EntityGraph
        return mapToDto(kegiatan);
    }

    private KegiatanDtoDetail mapToDto(Kegiatan kegiatan) {
        KegiatanDtoDetail dto = new KegiatanDtoDetail();
        dto.setId(kegiatan.getId());
        dto.setProgramId(kegiatan.getProgram().getId());
        dto.setProgramName(kegiatan.getProgram().getNama());
        dto.setSubDirektorat(kegiatan.getSubDirektorat());
        dto.setJenisKegiatan(kegiatan.getJenisKegiatan());
        dto.setRefPo(kegiatan.getRefPo());
        dto.setNamaKegiatan(kegiatan.getNamaKegiatan());
        dto.setKegiatanLokus(kegiatan.getKegiatanLokus());
        dto.setDetailPola(kegiatan.getDetailPola());
        dto.setDetailTahunKegiatan(kegiatan.getDetailTahunKegiatan());
        dto.setDetailSumberAnggaran(kegiatan.getDetailSumberAnggaran());
        dto.setDetailTotalBibit(kegiatan.getDetailTotalBibit());
        dto.setDetailTotalLuasHa(kegiatan.getDetailTotalLuasHa());
        dto.setDetailPemangkuKawasan(kegiatan.getDetailPemangkuKawasan());
        dto.setDetailPelaksana(kegiatan.getDetailPelaksana());
        dto.setKontrakNomor(kegiatan.getKontrakNomor());
        dto.setKontrakNilai(kegiatan.getKontrakNilai());
        dto.setKontrakTipe(kegiatan.getKontrakTipe());
        dto.setKontrakPelaksana(kegiatan.getKontrakPelaksana());
        dto.setKontrakTanggalKontrak(kegiatan.getKontrakTanggalKontrak());
        dto.setKontrakStatus(kegiatan.getKontrakStatus());
        dto.setDokumentasiCatatanFoto(kegiatan.getDokumentasiCatatanFoto());
        dto.setDokumentasiCatatanVideo(kegiatan.getDokumentasiCatatanVideo());

        // Map RancanganTeknisFotos
        if (kegiatan.getKegiatanRancanganTeknisFotos() != null) {
            dto.setRancanganTeknisFotos(
                    kegiatan.getKegiatanRancanganTeknisFotos().stream()
                            .map(foto -> {
                                KegiatanFileDto fotoDto = new KegiatanFileDto();
                                fotoDto.setId(foto.getId());
                                fotoDto.setFileName(foto.getNamaAsli());
                                fotoDto.setContentType(foto.getContentType());
                                fotoDto.setUrl("/api/kegiatan/" + kegiatan.getId() +
                                        "/rancangan-teknis/fotos/" + foto.getId() + "/view");
                                return fotoDto;
                            })
                            .collect(Collectors.toList()));
        }

        // Map RancanganTeknisPdfs
        if (kegiatan.getKegiatanRancanganTeknisPdfs() != null) {
            dto.setRancanganTeknisPdfs(
                    kegiatan.getKegiatanRancanganTeknisPdfs().stream()
                            .map(pdf -> {
                                KegiatanFileDto pdfDto = new KegiatanFileDto();
                                pdfDto.setId(pdf.getId());
                                pdfDto.setFileName(pdf.getNamaAsli());
                                pdfDto.setContentType(pdf.getContentType());
                                pdfDto.setUrl("/api/kegiatan/" + kegiatan.getId() +
                                        "/rancangan-teknis/pdfs/" + pdf.getId() + "/view");
                                return pdfDto;
                            })
                            .collect(Collectors.toList()));
        }

        // Map RancanganTeknisVideos
        if (kegiatan.getKegiatanRancanganTeknisVideos() != null) {
            dto.setRancanganTeknisVideos(
                    kegiatan.getKegiatanRancanganTeknisVideos().stream()
                            .map(video -> {
                                KegiatanFileDto videoDto = new KegiatanFileDto();
                                videoDto.setId(video.getId());
                                videoDto.setFileName(video.getNamaAsli());
                                videoDto.setContentType(video.getContentType());
                                videoDto.setUrl("/api/kegiatan/" + kegiatan.getId() +
                                        "/rancangan-teknis/videos/" + video.getId() + "/view");
                                return videoDto;
                            })
                            .collect(Collectors.toList()));
        }

        // Map KontrakPdfs
        if (kegiatan.getKegiatanKontrakPdfs() != null) {
            dto.setKontrakPdfs(
                    kegiatan.getKegiatanKontrakPdfs().stream()
                            .map(pdf -> {
                                KegiatanFileDto pdfDto = new KegiatanFileDto();
                                pdfDto.setId(pdf.getId());
                                pdfDto.setFileName(pdf.getNamaAsli());
                                pdfDto.setContentType(pdf.getContentType());
                                pdfDto.setUrl("/api/kegiatan/" + kegiatan.getId() +
                                        "/kontrak/pdfs/" + pdf.getId() + "/view");
                                return pdfDto;
                            })
                            .collect(Collectors.toList()));
        }

        // Map DokumentasiFotos
        if (kegiatan.getKegiatanDokumentasiFotos() != null) {
            dto.setDokumentasiFotos(
                    kegiatan.getKegiatanDokumentasiFotos().stream()
                            .map(foto -> {
                                KegiatanFileDto fotoDto = new KegiatanFileDto();
                                fotoDto.setId(foto.getId());
                                fotoDto.setFileName(foto.getNamaAsli());
                                fotoDto.setContentType(foto.getContentType());
                                fotoDto.setUrl("/api/kegiatan/" + kegiatan.getId() +
                                        "/dokumentasi/fotos/" + foto.getId() + "/view");
                                return fotoDto;
                            })
                            .collect(Collectors.toList()));
        }

        // Map DokumentasiVideos
        if (kegiatan.getKegiatanDokumentasiVideos() != null) {
            dto.setDokumentasiVideos(
                    kegiatan.getKegiatanDokumentasiVideos().stream()
                            .map(video -> {
                                KegiatanFileDto videoDto = new KegiatanFileDto();
                                videoDto.setId(video.getId());
                                videoDto.setFileName(video.getNamaAsli());
                                videoDto.setContentType(video.getContentType());
                                videoDto.setUrl("/api/kegiatan/" + kegiatan.getId() +
                                        "/dokumentasi/videos/" + video.getId() + "/view");
                                return videoDto;
                            })
                            .collect(Collectors.toList()));
        }

        return dto;
    }

    @Transactional
    public KegiatanDto create(KegiatanDto kegiatanDto) {
        Kegiatan kegiatan = convertToEntity(kegiatanDto);
        Kegiatan saved = kegiatanRepository.save(kegiatan);
        return convertToDto(saved);
    }

    @Transactional
    public Kegiatan update(UUID id, Kegiatan kegiatan) {
        Kegiatan existing = findById(id);

        existing.setSubDirektorat(kegiatan.getSubDirektorat());
        existing.setProgram(kegiatan.getProgram());
        existing.setJenisKegiatan(kegiatan.getJenisKegiatan());
        existing.setRefPo(kegiatan.getRefPo());
        existing.setNamaKegiatan(kegiatan.getNamaKegiatan());
        existing.setDetailPola(kegiatan.getDetailPola());
        existing.setDetailTahunKegiatan(kegiatan.getDetailTahunKegiatan());
        existing.setDetailSumberAnggaran(kegiatan.getDetailSumberAnggaran());
        existing.setDetailTotalBibit(kegiatan.getDetailTotalBibit());
        existing.setDetailTotalLuasHa(kegiatan.getDetailTotalLuasHa());
        existing.setDetailPemangkuKawasan(kegiatan.getDetailPemangkuKawasan());
        existing.setDetailPelaksana(kegiatan.getDetailPelaksana());
        existing.setKontrakNomor(kegiatan.getKontrakNomor());
        existing.setKontrakNilai(kegiatan.getKontrakNilai());
        existing.setKontrakTipe(kegiatan.getKontrakTipe());
        existing.setKontrakPelaksana(kegiatan.getKontrakPelaksana());
        existing.setKontrakTanggalKontrak(kegiatan.getKontrakTanggalKontrak());
        existing.setKontrakStatus(kegiatan.getKontrakStatus());
        existing.setDokumentasiCatatanFoto(kegiatan.getDokumentasiCatatanFoto());
        existing.setDokumentasiCatatanVideo(kegiatan.getDokumentasiCatatanVideo());

        return kegiatanRepository.save(existing);
    }

    @Transactional
    public KegiatanDto update(UUID id, KegiatanDto kegiatanDto) {
        Kegiatan existing = kegiatanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan tidak ditemukan dengan id: " + id));

        // Update fields
        updateEntityFromDto(existing, kegiatanDto);

        Kegiatan saved = kegiatanRepository.save(existing);
        return convertToDto(saved);
    }

    @Transactional
    public void delete(UUID id) {
        Kegiatan kegiatan = kegiatanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan tidak ditemukan dengan id: " + id));

        try {
            kegiatanRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Gagal menghapus Kegiatan dan data terkait: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal menghapus Kegiatan dan data terkait: " + e.getMessage(), e);
        }
    }

    // Helper method to convert Entity to DTO
    private KegiatanDto convertToDto(Kegiatan kegiatan) {
        KegiatanDto dto = new KegiatanDto();
        dto.setId(kegiatan.getId());
        dto.setSubDirektorat(kegiatan.getSubDirektorat());

        if (kegiatan.getProgram() != null) {
            dto.setProgramId(kegiatan.getProgram().getId());
            dto.setProgramName(kegiatan.getProgram().getNama());
        }

        dto.setJenisKegiatan(kegiatan.getJenisKegiatan());
        dto.setRefPo(kegiatan.getRefPo());
        dto.setNamaKegiatan(kegiatan.getNamaKegiatan());
        dto.setDetailPola(kegiatan.getDetailPola());
        dto.setDetailTahunKegiatan(kegiatan.getDetailTahunKegiatan());
        dto.setDetailSumberAnggaran(kegiatan.getDetailSumberAnggaran());
        dto.setDetailTotalBibit(kegiatan.getDetailTotalBibit());
        dto.setDetailTotalLuasHa(kegiatan.getDetailTotalLuasHa());
        dto.setDetailPemangkuKawasan(kegiatan.getDetailPemangkuKawasan());
        dto.setDetailPelaksana(kegiatan.getDetailPelaksana());
        dto.setKontrakNomor(kegiatan.getKontrakNomor());
        dto.setKontrakNilai(kegiatan.getKontrakNilai());
        dto.setKontrakTipe(kegiatan.getKontrakTipe());
        dto.setKontrakPelaksana(kegiatan.getKontrakPelaksana());
        dto.setKontrakTanggalKontrak(kegiatan.getKontrakTanggalKontrak());
        dto.setKontrakStatus(kegiatan.getKontrakStatus());
        dto.setDokumentasiCatatanFoto(kegiatan.getDokumentasiCatatanFoto());
        dto.setDokumentasiCatatanVideo(kegiatan.getDokumentasiCatatanVideo());

        return dto;
    }

    private Kegiatan convertToEntity(KegiatanDto dto) {
        Kegiatan kegiatan = new Kegiatan();

        // Don't set ID for new entities, let the database generate it
        if (dto.getId() != null) {
            kegiatan.setId(dto.getId());
        }

        kegiatan.setSubDirektorat(dto.getSubDirektorat());

        if (dto.getProgramId() != null) {
            Program program = programRepository.findById(dto.getProgramId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Program tidak ditemukan dengan id: " + dto.getProgramId()));
            kegiatan.setProgram(program);
        }

        kegiatan.setJenisKegiatan(dto.getJenisKegiatan());
        kegiatan.setRefPo(dto.getRefPo());
        kegiatan.setNamaKegiatan(dto.getNamaKegiatan());
        kegiatan.setDetailPola(dto.getDetailPola());
        kegiatan.setDetailTahunKegiatan(dto.getDetailTahunKegiatan());
        kegiatan.setDetailSumberAnggaran(dto.getDetailSumberAnggaran());
        kegiatan.setDetailTotalBibit(dto.getDetailTotalBibit());
        kegiatan.setDetailTotalLuasHa(dto.getDetailTotalLuasHa());
        kegiatan.setDetailPemangkuKawasan(dto.getDetailPemangkuKawasan());
        kegiatan.setDetailPelaksana(dto.getDetailPelaksana());
        kegiatan.setKontrakNomor(dto.getKontrakNomor());
        kegiatan.setKontrakNilai(dto.getKontrakNilai());
        kegiatan.setKontrakTipe(dto.getKontrakTipe());
        kegiatan.setKontrakPelaksana(dto.getKontrakPelaksana());
        kegiatan.setKontrakTanggalKontrak(dto.getKontrakTanggalKontrak());
        kegiatan.setKontrakStatus(dto.getKontrakStatus());
        kegiatan.setDokumentasiCatatanFoto(dto.getDokumentasiCatatanFoto());
        kegiatan.setDokumentasiCatatanVideo(dto.getDokumentasiCatatanVideo());

        return kegiatan;
    }

    // Helper method to update an entity from a DTO
    private void updateEntityFromDto(Kegiatan kegiatan, KegiatanDto dto) {
        kegiatan.setSubDirektorat(dto.getSubDirektorat());

        if (dto.getProgramId() != null) {
            Program program = programRepository.findById(dto.getProgramId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Program tidak ditemukan dengan id: " + dto.getProgramId()));
            kegiatan.setProgram(program);
        }

        kegiatan.setJenisKegiatan(dto.getJenisKegiatan());
        kegiatan.setRefPo(dto.getRefPo());
        kegiatan.setNamaKegiatan(dto.getNamaKegiatan());
        kegiatan.setDetailPola(dto.getDetailPola());
        kegiatan.setDetailTahunKegiatan(dto.getDetailTahunKegiatan());
        kegiatan.setDetailSumberAnggaran(dto.getDetailSumberAnggaran());
        kegiatan.setDetailTotalBibit(dto.getDetailTotalBibit());
        kegiatan.setDetailTotalLuasHa(dto.getDetailTotalLuasHa());
        kegiatan.setDetailPemangkuKawasan(dto.getDetailPemangkuKawasan());
        kegiatan.setDetailPelaksana(dto.getDetailPelaksana());
        kegiatan.setKontrakNomor(dto.getKontrakNomor());
        kegiatan.setKontrakNilai(dto.getKontrakNilai());
        kegiatan.setKontrakTipe(dto.getKontrakTipe());
        kegiatan.setKontrakPelaksana(dto.getKontrakPelaksana());
        kegiatan.setKontrakTanggalKontrak(dto.getKontrakTanggalKontrak());
        kegiatan.setKontrakStatus(dto.getKontrakStatus());
        kegiatan.setDokumentasiCatatanFoto(dto.getDokumentasiCatatanFoto());
        kegiatan.setDokumentasiCatatanVideo(dto.getDokumentasiCatatanVideo());
    }

    public List<KegiatanRancanganTeknisFoto> uploadKegiatanRancanganTeknisFotos(UUID id, List<MultipartFile> files)
            throws Exception {
        Kegiatan kegiatan = kegiatanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("kegiatan tidak ditemukan dengan id: " + id));

        List<KegiatanRancanganTeknisFoto> uploadedFotos = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioKegiatanService.uploadFile(fileName, file.getInputStream(), file.getContentType());

            KegiatanRancanganTeknisFoto kegiatanRancanganTeknisFoto = new KegiatanRancanganTeknisFoto();
            kegiatanRancanganTeknisFoto.setKegiatan(kegiatan);
            kegiatanRancanganTeknisFoto.setNamaFile(fileName);
            kegiatanRancanganTeknisFoto.setNamaAsli(file.getOriginalFilename());
            kegiatanRancanganTeknisFoto.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanRancanganTeknisFoto.setContentType(file.getContentType());
            kegiatanRancanganTeknisFoto.setUploadedAt(LocalDateTime.now());

            uploadedFotos.add(kegiatanRancanganTeknisFotoRepository.save(kegiatanRancanganTeknisFoto));
        }

        return uploadedFotos;
    }

    public List<KegiatanRancanganTeknisPdf> uploadKegiatanRancanganTeknisPdfs(UUID id, List<MultipartFile> files)
            throws Exception {
        Kegiatan kegiatan = kegiatanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("kegiatan tidak ditemukan dengan id: " + id));

        List<KegiatanRancanganTeknisPdf> uploadedPdfs = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioKegiatanService.uploadFile(fileName, file.getInputStream(), file.getContentType());

            KegiatanRancanganTeknisPdf kegiatanRancanganTeknisPdf = new KegiatanRancanganTeknisPdf();
            kegiatanRancanganTeknisPdf.setKegiatan(kegiatan);
            kegiatanRancanganTeknisPdf.setNamaFile(fileName);
            kegiatanRancanganTeknisPdf.setNamaAsli(file.getOriginalFilename());
            kegiatanRancanganTeknisPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanRancanganTeknisPdf.setContentType(file.getContentType());
            kegiatanRancanganTeknisPdf.setUploadedAt(LocalDateTime.now());

            uploadedPdfs.add(kegiatanRancanganTeknisPdfRepository.save(kegiatanRancanganTeknisPdf));
        }

        return uploadedPdfs;
    }

    public List<KegiatanRancanganTeknisVideo> uploadKegiatanRancanganTeknisVideos(UUID id, List<MultipartFile> files)
            throws Exception {
        Kegiatan kegiatan = kegiatanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("kegiatan tidak ditemukan dengan id: " + id));

        List<KegiatanRancanganTeknisVideo> uploadedPdfs = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioKegiatanService.uploadFile(fileName, file.getInputStream(), file.getContentType());

            KegiatanRancanganTeknisVideo uploadedVideos = new KegiatanRancanganTeknisVideo();
            uploadedVideos.setKegiatan(kegiatan);
            uploadedVideos.setNamaFile(fileName);
            uploadedVideos.setNamaAsli(file.getOriginalFilename());
            uploadedVideos.setUkuranMb((double) file.getSize() / (1024 * 1024));
            uploadedVideos.setContentType(file.getContentType());
            uploadedVideos.setUploadedAt(LocalDateTime.now());

            uploadedPdfs.add(kegiatanRancanganTeknisVideoRepository.save(uploadedVideos));
        }

        return uploadedPdfs;
    }

    public List<KegiatanKontrakPdf> uploadKegiatanKontrakPdfs(UUID id, List<MultipartFile> files) throws Exception {
        Kegiatan kegiatan = kegiatanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("kegiatan tidak ditemukan dengan id: " + id));

        List<KegiatanKontrakPdf> uploadedPdfs = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioKegiatanService.uploadFile(fileName, file.getInputStream(), file.getContentType());

            KegiatanKontrakPdf uploadedVideos = new KegiatanKontrakPdf();
            uploadedVideos.setKegiatan(kegiatan);
            uploadedVideos.setNamaFile(fileName);
            uploadedVideos.setNamaAsli(file.getOriginalFilename());
            uploadedVideos.setUkuranMb((double) file.getSize() / (1024 * 1024));
            uploadedVideos.setContentType(file.getContentType());
            uploadedVideos.setUploadedAt(LocalDateTime.now());

            uploadedPdfs.add(kegiatanKontrakPdfRepository.save(uploadedVideos));
        }

        return uploadedPdfs;
    }

    public List<KegiatanDokumentasiFoto> uploadKegiatanDokumentasiFotos(UUID id, List<MultipartFile> files)
            throws Exception {
        Kegiatan kegiatan = kegiatanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("kegiatan tidak ditemukan dengan id: " + id));

        List<KegiatanDokumentasiFoto> uploadedPdfs = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioKegiatanService.uploadFile(fileName, file.getInputStream(), file.getContentType());

            KegiatanDokumentasiFoto uploadedFile = new KegiatanDokumentasiFoto();
            uploadedFile.setKegiatan(kegiatan);
            uploadedFile.setNamaFile(fileName);
            uploadedFile.setNamaAsli(file.getOriginalFilename());
            uploadedFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
            uploadedFile.setContentType(file.getContentType());
            uploadedFile.setUploadedAt(LocalDateTime.now());

            uploadedPdfs.add(kegiatanDokumentasiFotoRepository.save(uploadedFile));
        }

        return uploadedPdfs;
    }

    public List<KegiatanDokumentasiVideo> uploadKegiatanDokumentasiVideos(UUID id, List<MultipartFile> files)
            throws Exception {
        Kegiatan kegiatan = kegiatanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("kegiatan tidak ditemukan dengan id: " + id));

        List<KegiatanDokumentasiVideo> uploadedPdfs = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioKegiatanService.uploadFile(fileName, file.getInputStream(), file.getContentType());

            KegiatanDokumentasiVideo uploadedFile = new KegiatanDokumentasiVideo();
            uploadedFile.setKegiatan(kegiatan);
            uploadedFile.setNamaFile(fileName);
            uploadedFile.setNamaAsli(file.getOriginalFilename());
            uploadedFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
            uploadedFile.setContentType(file.getContentType());
            uploadedFile.setUploadedAt(LocalDateTime.now());

            uploadedPdfs.add(kegiatanDokumentasiVideoRepository.save(uploadedFile));
        }

        return uploadedPdfs;
    }

    // For KegiatanRancanganTeknisFoto
    @Transactional(readOnly = true)
    public byte[] viewRancanganTeknisFoto(UUID kegiatanId, UUID fotoId) {
        // Ambil data foto dari database
        KegiatanRancanganTeknisFoto foto = kegiatanRancanganTeknisFotoRepository.findById(fotoId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Foto rancangan teknis tidak ditemukan dengan id: " + fotoId));

        // Validasi foto tersebut memang milik kegiatan yang dimaksud
        if (!foto.getKegiatan().getId().equals(kegiatanId)) {
            throw new EntityNotFoundException("Foto tidak terkait dengan kegiatan yang dimaksud");
        }

        try {
            // Ambil data file dari penyimpanan (Minio atau sistem file)
            return minioKegiatanService.getFileData(foto.getNamaFile());
        } catch (Exception e) {
            log.error("Gagal mengambil data foto rancangan teknis: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal mengambil data foto rancangan teknis: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public KegiatanRancanganTeknisFoto getRancanganTeknisFotoById(UUID fotoId) {
        return kegiatanRancanganTeknisFotoRepository.findById(fotoId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Foto rancangan teknis tidak ditemukan dengan id: " + fotoId));
    }

    // For KegiatanRancanganTeknisPdf
    @Transactional(readOnly = true)
    public byte[] viewRancanganTeknisPdf(UUID kegiatanId, UUID pdfId) {
        // Ambil data pdf dari database
        KegiatanRancanganTeknisPdf pdf = kegiatanRancanganTeknisPdfRepository.findById(pdfId)
                .orElseThrow(
                        () -> new EntityNotFoundException("PDF rancangan teknis tidak ditemukan dengan id: " + pdfId));

        // Validasi pdf tersebut memang milik kegiatan yang dimaksud
        if (!pdf.getKegiatan().getId().equals(kegiatanId)) {
            throw new EntityNotFoundException("PDF tidak terkait dengan kegiatan yang dimaksud");
        }

        try {
            // Ambil data file dari penyimpanan (Minio atau sistem file)
            return minioKegiatanService.getFileData(pdf.getNamaFile());
        } catch (Exception e) {
            log.error("Gagal mengambil data PDF rancangan teknis: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal mengambil data PDF rancangan teknis: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public KegiatanRancanganTeknisPdf getRancanganTeknisPdfById(UUID pdfId) {
        return kegiatanRancanganTeknisPdfRepository.findById(pdfId)
                .orElseThrow(
                        () -> new EntityNotFoundException("PDF rancangan teknis tidak ditemukan dengan id: " + pdfId));
    }

    // For KegiatanRancanganTeknisVideo
    @Transactional(readOnly = true)
    public byte[] viewRancanganTeknisVideo(UUID kegiatanId, UUID videoId) {
        // Ambil data video dari database
        KegiatanRancanganTeknisVideo video = kegiatanRancanganTeknisVideoRepository.findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Video rancangan teknis tidak ditemukan dengan id: " + videoId));

        // Validasi video tersebut memang milik kegiatan yang dimaksud
        if (!video.getKegiatan().getId().equals(kegiatanId)) {
            throw new EntityNotFoundException("Video tidak terkait dengan kegiatan yang dimaksud");
        }

        try {
            // Ambil data file dari penyimpanan (Minio atau sistem file)
            return minioKegiatanService.getFileData(video.getNamaFile());
        } catch (Exception e) {
            log.error("Gagal mengambil data video rancangan teknis: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal mengambil data video rancangan teknis: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public KegiatanRancanganTeknisVideo getRancanganTeknisVideoById(UUID videoId) {
        return kegiatanRancanganTeknisVideoRepository.findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Video rancangan teknis tidak ditemukan dengan id: " + videoId));
    }

    // For KegiatanKontrakPdf
    @Transactional(readOnly = true)
    public byte[] viewKontrakPdf(UUID kegiatanId, UUID pdfId) {
        // Ambil data pdf dari database
        KegiatanKontrakPdf pdf = kegiatanKontrakPdfRepository.findById(pdfId)
                .orElseThrow(() -> new EntityNotFoundException("PDF kontrak tidak ditemukan dengan id: " + pdfId));

        // Validasi pdf tersebut memang milik kegiatan yang dimaksud
        if (!pdf.getKegiatan().getId().equals(kegiatanId)) {
            throw new EntityNotFoundException("PDF kontrak tidak terkait dengan kegiatan yang dimaksud");
        }

        try {
            // Ambil data file dari penyimpanan (Minio atau sistem file)
            return minioKegiatanService.getFileData(pdf.getNamaFile());
        } catch (Exception e) {
            log.error("Gagal mengambil data PDF kontrak: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal mengambil data PDF kontrak: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public KegiatanKontrakPdf getKontrakPdfById(UUID pdfId) {
        return kegiatanKontrakPdfRepository.findById(pdfId)
                .orElseThrow(() -> new EntityNotFoundException("PDF kontrak tidak ditemukan dengan id: " + pdfId));
    }

    // For KegiatanDokumentasiFoto
    @Transactional(readOnly = true)
    public byte[] viewDokumentasiFoto(UUID kegiatanId, UUID fotoId) {
        // Ambil data foto dari database
        KegiatanDokumentasiFoto foto = kegiatanDokumentasiFotoRepository.findById(fotoId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Foto dokumentasi tidak ditemukan dengan id: " + fotoId));

        // Validasi foto tersebut memang milik kegiatan yang dimaksud
        if (!foto.getKegiatan().getId().equals(kegiatanId)) {
            throw new EntityNotFoundException("Foto dokumentasi tidak terkait dengan kegiatan yang dimaksud");
        }

        try {
            // Ambil data file dari penyimpanan (Minio atau sistem file)
            return minioKegiatanService.getFileData(foto.getNamaFile());
        } catch (Exception e) {
            log.error("Gagal mengambil data foto dokumentasi: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal mengambil data foto dokumentasi: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public KegiatanDokumentasiFoto getDokumentasiFotoById(UUID fotoId) {
        return kegiatanDokumentasiFotoRepository.findById(fotoId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Foto dokumentasi tidak ditemukan dengan id: " + fotoId));
    }

    // For KegiatanDokumentasiVideo
    @Transactional(readOnly = true)
    public byte[] viewDokumentasiVideo(UUID kegiatanId, UUID videoId) {
        // Ambil data video dari database
        KegiatanDokumentasiVideo video = kegiatanDokumentasiVideoRepository.findById(videoId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Video dokumentasi tidak ditemukan dengan id: " + videoId));

        // Validasi video tersebut memang milik kegiatan yang dimaksud
        if (!video.getKegiatan().getId().equals(kegiatanId)) {
            throw new EntityNotFoundException("Video dokumentasi tidak terkait dengan kegiatan yang dimaksud");
        }

        try {
            // Ambil data file dari penyimpanan (Minio atau sistem file)
            return minioKegiatanService.getFileData(video.getNamaFile());
        } catch (Exception e) {
            log.error("Gagal mengambil data video dokumentasi: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal mengambil data video dokumentasi: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public KegiatanDokumentasiVideo getDokumentasiVideoById(UUID videoId) {
        return kegiatanDokumentasiVideoRepository.findById(videoId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Video dokumentasi tidak ditemukan dengan id: " + videoId));
    }

    @Transactional
    public KegiatanDto deleteRancanganTeknisFotos(UUID kegiatanId, List<UUID> fotoIds) throws Exception {
        Kegiatan kegiatan = kegiatanRepository.findById(kegiatanId)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan tidak ditemukan dengan id: " + kegiatanId));

        List<KegiatanRancanganTeknisFoto> fotosToDelete = kegiatanRancanganTeknisFotoRepository.findAllById(fotoIds);

        for (KegiatanRancanganTeknisFoto foto : fotosToDelete) {
            if (foto.getKegiatan().getId().equals(kegiatanId)) {
                minioKegiatanService.deleteFile(foto.getNamaFile());
                kegiatanRancanganTeknisFotoRepository.delete(foto);
            }
        }

        Kegiatan saved = kegiatanRepository.save(kegiatan);
        return convertToDto(saved);
    }

    @Transactional
    public KegiatanDto deleteRancanganTeknisPdfs(UUID kegiatanId, List<UUID> pdfIds) throws Exception {
        Kegiatan kegiatan = kegiatanRepository.findById(kegiatanId)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan tidak ditemukan dengan id: " + kegiatanId));

        List<KegiatanRancanganTeknisPdf> pdfsToDelete = kegiatanRancanganTeknisPdfRepository.findAllById(pdfIds);

        for (KegiatanRancanganTeknisPdf pdf : pdfsToDelete) {
            if (pdf.getKegiatan().getId().equals(kegiatanId)) {
                minioKegiatanService.deleteFile(pdf.getNamaFile());
                kegiatanRancanganTeknisPdfRepository.delete(pdf);
            }
        }

        Kegiatan saved = kegiatanRepository.save(kegiatan);
        return convertToDto(saved);
    }

    @Transactional
    public KegiatanDto deleteRancanganTeknisVideos(UUID kegiatanId, List<UUID> videoIds) throws Exception {
        Kegiatan kegiatan = kegiatanRepository.findById(kegiatanId)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan tidak ditemukan dengan id: " + kegiatanId));

        List<KegiatanRancanganTeknisVideo> videosToDelete = kegiatanRancanganTeknisVideoRepository
                .findAllById(videoIds);

        for (KegiatanRancanganTeknisVideo video : videosToDelete) {
            if (video.getKegiatan().getId().equals(kegiatanId)) {
                minioKegiatanService.deleteFile(video.getNamaFile());
                kegiatanRancanganTeknisVideoRepository.delete(video);
            }
        }

        Kegiatan saved = kegiatanRepository.save(kegiatan);
        return convertToDto(saved);
    }

    @Transactional
    public KegiatanDto deleteKontrakPdfs(UUID kegiatanId, List<UUID> pdfIds) throws Exception {
        Kegiatan kegiatan = kegiatanRepository.findById(kegiatanId)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan tidak ditemukan dengan id: " + kegiatanId));

        List<KegiatanKontrakPdf> pdfsToDelete = kegiatanKontrakPdfRepository.findAllById(pdfIds);

        for (KegiatanKontrakPdf pdf : pdfsToDelete) {
            if (pdf.getKegiatan().getId().equals(kegiatanId)) {
                minioKegiatanService.deleteFile(pdf.getNamaFile());
                kegiatanKontrakPdfRepository.delete(pdf);
            }
        }

        Kegiatan saved = kegiatanRepository.save(kegiatan);
        return convertToDto(saved);
    }

    @Transactional
    public KegiatanDto deleteDokumentasiFotos(UUID kegiatanId, List<UUID> fotoIds) throws Exception {
        Kegiatan kegiatan = kegiatanRepository.findById(kegiatanId)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan tidak ditemukan dengan id: " + kegiatanId));

        List<KegiatanDokumentasiFoto> fotosToDelete = kegiatanDokumentasiFotoRepository.findAllById(fotoIds);

        for (KegiatanDokumentasiFoto foto : fotosToDelete) {
            if (foto.getKegiatan().getId().equals(kegiatanId)) {
                minioKegiatanService.deleteFile(foto.getNamaFile());
                kegiatanDokumentasiFotoRepository.delete(foto);
            }
        }

        Kegiatan saved = kegiatanRepository.save(kegiatan);
        return convertToDto(saved);
    }

    @Transactional
    public KegiatanDto deleteDokumentasiVideos(UUID kegiatanId, List<UUID> videoIds) throws Exception {
        Kegiatan kegiatan = kegiatanRepository.findById(kegiatanId)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan tidak ditemukan dengan id: " + kegiatanId));

        List<KegiatanDokumentasiVideo> videosToDelete = kegiatanDokumentasiVideoRepository.findAllById(videoIds);

        for (KegiatanDokumentasiVideo video : videosToDelete) {
            if (video.getKegiatan().getId().equals(kegiatanId)) {
                minioKegiatanService.deleteFile(video.getNamaFile());
                kegiatanDokumentasiVideoRepository.delete(video);
            }
        }

        Kegiatan saved = kegiatanRepository.save(kegiatan);
        return convertToDto(saved);
    }
}
