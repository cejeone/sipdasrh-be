package com.kehutanan.tktrh.bkta.kegiatan.controller;

import java.util.Date;
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

import com.kehutanan.tktrh.bkta.kegiatan.dto.KegiatanPageDTO;
import com.kehutanan.tktrh.bkta.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.bkta.kegiatan.model.dto.KegiatanDTO;
import com.kehutanan.tktrh.bkta.kegiatan.service.KegiatanService;
import com.kehutanan.tktrh.bkta.program.model.Program;
import com.kehutanan.tktrh.bkta.program.service.ProgramService;
import com.kehutanan.tktrh.master.model.Eselon3;
import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.service.Eselon3Service;
import com.kehutanan.tktrh.master.service.LovService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/kegiatan")
public class KegiatanController {

    private final KegiatanService kegiatanService;
    private final ProgramService programService;
    private final Eselon3Service eselon3Service;
    private final LovService lovService;
    private final PagedResourcesAssembler<Kegiatan> pagedResourcesAssembler;

    @Autowired
    public KegiatanController(
            KegiatanService kegiatanService,
            ProgramService programService,
            Eselon3Service eselon3Service,
            LovService lovService,
            PagedResourcesAssembler<Kegiatan> pagedResourcesAssembler) {
        this.kegiatanService = kegiatanService;
        this.programService = programService;
        this.eselon3Service = eselon3Service;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<KegiatanPageDTO> getAllKegiatan(
            @RequestParam(required = false) String namaProgram,
            @RequestParam(required = false) String namaKegiatan,
            @RequestParam(required = false) List<String> programList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        String baseUrl = request.getRequestURL().toString();
        KegiatanPageDTO kegiatanPage;

        // Check if any filter is provided
        if ((namaKegiatan != null && !namaKegiatan.isEmpty()) ||
                (namaProgram != null) ||
                (programList != null)) {
            kegiatanPage = kegiatanService.findByFiltersWithCache(namaProgram, namaKegiatan, programList, pageable, baseUrl);
        } else {
            kegiatanPage = kegiatanService.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(kegiatanPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search kegiatan by keyword")
    public ResponseEntity<KegiatanPageDTO> searchKegiatan(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        KegiatanPageDTO kegiatanPage = kegiatanService.searchWithCache(keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(kegiatanPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KegiatanDTO> getKegiatanById(@PathVariable Long id) {
        try {
            KegiatanDTO kegiatanDTO = kegiatanService.findDTOById(id);
            return ResponseEntity.ok(kegiatanDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Kegiatan> createKegiatan(
            @RequestPart(required = false) String subDirektoratId,
            @RequestPart(required = false) String programId,
            @RequestPart(required = false) String namaKegiatan,
            @RequestPart(required = false) String sumberAnggaranId,
            @RequestPart(required = false) String totalAnggaran,
            @RequestPart(required = false) String fungsiKawasanHk,
            @RequestPart(required = false) String fungsiKawasanHl,
            @RequestPart(required = false) String fungsiKawasanApl,
            @RequestPart(required = false) String fungsiKawasanHpt,
            @RequestPart(required = false) String skemaId,
            @RequestPart(required = false) String tahunKegiatan,
            @RequestPart(required = false) String totalDpn,
            @RequestPart(required = false) String totalGullyPlug,
            @RequestPart(required = false) String pemangkuKawasan,
            @RequestPart(required = false) String nomor,
            @RequestPart(required = false) String nilai,
            @RequestPart(required = false) String tanggalKontrak,
            @RequestPart(required = false) String tanggalBerakhirKontrak,
            @RequestPart(required = false) String pelaksanaId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String catatanDokumentasiFoto) {
        
        try {
            Kegiatan kegiatan = new Kegiatan();
            
            // Set basic properties
            kegiatan.setNamaKegiatan(namaKegiatan);
            
            // Set relationships
            if (subDirektoratId != null && !subDirektoratId.isEmpty()) {
                Eselon3 subDirektorat = eselon3Service.findById(Long.parseLong(subDirektoratId));
                kegiatan.setSubDirektorat(subDirektorat);
            }
            
            if (programId != null && !programId.isEmpty()) {
                Program program = programService.findById(Long.parseLong(programId));
                kegiatan.setProgram(program);
            }
            
            if (sumberAnggaranId != null && !sumberAnggaranId.isEmpty()) {
                Lov sumberAnggaran = lovService.findById(Long.parseLong(sumberAnggaranId));
                kegiatan.setSumberAnggaran(sumberAnggaran);
            }
            
            if (totalAnggaran != null && !totalAnggaran.isEmpty()) {
                kegiatan.setTotalAnggaran(Double.parseDouble(totalAnggaran));
            }
            
            if (fungsiKawasanHk != null) {
                kegiatan.setFungsiKawasanHk(Boolean.parseBoolean(fungsiKawasanHk));
            }
            
            if (fungsiKawasanHl != null) {
                kegiatan.setFungsiKawasanHl(Boolean.parseBoolean(fungsiKawasanHl));
            }
            
            if (fungsiKawasanApl != null) {
                kegiatan.setFungsiKawasanApl(Boolean.parseBoolean(fungsiKawasanApl));
            }
            
            if (fungsiKawasanHpt != null) {
                kegiatan.setFungsiKawasanHpt(Boolean.parseBoolean(fungsiKawasanHpt));
            }
            
            if (skemaId != null && !skemaId.isEmpty()) {
                Lov skema = lovService.findById(Long.parseLong(skemaId));
                kegiatan.setSkema(skema);
            }
            
            kegiatan.setTahunKegiatan(tahunKegiatan);
            
            if (totalDpn != null && !totalDpn.isEmpty()) {
                kegiatan.setTotalDpn(Double.parseDouble(totalDpn));
            }
            
            if (totalGullyPlug != null && !totalGullyPlug.isEmpty()) {
                kegiatan.setTotalGullyPlug(Double.parseDouble(totalGullyPlug));
            }
            
            kegiatan.setPemangkuKawasan(pemangkuKawasan);
            kegiatan.setNomor(nomor);
            kegiatan.setNilai(nilai);
            
            // Handle dates with proper parsing
            // For simplicity, assuming date strings are in ISO format or need to be converted
            if (tanggalKontrak != null && !tanggalKontrak.isEmpty()) {
                // Add appropriate date parsing based on your expected format
                // This is a placeholder - use proper date parsing logic
                // kegiatan.setTanggalKontrak(new SimpleDateFormat("yyyy-MM-dd").parse(tanggalKontrak));
            }
            
            if (tanggalBerakhirKontrak != null && !tanggalBerakhirKontrak.isEmpty()) {
                // Add appropriate date parsing based on your expected format
                // This is a placeholder - use proper date parsing logic
                // kegiatan.setTanggalBerakhirKontrak(new SimpleDateFormat("yyyy-MM-dd").parse(tanggalBerakhirKontrak));
            }
            
            if (pelaksanaId != null && !pelaksanaId.isEmpty()) {
                Lov pelaksana = lovService.findById(Long.parseLong(pelaksanaId));
                kegiatan.setPelaksana(pelaksana);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Lov status = lovService.findById(Long.parseLong(statusId));
                kegiatan.setStatus(status);
            }
            
            kegiatan.setCatatanDokumentasiFoto(catatanDokumentasiFoto);
            
            Kegiatan savedKegiatan = kegiatanService.save(kegiatan);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKegiatan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Kegiatan> updateKegiatan(
            @PathVariable Long id,
            @RequestPart(required = false) String subDirektoratId,
            @RequestPart(required = false) String programId,
            @RequestPart(required = false) String namaKegiatan,
            @RequestPart(required = false) String sumberAnggaranId,
            @RequestPart(required = false) String totalAnggaran,
            @RequestPart(required = false) String fungsiKawasanHk,
            @RequestPart(required = false) String fungsiKawasanHl,
            @RequestPart(required = false) String fungsiKawasanApl,
            @RequestPart(required = false) String fungsiKawasanHpt,
            @RequestPart(required = false) String skemaId,
            @RequestPart(required = false) String tahunKegiatan,
            @RequestPart(required = false) String totalDpn,
            @RequestPart(required = false) String totalGullyPlug,
            @RequestPart(required = false) String pemangkuKawasan,
            @RequestPart(required = false) String nomor,
            @RequestPart(required = false) String nilai,
            @RequestPart(required = false) String tanggalKontrak,
            @RequestPart(required = false) String tanggalBerakhirKontrak,
            @RequestPart(required = false) String pelaksanaId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String catatanDokumentasiFoto) {
        
        try {
            Kegiatan existingKegiatan = kegiatanService.findById(id);
            
            // Update properties if provided
            if (namaKegiatan != null) {
                existingKegiatan.setNamaKegiatan(namaKegiatan);
            }
            
            // Update relationships
            if (subDirektoratId != null && !subDirektoratId.isEmpty()) {
                Eselon3 subDirektorat = eselon3Service.findById(Long.parseLong(subDirektoratId));
                existingKegiatan.setSubDirektorat(subDirektorat);
            }
            
            if (programId != null && !programId.isEmpty()) {
                Program program = programService.findById(Long.parseLong(programId));
                existingKegiatan.setProgram(program);
            }
            
            if (sumberAnggaranId != null && !sumberAnggaranId.isEmpty()) {
                Lov sumberAnggaran = lovService.findById(Long.parseLong(sumberAnggaranId));
                existingKegiatan.setSumberAnggaran(sumberAnggaran);
            }
            
            if (totalAnggaran != null && !totalAnggaran.isEmpty()) {
                existingKegiatan.setTotalAnggaran(Double.parseDouble(totalAnggaran));
            }
            
            if (fungsiKawasanHk != null) {
                existingKegiatan.setFungsiKawasanHk(Boolean.parseBoolean(fungsiKawasanHk));
            }
            
            if (fungsiKawasanHl != null) {
                existingKegiatan.setFungsiKawasanHl(Boolean.parseBoolean(fungsiKawasanHl));
            }
            
            if (fungsiKawasanApl != null) {
                existingKegiatan.setFungsiKawasanApl(Boolean.parseBoolean(fungsiKawasanApl));
            }
            
            if (fungsiKawasanHpt != null) {
                existingKegiatan.setFungsiKawasanHpt(Boolean.parseBoolean(fungsiKawasanHpt));
            }
            
            if (skemaId != null && !skemaId.isEmpty()) {
                Lov skema = lovService.findById(Long.parseLong(skemaId));
                existingKegiatan.setSkema(skema);
            }
            
            if (tahunKegiatan != null) {
                existingKegiatan.setTahunKegiatan(tahunKegiatan);
            }
            
            if (totalDpn != null && !totalDpn.isEmpty()) {
                existingKegiatan.setTotalDpn(Double.parseDouble(totalDpn));
            }
            
            if (totalGullyPlug != null && !totalGullyPlug.isEmpty()) {
                existingKegiatan.setTotalGullyPlug(Double.parseDouble(totalGullyPlug));
            }
            
            if (pemangkuKawasan != null) {
                existingKegiatan.setPemangkuKawasan(pemangkuKawasan);
            }
            
            if (nomor != null) {
                existingKegiatan.setNomor(nomor);
            }
            
            if (nilai != null) {
                existingKegiatan.setNilai(nilai);
            }
            
            // Handle dates with proper parsing
            if (tanggalKontrak != null && !tanggalKontrak.isEmpty()) {
                // Add appropriate date parsing based on your expected format
                // existingKegiatan.setTanggalKontrak(new SimpleDateFormat("yyyy-MM-dd").parse(tanggalKontrak));
            }
            
            if (tanggalBerakhirKontrak != null && !tanggalBerakhirKontrak.isEmpty()) {
                // Add appropriate date parsing based on your expected format
                // existingKegiatan.setTanggalBerakhirKontrak(new SimpleDateFormat("yyyy-MM-dd").parse(tanggalBerakhirKontrak));
            }
            
            if (pelaksanaId != null && !pelaksanaId.isEmpty()) {
                Lov pelaksana = lovService.findById(Long.parseLong(pelaksanaId));
                existingKegiatan.setPelaksana(pelaksana);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Lov status = lovService.findById(Long.parseLong(statusId));
                existingKegiatan.setStatus(status);
            }
            
            if (catatanDokumentasiFoto != null) {
                existingKegiatan.setCatatanDokumentasiFoto(catatanDokumentasiFoto);
            }
            
            Kegiatan updatedKegiatan = kegiatanService.update(id, existingKegiatan);
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
            kegiatanService.findById(id); // Check if exists
            kegiatanService.deleteById(id);
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
            @RequestPart(value = "rancanganTeknisShps", required = false) List<MultipartFile> rancanganTeknisShps,
            @RequestPart(value = "kontrakPdfs", required = false) List<MultipartFile> kontrakPdfs,
            @RequestPart(value = "dokumentasiFotos", required = false) List<MultipartFile> dokumentasiFotos,
            @RequestPart(value = "dokumentasiVideos", required = false) List<MultipartFile> dokumentasiVideos) {
        
        try {
            if (rancanganTeknisPdfs != null && !rancanganTeknisPdfs.isEmpty()) {
                kegiatanService.uploadRancanganTeknisPdf(id, rancanganTeknisPdfs);
            }
            
            if (rancanganTeknisFotos != null && !rancanganTeknisFotos.isEmpty()) {
                kegiatanService.uploadRancanganTeknisFoto(id, rancanganTeknisFotos);
            }
            
            if (rancanganTeknisShps != null && !rancanganTeknisShps.isEmpty()) {
                kegiatanService.uploadRancanganTeknisShp(id, rancanganTeknisShps);
            }
            
            if (kontrakPdfs != null && !kontrakPdfs.isEmpty()) {
                kegiatanService.uploadKontrakPdf(id, kontrakPdfs);
            }
            
            if (dokumentasiFotos != null && !dokumentasiFotos.isEmpty()) {
                kegiatanService.uploadDokumentasiFoto(id, dokumentasiFotos);
            }
            
            if (dokumentasiVideos != null && !dokumentasiVideos.isEmpty()) {
                kegiatanService.uploadDokumentasiVideo(id, dokumentasiVideos);
            }
            
            Kegiatan kegiatan = kegiatanService.findById(id);
            return ResponseEntity.ok(kegiatan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload file: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/files")
    @Operation(summary = "Delete multiple files for Kegiatan")
    public ResponseEntity<?> deleteFiles(
            @PathVariable Long id,
            @RequestBody DeleteFilesRequest filesRequest) {
        
        try {
            if (filesRequest.getRancanganTeknisPdfIds() != null && !filesRequest.getRancanganTeknisPdfIds().isEmpty()) {
                kegiatanService.deleteRancanganTeknisPdf(id, filesRequest.getRancanganTeknisPdfIds());
            }
            
            if (filesRequest.getRancanganTeknisFotoIds() != null && !filesRequest.getRancanganTeknisFotoIds().isEmpty()) {
                kegiatanService.deleteRancanganTeknisFoto(id, filesRequest.getRancanganTeknisFotoIds());
            }
            
            if (filesRequest.getRancanganTeknisShpIds() != null && !filesRequest.getRancanganTeknisShpIds().isEmpty()) {
                kegiatanService.deleteRancanganTeknisShp(id, filesRequest.getRancanganTeknisShpIds());
            }
            
            if (filesRequest.getKontrakPdfIds() != null && !filesRequest.getKontrakPdfIds().isEmpty()) {
                kegiatanService.deleteKontrakPdf(id, filesRequest.getKontrakPdfIds());
            }
            
            if (filesRequest.getDokumentasiFotoIds() != null && !filesRequest.getDokumentasiFotoIds().isEmpty()) {
                kegiatanService.deleteDokumentasiFoto(id, filesRequest.getDokumentasiFotoIds());
            }
            
            if (filesRequest.getDokumentasiVideoIds() != null && !filesRequest.getDokumentasiVideoIds().isEmpty()) {
                kegiatanService.deleteDokumentasiVideo(id, filesRequest.getDokumentasiVideoIds());
            }
            
            Kegiatan kegiatan = kegiatanService.findById(id);
            return ResponseEntity.ok(kegiatan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal menghapus file: " + e.getMessage());
        }
    }

    // Helper class for file deletion requests
    public static class DeleteFilesRequest {
        private List<String> rancanganTeknisPdfIds;
        private List<String> rancanganTeknisFotoIds;
        private List<String> rancanganTeknisShpIds;
        private List<String> kontrakPdfIds;
        private List<String> dokumentasiFotoIds;
        private List<String> dokumentasiVideoIds;

        public List<String> getRancanganTeknisPdfIds() {
            return rancanganTeknisPdfIds;
        }

        public void setRancanganTeknisPdfIds(List<String> rancanganTeknisPdfIds) {
            this.rancanganTeknisPdfIds = rancanganTeknisPdfIds;
        }

        public List<String> getRancanganTeknisFotoIds() {
            return rancanganTeknisFotoIds;
        }

        public void setRancanganTeknisFotoIds(List<String> rancanganTeknisFotoIds) {
            this.rancanganTeknisFotoIds = rancanganTeknisFotoIds;
        }

        public List<String> getRancanganTeknisShpIds() {
            return rancanganTeknisShpIds;
        }

        public void setRancanganTeknisShpIds(List<String> rancanganTeknisShpIds) {
            this.rancanganTeknisShpIds = rancanganTeknisShpIds;
        }

        public List<String> getKontrakPdfIds() {
            return kontrakPdfIds;
        }

        public void setKontrakPdfIds(List<String> kontrakPdfIds) {
            this.kontrakPdfIds = kontrakPdfIds;
        }

        public List<String> getDokumentasiFotoIds() {
            return dokumentasiFotoIds;
        }

        public void setDokumentasiFotoIds(List<String> dokumentasiFotoIds) {
            this.dokumentasiFotoIds = dokumentasiFotoIds;
        }

        public List<String> getDokumentasiVideoIds() {
            return dokumentasiVideoIds;
        }

        public void setDokumentasiVideoIds(List<String> dokumentasiVideoIds) {
            this.dokumentasiVideoIds = dokumentasiVideoIds;
        }
    }
}