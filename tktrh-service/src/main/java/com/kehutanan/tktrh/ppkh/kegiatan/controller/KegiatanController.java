package com.kehutanan.tktrh.ppkh.kegiatan.controller;

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

import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanLokus;
import com.kehutanan.tktrh.bkta.program.model.Program;
import com.kehutanan.tktrh.master.model.Bpdas;
import com.kehutanan.tktrh.master.model.Eselon3;
import com.kehutanan.tktrh.master.model.KabupatenKota;
import com.kehutanan.tktrh.master.model.Kecamatan;
import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.model.Provinsi;
import com.kehutanan.tktrh.ppkh.kegiatan.dto.KegiatanDeleteFilesRequest;
import com.kehutanan.tktrh.ppkh.kegiatan.dto.KegiatanPageDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.ppkh.kegiatan.model.dto.KegiatanDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.service.KegiatanService;
import com.kehutanan.tktrh.bkta.program.service.ProgramService;
import com.kehutanan.tktrh.master.service.BpdasService;
import com.kehutanan.tktrh.master.service.Eselon3Service;
import com.kehutanan.tktrh.master.service.KabupatenKotaService;
import com.kehutanan.tktrh.master.service.KecamatanService;
import com.kehutanan.tktrh.master.service.LovService;
import com.kehutanan.tktrh.master.service.ProvinsiService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/kegiatan")
public class KegiatanController {

    private final KegiatanService kegiatanService;
    private final ProgramService programService;
    private final Eselon3Service eselon3Service;
    private final ProvinsiService provinsiService;
    private final KabupatenKotaService kabupatenKotaService;
    private final KecamatanService kecamatanService;
    private final LovService lovService;
    private final BpdasService bpdasService;
    private final PagedResourcesAssembler<Kegiatan> pagedResourcesAssembler;

