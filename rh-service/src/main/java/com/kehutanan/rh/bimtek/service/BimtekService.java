package com.kehutanan.rh.bimtek.service;

import com.kehutanan.rh.bimtek.dto.BimtekDto;
import com.kehutanan.rh.bimtek.model.Bimtek;
import com.kehutanan.rh.bimtek.model.BimtekFoto;
import com.kehutanan.rh.bimtek.model.BimtekPdf;
import com.kehutanan.rh.bimtek.model.BimtekVideo;
import com.kehutanan.rh.bimtek.repository.BimtekFotoRepository;
import com.kehutanan.rh.bimtek.repository.BimtekPdfRepository;
import com.kehutanan.rh.bimtek.repository.BimtekRepository;
import com.kehutanan.rh.bimtek.repository.BimtekVideoRepository;
import com.kehutanan.rh.util.FileValidationUtil;

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
public class BimtekService {

    private final BimtekRepository bimtekRepository;
    private final BimtekFotoRepository bimtekFotoRepository;
    private final BimtekVideoRepository bimtekVideoRepository;
    private final MinioBimtekService minioBimtekService;
    private final BimtekPdfRepository bimtekPdfRepository;
    private final FileValidationUtil fileValidationUtil;

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

    @Transactional(readOnly = true)
    public Bimtek getBimtekById(UUID id) {
        Bimtek bimtek = bimtekRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bimtek tidak ditemukan dengan id: " + id));

        // Load photos and set URLs
        List<BimtekFoto> fotos = bimtekFotoRepository.findByBimtekId(id);

        bimtek.setFotos(fotos);

        return bimtek;
    }

    @Transactional(readOnly = true)
    public List<BimtekFoto> getFotosByBimtekId(UUID bimtekId) {
        if (!bimtekRepository.existsById(bimtekId)) {
            throw new EntityNotFoundException("Bimtek tidak ditemukan dengan id: " + bimtekId);
        }
        List<BimtekFoto> fotos = bimtekFotoRepository.findByBimtekId(bimtekId);


        return fotos;
    }

    @Transactional
    public Bimtek create(BimtekDto bimtekDto) {
        Bimtek bimtek = new Bimtek();
        bimtek.setNamaBimtek(bimtekDto.getNamaBimtek());
        bimtek.setSubjek(bimtekDto.getSubjek());
        bimtek.setProgram(bimtekDto.getProgram());
        bimtek.setBpdas(bimtekDto.getBpdas());
        bimtek.setTempat(bimtekDto.getTempat());
        bimtek.setTanggal(bimtekDto.getTanggal());
        bimtek.setAudience(bimtekDto.getAudience());
        bimtek.setEvaluasi(bimtekDto.getEvaluasi());
        bimtek.setKeterangan(bimtekDto.getKeterangan());

        return bimtekRepository.save(bimtek);
    }

    @Transactional
    public Bimtek update(UUID id, BimtekDto bimtekDto) {
        Bimtek existing = findById(id);

        existing.setNamaBimtek(bimtekDto.getNamaBimtek());
        existing.setSubjek(bimtekDto.getSubjek());
        existing.setProgram(bimtekDto.getProgram());
        existing.setBpdas(bimtekDto.getBpdas());
        existing.setTempat(bimtekDto.getTempat());
        existing.setTanggal(bimtekDto.getTanggal());
        existing.setAudience(bimtekDto.getAudience());
        existing.setEvaluasi(bimtekDto.getEvaluasi());
        existing.setKeterangan(bimtekDto.getKeterangan());

        return bimtekRepository.save(existing);
    }

@Transactional
public void delete(UUID id) {
    Bimtek bimtek = bimtekRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Bimtek tidak ditemukan dengan id: " + id));
    
    try {
        // Delete all photos associated with the bimtek
        List<BimtekFoto> fotos = bimtekFotoRepository.findByBimtekId(id);
        List<UUID> fotoIds = fotos.stream().map(BimtekFoto::getId).toList();
        if (!fotoIds.isEmpty()) {
            deleteFotos(id, fotoIds);
        }
        
        // Delete all videos associated with the bimtek
        List<BimtekVideo> videos = bimtekVideoRepository.findByBimtekId(id);
        List<UUID> videoIds = videos.stream().map(BimtekVideo::getId).toList();
        if (!videoIds.isEmpty()) {
            deleteVideos(id, videoIds);
        }
        
        // Delete all PDFs associated with the bimtek
        List<BimtekPdf> pdfs = bimtekPdfRepository.findByBimtekId(id);
        List<UUID> pdfIds = pdfs.stream().map(BimtekPdf::getId).toList();
        if (!pdfIds.isEmpty()) {
            deletePdfs(id, pdfIds);
        }
        
        // Finally delete the bimtek record itself
        bimtekRepository.deleteById(id);
    } catch (Exception e) {
        log.error("Gagal menghapus Bimtek dan file terkait: {}", e.getMessage(), e);
        throw new RuntimeException("Gagal menghapus Bimtek dan file terkait: " + e.getMessage(), e);
    }
}

