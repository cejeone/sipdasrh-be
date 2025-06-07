package com.kehutanan.tktrh.bkta.kegiatan.controller;

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

import com.kehutanan.tktrh.bkta.kegiatan.dto.KegiatanLokusPageDTO;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanLokus;
import com.kehutanan.tktrh.bkta.kegiatan.model.dto.KegiatanLokusDTO;
import com.kehutanan.tktrh.bkta.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.bkta.kegiatan.service.KegiatanLokusService;
import com.kehutanan.tktrh.bkta.kegiatan.service.KegiatanService;
import com.kehutanan.tktrh.master.model.Bpdas;
import com.kehutanan.tktrh.master.model.Das;
import com.kehutanan.tktrh.master.model.KabupatenKota;
import com.kehutanan.tktrh.master.model.Kecamatan;
import com.kehutanan.tktrh.master.model.KelurahanDesa;
import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.model.Provinsi;
import com.kehutanan.tktrh.master.service.BpdasService;
import com.kehutanan.tktrh.master.service.DasService;
import com.kehutanan.tktrh.master.service.KabupatenKotaService;
import com.kehutanan.tktrh.master.service.KecamatanService;
import com.kehutanan.tktrh.master.service.KelurahanDesaService;
import com.kehutanan.tktrh.master.service.LovService;
import com.kehutanan.tktrh.master.service.ProvinsiService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/bkta/kegiatan-lokus")
public class KegiatanLokusController {

    private final KegiatanLokusService service;
    private final KegiatanService kegiatanService;
    private final ProvinsiService provinsiService;
    private final KabupatenKotaService kabupatenKotaService;
    private final KecamatanService kecamatanService;
    private final KelurahanDesaService kelurahanDesaService;
    private final BpdasService bpdasService;
    private final DasService dasService;
    private final LovService lovService;
    private final PagedResourcesAssembler<KegiatanLokus> pagedResourcesAssembler;