    @Autowired
    public KegiatanController(
            KegiatanService kegiatanService,
            ProgramService programService,
            Eselon3Service eselon3Service,
            ProvinsiService provinsiService,
            KabupatenKotaService kabupatenKotaService,
            KecamatanService kecamatanService,
            LovService lovService,
            BpdasService bpdasService,
            PagedResourcesAssembler<Kegiatan> pagedResourcesAssembler) {
        this.kegiatanService = kegiatanService;
        this.programService = programService;
        this.eselon3Service = eselon3Service;
        this.provinsiService = provinsiService;
        this.kabupatenKotaService = kabupatenKotaService;
        this.kecamatanService = kecamatanService;
        this.lovService = lovService;
        this.bpdasService = bpdasService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<KegiatanPageDTO> getAllKegiatan(
            @RequestParam(required = false) String namaKegiatan,
            @RequestParam(required = false) String peruntukan,
            @RequestParam(required = false) List<String> tahunList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanPageDTO kegiatanPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((namaKegiatan != null && !namaKegiatan.isEmpty()) ||
                (peruntukan != null && !peruntukan.isEmpty()) ||
                (tahunList != null && !tahunList.isEmpty())) {
            kegiatanPage = kegiatanService.findByFiltersWithCache(namaKegiatan, peruntukan, tahunList, pageable, baseUrl);
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
            @RequestPart(required = false) String subDirektoratId,
            @RequestPart(required = false) String namaKegiatan,
            @RequestPart(required = false) String tahun,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String pemegangIjinId,
            @RequestPart(required = false) String peruntukanId,
            @RequestPart(required = false) String namaPeruntukan,
            @RequestPart(required = false) String jenisIjinId,
            @RequestPart(required = false) String nomorSkPerijinan,
            @RequestPart(required = false) String tanggalSkPerijinan,
            @RequestPart(required = false) String tanggalBerakhirSkPerijinan,
            @RequestPart(required = false) String luasSesuaiSkPerijinanHa,
            @RequestPart(required = false) String nomorSkPak,
            @RequestPart(required = false) String tanggalSkPak,
            @RequestPart(required = false) String tanggalBerakhirSkPak,
            @RequestPart(required = false) String luasSesuaiSkPakHa,
            @RequestPart(required = false) String nomorSkRehabilitasi,
            @RequestPart(required = false) String tanggalSkRehabilitasi,
            @RequestPart(required = false) String tanggalBerakhirSkRehabilitasi,
            @RequestPart(required = false) String luasSkRehabDas,
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
            Kegiatan kegiatan = new Kegiatan();
            
            // Set basic properties
            kegiatan.setNamaKegiatan(namaKegiatan);
            
            if (tahun != null && !tahun.isEmpty()) {
                kegiatan.setTahun(Integer.parseInt(tahun));
            }
            
            kegiatan.setNamaPeruntukan(namaPeruntukan);
            kegiatan.setNomorSkPerijinan(nomorSkPerijinan);
            
            if (tanggalSkPerijinan != null && !tanggalSkPerijinan.isEmpty()) {
                kegiatan.setTanggalSkPerijinan(LocalDate.parse(tanggalSkPerijinan));
            }
            
            if (tanggalBerakhirSkPerijinan != null && !tanggalBerakhirSkPerijinan.isEmpty()) {
                kegiatan.setTanggalBerakhirSkPerijinan(LocalDate.parse(tanggalBerakhirSkPerijinan));
            }
            
            if (luasSesuaiSkPerijinanHa != null && !luasSesuaiSkPerijinanHa.isEmpty()) {
                kegiatan.setLuasSesuaiSkPerijinanHa(Integer.parseInt(luasSesuaiSkPerijinanHa));
            }
            
            kegiatan.setNomorSkPak(nomorSkPak);
            
            if (tanggalSkPak != null && !tanggalSkPak.isEmpty()) {
                kegiatan.setTanggalSkPak(LocalDate.parse(tanggalSkPak));
            }
            
            if (tanggalBerakhirSkPak != null && !tanggalBerakhirSkPak.isEmpty()) {
                kegiatan.setTanggalBerakhirSkPak(LocalDate.parse(tanggalBerakhirSkPak));
            }
            
            if (luasSesuaiSkPakHa != null && !luasSesuaiSkPakHa.isEmpty()) {
                kegiatan.setLuasSesuaiSkPakHa(Integer.parseInt(luasSesuaiSkPakHa));
            }
            
            kegiatan.setNomorSkRehabilitasi(nomorSkRehabilitasi);
            
            if (tanggalSkRehabilitasi != null && !tanggalSkRehabilitasi.isEmpty()) {
                kegiatan.setTanggalSkRehabilitasi(LocalDate.parse(tanggalSkRehabilitasi));
            }
            
            if (tanggalBerakhirSkRehabilitasi != null && !tanggalBerakhirSkRehabilitasi.isEmpty()) {
                kegiatan.setTanggalBerakhirSkRehabilitasi(LocalDate.parse(tanggalBerakhirSkRehabilitasi));
            }
            
            if (luasSkRehabDas != null && !luasSkRehabDas.isEmpty()) {
                kegiatan.setLuasSkRehabDas(Integer.parseInt(luasSkRehabDas));
            }
            
            kegiatan.setNomorBastPpkhKeDirjen(nomorBastPpkhKeDirjen);
            
            if (tanggalBastPpkhKeDirjen != null && !tanggalBastPpkhKeDirjen.isEmpty()) {
                kegiatan.setTanggalBastPpkhKeDirjen(LocalDate.parse(tanggalBastPpkhKeDirjen));
            }
            
            kegiatan.setNomorBastDirjenKeDishut(nomorBastDirjenKeDishut);
            
            if (tanggalBastDirjenKeDishut != null && !tanggalBastDirjenKeDishut.isEmpty()) {
                kegiatan.setTanggalBastDirjenKeDishut(LocalDate.parse(tanggalBastDirjenKeDishut));
            }
            
            kegiatan.setNomorBastDishutKePengelola(nomorBastDishutKePengelola);
            
            if (tanggalBastDishutKePengelola != null && !tanggalBastDishutKePengelola.isEmpty()) {
                kegiatan.setTanggalBastDishutKePengelola(LocalDate.parse(tanggalBastDishutKePengelola));
            }
            
            kegiatan.setNamaPic(namaPic);
            kegiatan.setNomorPic(nomorPic);
            kegiatan.setEmailPic(emailPic);
            
            // Set relations if IDs are provided
            if (programId != null && !programId.isEmpty()) {
                Long progId = Long.parseLong(programId);
                Program program = programService.findById(progId);
                kegiatan.setProgram(program);
            }
            
            if (subDirektoratId != null && !subDirektoratId.isEmpty()) {
                Long eselon3Id = Long.parseLong(subDirektoratId);
                Eselon3 subDirektorat = eselon3Service.findById(eselon3Id);
                kegiatan.setSubDirektorat(subDirektorat);
            }
            
            if (provinsiId != null && !provinsiId.isEmpty()) {
                Long provId = Long.parseLong(provinsiId);
                Provinsi provinsi = provinsiService.findById(provId);
                kegiatan.setProvinsi(provinsi);
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                Long kabId = Long.parseLong(kabupatenKotaId);
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(kabId);
                kegiatan.setKabupatenKota(kabupatenKota);
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Long kecId = Long.parseLong(kecamatanId);
                Kecamatan kecamatan = kecamatanService.findById(kecId);
                kegiatan.setKecamatan(kecamatan);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Long statId = Long.parseLong(statusId);
                Lov status = lovService.findById(statId);
                kegiatan.setStatus(status);
            }
            
            if (pemegangIjinId != null && !pemegangIjinId.isEmpty()) {
                Long pemegangId = Long.parseLong(pemegangIjinId);
                Lov pemegangIjin = lovService.findById(pemegangId);
                kegiatan.setPemegangIjinId(pemegangIjin);
            }
            
            if (peruntukanId != null && !peruntukanId.isEmpty()) {
                Long peruntId = Long.parseLong(peruntukanId);
                Lov peruntukan = lovService.findById(peruntId);
                kegiatan.setPeruntukanId(peruntukan);
            }
            
            if (jenisIjinId != null && !jenisIjinId.isEmpty()) {
                Long jenisId = Long.parseLong(jenisIjinId);
                Lov jenisIjin = lovService.findById(jenisId);
                kegiatan.setJenisIjinId(jenisIjin);
            }
            
            if (bpdasRehabId != null && !bpdasRehabId.isEmpty()) {
                Long bpdasId = Long.parseLong(bpdasRehabId);
                Bpdas bpdas = bpdasService.findById(bpdasId);
                kegiatan.setBpdasRehab(bpdas);
            }
            
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
            @RequestPart(required = false) String programId,
            @RequestPart(required = false) String subDirektoratId,
            @RequestPart(required = false) String namaKegiatan,
            @RequestPart(required = false) String tahun,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String pemegangIjinId,
            @RequestPart(required = false) String peruntukanId,
            @RequestPart(required = false) String namaPeruntukan,
            @RequestPart(required = false) String jenisIjinId,
            @RequestPart(required = false) String nomorSkPerijinan,
            @RequestPart(required = false) String tanggalSkPerijinan,
            @RequestPart(required = false) String tanggalBerakhirSkPerijinan,
            @RequestPart(required = false) String luasSesuaiSkPerijinanHa,
            @RequestPart(required = false) String nomorSkPak,
            @RequestPart(required = false) String tanggalSkPak,
            @RequestPart(required = false) String tanggalBerakhirSkPak,
            @RequestPart(required = false) String luasSesuaiSkPakHa,
            @RequestPart(required = false) String nomorSkRehabilitasi,
            @RequestPart(required = false) String tanggalSkRehabilitasi,
            @RequestPart(required = false) String tanggalBerakhirSkRehabilitasi,
            @RequestPart(required = false) String luasSkRehabDas,
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
            
            // Update basic properties if provided
            if (namaKegiatan != null) {
                existingKegiatan.setNamaKegiatan(namaKegiatan);
            }
            
            if (tahun != null && !tahun.isEmpty()) {
                existingKegiatan.setTahun(Integer.parseInt(tahun));
            }
            
            if (namaPeruntukan != null) {
                existingKegiatan.setNamaPeruntukan(namaPeruntukan);
            }
            
            if (nomorSkPerijinan != null) {
                existingKegiatan.setNomorSkPerijinan(nomorSkPerijinan);
            }
            
            if (tanggalSkPerijinan != null && !tanggalSkPerijinan.isEmpty()) {
                existingKegiatan.setTanggalSkPerijinan(LocalDate.parse(tanggalSkPerijinan));
            }
            
            if (tanggalBerakhirSkPerijinan != null && !tanggalBerakhirSkPerijinan.isEmpty()) {
                existingKegiatan.setTanggalBerakhirSkPerijinan(LocalDate.parse(tanggalBerakhirSkPerijinan));
            }
            
            if (luasSesuaiSkPerijinanHa != null && !luasSesuaiSkPerijinanHa.isEmpty()) {
                existingKegiatan.setLuasSesuaiSkPerijinanHa(Integer.parseInt(luasSesuaiSkPerijinanHa));
            }
            
            if (nomorSkPak != null) {
                existingKegiatan.setNomorSkPak(nomorSkPak);
            }
            
            if (tanggalSkPak != null && !tanggalSkPak.isEmpty()) {
                existingKegiatan.setTanggalSkPak(LocalDate.parse(tanggalSkPak));
            }
            
            if (tanggalBerakhirSkPak != null && !tanggalBerakhirSkPak.isEmpty()) {
                existingKegiatan.setTanggalBerakhirSkPak(LocalDate.parse(tanggalBerakhirSkPak));
            }
            
            if (luasSesuaiSkPakHa != null && !luasSesuaiSkPakHa.isEmpty()) {
                existingKegiatan.setLuasSesuaiSkPakHa(Integer.parseInt(luasSesuaiSkPakHa));
            }
            
            if (nomorSkRehabilitasi != null) {
                existingKegiatan.setNomorSkRehabilitasi(nomorSkRehabilitasi);
            }
            
            if (tanggalSkRehabilitasi != null && !tanggalSkRehabilitasi.isEmpty()) {
                existingKegiatan.setTanggalSkRehabilitasi(LocalDate.parse(tanggalSkRehabilitasi));
            }
            
            if (tanggalBerakhirSkRehabilitasi != null && !tanggalBerakhirSkRehabilitasi.isEmpty()) {
                existingKegiatan.setTanggalBerakhirSkRehabilitasi(LocalDate.parse(tanggalBerakhirSkRehabilitasi));
            }
            
            if (luasSkRehabDas != null && !luasSkRehabDas.isEmpty()) {
                existingKegiatan.setLuasSkRehabDas(Integer.parseInt(luasSkRehabDas));
            }
            
            if (nomorBastPpkhKeDirjen != null) {
                existingKegiatan.setNomorBastPpkhKeDirjen(nomorBastPpkhKeDirjen);
            }
            
            if (tanggalBastPpkhKeDirjen != null && !tanggalBastPpkhKeDirjen.isEmpty()) {
                existingKegiatan.setTanggalBastPpkhKeDirjen(LocalDate.parse(tanggalBastPpkhKeDirjen));
            }
            
            if (nomorBastDirjenKeDishut != null) {
                existingKegiatan.setNomorBastDirjenKeDishut(nomorBastDirjenKeDishut);
            }
            
            if (tanggalBastDirjenKeDishut != null && !tanggalBastDirjenKeDishut.isEmpty()) {
                existingKegiatan.setTanggalBastDirjenKeDishut(LocalDate.parse(tanggalBastDirjenKeDishut));
            }
            
            if (nomorBastDishutKePengelola != null) {
                existingKegiatan.setNomorBastDishutKePengelola(nomorBastDishutKePengelola);
            }
            
            if (tanggalBastDishutKePengelola != null && !tanggalBastDishutKePengelola.isEmpty()) {
                existingKegiatan.setTanggalBastDishutKePengelola(LocalDate.parse(tanggalBastDishutKePengelola));
            }
            
            if (namaPic != null) {
                existingKegiatan.setNamaPic(namaPic);
            }
            
            if (nomorPic != null) {
                existingKegiatan.setNomorPic(nomorPic);
            }
            
            if (emailPic != null) {
                existingKegiatan.setEmailPic(emailPic);
            }
            
            // Update relations if IDs are provided
            if (programId != null && !programId.isEmpty()) {
                Long progId = Long.parseLong(programId);
                Program program = programService.findById(progId);
                existingKegiatan.setProgram(program);
            }
            
            if (subDirektoratId != null && !subDirektoratId.isEmpty()) {
                Long eselon3Id = Long.parseLong(subDirektoratId);
                Eselon3 subDirektorat = eselon3Service.findById(eselon3Id);
                existingKegiatan.setSubDirektorat(subDirektorat);
            }
            
            if (provinsiId != null && !provinsiId.isEmpty()) {
                Long provId = Long.parseLong(provinsiId);
                Provinsi provinsi = provinsiService.findById(provId);
                existingKegiatan.setProvinsi(provinsi);
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                Long kabId = Long.parseLong(kabupatenKotaId);
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(kabId);
                existingKegiatan.setKabupatenKota(kabupatenKota);
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Long kecId = Long.parseLong(kecamatanId);
                Kecamatan kecamatan = kecamatanService.findById(kecId);
                existingKegiatan.setKecamatan(kecamatan);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Long statId = Long.parseLong(statusId);
                Lov status = lovService.findById(statId);
                existingKegiatan.setStatus(status);
            }
            
            if (pemegangIjinId != null && !pemegangIjinId.isEmpty()) {
                Long pemegangId = Long.parseLong(pemegangIjinId);
                Lov pemegangIjin = lovService.findById(pemegangId);
                existingKegiatan.setPemegangIjinId(pemegangIjin);
            }
            
            if (peruntukanId != null && !peruntukanId.isEmpty()) {
                Long peruntId = Long.parseLong(peruntukanId);
                Lov peruntukan = lovService.findById(peruntId);
                existingKegiatan.setPeruntukanId(peruntukan);
            }
            
            if (jenisIjinId != null && !jenisIjinId.isEmpty()) {
                Long jenisId = Long.parseLong(jenisIjinId);
                Lov jenisIjin = lovService.findById(jenisId);
                existingKegiatan.setJenisIjinId(jenisIjin);
            }
            
            if (bpdasRehabId != null && !bpdasRehabId.isEmpty()) {
                Long bpdasId = Long.parseLong(bpdasRehabId);
                Bpdas bpdas = bpdasService.findById(bpdasId);
                existingKegiatan.setBpdasRehab(bpdas);
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
            @RequestPart(value = "perijinanPdf", required = false) List<MultipartFile> perijinanPdf,
            @RequestPart(value = "riwayatSk", required = false) List<MultipartFile> riwayatSk,
            @RequestPart(value = "pakPdfShp", required = false) List<MultipartFile> pakPdfShp,
            @RequestPart(value = "fungsiKawasan", required = false) List<MultipartFile> fungsiKawasan,
            @RequestPart(value = "rantekPdf", required = false) List<MultipartFile> rantekPdf,
            @RequestPart(value = "rencanaRealisasi", required = false) List<MultipartFile> rencanaRealisasi,
            @RequestPart(value = "bastReboRehab", required = false) List<MultipartFile> bastReboRehab,
            @RequestPart(value = "bastZip", required = false) List<MultipartFile> bastZip) {
        try {
            if (perijinanPdf != null && !perijinanPdf.isEmpty()) {
                kegiatanService.uploadPerijinanPdf(id, perijinanPdf);
            }

            if (pakPdfShp != null && !pakPdfShp.isEmpty()) {
                kegiatanService.uploadPakPdfShp(id, pakPdfShp);
            }
            

            
            if (rantekPdf != null && !rantekPdf.isEmpty()) {
                kegiatanService.uploadRantekPdf(id, rantekPdf);
            }
            

            

            
            if (bastZip != null && !bastZip.isEmpty()) {
                kegiatanService.uploadBastZip(id, bastZip);
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
            @RequestBody KegiatanDeleteFilesRequest filesRequest) {
        try {
            if (filesRequest.getPerijinanPdfIds() != null && !filesRequest.getPerijinanPdfIds().isEmpty()) {
                kegiatanService.deletePerijinanPdf(id, filesRequest.getPerijinanPdfIds());
            }

            
            if (filesRequest.getPakPdfShpIds() != null && !filesRequest.getPakPdfShpIds().isEmpty()) {
                kegiatanService.deletePakPdfShp(id, filesRequest.getPakPdfShpIds());
            }
            

            
            if (filesRequest.getRantekPdfIds() != null && !filesRequest.getRantekPdfIds().isEmpty()) {
                kegiatanService.deleteRantekPdf(id, filesRequest.getRantekPdfIds());
            }
            

            if (filesRequest.getBastZipIds() != null && !filesRequest.getBastZipIds().isEmpty()) {
                kegiatanService.deleteBastZip(id, filesRequest.getBastZipIds());
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