    @Transactional
    public List<BimtekFoto> uploadFotos(UUID bimtekId, List<MultipartFile> files) throws Exception {
        Bimtek bimtek = bimtekRepository.findById(bimtekId)
                .orElseThrow(() -> new EntityNotFoundException("Bimtek tidak ditemukan dengan id: " + bimtekId));

        List<BimtekFoto> uploadedFotos = new ArrayList<>();

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "image");
fileValidationUtil.validateFileSize(file);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

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

    /**
     * Menampilkan foto Bimtek berdasarkan ID bimtek dan ID foto
     * 
     * @param bimtekId ID dari Bimtek
     * @param fotoId   ID dari foto
     * @return byte array berisi data foto
     */
    @Transactional(readOnly = true)
    public byte[] viewFoto(UUID bimtekId, UUID fotoId) {
        // Ambil data foto dari database
        BimtekFoto foto = bimtekFotoRepository.findById(fotoId)
                .orElseThrow(() -> new EntityNotFoundException("Foto tidak ditemukan dengan id: " + fotoId));

        // Validasi foto tersebut memang milik bimtek yang dimaksud
        if (!foto.getBimtek().getId().equals(bimtekId)) {
            throw new EntityNotFoundException("Foto tidak terkait dengan Bimtek yang dimaksud");
        }

        try {
            // Ambil data file dari penyimpanan (Minio atau sistem file)
            return minioBimtekService.getFileData(foto.getNamaFile());
        } catch (Exception e) {
            log.error("Gagal mengambil data foto: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal mengambil data foto: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public BimtekFoto getFotoById(UUID fotoId) {
        return bimtekFotoRepository.findById(fotoId)
                .orElseThrow(() -> new EntityNotFoundException("Foto tidak ditemukan dengan id: " + fotoId));
    }

    @Transactional
    public List<BimtekVideo> uploadVideos(UUID bimtekId, List<MultipartFile> files) throws Exception {
        Bimtek bimtek = bimtekRepository.findById(bimtekId)
                .orElseThrow(() -> new EntityNotFoundException("Bimtek tidak ditemukan dengan id: " + bimtekId));

        List<BimtekVideo> uploadedVideos = new ArrayList<>();

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "video");
            fileValidationUtil.validateFileSize(file);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioBimtekService.uploadFile(fileName, file.getInputStream(), file.getContentType());

            BimtekVideo bimtekVideo = new BimtekVideo();
            bimtekVideo.setBimtek(bimtek);
            bimtekVideo.setNamaFile(fileName);
            bimtekVideo.setNamaAsli(file.getOriginalFilename());
            bimtekVideo.setUkuranMb((double) file.getSize() / (1024 * 1024));
            bimtekVideo.setContentType(file.getContentType());
            bimtekVideo.setUploadedAt(LocalDateTime.now());

            uploadedVideos.add(bimtekVideoRepository.save(bimtekVideo));
        }

        return uploadedVideos;
    }

    @Transactional
    public Bimtek deleteVideos(UUID bimtekId, List<UUID> videoIds) throws Exception {
        Bimtek bimtek = bimtekRepository.findById(bimtekId)
                .orElseThrow(() -> new EntityNotFoundException("Bimtek tidak ditemukan dengan id: " + bimtekId));

        List<BimtekVideo> videosToDelete = bimtekVideoRepository.findAllById(videoIds);

        for (BimtekVideo video : videosToDelete) {
            if (video.getBimtek().getId().equals(bimtekId)) {
                minioBimtekService.deleteFile(video.getNamaFile());
                bimtekVideoRepository.delete(video);
            }
        }

        return bimtekRepository.save(bimtek);
    }

    @Transactional(readOnly = true)
    public BimtekVideo getVideoById(UUID videoId) {
        return bimtekVideoRepository.findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Video tidak ditemukan dengan id: " + videoId));
    }

    public InputStream getVideoStream(UUID bimtekId, UUID videoId) throws Exception {
        // Ambil data video dari database
        BimtekVideo video = bimtekVideoRepository.findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Video tidak ditemukan dengan id: " + videoId));

        // Validasi video tersebut memang milik bimtek yang dimaksud
        if (!video.getBimtek().getId().equals(bimtekId)) {
            throw new EntityNotFoundException("Video tidak terkait dengan Bimtek yang dimaksud");
        }

        // Return the video stream instead of loading all bytes at once
        return minioBimtekService.getFileStream(video.getNamaFile());
    }

