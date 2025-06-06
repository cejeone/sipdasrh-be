package com.kehutanan.tktrh.tmkh.kegiatan.controller;

import java.time.LocalDate;
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

import com.kehutanan.tktrh.bkta.program.model.Program;
import com.kehutanan.tktrh.bkta.program.service.ProgramService;
import com.kehutanan.tktrh.master.model.Bpdas;
import com.kehutanan.tktrh.master.model.KabupatenKota;
import com.kehutanan.tktrh.master.model.Kecamatan;
import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.model.Provinsi;
import com.kehutanan.tktrh.master.service.BpdasService;
import com.kehutanan.tktrh.master.service.KabupatenKotaService;
import com.kehutanan.tktrh.master.service.KecamatanService;
import com.kehutanan.tktrh.master.service.LovService;
import com.kehutanan.tktrh.master.service.ProvinsiService;
import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanDeleteFilesRequest;
import com.kehutanan.tktrh.tmkh.kegiatan.dto.KegiatanPageDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.tmkh.kegiatan.model.dto.KegiatanDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.service.KegiatanService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/kegiatan")
public class KegiatanController {

    private final KegiatanService kegiatanService;
    private final ProgramService programService;
    private final ProvinsiService provinsiService;
    private final KabupatenKotaService kabupatenKotaService;
    private final KecamatanService kecamatanService;
    private final BpdasService bpdasService;
    private final LovService lovService;
    private final PagedResourcesAssembler<Kegiatan> pagedResourcesAssembler;

