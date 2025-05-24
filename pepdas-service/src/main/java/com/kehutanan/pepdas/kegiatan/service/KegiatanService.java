package com.kehutanan.pepdas.kegiatan.service;

import com.kehutanan.pepdas.common.service.MinioStorageService;
import com.kehutanan.pepdas.kegiatan.dto.KegiatanDto;
import com.kehutanan.pepdas.kegiatan.dto.KegiatanFileDto;
import com.kehutanan.pepdas.kegiatan.model.Kegiatan;
import com.kehutanan.pepdas.kegiatan.model.KegiatanDokumentasiFoto;
import com.kehutanan.pepdas.kegiatan.model.KegiatanDokumentasiVideo;
import com.kehutanan.pepdas.kegiatan.model.KegiatanKontrakPdf;
import com.kehutanan.pepdas.kegiatan.model.KegiatanLokusShp;
import com.kehutanan.pepdas.kegiatan.model.KegiatanRancanganTeknisFoto;
import com.kehutanan.pepdas.kegiatan.model.KegiatanRancanganTeknisPdf;
import com.kehutanan.pepdas.kegiatan.model.KegiatanRancanganTeknisVideo;
import com.kehutanan.pepdas.kegiatan.repository.KegiatanRancanganTeknisVideoRepository;
import com.kehutanan.pepdas.kegiatan.repository.KegiatanRepository;
import com.kehutanan.pepdas.program.model.Program;
import com.kehutanan.pepdas.program.repository.ProgramRepository;
import com.kehutanan.pepdas.util.FileValidationUtil;
import com.kehutanan.pepdas.kegiatan.repository.KegiatanDokumentasiFotoRepository;
import com.kehutanan.pepdas.kegiatan.repository.KegiatanDokumentasiVideoRepository;
import com.kehutanan.pepdas.kegiatan.repository.KegiatanKontrakPdfRepository;
import com.kehutanan.pepdas.kegiatan.repository.KegiatanLokusShpRepository;
import com.kehutanan.pepdas.kegiatan.repository.KegiatanRancanganTeknisFotoRepository;
import com.kehutanan.pepdas.kegiatan.repository.KegiatanRancanganTeknisPdfRepository;

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
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    private final FileValidationUtil fileValidationUtil;
    private final ProgramRepository programRepository;
    private final KegiatanRepository kegiatanRepository;
    private final KegiatanRancanganTeknisFotoRepository kegiatanRancanganTeknisFotoRepository;
    private final KegiatanRancanganTeknisPdfRepository kegiatanRancanganTeknisPdfRepository;
    private final MinioStorageService minioStorageService;
    private final KegiatanKontrakPdfRepository kegiatanKontrakPdfRepository;
    private final KegiatanDokumentasiFotoRepository kegiatanDokumentasiFotoRepository;
    private final KegiatanDokumentasiVideoRepository kegiatanDokumentasiVideoRepository;
    private final KegiatanLokusShpRepository kegiatanLokusShpRepository;

    private String FOLDER_PREFIX = "Kegiatan/";

    public Page<Kegiatan> findAll(Pageable pageable, String programName, String namaKegiatan) {
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

        return kegiatanPage.map(kegiatan -> {
            return kegiatan;
        });
    }

    public Kegiatan findById(UUID id) {
        return kegiatanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan tidak ditemukan dengan id: " + id));
    }

    @Transactional
    public Kegiatan create(KegiatanDto kegiatanDto) {

        Kegiatan kegiatan = new Kegiatan();
        kegiatan.setSubDirektorat(kegiatanDto.getSubDirektorat());
        if (kegiatanDto.getProgramId() != null) {
            Program program = programRepository.findById(kegiatanDto.getProgramId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Program tidak ditemukan dengan id: " + kegiatanDto.getProgramId()));
            kegiatan.setProgram(program);
        }
        kegiatan.setJenisKegiatan(kegiatanDto.getJenisKegiatan());
        kegiatan.setNamaKegiatan(kegiatanDto.getNamaKegiatan());
        kegiatan.setBpdas(kegiatanDto.getBpdas());
        kegiatan.setDas(kegiatanDto.getDas());
        kegiatan.setLokusProvinsi(kegiatanDto.getLokusProvinsi());
        kegiatan.setLokusKabupatenKota(kegiatanDto.getLokusKabupatenKota());
        kegiatan.setLokusKecamatan(kegiatanDto.getLokusKecamatan());
        kegiatan.setLokusKelurahanDesa(kegiatanDto.getLokusKelurahanDesa());
        kegiatan.setLokusAlamat(kegiatanDto.getLokusAlamat());
        kegiatan.setDetailSkema(kegiatanDto.getDetailSkema());
        kegiatan.setDetailTahunKegiatan(kegiatanDto.getDetailTahunKegiatan());
        kegiatan.setDetailSumberAnggaran(kegiatanDto.getDetailSumberAnggaran());
        kegiatan.setDetailSumberAnggaran(kegiatanDto.getDetailSumberAnggaran());
        kegiatan.setDetailPenerimaManfaat(kegiatanDto.getDetailPenerimaManfaat());
        kegiatan.setDetailPelaksana(kegiatanDto.getDetailPelaksana());
        kegiatan.setKontrakNomor(kegiatanDto.getKontrakNomor());
        kegiatan.setKontrakNilai(kegiatanDto.getKontrakNilai());
        kegiatan.setKontrakTipe(kegiatanDto.getKontrakTipe());
        kegiatan.setKontrakPenerimaManfaat(kegiatanDto.getKontrakPenerimaManfaat());
        kegiatan.setKontrakTanggalKontrak(kegiatanDto.getKontrakTanggalKontrak());
        kegiatan.setKontrakStatus(kegiatanDto.getKontrakStatus());
        kegiatan.setDokumentasiCatatanFoto(kegiatanDto.getDokumentasiCatatanFoto());
        kegiatan.setDokumentasiCatatanVideo(kegiatanDto.getDokumentasiCatatanVideo());

        return kegiatanRepository.save(kegiatan);
    }

    @Transactional
    public Kegiatan update(UUID id, KegiatanDto kegiatanDto) {
        Kegiatan kegiatan = findById(id);

        kegiatan.setSubDirektorat(kegiatanDto.getSubDirektorat());
        if (kegiatanDto.getProgramId() != null) {
            Program program = programRepository.findById(kegiatanDto.getProgramId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Program tidak ditemukan dengan id: " + kegiatanDto.getProgramId()));
            kegiatan.setProgram(program);
        }
        kegiatan.setJenisKegiatan(kegiatanDto.getJenisKegiatan());
        kegiatan.setNamaKegiatan(kegiatanDto.getNamaKegiatan());
        kegiatan.setBpdas(kegiatanDto.getBpdas());
        kegiatan.setDas(kegiatanDto.getDas());
        kegiatan.setLokusProvinsi(kegiatanDto.getLokusProvinsi());
        kegiatan.setLokusKabupatenKota(kegiatanDto.getLokusKabupatenKota());
        kegiatan.setLokusKecamatan(kegiatanDto.getLokusKecamatan());
        kegiatan.setLokusKelurahanDesa(kegiatanDto.getLokusKelurahanDesa());
        kegiatan.setLokusAlamat(kegiatanDto.getLokusAlamat());
        kegiatan.setDetailSkema(kegiatanDto.getDetailSkema());
        kegiatan.setDetailTahunKegiatan(kegiatanDto.getDetailTahunKegiatan());
        kegiatan.setDetailSumberAnggaran(kegiatanDto.getDetailSumberAnggaran());
        kegiatan.setDetailSumberAnggaran(kegiatanDto.getDetailSumberAnggaran());
        kegiatan.setDetailPenerimaManfaat(kegiatanDto.getDetailPenerimaManfaat());
        kegiatan.setDetailPelaksana(kegiatanDto.getDetailPelaksana());
        kegiatan.setKontrakNomor(kegiatanDto.getKontrakNomor());
        kegiatan.setKontrakNilai(kegiatanDto.getKontrakNilai());
        kegiatan.setKontrakTipe(kegiatanDto.getKontrakTipe());
        kegiatan.setKontrakPenerimaManfaat(kegiatanDto.getKontrakPenerimaManfaat());
        kegiatan.setKontrakTanggalKontrak(kegiatanDto.getKontrakTanggalKontrak());
        kegiatan.setKontrakStatus(kegiatanDto.getKontrakStatus());
        kegiatan.setDokumentasiCatatanFoto(kegiatanDto.getDokumentasiCatatanFoto());
        kegiatan.setDokumentasiCatatanVideo(kegiatanDto.getDokumentasiCatatanVideo());

        return kegiatanRepository.save(kegiatan);
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

    public ResponseEntity<byte[]> downloadFileShp(UUID kegiatanId, UUID shpId) throws Exception {
        Kegiatan kegiatan = findById(kegiatanId);
        KegiatanLokusShp shpFile = kegiatan.getKegiatanLokusShps().stream()
                .filter(f -> f.getId().equals(shpId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("File tidak ditemukan"));

        byte[] data = minioStorageService.getFileData(FOLDER_PREFIX, shpFile.getNamaFile());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType
                .parseMediaType(shpFile.getContentType()));
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(shpFile.getNamaAsli())
                .build());
        headers.setContentLength(data.length);

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    @Transactional
    public Kegiatan addFilesShp(UUID id, List<MultipartFile> files) throws Exception {
        Kegiatan kegiatanLokus = findById(id);

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "shp");
            fileValidationUtil.validateFileSize(file);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioStorageService.uploadFile(FOLDER_PREFIX, fileName, file.getInputStream(), file.getContentType());

            KegiatanLokusShp dokumenFile = new KegiatanLokusShp();
            dokumenFile.setKegiatanLokus(kegiatanLokus);
            dokumenFile.setNamaFile(fileName);
            dokumenFile.setNamaAsli(file.getOriginalFilename());
            dokumenFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
            dokumenFile.setContentType(file.getContentType());
            dokumenFile.setUploadedAt(LocalDateTime.now());

            kegiatanLokus.getKegiatanLokusShps().add(dokumenFile);
        }

        return kegiatanRepository.save(kegiatanLokus);
    }

    @Transactional
    public Kegiatan deleteFilesShp(UUID lokusId, List<UUID> fileIds) throws Exception {
        Kegiatan kegiatan = findById(lokusId);
        System.out.println("Deleting files with IDs: " + fileIds);

        List<KegiatanLokusShp> shpsToDelete = kegiatanLokusShpRepository.findAllById(fileIds);

        for (KegiatanLokusShp shp : shpsToDelete) {
            if (shp.getKegiatanLokus().getId().equals(lokusId)) {
                try {
                    minioStorageService.deleteFile(FOLDER_PREFIX, shp.getNamaFile());
                    kegiatanLokusShpRepository.delete(shp);
                } catch (Exception e) {
                    System.err.println("Failed to delete file from storage: " + e.getMessage());
                    throw e;
                }
            }
        }

        return kegiatanRepository.save(kegiatan);
    }

    /**
     * Download PDF file for rancangan teknis
     * 
     * @param kegiatanId ID of the kegiatan
     * @param pdfId      ID of the PDF file to download
     * @return ResponseEntity with file data and appropriate headers
     * @throws Exception if file cannot be downloaded
     */
    public ResponseEntity<byte[]> downloadFileRancanganTeknisPdf(UUID kegiatanId, UUID pdfId) throws Exception {
        Kegiatan kegiatan = findById(kegiatanId);
        KegiatanRancanganTeknisPdf pdfFile = kegiatan.getKegiatanRancanganTeknisPdfs().stream()
                .filter(f -> f.getId().equals(pdfId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("File PDF tidak ditemukan"));

        byte[] data = minioStorageService.getFileData(FOLDER_PREFIX, pdfFile.getNamaFile());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(pdfFile.getContentType()));
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(pdfFile.getNamaAsli())
                .build());
        headers.setContentLength(data.length);

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    /**
     * Add PDF files to a kegiatan's rancangan teknis
     * 
     * @param id    ID of the kegiatan
     * @param files List of PDF files to upload
     * @return Updated kegiatan entity
     * @throws Exception if files cannot be processed or uploaded
     */
    @Transactional
    public Kegiatan addFilesRancanganTeknisPdf(UUID id, List<MultipartFile> files) throws Exception {
        Kegiatan kegiatan = findById(id);

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioStorageService.uploadFile(FOLDER_PREFIX, fileName, file.getInputStream(), file.getContentType());

            KegiatanRancanganTeknisPdf pdfFile = new KegiatanRancanganTeknisPdf();
            pdfFile.setKegiatan(kegiatan);
            pdfFile.setNamaFile(fileName);
            pdfFile.setNamaAsli(file.getOriginalFilename());
            pdfFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
            pdfFile.setContentType(file.getContentType());
            pdfFile.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanRancanganTeknisPdfs().add(pdfFile);
        }

        return kegiatanRepository.save(kegiatan);
    }

    /**
     * Delete PDF files from a kegiatan's rancangan teknis
     * 
     * @param kegiatanId ID of the kegiatan
     * @param fileIds    List of file IDs to delete
     * @return Updated kegiatan entity
     * @throws Exception if files cannot be deleted
     */
    @Transactional
    public Kegiatan deleteFilesRancanganTeknisPdf(UUID kegiatanId, List<UUID> fileIds) throws Exception {
        Kegiatan kegiatan = findById(kegiatanId);

        List<KegiatanRancanganTeknisPdf> pdfsToDelete = kegiatanRancanganTeknisPdfRepository.findAllById(fileIds);

        for (KegiatanRancanganTeknisPdf pdf : pdfsToDelete) {
            if (pdf.getKegiatan().getId().equals(kegiatanId)) {
                try {
                    minioStorageService.deleteFile(FOLDER_PREFIX, pdf.getNamaFile());
                    kegiatanRancanganTeknisPdfRepository.delete(pdf);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete PDF file from storage: " + e.getMessage(), e);
                }
            }
        }

        return kegiatanRepository.save(kegiatan);
    }

    /**
     * Download Image file for rancangan teknis
     * 
     * @param kegiatanId ID of the kegiatan
     * @param imageId    ID of the Image file to download
     * @return ResponseEntity with file data and appropriate headers
     * @throws Exception if file cannot be downloaded
     */
    public ResponseEntity<byte[]> downloadFileRancanganTeknisImage(UUID kegiatanId, UUID imageId) throws Exception {
        Kegiatan kegiatan = findById(kegiatanId);
        KegiatanRancanganTeknisFoto imageFile = kegiatan.getKegiatanRancanganTeknisFotos().stream()
                .filter(f -> f.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("File Foto tidak ditemukan"));

        byte[] data = minioStorageService.getFileData(FOLDER_PREFIX, imageFile.getNamaFile());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(imageFile.getContentType()));
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename(imageFile.getNamaAsli())
                .build());
        headers.setContentLength(data.length);

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    /**
     * Add Image files to a kegiatan's rancangan teknis
     * 
     * @param id    ID of the kegiatan
     * @param files List of Image files to upload
     * @return Updated kegiatan entity
     * @throws Exception if files cannot be processed or uploaded
     */
    @Transactional
    public Kegiatan addFilesRancanganTeknisImage(UUID id, List<MultipartFile> files) throws Exception {
        Kegiatan kegiatan = findById(id);

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "image");
            fileValidationUtil.validateFileSize(file);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioStorageService.uploadFile(FOLDER_PREFIX, fileName, file.getInputStream(), file.getContentType());

            KegiatanRancanganTeknisFoto fotoFile = new KegiatanRancanganTeknisFoto();
            fotoFile.setKegiatan(kegiatan);
            fotoFile.setNamaFile(fileName);
            fotoFile.setNamaAsli(file.getOriginalFilename());
            fotoFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
            fotoFile.setContentType(file.getContentType());
            fotoFile.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanRancanganTeknisFotos().add(fotoFile);
        }

        return kegiatanRepository.save(kegiatan);
    }

    /**
     * Delete Image files from a kegiatan's rancangan teknis
     * 
     * @param kegiatanId ID of the kegiatan
     * @param fileIds    List of file IDs to delete
     * @return Updated kegiatan entity
     * @throws Exception if files cannot be deleted
     */
    @Transactional
    public Kegiatan deleteFilesRancanganTeknisImage(UUID kegiatanId, List<UUID> fileIds) throws Exception {
        Kegiatan kegiatan = findById(kegiatanId);

        List<KegiatanRancanganTeknisFoto> fotosToDelete = kegiatanRancanganTeknisFotoRepository.findAllById(fileIds);

        for (KegiatanRancanganTeknisFoto foto : fotosToDelete) {
            if (foto.getKegiatan().getId().equals(kegiatanId)) {
                try {
                    minioStorageService.deleteFile(FOLDER_PREFIX, foto.getNamaFile());
                    kegiatanRancanganTeknisFotoRepository.delete(foto);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete Image file from storage: " + e.getMessage(), e);
                }
            }
        }

        return kegiatanRepository.save(kegiatan);
    }

    /**
     * Add Image files to a kegiatan's rancangan teknis
     * 
     * @param id    ID of the kegiatan
     * @param files List of Image files to upload
     * @return Updated kegiatan entity
     * @throws Exception if files cannot be processed or uploaded
     */
    @Transactional
    public Kegiatan addFilesRancanganTeknisVideo(UUID id, List<MultipartFile> files) throws Exception {
        Kegiatan kegiatan = findById(id);

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "video");
            fileValidationUtil.validateFileSize(file);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioStorageService.uploadFile(FOLDER_PREFIX, fileName, file.getInputStream(), file.getContentType());

            KegiatanRancanganTeknisVideo fotoFile = new KegiatanRancanganTeknisVideo();
            fotoFile.setKegiatan(kegiatan);
            fotoFile.setNamaFile(fileName);
            fotoFile.setNamaAsli(file.getOriginalFilename());
            fotoFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
            fotoFile.setContentType(file.getContentType());
            fotoFile.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanRancanganTeknisVideos().add(fotoFile);
        }

        return kegiatanRepository.save(kegiatan);
    }

    /**
     * Delete Image files from a kegiatan's rancangan teknis
     * 
     * @param kegiatanId ID of the kegiatan
     * @param fileIds    List of file IDs to delete
     * @return Updated kegiatan entity
     * @throws Exception if files cannot be deleted
     */
    @Transactional
    public Kegiatan deleteFilesRancanganTeknisVideo(UUID kegiatanId, List<UUID> fileIds) throws Exception {
        Kegiatan kegiatan = findById(kegiatanId);

        List<KegiatanRancanganTeknisVideo> videoToDelete = kegiatanRancanganTeknisVideoRepository.findAllById(fileIds);

        for (KegiatanRancanganTeknisVideo video : videoToDelete) {
            if (video.getKegiatan().getId().equals(kegiatanId)) {
                try {
                    minioStorageService.deleteFile(FOLDER_PREFIX, video.getNamaFile());
                    kegiatanRancanganTeknisVideoRepository.delete(video);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete Video file from storage: " + e.getMessage(), e);
                }
            }
        }

        return kegiatanRepository.save(kegiatan);
    }

    /**
     * Stream Video file for rancangan teknis - untuk ditampilkan di browser
     * 
     * @param kegiatanId ID of the kegiatan
     * @param videoId    ID of the Video file to stream
     * @return ResponseEntity with file data and appropriate headers for streaming
     * @throws Exception if file cannot be streamed
     */
    public ResponseEntity<byte[]> streamFileRancanganTeknisVideo(UUID kegiatanId, UUID videoId) throws Exception {
        Kegiatan kegiatan = findById(kegiatanId);
        KegiatanRancanganTeknisVideo videoFile = kegiatan.getKegiatanRancanganTeknisVideos().stream()
                .filter(f -> f.getId().equals(videoId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("File Video tidak ditemukan"));

        byte[] data = minioStorageService.getFileData(FOLDER_PREFIX, videoFile.getNamaFile());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(videoFile.getContentType()));
        // Gunakan inline untuk streaming/view di browser
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename(videoFile.getNamaAsli())
                .build());
        headers.setContentLength(data.length);

        // Headers untuk optimasi streaming
        headers.add("Accept-Ranges", "bytes");
        headers.setCacheControl("max-age=3600");

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

     public Kegiatan addFilesKontrakPdf(UUID id, List<MultipartFile> files) throws IOException, Exception {
        Kegiatan kegiatan = findById(id);

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioStorageService.uploadFile(FOLDER_PREFIX, fileName, file.getInputStream(), file.getContentType());

            KegiatanKontrakPdf pdfFile = new KegiatanKontrakPdf();
            pdfFile.setKegiatan(kegiatan);
            pdfFile.setNamaFile(fileName);
            pdfFile.setNamaAsli(file.getOriginalFilename());
            pdfFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
            pdfFile.setContentType(file.getContentType());
            pdfFile.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanKontrakPdfs().add(pdfFile);
        }

        return kegiatanRepository.save(kegiatan);
    }

    public ResponseEntity<byte[]> downloadFileKontrakPdf(UUID kegiatanId, UUID pdfId)  throws IOException, Exception{
        Kegiatan kegiatan = findById(kegiatanId);
        KegiatanKontrakPdf pdfFile = kegiatan.getKegiatanKontrakPdfs().stream()
                .filter(f -> f.getId().equals(pdfId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("File PDF tidak ditemukan"));

        byte[] data = minioStorageService.getFileData(FOLDER_PREFIX, pdfFile.getNamaFile());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(pdfFile.getContentType()));
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(pdfFile.getNamaAsli())
                .build());
        headers.setContentLength(data.length);

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

     public Kegiatan deleteFilesKontrakPdf(UUID kegiatanId, List<UUID> fileIds) throws IOException, Exception {
        Kegiatan kegiatan = findById(kegiatanId);

        List<KegiatanKontrakPdf> pdfsToDelete = kegiatanKontrakPdfRepository.findAllById(fileIds);

        for (KegiatanKontrakPdf pdf : pdfsToDelete) {
            if (pdf.getKegiatan().getId().equals(kegiatanId)) {
                try {
                    minioStorageService.deleteFile(FOLDER_PREFIX, pdf.getNamaFile());
                    kegiatanKontrakPdfRepository.delete(pdf);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete PDF file from storage: " + e.getMessage(), e);
                }
            }
        }

        return kegiatanRepository.save(kegiatan);
    }

    
    /**
     * Download Image file for rancangan teknis
     * 
     * @param kegiatanId ID of the kegiatan
     * @param imageId    ID of the Image file to download
     * @return ResponseEntity with file data and appropriate headers
     * @throws Exception if file cannot be downloaded
     */
    public ResponseEntity<byte[]> downloadFileDokumentasiImage(UUID kegiatanId, UUID imageId) throws Exception {
        Kegiatan kegiatan = findById(kegiatanId);
        KegiatanDokumentasiFoto imageFile = kegiatan.getKegiatanDokumentasiFotos().stream()
                .filter(f -> f.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("File Foto tidak ditemukan"));

        byte[] data = minioStorageService.getFileData(FOLDER_PREFIX, imageFile.getNamaFile());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(imageFile.getContentType()));
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename(imageFile.getNamaAsli())
                .build());
        headers.setContentLength(data.length);

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    @Transactional
    public Kegiatan addFilesDokumentasiImage(UUID id, List<MultipartFile> files) throws Exception {
        Kegiatan kegiatan = findById(id);

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "image");
            fileValidationUtil.validateFileSize(file);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioStorageService.uploadFile(FOLDER_PREFIX, fileName, file.getInputStream(), file.getContentType());

            KegiatanDokumentasiFoto fotoFile = new KegiatanDokumentasiFoto();
            fotoFile.setKegiatan(kegiatan);
            fotoFile.setNamaFile(fileName);
            fotoFile.setNamaAsli(file.getOriginalFilename());
            fotoFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
            fotoFile.setContentType(file.getContentType());
            fotoFile.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanDokumentasiFotos().add(fotoFile);
        }

        return kegiatanRepository.save(kegiatan);
    }

    /**
     * Delete Image files from a kegiatan's rancangan teknis
     * 
     * @param kegiatanId ID of the kegiatan
     * @param fileIds    List of file IDs to delete
     * @return Updated kegiatan entity
     * @throws Exception if files cannot be deleted
     */
    @Transactional
    public Kegiatan deleteFilesDokumentasiImage(UUID kegiatanId, List<UUID> fileIds) throws Exception {
        Kegiatan kegiatan = findById(kegiatanId);

        List<KegiatanDokumentasiFoto> fotosToDelete = kegiatanDokumentasiFotoRepository.findAllById(fileIds);

        for (KegiatanDokumentasiFoto foto : fotosToDelete) {
            if (foto.getKegiatan().getId().equals(kegiatanId)) {
                try {
                    minioStorageService.deleteFile(FOLDER_PREFIX, foto.getNamaFile());
                    kegiatanDokumentasiFotoRepository.delete(foto);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete Image file from storage: " + e.getMessage(), e);
                }
            }
        }

        return kegiatanRepository.save(kegiatan);
    }

    @Transactional
    public Kegiatan addFilesDokumentasiVideo(UUID id, List<MultipartFile> files) throws Exception {
        Kegiatan kegiatan = findById(id);

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "video");
            fileValidationUtil.validateFileSize(file);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioStorageService.uploadFile(FOLDER_PREFIX, fileName, file.getInputStream(), file.getContentType());

            KegiatanDokumentasiVideo fotoFile = new KegiatanDokumentasiVideo();
            fotoFile.setKegiatan(kegiatan);
            fotoFile.setNamaFile(fileName);
            fotoFile.setNamaAsli(file.getOriginalFilename());
            fotoFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
            fotoFile.setContentType(file.getContentType());
            fotoFile.setUploadedAt(LocalDateTime.now());

            kegiatan.getKegiatanDokumentasiVideos().add(fotoFile);
        }

        return kegiatanRepository.save(kegiatan);
    }


    @Transactional
    public Kegiatan deleteFilesDokumentasiVideo(UUID kegiatanId, List<UUID> fileIds) throws Exception {
        Kegiatan kegiatan = findById(kegiatanId);

        List<KegiatanDokumentasiVideo> videoToDelete = kegiatanDokumentasiVideoRepository.findAllById(fileIds);

        for (KegiatanDokumentasiVideo video : videoToDelete) {
            if (video.getKegiatan().getId().equals(kegiatanId)) {
                try {
                    minioStorageService.deleteFile(FOLDER_PREFIX, video.getNamaFile());
                    kegiatanDokumentasiVideoRepository.delete(video);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete Video file from storage: " + e.getMessage(), e);
                }
            }
        }

        return kegiatanRepository.save(kegiatan);
    }

    /**
     * Stream Video file for rancangan teknis - untuk ditampilkan di browser
     * 
     * @param kegiatanId ID of the kegiatan
     * @param videoId    ID of the Video file to stream
     * @return ResponseEntity with file data and appropriate headers for streaming
     * @throws Exception if file cannot be streamed
     */
    public ResponseEntity<byte[]> streamFileDokumentasiVideo(UUID kegiatanId, UUID videoId) throws Exception {
        Kegiatan kegiatan = findById(kegiatanId);
        KegiatanDokumentasiVideo videoFile = kegiatan.getKegiatanDokumentasiVideos().stream()
                .filter(f -> f.getId().equals(videoId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("File Video tidak ditemukan"));

        byte[] data = minioStorageService.getFileData(FOLDER_PREFIX, videoFile.getNamaFile());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(videoFile.getContentType()));
        // Gunakan inline untuk streaming/view di browser
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename(videoFile.getNamaAsli())
                .build());
        headers.setContentLength(data.length);

        // Headers untuk optimasi streaming
        headers.add("Accept-Ranges", "bytes");
        headers.setCacheControl("max-age=3600");

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

}
