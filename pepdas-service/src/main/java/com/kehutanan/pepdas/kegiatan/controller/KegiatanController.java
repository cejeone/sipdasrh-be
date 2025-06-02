package com.kehutanan.pepdas.kegiatan.controller;

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

import com.kehutanan.pepdas.kegiatan.dto.KegiatanDTO;
import com.kehutanan.pepdas.kegiatan.dto.KegiatanDeleteFilesRequest;
import com.kehutanan.pepdas.kegiatan.dto.KegiatanPageDTO;
import com.kehutanan.pepdas.kegiatan.model.Kegiatan;
import com.kehutanan.pepdas.kegiatan.service.KegiatanService;
import com.kehutanan.pepdas.master.model.Bpdas;
import com.kehutanan.pepdas.master.model.Das;
import com.kehutanan.pepdas.master.model.Eselon2;
import com.kehutanan.pepdas.master.model.KabupatenKota;
import com.kehutanan.pepdas.master.model.Kecamatan;
import com.kehutanan.pepdas.master.model.KelurahanDesa;
import com.kehutanan.pepdas.master.model.Lov;
import com.kehutanan.pepdas.master.model.Provinsi;
import com.kehutanan.pepdas.master.service.BpdasService;
import com.kehutanan.pepdas.master.service.DasService;
import com.kehutanan.pepdas.master.service.Eselon2Service;
import com.kehutanan.pepdas.master.service.KabupatenKotaService;
import com.kehutanan.pepdas.master.service.KecamatanService;
import com.kehutanan.pepdas.master.service.KelurahanDesaService;
import com.kehutanan.pepdas.master.service.LovService;
import com.kehutanan.pepdas.master.service.ProvinsiService;
import com.kehutanan.pepdas.program.model.Program;
import com.kehutanan.pepdas.program.service.ProgramService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/kegiatan")
public class KegiatanController {

    private final KegiatanService kegiatanService;
    private final PagedResourcesAssembler<Kegiatan> pagedResourcesAssembler;

    @Autowired
    private Eselon2Service eselon2Service;

    @Autowired
    private ProgramService programService;

    @Autowired
    private LovService lovService;

    @Autowired
    private BpdasService bpdasService;

    @Autowired
    private DasService dasService;

    @Autowired
    private ProvinsiService provinsiService;

    @Autowired
    private KabupatenKotaService kabupatenKotaService;

    @Autowired
    private KecamatanService kecamatanService;