    @Autowired
    public KegiatanController(
            KegiatanService kegiatanService,
            ProgramService programService,
            ProvinsiService provinsiService,
            KabupatenKotaService kabupatenKotaService,
            KecamatanService kecamatanService,
            BpdasService bpdasService,
            LovService lovService,
            PagedResourcesAssembler<Kegiatan> pagedResourcesAssembler) {
        this.kegiatanService = kegiatanService;
        this.programService = programService;
        this.provinsiService = provinsiService;
        this.kabupatenKotaService = kabupatenKotaService;
        this.kecamatanService = kecamatanService;
        this.bpdasService = bpdasService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<KegiatanPageDTO> getAllKegiatan(
            @RequestParam(required = false) String namaKegiatan,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) List<String> bpdasList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanPageDTO kegiatanPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((namaKegiatan != null && !namaKegiatan.isEmpty()) ||
                (status != null) ||
                (bpdasList != null)) {
            kegiatanPage = kegiatanService.findByFiltersWithCache(namaKegiatan, status, bpdasList, pageable, baseUrl);
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
            @RequestPart(required = false) String programId,
            @RequestPart String namaKegiatan,
            @RequestPart(required = false) String tahun,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String bpdasId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String pemegangIjinId,
            @RequestPart(required = false) String peruntukanId,
            @RequestPart(required = false) String namaPeruntukan,
            @RequestPart(required = false) String jenisIjinId,
            @RequestPart(required = false) String nomorSkPerijinan,
            @RequestPart(required = false) String tanggalSkPerijinan,
            @RequestPart(required = false) String tanggalBerakhirSkPerijinan,
            @RequestPart(required = false) String luasSesuaiSkPerijinanHa,
            @RequestPart(required = false) String nomorSkPbak,
            @RequestPart(required = false) String tanggalSkPbak,
            @RequestPart(required = false) String tanggalBerakhirSkPbak,
            @RequestPart(required = false) String luasSesuaiSkPbakHa,
            @RequestPart(required = false) String nomorSkLahanPengganti,
            @RequestPart(required = false) String tanggalSkLahanPengganti,
            @RequestPart(required = false) String tanggalBerakhirSkLahanPengganti,
            @RequestPart(required = false) String luasSkLahanPengganti,
            @RequestPart(required = false) String keteranganLahanPengganti,
            @RequestPart(required = false) String nomorSkRehabilitasi,
            @RequestPart(required = false) String tanggalSkRehabilitasi,
            @RequestPart(required = false) String tanggalBerakhirSkRehabilitasi,
            @RequestPart(required = false) String luasSkRehabilitasi,
            @RequestPart(required = false) String bpdasRehabId,
            @RequestPart(required = false) String nomorBastPpkhKeDirjen,
            @RequestPart(required = false) String tanggalBastPpkhKeDirjen,
            @RequestPart(required = false) String nomorBastDirjenKeDishut,
            @RequestPart(required = false) String tanggalBastDirjenKeDishut,
            @RequestPart(required = false) String nomorBastDishutKePengelola,
            @RequestPart(required = false) String tanggalBastDishutKePengelola,
            @RequestPart(required = false) String namaPic,
            @RequestPart(required = false) String nomorPic,
            @RequestPart(required = false) String emailPic) {

        try {
            Kegiatan newKegiatan = new Kegiatan();
            newKegiatan.setNamaKegiatan(namaKegiatan);
            
            if (programId != null && !programId.isEmpty()) {
                Program program = programService.findById(Long.parseLong(programId));
                newKegiatan.setProgram(program);
            }
            
            if (tahun != null && !tahun.isEmpty()) {
                newKegiatan.setTahun(Integer.parseInt(tahun));
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
            
            if (bpdasId != null && !bpdasId.isEmpty()) {
                Bpdas bpdas = bpdasService.findById(Long.parseLong(bpdasId));
                newKegiatan.setBpdas(bpdas);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Lov status = lovService.findById(Long.parseLong(statusId));
                newKegiatan.setStatus(status);
            }
            
            if (pemegangIjinId != null && !pemegangIjinId.isEmpty()) {
                Lov pemegangIjin = lovService.findById(Long.parseLong(pemegangIjinId));
                newKegiatan.setPemegangIjinId(pemegangIjin);
            }
            
            if (peruntukanId != null && !peruntukanId.isEmpty()) {
                Lov peruntukan = lovService.findById(Long.parseLong(peruntukanId));
                newKegiatan.setPeruntukanId(peruntukan);
            }
            
            newKegiatan.setNamaPeruntukan(namaPeruntukan);
            
            if (jenisIjinId != null && !jenisIjinId.isEmpty()) {
                Lov jenisIjin = lovService.findById(Long.parseLong(jenisIjinId));
                newKegiatan.setJenisIjin(jenisIjin);
            }
            
            newKegiatan.setNomorSkPerijinan(nomorSkPerijinan);
            
            if (tanggalSkPerijinan != null && !tanggalSkPerijinan.isEmpty()) {
                newKegiatan.setTanggalSkPerijinan(LocalDate.parse(tanggalSkPerijinan));
            }
            
            if (tanggalBerakhirSkPerijinan != null && !tanggalBerakhirSkPerijinan.isEmpty()) {
                newKegiatan.setTanggalBerakhirSkPerijinan(LocalDate.parse(tanggalBerakhirSkPerijinan));
            }
            
            if (luasSesuaiSkPerijinanHa != null && !luasSesuaiSkPerijinanHa.isEmpty()) {
                newKegiatan.setLuasSesuaiSkPerijinanHa(Double.parseDouble(luasSesuaiSkPerijinanHa));
            }
            
            // PBAK
            newKegiatan.setNomorSkPbak(nomorSkPbak);
            
            if (tanggalSkPbak != null && !tanggalSkPbak.isEmpty()) {
                newKegiatan.setTanggalSkPbak(LocalDate.parse(tanggalSkPbak));
            }
            
            if (tanggalBerakhirSkPbak != null && !tanggalBerakhirSkPbak.isEmpty()) {
                newKegiatan.setTanggalBerakhirSkPbak(LocalDate.parse(tanggalBerakhirSkPbak));
            }
            
            if (luasSesuaiSkPbakHa != null && !luasSesuaiSkPbakHa.isEmpty()) {
                newKegiatan.setLuasSesuaiSkPbakHa(Double.parseDouble(luasSesuaiSkPbakHa));
            }
            
            // Lahan Pengganti
            newKegiatan.setNomorSkLahanPengganti(nomorSkLahanPengganti);
            
            if (tanggalSkLahanPengganti != null && !tanggalSkLahanPengganti.isEmpty()) {
                newKegiatan.setTanggalSkLahanPengganti(LocalDate.parse(tanggalSkLahanPengganti));
            }
            
            if (tanggalBerakhirSkLahanPengganti != null && !tanggalBerakhirSkLahanPengganti.isEmpty()) {
                newKegiatan.setTanggalBerakhirSkLahanPengganti(LocalDate.parse(tanggalBerakhirSkLahanPengganti));
            }
            
            if (luasSkLahanPengganti != null && !luasSkLahanPengganti.isEmpty()) {
                newKegiatan.setLuasSkLahanPengganti(Double.parseDouble(luasSkLahanPengganti));
            }
            
            newKegiatan.setKeteranganLahanPengganti(keteranganLahanPengganti);
            
            // Rehabilitasi
            newKegiatan.setNomorSkRehabilitasi(nomorSkRehabilitasi);
            
            if (tanggalSkRehabilitasi != null && !tanggalSkRehabilitasi.isEmpty()) {
                newKegiatan.setTanggalSkRehabilitasi(LocalDate.parse(tanggalSkRehabilitasi));
            }
            
            if (tanggalBerakhirSkRehabilitasi != null && !tanggalBerakhirSkRehabilitasi.isEmpty()) {
                newKegiatan.setTanggalBerakhirSkRehabilitasi(LocalDate.parse(tanggalBerakhirSkRehabilitasi));
            }
            
            if (luasSkRehabilitasi != null && !luasSkRehabilitasi.isEmpty()) {
                newKegiatan.setLuasSkRehabilitasi(Double.parseDouble(luasSkRehabilitasi));
            }
            
            if (bpdasRehabId != null && !bpdasRehabId.isEmpty()) {
                Bpdas bpdasRehab = bpdasService.findById(Long.parseLong(bpdasRehabId));
                newKegiatan.setBpdasRehabId(bpdasRehab);
            }
            
            // BAST
            newKegiatan.setNomorBastPpkhKeDirjen(nomorBastPpkhKeDirjen);
            
            if (tanggalBastPpkhKeDirjen != null && !tanggalBastPpkhKeDirjen.isEmpty()) {
                newKegiatan.setTanggalBastPpkhKeDirjen(LocalDate.parse(tanggalBastPpkhKeDirjen));
            }
            
            newKegiatan.setNomorBastDirjenKeDishut(nomorBastDirjenKeDishut);
            
            if (tanggalBastDirjenKeDishut != null && !tanggalBastDirjenKeDishut.isEmpty()) {
                newKegiatan.setTanggalBastDirjenKeDishut(LocalDate.parse(tanggalBastDirjenKeDishut));
            }
            
            newKegiatan.setNomorBastDishutKePengelola(nomorBastDishutKePengelola);
            
            if (tanggalBastDishutKePengelola != null && !tanggalBastDishutKePengelola.isEmpty()) {
                newKegiatan.setTanggalBastDishutKePengelola(LocalDate.parse(tanggalBastDishutKePengelola));
            }
            
            // PIC
            newKegiatan.setNamaPic(namaPic);
            newKegiatan.setNomorPic(nomorPic);
            newKegiatan.setEmailPic(emailPic);

            Kegiatan savedKegiatan = kegiatanService.save(newKegiatan);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedKegiatan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Add detailed logging for troubleshooting
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Kegiatan> updateKegiatan(
            @PathVariable Long id,
            @RequestPart(required = false) String programId,
            @RequestPart String namaKegiatan,
            @RequestPart(required = false) String tahun,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String bpdasId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String pemegangIjinId,
            @RequestPart(required = false) String peruntukanId,
            @RequestPart(required = false) String namaPeruntukan,
            @RequestPart(required = false) String jenisIjinId,
            @RequestPart(required = false) String nomorSkPerijinan,
            @RequestPart(required = false) String tanggalSkPerijinan,
            @RequestPart(required = false) String tanggalBerakhirSkPerijinan,
            @RequestPart(required = false) String luasSesuaiSkPerijinanHa,
            @RequestPart(required = false) String nomorSkPbak,
            @RequestPart(required = false) String tanggalSkPbak,
            @RequestPart(required = false) String tanggalBerakhirSkPbak,
            @RequestPart(required = false) String luasSesuaiSkPbakHa,
            @RequestPart(required = false) String nomorSkLahanPengganti,
            @RequestPart(required = false) String tanggalSkLahanPengganti,
            @RequestPart(required = false) String tanggalBerakhirSkLahanPengganti,
            @RequestPart(required = false) String luasSkLahanPengganti,
            @RequestPart(required = false) String keteranganLahanPengganti,
            @RequestPart(required = false) String nomorSkRehabilitasi,
            @RequestPart(required = false) String tanggalSkRehabilitasi,
            @RequestPart(required = false) String tanggalBerakhirSkRehabilitasi,
            @RequestPart(required = false) String luasSkRehabilitasi,
            @RequestPart(required = false) String bpdasRehabId,
            @RequestPart(required = false) String nomorBastPpkhKeDirjen,
            @RequestPart(required = false) String tanggalBastPpkhKeDirjen,
            @RequestPart(required = false) String nomorBastDirjenKeDishut,
            @RequestPart(required = false) String tanggalBastDirjenKeDishut,
            @RequestPart(required = false) String nomorBastDishutKePengelola,
            @RequestPart(required = false) String tanggalBastDishutKePengelola,
            @RequestPart(required = false) String namaPic,
            @RequestPart(required = false) String nomorPic,
            @RequestPart(required = false) String emailPic) {

        try {
            Kegiatan existingKegiatan = kegiatanService.findById(id);
            
            existingKegiatan.setNamaKegiatan(namaKegiatan);
            
            if (programId != null && !programId.isEmpty()) {
                Program program = programService.findById(Long.parseLong(programId));
                existingKegiatan.setProgram(program);
            }
            
            if (tahun != null && !tahun.isEmpty()) {
                existingKegiatan.setTahun(Integer.parseInt(tahun));
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
            
            if (bpdasId != null && !bpdasId.isEmpty()) {
                Bpdas bpdas = bpdasService.findById(Long.parseLong(bpdasId));
                existingKegiatan.setBpdas(bpdas);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Lov status = lovService.findById(Long.parseLong(statusId));
                existingKegiatan.setStatus(status);
            }
            
            if (pemegangIjinId != null && !pemegangIjinId.isEmpty()) {
                Lov pemegangIjin = lovService.findById(Long.parseLong(pemegangIjinId));
                existingKegiatan.setPemegangIjinId(pemegangIjin);
            }
            
            if (peruntukanId != null && !peruntukanId.isEmpty()) {
                Lov peruntukan = lovService.findById(Long.parseLong(peruntukanId));
                existingKegiatan.setPeruntukanId(peruntukan);
            }
            
            existingKegiatan.setNamaPeruntukan(namaPeruntukan);
            
            if (jenisIjinId != null && !jenisIjinId.isEmpty()) {
                Lov jenisIjin = lovService.findById(Long.parseLong(jenisIjinId));
                existingKegiatan.setJenisIjin(jenisIjin);
            }
            
            existingKegiatan.setNomorSkPerijinan(nomorSkPerijinan);
            
            if (tanggalSkPerijinan != null && !tanggalSkPerijinan.isEmpty()) {
                existingKegiatan.setTanggalSkPerijinan(LocalDate.parse(tanggalSkPerijinan));
            }
            
            if (tanggalBerakhirSkPerijinan != null && !tanggalBerakhirSkPerijinan.isEmpty()) {
                existingKegiatan.setTanggalBerakhirSkPerijinan(LocalDate.parse(tanggalBerakhirSkPerijinan));
            }
            
            if (luasSesuaiSkPerijinanHa != null && !luasSesuaiSkPerijinanHa.isEmpty()) {
                existingKegiatan.setLuasSesuaiSkPerijinanHa(Double.parseDouble(luasSesuaiSkPerijinanHa));
            }
            
            // PBAK
            existingKegiatan.setNomorSkPbak(nomorSkPbak);
            
            if (tanggalSkPbak != null && !tanggalSkPbak.isEmpty()) {
                existingKegiatan.setTanggalSkPbak(LocalDate.parse(tanggalSkPbak));
            }
            
            if (tanggalBerakhirSkPbak != null && !tanggalBerakhirSkPbak.isEmpty()) {
                existingKegiatan.setTanggalBerakhirSkPbak(LocalDate.parse(tanggalBerakhirSkPbak));
            }
            
            if (luasSesuaiSkPbakHa != null && !luasSesuaiSkPbakHa.isEmpty()) {
                existingKegiatan.setLuasSesuaiSkPbakHa(Double.parseDouble(luasSesuaiSkPbakHa));
            }
            
            // Lahan Pengganti
            existingKegiatan.setNomorSkLahanPengganti(nomorSkLahanPengganti);
            
            if (tanggalSkLahanPengganti != null && !tanggalSkLahanPengganti.isEmpty()) {
                existingKegiatan.setTanggalSkLahanPengganti(LocalDate.parse(tanggalSkLahanPengganti));
            }
            
            if (tanggalBerakhirSkLahanPengganti != null && !tanggalBerakhirSkLahanPengganti.isEmpty()) {
                existingKegiatan.setTanggalBerakhirSkLahanPengganti(LocalDate.parse(tanggalBerakhirSkLahanPengganti));
            }
            
            if (luasSkLahanPengganti != null && !luasSkLahanPengganti.isEmpty()) {
                existingKegiatan.setLuasSkLahanPengganti(Double.parseDouble(luasSkLahanPengganti));
            }
            
            existingKegiatan.setKeteranganLahanPengganti(keteranganLahanPengganti);
            
            // Rehabilitasi
            existingKegiatan.setNomorSkRehabilitasi(nomorSkRehabilitasi);
            
            if (tanggalSkRehabilitasi != null && !tanggalSkRehabilitasi.isEmpty()) {
                existingKegiatan.setTanggalSkRehabilitasi(LocalDate.parse(tanggalSkRehabilitasi));
            }
            
            if (tanggalBerakhirSkRehabilitasi != null && !tanggalBerakhirSkRehabilitasi.isEmpty()) {
                existingKegiatan.setTanggalBerakhirSkRehabilitasi(LocalDate.parse(tanggalBerakhirSkRehabilitasi));
            }
            
            if (luasSkRehabilitasi != null && !luasSkRehabilitasi.isEmpty()) {
                existingKegiatan.setLuasSkRehabilitasi(Double.parseDouble(luasSkRehabilitasi));
            }
            
            if (bpdasRehabId != null && !bpdasRehabId.isEmpty()) {
                Bpdas bpdasRehab = bpdasService.findById(Long.parseLong(bpdasRehabId));
                existingKegiatan.setBpdasRehabId(bpdasRehab);
            }
            
            // BAST
            existingKegiatan.setNomorBastPpkhKeDirjen(nomorBastPpkhKeDirjen);
            
            if (tanggalBastPpkhKeDirjen != null && !tanggalBastPpkhKeDirjen.isEmpty()) {
                existingKegiatan.setTanggalBastPpkhKeDirjen(LocalDate.parse(tanggalBastPpkhKeDirjen));
            }
            
            existingKegiatan.setNomorBastDirjenKeDishut(nomorBastDirjenKeDishut);
            
            if (tanggalBastDirjenKeDishut != null && !tanggalBastDirjenKeDishut.isEmpty()) {
                existingKegiatan.setTanggalBastDirjenKeDishut(LocalDate.parse(tanggalBastDirjenKeDishut));
            }
            
            existingKegiatan.setNomorBastDishutKePengelola(nomorBastDishutKePengelola);
            
            if (tanggalBastDishutKePengelola != null && !tanggalBastDishutKePengelola.isEmpty()) {
                existingKegiatan.setTanggalBastDishutKePengelola(LocalDate.parse(tanggalBastDishutKePengelola));
            }
            
            // PIC
            existingKegiatan.setNamaPic(namaPic);
            existingKegiatan.setNomorPic(nomorPic);
            existingKegiatan.setEmailPic(emailPic);

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

    @PostMapping(value = "/{id}/perijinan-files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload Perijinan PDF files")
    public ResponseEntity<?> uploadPerijinanFiles(
            @PathVariable Long id,
            @RequestPart(value = "perijinanPdfs", required = false) List<MultipartFile> perijinanPdfs) {
        try {
            if (perijinanPdfs != null) {
                Kegiatan kegiatan = kegiatanService.uploadPerijinanPdf(id, perijinanPdfs);
                return ResponseEntity.ok(kegiatan);
            }
            return ResponseEntity.badRequest().body("No files were uploaded");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload files: " + e.getMessage());
        }
    }

    @PostMapping(value = "/{id}/pbak-files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload PBAK PDF and SHP files")
    public ResponseEntity<?> uploadPbakFiles(
            @PathVariable Long id,
            @RequestPart(value = "pbakPdfShp", required = false) List<MultipartFile> pbakPdfShp) {
        try {
            if (pbakPdfShp != null) {
                Kegiatan kegiatan = kegiatanService.uploadPbakPdfShp(id, pbakPdfShp);
                return ResponseEntity.ok(kegiatan);
            }
            return ResponseEntity.badRequest().body("No files were uploaded");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload files: " + e.getMessage());
        }
    }

    @PostMapping(value = "/{id}/rehab-files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload Rehabilitasi PDF files")
    public ResponseEntity<?> uploadRehabFiles(
            @PathVariable Long id,
            @RequestPart(value = "rehabPdfs", required = false) List<MultipartFile> rehabPdfs) {
        try {
            if (rehabPdfs != null) {
                Kegiatan kegiatan = kegiatanService.uploadRehabPdf(id, rehabPdfs);
                return ResponseEntity.ok(kegiatan);
            }
            return ResponseEntity.badRequest().body("No files were uploaded");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload files: " + e.getMessage());
        }
    }

    @PostMapping(value = "/{id}/bast-files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload BAST ZIP files")
    public ResponseEntity<?> uploadBastFiles(
            @PathVariable Long id,
            @RequestPart(value = "bastZips", required = false) List<MultipartFile> bastZips) {
        try {
            if (bastZips != null) {
                Kegiatan kegiatan = kegiatanService.uploadBastZip(id, bastZips);
                return ResponseEntity.ok(kegiatan);
            }
            return ResponseEntity.badRequest().body("No files were uploaded");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload files: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/files")
    @Operation(summary = "Delete files for Kegiatan")
    public ResponseEntity<?> deleteFiles(
            @PathVariable Long id,
            @RequestBody KegiatanDeleteFilesRequest filesRequest) {
        try {
            Kegiatan kegiatan = kegiatanService.findById(id);
            
            if (filesRequest.getPerijinanPdfIds() != null && !filesRequest.getPerijinanPdfIds().isEmpty()) {
                kegiatanService.deletePerijinanPdf(id, filesRequest.getPerijinanPdfIds());
            }
            
            if (filesRequest.getPbakPdfShpIds() != null && !filesRequest.getPbakPdfShpIds().isEmpty()) {
                kegiatanService.deletePbakPdfShp(id, filesRequest.getPbakPdfShpIds());
            }
            
            if (filesRequest.getRehabPdfIds() != null && !filesRequest.getRehabPdfIds().isEmpty()) {
                kegiatanService.deleteRehabPdf(id, filesRequest.getRehabPdfIds());
            }
            
            if (filesRequest.getBastZipIds() != null && !filesRequest.getBastZipIds().isEmpty()) {
                kegiatanService.deleteBastZip(id, filesRequest.getBastZipIds());
            }
            
            kegiatan = kegiatanService.findById(id);
            return ResponseEntity.ok(kegiatan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete files: " + e.getMessage());
        }
    }
}