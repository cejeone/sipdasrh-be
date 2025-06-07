package com.kehutanan.ppth.mataair.kegiatan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.ppth.mataair.kegiatan.dto.KegiatanDeleteFilesRequest;
import com.kehutanan.ppth.mataair.kegiatan.dto.KegiatanPageDTO;
import com.kehutanan.ppth.mataair.kegiatan.model.Kegiatan;
import com.kehutanan.ppth.mataair.kegiatan.model.dto.KegiatanDTO;
import com.kehutanan.ppth.mataair.kegiatan.service.KegiatanService;
import com.kehutanan.ppth.master.model.Eselon3;
import com.kehutanan.ppth.master.model.Lov;
import com.kehutanan.ppth.master.service.Eselon3Service;
import com.kehutanan.ppth.master.service.LovService;
import com.kehutanan.ppth.mataair.program.model.Program;
import com.kehutanan.ppth.mataair.program.service.ProgramService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController("mataAirKegiatanController")
@RequestMapping("/api/kegiatan")
public class KegiatanController {

    private final KegiatanService service;
    private final ProgramService programService;
    private final Eselon3Service eselon3Service;
    private final LovService lovService;
    private final PagedResourcesAssembler<Kegiatan> pagedResourcesAssembler;

    @Autowired
    public KegiatanController(
            KegiatanService service,
            ProgramService programService,
            Eselon3Service eselon3Service,
            LovService lovService,
            PagedResourcesAssembler<Kegiatan> pagedResourcesAssembler) {
        this.service = service;
        this.programService = programService;
        this.eselon3Service = eselon3Service;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<KegiatanPageDTO> getAllKegiatan(
            @RequestParam(required = false) String namaProgram,
            @RequestParam(required = false) String jenisKegiatan,
            @RequestParam(required = false) List<String> jenis,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanPageDTO kegiatanPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((namaProgram != null && !namaProgram.isEmpty()) ||
                (jenisKegiatan != null && !jenisKegiatan.isEmpty()) ||
                (jenis != null && !jenis.isEmpty())) {
            kegiatanPage = service.findByFiltersWithCache(namaProgram, jenisKegiatan, jenis, pageable, baseUrl);
        } else {
            kegiatanPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(kegiatanPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Kegiatan by keyword")
    public ResponseEntity<KegiatanPageDTO> searchKegiatan(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanPageDTO kegiatanPage = service.searchWithCache(keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(kegiatanPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KegiatanDTO> getKegiatanById(@PathVariable Long id) {
        try {
            KegiatanDTO kegiatanDTO = service.findDTOById(id);
            return ResponseEntity.ok(kegiatanDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Kegiatan> createKegiatan(
            @RequestPart String namaKegiatan,
            @RequestPart(required = false) String pemangkuKawasan,
            @RequestPart(required = false) String tahunKegiatan,
            @RequestPart(required = false) String totalBibit,
            @RequestPart(required = false) String totalLuas,
            @RequestPart(required = false) String nomor,
            @RequestPart(required = false) String nilai,
            @RequestPart(required = false) String tanggalKontrak,
            @RequestPart(required = false) String dokumentasiCatatanFoto,
            @RequestPart(required = false) String dokumentasiCatatanVideo,
            @RequestPart(required = false) String programId,
            @RequestPart(required = false) String jenisKegiatanId,
            @RequestPart(required = false) String referensiP0Id,
            @RequestPart(required = false) String polaId,
            @RequestPart(required = false) String sumberAnggaranId,
            @RequestPart(required = false) String pelaksanaId,
            @RequestPart(required = false) String tipeId,
            @RequestPart(required = false) String penerimaManfaatId,
            @RequestPart(required = false) String statusId) {

        try {
            Kegiatan newKegiatan = new Kegiatan();
            newKegiatan.setNamaKegiatan(namaKegiatan);

            if (tahunKegiatan != null && !tahunKegiatan.isEmpty()) {
                newKegiatan.setTahunKegiatan(Integer.parseInt(tahunKegiatan));
            }

            if (totalBibit != null && !totalBibit.isEmpty()) {
                newKegiatan.setTotalBibit(Integer.parseInt(totalBibit));
            }

            if (totalLuas != null && !totalLuas.isEmpty()) {
                newKegiatan.setTotalLuas(Double.parseDouble(totalLuas));
            }

            newKegiatan.setNomor(nomor);

            if (nilai != null && !nilai.isEmpty()) {
                newKegiatan.setNilai(Double.parseDouble(nilai));
            }

            newKegiatan.setTanggalKontrak(tanggalKontrak);
            newKegiatan.setDokumentasiCatatanFoto(dokumentasiCatatanFoto);
            newKegiatan.setDokumentasiCatatanVideo(dokumentasiCatatanVideo);

            if (programId != null && !programId.isEmpty()) {
                Long programIdLong = Long.parseLong(programId);
                Program program = programService.findById(programIdLong);
                newKegiatan.setProgram(program);
            }

            // Set Lov-type entities if IDs are provided
            if (jenisKegiatanId != null && !jenisKegiatanId.isEmpty()) {
                Long jenisKegiatanIdLong = Long.parseLong(jenisKegiatanId);
                Lov jenisKegiatan = lovService.findById(jenisKegiatanIdLong);
                newKegiatan.setJenisKegiatanId(jenisKegiatan);
            }

            if (referensiP0Id != null && !referensiP0Id.isEmpty()) {
                Long refP0IdLong = Long.parseLong(referensiP0Id);
                Lov refP0 = lovService.findById(refP0IdLong);
                newKegiatan.setReferensiP0Id(refP0);
            }

            if (sumberAnggaranId != null && !sumberAnggaranId.isEmpty()) {
                Long sumberAnggaranIdLong = Long.parseLong(sumberAnggaranId);
                Lov sumberAnggaran = lovService.findById(sumberAnggaranIdLong);
                newKegiatan.setSumberAnggaranId(sumberAnggaran);
            }

            if (pelaksanaId != null && !pelaksanaId.isEmpty()) {
                Long pelaksanaIdLong = Long.parseLong(pelaksanaId);
                Lov pelaksana = lovService.findById(pelaksanaIdLong);
                newKegiatan.setPelaksanaId(pelaksana);
            }

            if (tipeId != null && !tipeId.isEmpty()) {
                Long tipeIdLong = Long.parseLong(tipeId);
                Lov tipe = lovService.findById(tipeIdLong);
                newKegiatan.setTipeId(tipe);
            }

            if (penerimaManfaatId != null && !penerimaManfaatId.isEmpty()) {
                Long penerimaManfaatIdLong = Long.parseLong(penerimaManfaatId);
                Lov penerimaManfaat = lovService.findById(penerimaManfaatIdLong);
                newKegiatan.setPenerimaManfaatId(penerimaManfaat);
            }

            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newKegiatan.setStatusId(status);
            }

            Kegiatan savedKegiatan = service.save(newKegiatan);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKegiatan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Add detailed logging for troubleshooting
            e.printStackTrace(); // Log stack trace to console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Kegiatan> updateKegiatan(
            @PathVariable Long id,
            @RequestPart String namaKegiatan,
            @RequestPart(required = false) String pemangkuKawasan,
            @RequestPart(required = false) String tahunKegiatan,
            @RequestPart(required = false) String totalBibit,
            @RequestPart(required = false) String totalLuas,
            @RequestPart(required = false) String nomor,
            @RequestPart(required = false) String nilai,
            @RequestPart(required = false) String tanggalKontrak,
            @RequestPart(required = false) String dokumentasiCatatanFoto,
            @RequestPart(required = false) String dokumentasiCatatanVideo,
            @RequestPart(required = false) String programId,
            @RequestPart(required = false) String jenisKegiatanId,
            @RequestPart(required = false) String referensiP0Id,
            @RequestPart(required = false) String polaId,
            @RequestPart(required = false) String sumberAnggaranId,
            @RequestPart(required = false) String pelaksanaId,
            @RequestPart(required = false) String tipeId,
            @RequestPart(required = false) String penerimaManfaatId,
            @RequestPart(required = false) String statusId) {

        try {
            Kegiatan existingKegiatan = service.findById(id);

            existingKegiatan.setNamaKegiatan(namaKegiatan);

            if (tahunKegiatan != null && !tahunKegiatan.isEmpty()) {
                existingKegiatan.setTahunKegiatan(Integer.parseInt(tahunKegiatan));
            }

            if (totalBibit != null && !totalBibit.isEmpty()) {
                existingKegiatan.setTotalBibit(Integer.parseInt(totalBibit));
            }

            if (totalLuas != null && !totalLuas.isEmpty()) {
                existingKegiatan.setTotalLuas(Double.parseDouble(totalLuas));
            }

            existingKegiatan.setNomor(nomor);

            if (nilai != null && !nilai.isEmpty()) {
                existingKegiatan.setNilai(Double.parseDouble(nilai));
            }

            existingKegiatan.setTanggalKontrak(tanggalKontrak);
            existingKegiatan.setDokumentasiCatatanFoto(dokumentasiCatatanFoto);
            existingKegiatan.setDokumentasiCatatanVideo(dokumentasiCatatanVideo);

            if (programId != null && !programId.isEmpty()) {
                Long programIdLong = Long.parseLong(programId);
                Program program = programService.findById(programIdLong);
                existingKegiatan.setProgram(program);
            }

            // Update Lov-type entities if IDs are provided
            if (jenisKegiatanId != null && !jenisKegiatanId.isEmpty()) {
                Long jenisKegiatanIdLong = Long.parseLong(jenisKegiatanId);
                Lov jenisKegiatan = lovService.findById(jenisKegiatanIdLong);
                existingKegiatan.setJenisKegiatanId(jenisKegiatan);
            }

            if (referensiP0Id != null && !referensiP0Id.isEmpty()) {
                Long refP0IdLong = Long.parseLong(referensiP0Id);
                Lov refP0 = lovService.findById(refP0IdLong);
                existingKegiatan.setReferensiP0Id(refP0);
            }

 

            if (sumberAnggaranId != null && !sumberAnggaranId.isEmpty()) {
                Long sumberAnggaranIdLong = Long.parseLong(sumberAnggaranId);
                Lov sumberAnggaran = lovService.findById(sumberAnggaranIdLong);
                existingKegiatan.setSumberAnggaranId(sumberAnggaran);
            }

            if (pelaksanaId != null && !pelaksanaId.isEmpty()) {
                Long pelaksanaIdLong = Long.parseLong(pelaksanaId);
                Lov pelaksana = lovService.findById(pelaksanaIdLong);
                existingKegiatan.setPelaksanaId(pelaksana);
            }

            if (tipeId != null && !tipeId.isEmpty()) {
                Long tipeIdLong = Long.parseLong(tipeId);
                Lov tipe = lovService.findById(tipeIdLong);
                existingKegiatan.setTipeId(tipe);
            }

            if (penerimaManfaatId != null && !penerimaManfaatId.isEmpty()) {
                Long penerimaManfaatIdLong = Long.parseLong(penerimaManfaatId);
                Lov penerimaManfaat = lovService.findById(penerimaManfaatIdLong);
                existingKegiatan.setPenerimaManfaatId(penerimaManfaat);
            }

            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingKegiatan.setStatusId(status);
            }

            Kegiatan updatedKegiatan = service.update(id, existingKegiatan);
            return ResponseEntity.ok(updatedKegiatan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKegiatan(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/{id}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple files for Kegiatan")
    public ResponseEntity<?> uploadFiles(
            @PathVariable Long id,
            @RequestPart(value = "rancanganTeknisPdfs", required = false) List<MultipartFile> rancanganTeknisPdfs,
            @RequestPart(value = "rancanganTeknisFotos", required = false) List<MultipartFile> rancanganTeknisFotos,
            @RequestPart(value = "rancanganTeknisVideos", required = false) List<MultipartFile> rancanganTeknisVideos,
            @RequestPart(value = "kontrakPdfs", required = false) List<MultipartFile> kontrakPdfs,
            @RequestPart(value = "dokumentasiFotos", required = false) List<MultipartFile> dokumentasiFotos,
            @RequestPart(value = "dokumentasiVideos", required = false) List<MultipartFile> dokumentasiVideos,
            @RequestPart(value = "lokusShps", required = false) List<MultipartFile> lokusShps // Tambahan baru
    ) {
        try {
            if (rancanganTeknisPdfs != null) {
                service.uploadRancanganTeknisPdf(id, rancanganTeknisPdfs);
            }
            if (rancanganTeknisFotos != null) {
                service.uploadRancanganTeknisFoto(id, rancanganTeknisFotos);
            }
            if (rancanganTeknisVideos != null) {
                service.uploadRancanganTeknisVideo(id, rancanganTeknisVideos);
            }
            if (kontrakPdfs != null) {
                service.uploadKontrakPdf(id, kontrakPdfs);
            }
            if (dokumentasiFotos != null) {
                service.uploadDokumentasiFoto(id, dokumentasiFotos);
            }
            if (dokumentasiVideos != null) {
                service.uploadDokumentasiVideo(id, dokumentasiVideos);
            }
            if (lokusShps != null) { // Tambahan baru
                service.uploadLokusShp(id, lokusShps);
            }

            Kegiatan kegiatan = service.findById(id);
            return ResponseEntity.ok(kegiatan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload file: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/files")
    @Operation(summary = "Delete multiple files for Kegiatan")
    public ResponseEntity<?> deleteFiles(
            @PathVariable Long id,
            @RequestBody(required = false) KegiatanDeleteFilesRequest filesRequest) {
        try {
            // Handle each file type list if provided
            if (filesRequest.getRancanganTeknisPdfIds() != null && !filesRequest.getRancanganTeknisPdfIds().isEmpty()) {
                service.deleteRancanganTeknisPdf(id, filesRequest.getRancanganTeknisPdfIds());
            }

            if (filesRequest.getRancanganTeknisFotoIds() != null && !filesRequest.getRancanganTeknisFotoIds().isEmpty()) {
                service.deleteRancanganTeknisFoto(id, filesRequest.getRancanganTeknisFotoIds());
            }

            if (filesRequest.getRancanganTeknisVideoIds() != null && !filesRequest.getRancanganTeknisVideoIds().isEmpty()) {
                service.deleteRancanganTeknisVideo(id, filesRequest.getRancanganTeknisVideoIds());
            }

            if (filesRequest.getKontrakPdfIds() != null && !filesRequest.getKontrakPdfIds().isEmpty()) {
                service.deleteKontrakPdf(id, filesRequest.getKontrakPdfIds());
            }

            if (filesRequest.getDokumentasiFotoIds() != null && !filesRequest.getDokumentasiFotoIds().isEmpty()) {
                service.deleteDokumentasiFoto(id, filesRequest.getDokumentasiFotoIds());
            }

            if (filesRequest.getDokumentasiVideoIds() != null && !filesRequest.getDokumentasiVideoIds().isEmpty()) {
                service.deleteDokumentasiVideo(id, filesRequest.getDokumentasiVideoIds());
            }

            if (filesRequest.getLokusShpIds() != null && !filesRequest.getLokusShpIds().isEmpty()) {
                service.deleteLokusShp(id, filesRequest.getLokusShpIds());
            }

            // Fetch and return the updated Kegiatan entity
            Kegiatan kegiatan = service.findById(id);
            return ResponseEntity.ok(kegiatan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal menghapus file: " + e.getMessage());
        }
    }
}