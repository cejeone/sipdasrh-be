package com.kehutanan.rh.kegiatan.service;

import com.kehutanan.rh.dokumen.model.DokumenFile;
import com.kehutanan.rh.kegiatan.dto.KegiatanMonevDto;
import com.kehutanan.rh.kegiatan.model.KegiatanMonev;
import com.kehutanan.rh.kegiatan.model.KegiatanMonevPdf;
import com.kehutanan.rh.kegiatan.repository.KegiatanMonevPdfRepository;
import com.kehutanan.rh.kegiatan.repository.KegiatanMonevRepository;
import com.kehutanan.rh.kegiatan.repository.KegiatanRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class KegiatanMonevService {

    private final KegiatanMonevRepository kegiatanMonevRepository;
    private final KegiatanRepository kegiatanRepository;
    private final MinioKegiatanService minioKegiatanService;
    private final KegiatanMonevPdfRepository kegiatanMonevPdfRepository;

    public KegiatanMonevService(KegiatanMonevRepository kegiatanMonevRepository, KegiatanRepository kegiatanRepository,
            MinioKegiatanService minioKegiatanService, KegiatanMonevPdfRepository kegiatanMonevPdfRepository) {
        this.kegiatanMonevPdfRepository = kegiatanMonevPdfRepository;
        this.kegiatanMonevRepository = kegiatanMonevRepository;
        this.kegiatanRepository = kegiatanRepository;
        this.minioKegiatanService = minioKegiatanService;
    }

    /**
     * Find all kegiatan monev with optional search filtering
     * 
     * @param search    search term for filtering
     * @param pageable  pagination information
     * @param assembler resource assembler for HATEOAS
     * @return PagedModel with kegiatan monev data
     */
    public PagedModel<EntityModel<KegiatanMonev>> findAll(
            String search,
            Pageable pageable,
            PagedResourcesAssembler<KegiatanMonev> assembler) {

        Page<KegiatanMonev> page;

        if (search != null && !search.isEmpty()) {
            // Create specification for searching in nomor and deskripsi fields
            Specification<KegiatanMonev> spec = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                String searchPattern = "%" + search.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("deskripsi")), searchPattern)));

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };

            page = kegiatanMonevRepository.findAll(spec, pageable);
        } else {
            page = kegiatanMonevRepository.findAll(pageable);
        }

        return assembler.toModel(page);
    }

    /**
     * Find a kegiatan monev by ID
     * 
     * @param id the ID of the kegiatan monev
     * @return the found kegiatan monev
     * @throws EntityNotFoundException if kegiatan monev is not found
     */
    public KegiatanMonev findById(UUID id) {
        return kegiatanMonevRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KegiatanMonev not found with ID: " + id));
    }

    /**
     * Create a new kegiatan monev
     * 
     * @param kegiatanMonev the kegiatan monev to create
     * @return the created kegiatan monev
     */
    public KegiatanMonev create(KegiatanMonev kegiatanMonev) {
        return kegiatanMonevRepository.save(kegiatanMonev);
    }

    /**
     * Update an existing kegiatan monev
     * 
     * @param id               the ID of the kegiatan monev to update
     * @param kegiatanMonevDto the updated kegiatan monev data
     * @return the updated kegiatan monev
     * @throws EntityNotFoundException if kegiatan monev is not found
     */
    public KegiatanMonev update(UUID id, KegiatanMonevDto kegiatanMonevDto) {
        KegiatanMonev existing = findById(id);

        // Update fields from the DTO
        existing.setNomor(kegiatanMonevDto.getNomor());
        existing.setTanggal(kegiatanMonevDto.getTanggal());
        existing.setDeskripsi(kegiatanMonevDto.getDeskripsi());
        existing.setStatus(kegiatanMonevDto.getStatus());

        return kegiatanMonevRepository.save(existing);
    }

    /**
     * Delete a kegiatan monev
     * 
     * @param id the ID of the kegiatan monev to delete
     * @throws EntityNotFoundException if kegiatan monev is not found
     */
    public void delete(UUID id) {
        if (!kegiatanMonevRepository.existsById(id)) {
            throw new EntityNotFoundException("KegiatanMonev not found with ID: " + id);
        }

        kegiatanMonevRepository.deleteById(id);
    }

    public KegiatanMonev create(List<MultipartFile> files, String nomor, LocalDate tanggal, String deskripsi,
            String status, UUID kegiatanId) throws IOException, Exception {

        KegiatanMonev kegiatanMonev = new KegiatanMonev();
        kegiatanMonev.setNomor(nomor);
        kegiatanMonev.setTanggal(tanggal);
        kegiatanMonev.setDeskripsi(deskripsi);
        kegiatanMonev.setStatus(status);
        kegiatanMonev.setKegiatan(kegiatanRepository.findById(kegiatanId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Kegiatan not found with ID: " + kegiatanId)));

        // Handle file upload logic here
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

                minioKegiatanService.uploadFile(fileName, file.getInputStream(), file.getContentType());

                KegiatanMonevPdf dokumenFile = new KegiatanMonevPdf();
                dokumenFile.setKegiatanMonev(kegiatanMonev);
                dokumenFile.setNamaFile(fileName);
                dokumenFile.setNamaAsli(file.getOriginalFilename());
                dokumenFile.setUkuranMb((double) file.getSize() / (1024 * 1024));
                dokumenFile.setContentType(file.getContentType());
                dokumenFile.setUploadedAt(LocalDateTime.now());

                // Add the file to the kegiatan monev
                kegiatanMonev.getKegiatanMonevPdfs().add(dokumenFile);
            }
        }

        // Save the kegiatanMonev object to the database
        return kegiatanMonevRepository.save(kegiatanMonev);
    }

    @Transactional
    public List<KegiatanMonevPdf> uploadPdfs(UUID kegiatanMonevId, List<MultipartFile> files) throws Exception {
        KegiatanMonev kegiatanMonev = kegiatanMonevRepository.findById(kegiatanMonevId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Kegiatan Monev tidak ditemukan dengan id: " + kegiatanMonevId));

        List<KegiatanMonevPdf> uploadedPdfs = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioKegiatanService.uploadFile(fileName, file.getInputStream(), file.getContentType());

            KegiatanMonevPdf kegiatanMonevPdf = new KegiatanMonevPdf();
            kegiatanMonevPdf.setKegiatanMonev(kegiatanMonev);
            kegiatanMonevPdf.setNamaFile(fileName);
            kegiatanMonevPdf.setNamaAsli(file.getOriginalFilename());
            kegiatanMonevPdf.setUkuranMb((double) file.getSize() / (1024 * 1024));
            kegiatanMonevPdf.setContentType(file.getContentType());
            kegiatanMonevPdf.setUploadedAt(LocalDateTime.now());

            uploadedPdfs.add(kegiatanMonevPdfRepository.save(kegiatanMonevPdf));
        }

        return uploadedPdfs;
    }

    @Transactional
    public KegiatanMonev deletePdfs(UUID kegiatanMonevId, List<UUID> pdfIds) throws Exception {
        KegiatanMonev kegiatanMonev = kegiatanMonevRepository.findById(kegiatanMonevId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Kegiatan Monev tidak ditemukan dengan id: " + kegiatanMonevId));

        List<KegiatanMonevPdf> pdfsToDelete = kegiatanMonevPdfRepository.findAllById(pdfIds);

        for (KegiatanMonevPdf pdf : pdfsToDelete) {
            if (pdf.getKegiatanMonev().getId().equals(kegiatanMonevId)) {
                minioKegiatanService.deleteFile(pdf.getNamaFile());
                kegiatanMonevPdfRepository.delete(pdf);
            }
        }

        return kegiatanMonevRepository.save(kegiatanMonev);
    }

    /**
     * Menampilkan PDF Kegiatan Monev berdasarkan ID kegiatan monev dan ID pdf
     * 
     * @param kegiatanMonevId ID dari Kegiatan Monev
     * @param pdfId           ID dari PDF
     * @return byte array berisi data PDF
     * @throws EntityNotFoundException jika PDF tidak ditemukan atau tidak terkait
     *                                 dengan Kegiatan Monev
     */
    @Transactional(readOnly = true)
    public byte[] viewPdf(UUID kegiatanMonevId, UUID pdfId) {
        // Ambil data PDF dari database
        KegiatanMonevPdf pdf = kegiatanMonevPdfRepository.findById(pdfId)
                .orElseThrow(() -> new EntityNotFoundException("PDF tidak ditemukan dengan id: " + pdfId));

        // Validasi PDF tersebut memang milik kegiatan monev yang dimaksud
        if (!pdf.getKegiatanMonev().getId().equals(kegiatanMonevId)) {
            throw new EntityNotFoundException("PDF tidak terkait dengan Kegiatan Monev yang dimaksud");
        }

        try {
            // Ambil data file dari penyimpanan (Minio atau sistem file)
            return minioKegiatanService.getFileData(pdf.getNamaFile());
        } catch (Exception e) {
            log.error("Gagal mengambil data PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal mengambil data PDF: " + e.getMessage(), e);
        }
    }

    /**
     * Mendapatkan metadata PDF berdasarkan ID
     * 
     * @param id ID dari PDF yang dicari
     * @return metadata PDF
     * @throws EntityNotFoundException jika PDF tidak ditemukan
     */
    public KegiatanMonevPdf getPdfById(UUID id) {
        return kegiatanMonevPdfRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PDF tidak ditemukan dengan id: " + id));
    }

}