    @Transactional
    public List<BimtekPdf> uploadPdfs(UUID bimtekId, List<MultipartFile> files) throws Exception {
        Bimtek bimtek = bimtekRepository.findById(bimtekId)
                .orElseThrow(() -> new EntityNotFoundException("Bimtek tidak ditemukan dengan id: " + bimtekId));

        List<BimtekPdf> uploadedPdfs = new ArrayList<>();

        for (MultipartFile file : files) {
            fileValidationUtil.validateFileType(file, "pdf");
            fileValidationUtil.validateFileSize(file);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioBimtekService.uploadFile(fileName, file.getInputStream(), file.getContentType());

            BimtekPdf bimtekPdf = new BimtekPdf();
            bimtekPdf.setBimtek(bimtek);
            bimtekPdf.setNamaFile(fileName);
            bimtekPdf.setNamaAsli(file.getOriginalFilename());
            bimtekPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            bimtekPdf.setContentType(file.getContentType());
            bimtekPdf.setUploadedAt(LocalDateTime.now());

            uploadedPdfs.add(bimtekPdfRepository.save(bimtekPdf));
        }

        return uploadedPdfs;
    }

    @Transactional
    public Bimtek deletePdfs(UUID bimtekId, List<UUID> pdfIds) throws Exception {
        Bimtek bimtek = bimtekRepository.findById(bimtekId)
                .orElseThrow(() -> new EntityNotFoundException("Bimtek tidak ditemukan dengan id: " + bimtekId));

        List<BimtekPdf> pdfsToDelete = bimtekPdfRepository.findAllById(pdfIds);

        for (BimtekPdf pdf : pdfsToDelete) {
            if (pdf.getBimtek().getId().equals(bimtekId)) {
                minioBimtekService.deleteFile(pdf.getNamaFile());
                bimtekPdfRepository.delete(pdf);
            }
        }

        return bimtekRepository.save(bimtek);
    }

    /**
     * Mendapatkan PDF Bimtek berdasarkan ID PDF
     * 
     * @param pdfId ID dari PDF
     * @return BimtekPdf objek yang dicari
     * @throws EntityNotFoundException jika PDF tidak ditemukan
     */
    @Transactional(readOnly = true)
    public BimtekPdf getPdfById(UUID pdfId) {
        return bimtekPdfRepository.findById(pdfId)
                .orElseThrow(() -> new EntityNotFoundException("PDF tidak ditemukan dengan id: " + pdfId));
    }

    /**
     * Menampilkan PDF Bimtek berdasarkan ID bimtek dan ID pdf
     * 
     * @param bimtekId ID dari Bimtek
     * @param pdfId    ID dari PDF
     * @return byte array berisi data PDF
     * @throws EntityNotFoundException jika PDF tidak ditemukan atau tidak terkait
     *                                 dengan Bimtek
     */
    @Transactional(readOnly = true)
    public byte[] viewPdf(UUID bimtekId, UUID pdfId) {
        // Ambil data PDF dari database
        BimtekPdf pdf = bimtekPdfRepository.findById(pdfId)
                .orElseThrow(() -> new EntityNotFoundException("PDF tidak ditemukan dengan id: " + pdfId));

        // Validasi PDF tersebut memang milik bimtek yang dimaksud
        if (!pdf.getBimtek().getId().equals(bimtekId)) {
            throw new EntityNotFoundException("PDF tidak terkait dengan Bimtek yang dimaksud");
        }

        try {
            // Ambil data file dari penyimpanan (Minio atau sistem file)
            return minioBimtekService.getFileData(pdf.getNamaFile());
        } catch (Exception e) {
            log.error("Gagal mengambil data PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal mengambil data PDF: " + e.getMessage(), e);
        }
    }

    public Page<Bimtek> findByFilters(String namaBimtek, String subjek, List<String> bpdas, Pageable pageable) {
        Specification<Bimtek> spec = Specification.where(null);
        
        if (namaBimtek != null && !namaBimtek.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("namaBimtek")),
                        "%" + namaBimtek.toLowerCase() + "%"
                    ));
        }
        
        if (subjek != null && !subjek.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("subjek")),
                        "%" + subjek.toLowerCase() + "%"
                    ));
        }
        
        if (bpdas != null && !bpdas.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    root.get("bpdas").in(bpdas));
        }
        
        return bimtekRepository.findAll(spec, pageable);
    }

}