    @Autowired
    public KegiatanLokusController(
            KegiatanLokusService service,
            KegiatanService kegiatanService,
            ProvinsiService provinsiService,
            KabupatenKotaService kabupatenKotaService,
            KecamatanService kecamatanService,
            KelurahanDesaService kelurahanDesaService,
            BpdasService bpdasService,
            DasService dasService,
            LovService lovService,
            PagedResourcesAssembler<KegiatanLokus> pagedResourcesAssembler) {
        this.service = service;
        this.kegiatanService = kegiatanService;
        this.provinsiService = provinsiService;
        this.kabupatenKotaService = kabupatenKotaService;
        this.kecamatanService = kecamatanService;
        this.kelurahanDesaService = kelurahanDesaService;
        this.bpdasService = bpdasService;
        this.dasService = dasService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<KegiatanLokusPageDTO> getAllKegiatanLokus(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String provinsi,
            @RequestParam(required = false) String catatan,
            @RequestParam(required = false) List<String> status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanLokusPageDTO kegiatanLokusPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if (kegiatanId != null || provinsi != null || catatan != null || (status != null && !status.isEmpty())) {
            kegiatanLokusPage = service.findByFiltersWithCache(kegiatanId, provinsi, catatan, status, pageable, baseUrl);
        } else {
            kegiatanLokusPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(kegiatanLokusPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Kegiatan Lokus")
    public ResponseEntity<KegiatanLokusPageDTO> searchKegiatanLokus(
            @RequestParam(required = false) Long kegiatanId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanLokusPageDTO kegiatanLokusPage = service.searchWithCache(kegiatanId, keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(kegiatanLokusPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KegiatanLokusDTO> getKegiatanLokusById(@PathVariable Long id) {
        try {
            KegiatanLokusDTO kegiatanLokusDTO = service.findDTOById(id);
            return ResponseEntity.ok(kegiatanLokusDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<KegiatanLokus> createKegiatanLokus(
            @RequestPart(required = false) String kegiatanId,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String kelurahanDesaId,
            @RequestPart(required = false) String bpdasId,
            @RequestPart(required = false) String dasId,
            @RequestPart(required = false) String subDas,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String identitasBkta,
            @RequestPart(required = false) String jenisBangunanId,
            @RequestPart(required = false) String koordinatX,
            @RequestPart(required = false) String koordinatY,
            @RequestPart(required = false) String lebarAlurAtas,
            @RequestPart(required = false) String lebarAlurBawah,
            @RequestPart(required = false) String panjang,
            @RequestPart(required = false) String tinggi,
            @RequestPart(required = false) String lebar,
            @RequestPart(required = false) String volumeBangunan,
            @RequestPart(required = false) String dayaTampungSedimen,
            @RequestPart(required = false) String penerimaManfaatId,
            @RequestPart(required = false) String anggaran,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String informasiTambahan,
            @RequestPart(required = false) String integrasiRhl,
            @RequestPart(required = false) String rawanBencana,
            @RequestPart(required = false) String dasPrioritas,
            @RequestPart(required = false) String perlindunganMataAir,
            @RequestPart(required = false) String danauPrioritas,
            @RequestPart(required = false) String pengendalianErosiDanSedimentasi,
            @RequestPart(required = false) String catatan) {

        try {
            KegiatanLokus newKegiatanLokus = new KegiatanLokus();
            
            // Set relations if IDs are provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                newKegiatanLokus.setKegiatan(kegiatan);
            }
            
            if (provinsiId != null && !provinsiId.isEmpty()) {
                Long provinsiIdLong = Long.parseLong(provinsiId);
                Provinsi provinsi = provinsiService.findById(provinsiIdLong);
                newKegiatanLokus.setProvinsi(provinsi);
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                Long kabupatenKotaIdLong = Long.parseLong(kabupatenKotaId);
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(kabupatenKotaIdLong);
                newKegiatanLokus.setKabupatenKota(kabupatenKota);
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Long kecamatanIdLong = Long.parseLong(kecamatanId);
                Kecamatan kecamatan = kecamatanService.findById(kecamatanIdLong);
                newKegiatanLokus.setKecamatan(kecamatan);
            }
            
            if (kelurahanDesaId != null && !kelurahanDesaId.isEmpty()) {
                Long kelurahanDesaIdLong = Long.parseLong(kelurahanDesaId);
                KelurahanDesa kelurahanDesa = kelurahanDesaService.findById(kelurahanDesaIdLong);
                newKegiatanLokus.setKelurahanDesa(kelurahanDesa);
            }
            
            if (bpdasId != null && !bpdasId.isEmpty()) {
                Long bpdasIdLong = Long.parseLong(bpdasId);
                Bpdas bpdas = bpdasService.findById(bpdasIdLong);
                newKegiatanLokus.setBpdas(bpdas);
            }
            
            if (dasId != null && !dasId.isEmpty()) {
                Long dasIdLong = Long.parseLong(dasId);
                Das das = dasService.findById(dasIdLong);
                newKegiatanLokus.setDas(das);
            }
            
            newKegiatanLokus.setSubDas(subDas);
            
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newKegiatanLokus.setStatus(status);
            }
            
            newKegiatanLokus.setIdentitasBkta(identitasBkta);
            
            if (jenisBangunanId != null && !jenisBangunanId.isEmpty()) {
                Long jenisBangunanIdLong = Long.parseLong(jenisBangunanId);
                Lov jenisBangunan = lovService.findById(jenisBangunanIdLong);
                newKegiatanLokus.setJenisBangunan(jenisBangunan);
            }
            
            if (koordinatX != null && !koordinatX.isEmpty()) {
                newKegiatanLokus.setKoordinatX(Double.parseDouble(koordinatX));
            }
            
            if (koordinatY != null && !koordinatY.isEmpty()) {
                newKegiatanLokus.setKoordinatY(Double.parseDouble(koordinatY));
            }
            
            if (lebarAlurAtas != null && !lebarAlurAtas.isEmpty()) {
                newKegiatanLokus.setLebarAlurAtas(Double.parseDouble(lebarAlurAtas));
            }
            
            if (lebarAlurBawah != null && !lebarAlurBawah.isEmpty()) {
                newKegiatanLokus.setLebarAlurBawah(Double.parseDouble(lebarAlurBawah));
            }
            
            if (panjang != null && !panjang.isEmpty()) {
                newKegiatanLokus.setPanjang(Double.parseDouble(panjang));
            }
            
            if (tinggi != null && !tinggi.isEmpty()) {
                newKegiatanLokus.setTinggi(Double.parseDouble(tinggi));
            }
            
            if (lebar != null && !lebar.isEmpty()) {
                newKegiatanLokus.setLebar(Double.parseDouble(lebar));
            }
            
            if (volumeBangunan != null && !volumeBangunan.isEmpty()) {
                newKegiatanLokus.setVolumeBangunan(Double.parseDouble(volumeBangunan));
            }
            
            if (dayaTampungSedimen != null && !dayaTampungSedimen.isEmpty()) {
                newKegiatanLokus.setDayaTampungSedimen(Double.parseDouble(dayaTampungSedimen));
            }
            
            if (penerimaManfaatId != null && !penerimaManfaatId.isEmpty()) {
                Long penerimaManfaatIdLong = Long.parseLong(penerimaManfaatId);
                Lov penerimaManfaat = lovService.findById(penerimaManfaatIdLong);
                newKegiatanLokus.setPenerimaManfaat(penerimaManfaat);
            }
            
            if (anggaran != null && !anggaran.isEmpty()) {
                newKegiatanLokus.setAnggaran(Double.parseDouble(anggaran));
            }
            
            newKegiatanLokus.setKeterangan(keterangan);
            
            if (informasiTambahan != null) {
                newKegiatanLokus.setInformasiTambahan(Boolean.parseBoolean(informasiTambahan));
            }
            
            if (integrasiRhl != null) {
                newKegiatanLokus.setIntegrasiRhl(Boolean.parseBoolean(integrasiRhl));
            }
            
            if (rawanBencana != null) {
                newKegiatanLokus.setRawanBencana(Boolean.parseBoolean(rawanBencana));
            }
            
            if (dasPrioritas != null) {
                newKegiatanLokus.setDasPrioritas(Boolean.parseBoolean(dasPrioritas));
            }
            
            if (perlindunganMataAir != null) {
                newKegiatanLokus.setPerlindunganMataAir(Boolean.parseBoolean(perlindunganMataAir));
            }
            
            if (danauPrioritas != null) {
                newKegiatanLokus.setDanauPrioritas(Boolean.parseBoolean(danauPrioritas));
            }
            
            if (pengendalianErosiDanSedimentasi != null) {
                newKegiatanLokus.setPengendalianErosiDanSedimentasi(Boolean.parseBoolean(pengendalianErosiDanSedimentasi));
            }
            
            newKegiatanLokus.setCatatan(catatan);

            KegiatanLokus savedKegiatanLokus = service.save(newKegiatanLokus);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKegiatanLokus);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KegiatanLokus> updateKegiatanLokus(
            @PathVariable Long id,
            @RequestPart(required = false) String kegiatanId,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String kelurahanDesaId,
            @RequestPart(required = false) String bpdasId,
            @RequestPart(required = false) String dasId,
            @RequestPart(required = false) String subDas,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String identitasBkta,
            @RequestPart(required = false) String jenisBangunanId,
            @RequestPart(required = false) String koordinatX,
            @RequestPart(required = false) String koordinatY,
            @RequestPart(required = false) String lebarAlurAtas,
            @RequestPart(required = false) String lebarAlurBawah,
            @RequestPart(required = false) String panjang,
            @RequestPart(required = false) String tinggi,
            @RequestPart(required = false) String lebar,
            @RequestPart(required = false) String volumeBangunan,
            @RequestPart(required = false) String dayaTampungSedimen,
            @RequestPart(required = false) String penerimaManfaatId,
            @RequestPart(required = false) String anggaran,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String informasiTambahan,
            @RequestPart(required = false) String integrasiRhl,
            @RequestPart(required = false) String rawanBencana,
            @RequestPart(required = false) String dasPrioritas,
            @RequestPart(required = false) String perlindunganMataAir,
            @RequestPart(required = false) String danauPrioritas,
            @RequestPart(required = false) String pengendalianErosiDanSedimentasi,
            @RequestPart(required = false) String catatan) {

        try {
            KegiatanLokus existingKegiatanLokus = service.findById(id);
            
            // Update relations if IDs are provided
            if (kegiatanId != null && !kegiatanId.isEmpty()) {
                Long kegiatanIdLong = Long.parseLong(kegiatanId);
                Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
                existingKegiatanLokus.setKegiatan(kegiatan);
            }
            
            if (provinsiId != null && !provinsiId.isEmpty()) {
                Long provinsiIdLong = Long.parseLong(provinsiId);
                Provinsi provinsi = provinsiService.findById(provinsiIdLong);
                existingKegiatanLokus.setProvinsi(provinsi);
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                Long kabupatenKotaIdLong = Long.parseLong(kabupatenKotaId);
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(kabupatenKotaIdLong);
                existingKegiatanLokus.setKabupatenKota(kabupatenKota);
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Long kecamatanIdLong = Long.parseLong(kecamatanId);
                Kecamatan kecamatan = kecamatanService.findById(kecamatanIdLong);
                existingKegiatanLokus.setKecamatan(kecamatan);
            }
            
            if (kelurahanDesaId != null && !kelurahanDesaId.isEmpty()) {
                Long kelurahanDesaIdLong = Long.parseLong(kelurahanDesaId);
                KelurahanDesa kelurahanDesa = kelurahanDesaService.findById(kelurahanDesaIdLong);
                existingKegiatanLokus.setKelurahanDesa(kelurahanDesa);
            }
            
            if (bpdasId != null && !bpdasId.isEmpty()) {
                Long bpdasIdLong = Long.parseLong(bpdasId);
                Bpdas bpdas = bpdasService.findById(bpdasIdLong);
                existingKegiatanLokus.setBpdas(bpdas);
            }
            
            if (dasId != null && !dasId.isEmpty()) {
                Long dasIdLong = Long.parseLong(dasId);
                Das das = dasService.findById(dasIdLong);
                existingKegiatanLokus.setDas(das);
            }
            
            if (subDas != null) {
                existingKegiatanLokus.setSubDas(subDas);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingKegiatanLokus.setStatus(status);
            }
            
            if (identitasBkta != null) {
                existingKegiatanLokus.setIdentitasBkta(identitasBkta);
            }
            
            if (jenisBangunanId != null && !jenisBangunanId.isEmpty()) {
                Long jenisBangunanIdLong = Long.parseLong(jenisBangunanId);
                Lov jenisBangunan = lovService.findById(jenisBangunanIdLong);
                existingKegiatanLokus.setJenisBangunan(jenisBangunan);
            }
            
            if (koordinatX != null && !koordinatX.isEmpty()) {
                existingKegiatanLokus.setKoordinatX(Double.parseDouble(koordinatX));
            }
            
            if (koordinatY != null && !koordinatY.isEmpty()) {
                existingKegiatanLokus.setKoordinatY(Double.parseDouble(koordinatY));
            }
            
            if (lebarAlurAtas != null && !lebarAlurAtas.isEmpty()) {
                existingKegiatanLokus.setLebarAlurAtas(Double.parseDouble(lebarAlurAtas));
            }
            
            if (lebarAlurBawah != null && !lebarAlurBawah.isEmpty()) {
                existingKegiatanLokus.setLebarAlurBawah(Double.parseDouble(lebarAlurBawah));
            }
            
            if (panjang != null && !panjang.isEmpty()) {
                existingKegiatanLokus.setPanjang(Double.parseDouble(panjang));
            }
            
            if (tinggi != null && !tinggi.isEmpty()) {
                existingKegiatanLokus.setTinggi(Double.parseDouble(tinggi));
            }
            
            if (lebar != null && !lebar.isEmpty()) {
                existingKegiatanLokus.setLebar(Double.parseDouble(lebar));
            }
            
            if (volumeBangunan != null && !volumeBangunan.isEmpty()) {
                existingKegiatanLokus.setVolumeBangunan(Double.parseDouble(volumeBangunan));
            }
            
            if (dayaTampungSedimen != null && !dayaTampungSedimen.isEmpty()) {
                existingKegiatanLokus.setDayaTampungSedimen(Double.parseDouble(dayaTampungSedimen));
            }
            
            if (penerimaManfaatId != null && !penerimaManfaatId.isEmpty()) {
                Long penerimaManfaatIdLong = Long.parseLong(penerimaManfaatId);
                Lov penerimaManfaat = lovService.findById(penerimaManfaatIdLong);
                existingKegiatanLokus.setPenerimaManfaat(penerimaManfaat);
            }
            
            if (anggaran != null && !anggaran.isEmpty()) {
                existingKegiatanLokus.setAnggaran(Double.parseDouble(anggaran));
            }
            
            if (keterangan != null) {
                existingKegiatanLokus.setKeterangan(keterangan);
            }
            
            if (informasiTambahan != null) {
                existingKegiatanLokus.setInformasiTambahan(Boolean.parseBoolean(informasiTambahan));
            }
            
            if (integrasiRhl != null) {
                existingKegiatanLokus.setIntegrasiRhl(Boolean.parseBoolean(integrasiRhl));
            }
            
            if (rawanBencana != null) {
                existingKegiatanLokus.setRawanBencana(Boolean.parseBoolean(rawanBencana));
            }
            
            if (dasPrioritas != null) {
                existingKegiatanLokus.setDasPrioritas(Boolean.parseBoolean(dasPrioritas));
            }
            
            if (perlindunganMataAir != null) {
                existingKegiatanLokus.setPerlindunganMataAir(Boolean.parseBoolean(perlindunganMataAir));
            }
            
            if (danauPrioritas != null) {
                existingKegiatanLokus.setDanauPrioritas(Boolean.parseBoolean(danauPrioritas));
            }
            
            if (pengendalianErosiDanSedimentasi != null) {
                existingKegiatanLokus.setPengendalianErosiDanSedimentasi(Boolean.parseBoolean(pengendalianErosiDanSedimentasi));
            }
            
            if (catatan != null) {
                existingKegiatanLokus.setCatatan(catatan);
            }

            KegiatanLokus updatedKegiatanLokus = service.update(id, existingKegiatanLokus);
            return ResponseEntity.ok(updatedKegiatanLokus);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKegiatanLokus(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/{id}/proposal-files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload proposal PDF files for Kegiatan Lokus")
    public ResponseEntity<?> uploadProposalFiles(
            @PathVariable Long id,
            @RequestPart(value = "pdfs") List<MultipartFile> pdfs) {
        try {
            KegiatanLokus kegiatanLokus = service.uploadProposalPdf(id, pdfs);
            return ResponseEntity.ok(kegiatanLokus);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload proposal files: " + e.getMessage());
        }
    }

    @PostMapping(value = "/{id}/lokasi-files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload lokasi PDF files for Kegiatan Lokus")
    public ResponseEntity<?> uploadLokasiFiles(
            @PathVariable Long id,
            @RequestPart(value = "pdfs") List<MultipartFile> pdfs) {
        try {
            KegiatanLokus kegiatanLokus = service.uploadLokasiPdf(id, pdfs);
            return ResponseEntity.ok(kegiatanLokus);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload lokasi files: " + e.getMessage());
        }
    }

    @PostMapping(value = "/{id}/bangunan-files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload bangunan PDF files for Kegiatan Lokus")
    public ResponseEntity<?> uploadBangunanFiles(
            @PathVariable Long id,
            @RequestPart(value = "pdfs") List<MultipartFile> pdfs) {
        try {
            KegiatanLokus kegiatanLokus = service.uploadBangunanPdf(id, pdfs);
            return ResponseEntity.ok(kegiatanLokus);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload bangunan files: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/proposal-files")
    @Operation(summary = "Delete proposal PDF files from Kegiatan Lokus")
    public ResponseEntity<?> deleteProposalFiles(
            @PathVariable Long id,
            @RequestBody List<String> uuidPdfs) {
        try {
            KegiatanLokus kegiatanLokus = service.deleteProposalPdf(id, uuidPdfs);
            return ResponseEntity.ok(kegiatanLokus);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete proposal files: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/lokasi-files")
    @Operation(summary = "Delete lokasi PDF files from Kegiatan Lokus")
    public ResponseEntity<?> deleteLokasiFiles(
            @PathVariable Long id,
            @RequestBody List<String> uuidPdfs) {
        try {
            KegiatanLokus kegiatanLokus = service.deleteLokasiPdf(id, uuidPdfs);
            return ResponseEntity.ok(kegiatanLokus);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete lokasi files: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/bangunan-files")
    @Operation(summary = "Delete bangunan PDF files from Kegiatan Lokus")
    public ResponseEntity<?> deleteBangunanFiles(
            @PathVariable Long id,
            @RequestBody List<String> uuidPdfs) {
        try {
            KegiatanLokus kegiatanLokus = service.deleteBangunanPdf(id, uuidPdfs);
            return ResponseEntity.ok(kegiatanLokus);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete bangunan files: " + e.getMessage());
        }
    }
}