    @Autowired
    private KelurahanDesaService kelurahanDesaService;
    @Autowired
    public KegiatanController(
            KegiatanService kegiatanService,
            PagedResourcesAssembler<Kegiatan> pagedResourcesAssembler) {
        this.kegiatanService = kegiatanService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<KegiatanPageDTO> getAllKegiatan(
            @RequestParam(required = false) String program,
            @RequestParam(required = false) String kegiatan,
            @RequestParam(required = false) List<String> bpdasList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        Pageable pageable = PageRequest.of(page, size);
        KegiatanPageDTO kegiatanPage;
        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((program != null && !program.isEmpty()) ||
                (kegiatan != null && !kegiatan.isEmpty()) ||
                (bpdasList != null && !bpdasList.isEmpty())) {
            kegiatanPage = kegiatanService.findByFiltersWithCache(program, kegiatan, bpdasList, pageable, baseUrl);
        } else {
            kegiatanPage = kegiatanService.findAllWithCache(pageable, baseUrl);
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
            @RequestPart String namaKegiatan,
            @RequestPart(required = false) String eselon2Id,
            @RequestPart(required = false) String programId,
            @RequestPart(required = false) String jenisKegiatanId,
            @RequestPart(required = false) String bpdasId,
            @RequestPart(required = false) String dasId,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String kelurahanDesaId,
            @RequestPart(required = false) String alamat,
            @RequestPart(required = false) String skemaId,
            @RequestPart(required = false) String tahunKegiatan,
            @RequestPart(required = false) String sumberAnggaranId,
            @RequestPart(required = false) String penerimaManfaatKegiatanId,
            @RequestPart(required = false) String detailPelaksanaId,
            @RequestPart(required = false) String nomorKontrak,
            @RequestPart(required = false) String nilaiKontrak,
            @RequestPart(required = false) String tipeId,
            @RequestPart(required = false) String penerimaManfaatKontrakId,
            @RequestPart(required = false) String tanggalKontrak,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String dokumentasiCatatanFoto,
            @RequestPart(required = false) String catatanVideo) {

        try {
            Kegiatan newKegiatan = new Kegiatan();
            newKegiatan.setNamaKegiatan(namaKegiatan);
            
            // Set reference entities using service lookups
            if (eselon2Id != null && !eselon2Id.isEmpty()) {
                Eselon2 eselon2 = eselon2Service.findById(Long.parseLong(eselon2Id));
                newKegiatan.setEselon2(eselon2);
            }
            
            if (programId != null && !programId.isEmpty()) {
                Program program = programService.findById(Long.parseLong(programId));
                newKegiatan.setProgram(program);
            }
            
            if (jenisKegiatanId != null && !jenisKegiatanId.isEmpty()) {
                Lov jenisKegiatan = lovService.findById(Long.parseLong(jenisKegiatanId));
                newKegiatan.setJenisKegiatan(jenisKegiatan);
            }
            
            if (bpdasId != null && !bpdasId.isEmpty()) {
                Bpdas bpdas = bpdasService.findById(Long.parseLong(bpdasId));
                newKegiatan.setBpdas(bpdas);
            }
            
            if (dasId != null && !dasId.isEmpty()) {
                Das das = dasService.findById(Long.parseLong(dasId));
                newKegiatan.setDas(das);
            }
            
            if (provinsiId != null && !provinsiId.isEmpty()) {
                Provinsi provinsi = provinsiService.findById(Long.parseLong(provinsiId));
                newKegiatan.setProvinsi(provinsi);
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(Long.parseLong(kabupatenKotaId));
                newKegiatan.setKabupatenKota(kabupatenKota);
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Kecamatan kecamatan = kecamatanService.findById(Long.parseLong(kecamatanId));
                newKegiatan.setKecamatan(kecamatan);
            }
            
            if (kelurahanDesaId != null && !kelurahanDesaId.isEmpty()) {
                KelurahanDesa kelurahanDesa = kelurahanDesaService.findById(Long.parseLong(kelurahanDesaId));
                newKegiatan.setKelurahanDesa(kelurahanDesa);
            }
            
            newKegiatan.setAlamat(alamat);
            
            if (skemaId != null && !skemaId.isEmpty()) {
                Lov skema = lovService.findById(Long.parseLong(skemaId));
                newKegiatan.setSkema(skema);
            }
            
            if (tahunKegiatan != null && !tahunKegiatan.isEmpty()) {
                newKegiatan.setTahunKegiatan(Integer.parseInt(tahunKegiatan));
            }
            
            if (sumberAnggaranId != null && !sumberAnggaranId.isEmpty()) {
                Lov sumberAnggaran = lovService.findById(Long.parseLong(sumberAnggaranId));
                newKegiatan.setSumberAnggaran(sumberAnggaran);
            }
            
            if (penerimaManfaatKegiatanId != null && !penerimaManfaatKegiatanId.isEmpty()) {
                Lov penerimaManfaatKegiatan = lovService.findById(Long.parseLong(penerimaManfaatKegiatanId));
                newKegiatan.setPenerimaManfaatKegiatan(penerimaManfaatKegiatan);
            }
            
            if (detailPelaksanaId != null && !detailPelaksanaId.isEmpty()) {
                Lov detailPelaksana = lovService.findById(Long.parseLong(detailPelaksanaId));
                newKegiatan.setDetailPelaksana(detailPelaksana);
            }
            
            newKegiatan.setNomorKontrak(nomorKontrak);
            
            if (nilaiKontrak != null && !nilaiKontrak.isEmpty()) {
                newKegiatan.setNilaiKontrak(Integer.parseInt(nilaiKontrak));
            }
            
            if (tipeId != null && !tipeId.isEmpty()) {
                Lov tipe = lovService.findById(Long.parseLong(tipeId));
                newKegiatan.setTipe(tipe);
            }
            
            if (penerimaManfaatKontrakId != null && !penerimaManfaatKontrakId.isEmpty()) {
                Lov penerimaManfaatKontrak = lovService.findById(Long.parseLong(penerimaManfaatKontrakId));
                newKegiatan.setPenerimaManfaatKontrak(penerimaManfaatKontrak);
            }
            
            if (tanggalKontrak != null && !tanggalKontrak.isEmpty()) {
                newKegiatan.setTanggalKontrak(java.time.LocalDate.parse(tanggalKontrak));
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Lov status = lovService.findById(Long.parseLong(statusId));
                newKegiatan.setStatus(status);
            }
            
            newKegiatan.setDokumentasiCatatanFoto(dokumentasiCatatanFoto);
            newKegiatan.setDokumentasiCatatanVideo(catatanVideo);

            Kegiatan savedKegiatan = kegiatanService.save(newKegiatan);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKegiatan);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Kegiatan> updateKegiatan(
            @PathVariable Long id,
            @RequestPart String namaKegiatan,
            @RequestPart(required = false) String eselon2Id,
            @RequestPart(required = false) String programId,
            @RequestPart(required = false) String jenisKegiatanId,
            @RequestPart(required = false) String bpdasId,
            @RequestPart(required = false) String dasId,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String kelurahanDesaId,
            @RequestPart(required = false) String alamat,
            @RequestPart(required = false) String skemaId,
            @RequestPart(required = false) String tahunKegiatan,
            @RequestPart(required = false) String sumberAnggaranId,
            @RequestPart(required = false) String penerimaManfaatKegiatanId,
            @RequestPart(required = false) String detailPelaksanaId,
            @RequestPart(required = false) String nomorKontrak,
            @RequestPart(required = false) String nilaiKontrak,
            @RequestPart(required = false) String tipeId,
            @RequestPart(required = false) String penerimaManfaatKontrakId,
            @RequestPart(required = false) String tanggalKontrak,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String dokumentasiCatatanFoto,
            @RequestPart(required = false) String catatanVideo) {

        try {
            Kegiatan existingKegiatan = kegiatanService.findById(id);
            
            existingKegiatan.setNamaKegiatan(namaKegiatan);
            
            // Set reference entities using service lookups
            if (eselon2Id != null && !eselon2Id.isEmpty()) {
                Eselon2 eselon2 = eselon2Service.findById(Long.parseLong(eselon2Id));
                existingKegiatan.setEselon2(eselon2);
            }
            
            if (programId != null && !programId.isEmpty()) {
                Program program = programService.findById(Long.parseLong(programId));
                existingKegiatan.setProgram(program);
            }
            
            if (jenisKegiatanId != null && !jenisKegiatanId.isEmpty()) {
                Lov jenisKegiatan = lovService.findById(Long.parseLong(jenisKegiatanId));
                existingKegiatan.setJenisKegiatan(jenisKegiatan);
            }
            
            if (bpdasId != null && !bpdasId.isEmpty()) {
                Bpdas bpdas = bpdasService.findById(Long.parseLong(bpdasId));
                existingKegiatan.setBpdas(bpdas);
            }
            
            if (dasId != null && !dasId.isEmpty()) {
                Das das = dasService.findById(Long.parseLong(dasId));
                existingKegiatan.setDas(das);
            }
            
            if (provinsiId != null && !provinsiId.isEmpty()) {
                Provinsi provinsi = provinsiService.findById(Long.parseLong(provinsiId));
                existingKegiatan.setProvinsi(provinsi);
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(Long.parseLong(kabupatenKotaId));
                existingKegiatan.setKabupatenKota(kabupatenKota);
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Kecamatan kecamatan = kecamatanService.findById(Long.parseLong(kecamatanId));
                existingKegiatan.setKecamatan(kecamatan);
            }
            
            if (kelurahanDesaId != null && !kelurahanDesaId.isEmpty()) {
                KelurahanDesa kelurahanDesa = kelurahanDesaService.findById(Long.parseLong(kelurahanDesaId));
                existingKegiatan.setKelurahanDesa(kelurahanDesa);
            }
            
            existingKegiatan.setAlamat(alamat);
            
            if (skemaId != null && !skemaId.isEmpty()) {
                Lov skema = lovService.findById(Long.parseLong(skemaId));
                existingKegiatan.setSkema(skema);
            }
            
            if (tahunKegiatan != null && !tahunKegiatan.isEmpty()) {
                existingKegiatan.setTahunKegiatan(Integer.parseInt(tahunKegiatan));
            }
            
            if (sumberAnggaranId != null && !sumberAnggaranId.isEmpty()) {
                Lov sumberAnggaran = lovService.findById(Long.parseLong(sumberAnggaranId));
                existingKegiatan.setSumberAnggaran(sumberAnggaran);
            }
            
            if (penerimaManfaatKegiatanId != null && !penerimaManfaatKegiatanId.isEmpty()) {
                Lov penerimaManfaatKegiatan = lovService.findById(Long.parseLong(penerimaManfaatKegiatanId));
                existingKegiatan.setPenerimaManfaatKegiatan(penerimaManfaatKegiatan);
            }
            
            if (detailPelaksanaId != null && !detailPelaksanaId.isEmpty()) {
                Lov detailPelaksana = lovService.findById(Long.parseLong(detailPelaksanaId));
                existingKegiatan.setDetailPelaksana(detailPelaksana);
            }
            
            existingKegiatan.setNomorKontrak(nomorKontrak);
            
            if (nilaiKontrak != null && !nilaiKontrak.isEmpty()) {
                existingKegiatan.setNilaiKontrak(Integer.parseInt(nilaiKontrak));
            }
            
            if (tipeId != null && !tipeId.isEmpty()) {
                Lov tipe = lovService.findById(Long.parseLong(tipeId));
                existingKegiatan.setTipe(tipe);
            }
            
            if (penerimaManfaatKontrakId != null && !penerimaManfaatKontrakId.isEmpty()) {
                Lov penerimaManfaatKontrak = lovService.findById(Long.parseLong(penerimaManfaatKontrakId));
                existingKegiatan.setPenerimaManfaatKontrak(penerimaManfaatKontrak);
            }
            
            if (tanggalKontrak != null && !tanggalKontrak.isEmpty()) {
                existingKegiatan.setTanggalKontrak(java.time.LocalDate.parse(tanggalKontrak));
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Lov status = lovService.findById(Long.parseLong(statusId));
                existingKegiatan.setStatus(status);
            }
            
            existingKegiatan.setDokumentasiCatatanFoto(dokumentasiCatatanFoto);
            existingKegiatan.setDokumentasiCatatanVideo(catatanVideo);

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
            @RequestPart(value = "rancanganTeknisVideos", required = false) List<MultipartFile> rancanganTeknisVideos,
            @RequestPart(value = "kontrakPdfs", required = false) List<MultipartFile> kontrakPdfs,
            @RequestPart(value = "dokumentasiFotos", required = false) List<MultipartFile> dokumentasiFotos,
            @RequestPart(value = "dokumentasiVideos", required = false) List<MultipartFile> dokumentasiVideos,
            @RequestPart(value = "lokusShp", required = false) List<MultipartFile> lokusShp) {
        
        try {
            if (rancanganTeknisPdfs != null && !rancanganTeknisPdfs.isEmpty()) {
                kegiatanService.uploadRancanganTeknisPdf(id, rancanganTeknisPdfs);
            }
            
            if (rancanganTeknisFotos != null && !rancanganTeknisFotos.isEmpty()) {
                kegiatanService.uploadRancanganTeknisFoto(id, rancanganTeknisFotos);
            }
            
            if (rancanganTeknisVideos != null && !rancanganTeknisVideos.isEmpty()) {
                kegiatanService.uploadRancanganTeknisVideo(id, rancanganTeknisVideos);
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
            
            if (lokusShp != null && !lokusShp.isEmpty()) {
                kegiatanService.uploadLokusShp(id, lokusShp);
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
            @RequestBody(required = false) KegiatanDeleteFilesRequest filesRequest) {
        
        try {
            if (filesRequest.getRancanganTeknisPdfIds() != null && !filesRequest.getRancanganTeknisPdfIds().isEmpty()) {
                kegiatanService.deleteRancanganTeknisPdf(id, filesRequest.getRancanganTeknisPdfIds());
            }
            
            if (filesRequest.getRancanganTeknisFotoIds() != null && !filesRequest.getRancanganTeknisFotoIds().isEmpty()) {
                kegiatanService.deleteRancanganTeknisFoto(id, filesRequest.getRancanganTeknisFotoIds());
            }
            
            if (filesRequest.getRancanganTeknisVideoIds() != null && !filesRequest.getRancanganTeknisVideoIds().isEmpty()) {
                kegiatanService.deleteRancanganTeknisVideo(id, filesRequest.getRancanganTeknisVideoIds());
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
            
            if (filesRequest.getLokusShpIds() != null && !filesRequest.getLokusShpIds().isEmpty()) {
                kegiatanService.deleteLokusShp(id, filesRequest.getLokusShpIds());
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

}