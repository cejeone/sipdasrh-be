package com.kehutanan.rm.kegiatan.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kehutanan.rm.kegiatan.dto.KegiatanPemeliharaanSulamanPageDTO;
import com.kehutanan.rm.kegiatan.model.Kegiatan;
import com.kehutanan.rm.kegiatan.model.KegiatanPemeliharaanSulaman;
import com.kehutanan.rm.kegiatan.model.dto.KegiatanPemeliharaanSulamanDTO;
import com.kehutanan.rm.kegiatan.service.KegiatanPemeliharaanSulamanService;
import com.kehutanan.rm.kegiatan.service.KegiatanService;
import com.kehutanan.rm.master.model.Lov;
import com.kehutanan.rm.master.service.LovService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/kegiatan-pemeliharaan-sulaman")
public class KegiatanPemeliharaanSulamanController {

    private final KegiatanPemeliharaanSulamanService service;
    private final KegiatanService kegiatanService;
    private final LovService lovService;
    private final PagedResourcesAssembler<KegiatanPemeliharaanSulaman> pagedResourcesAssembler;

    @Autowired
    public KegiatanPemeliharaanSulamanController(
            KegiatanPemeliharaanSulamanService service,
            KegiatanService kegiatanService,
            LovService lovService,
            PagedResourcesAssembler<KegiatanPemeliharaanSulaman> pagedResourcesAssembler) {
        this.service = service;
        this.kegiatanService = kegiatanService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<KegiatanPemeliharaanSulamanPageDTO> getAllKegiatanPemeliharaanSulaman(
            @RequestParam(required = true) Long kegiatanId,
            @RequestParam(required = false) String Keterangan,
            @RequestParam(required = false) List<String> sumberBibitId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanPemeliharaanSulamanPageDTO sulamanPage;
        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if (kegiatanId != null || Keterangan != null || sumberBibitId != null ) {
            sulamanPage = service.findByFiltersWithCache(
                    kegiatanId,Keterangan, sumberBibitId, pageable, baseUrl);
        } else {
            sulamanPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(sulamanPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search KegiatanPemeliharaanSulaman by keyword")
    public ResponseEntity<KegiatanPemeliharaanSulamanPageDTO> searchKegiatanPemeliharaanSulaman(
            @RequestPart(required = true) Long kegiatanId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        KegiatanPemeliharaanSulamanPageDTO sulamanPage = service.searchWithCache(kegiatanId, keyWord, pageable,
                request.getRequestURL().toString());
        return ResponseEntity.ok(sulamanPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KegiatanPemeliharaanSulamanDTO> getKegiatanPemeliharaanSulamanById(@PathVariable Long id) {
        try {
            KegiatanPemeliharaanSulamanDTO sulamanDTO = service.findDTOById(id);
            return ResponseEntity.ok(sulamanDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<KegiatanPemeliharaanSulaman> createKegiatanPemeliharaanSulaman(
            @RequestPart String kegiatanId,
            @RequestPart(required = false) String kategoriId,
            @RequestPart(required = false) String namaBibitId,
            @RequestPart(required = false) String sumberBibitId,
            @RequestPart(required = false) String kondisiTanamanId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String waktuPenyulaman,
            @RequestPart(required = false) String jumlahBibit,
            @RequestPart(required = false) String jumlahTanamanHidup,
            @RequestPart(required = false) String jumlahHokPerempuan,
            @RequestPart(required = false) String jumlahHokLakiLaki,
            @RequestPart(required = false) String keterangan) {

        try {
            KegiatanPemeliharaanSulaman newSulaman = new KegiatanPemeliharaanSulaman();

            // Set required field - kegiatan
            Long kegiatanIdLong = Long.parseLong(kegiatanId);
            Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
            newSulaman.setKegiatan(kegiatan);

            // Set optional fields
            if (kategoriId != null && !kategoriId.isEmpty()) {
                Long lovId = Long.parseLong(kategoriId);
                Lov kategoriLov = lovService.findById(lovId);
                newSulaman.setKategoriId(kategoriLov);
            }

            if (namaBibitId != null && !namaBibitId.isEmpty()) {
                Long lovId = Long.parseLong(namaBibitId);
                Lov namaBibitLov = lovService.findById(lovId);
                newSulaman.setNamaBibitId(namaBibitLov);
            }

            if (sumberBibitId != null && !sumberBibitId.isEmpty()) {
                Long lovId = Long.parseLong(sumberBibitId);
                Lov sumberBibitLov = lovService.findById(lovId);
                newSulaman.setSumberBibitId(sumberBibitLov);
            }

            if (kondisiTanamanId != null && !kondisiTanamanId.isEmpty()) {
                Long lovId = Long.parseLong(kondisiTanamanId);
                Lov kondisiTanamanLov = lovService.findById(lovId);
                newSulaman.setKondisiTanamanId(kondisiTanamanLov);
            }

            if (statusId != null && !statusId.isEmpty()) {
                Long lovId = Long.parseLong(statusId);
                Lov statusLov = lovService.findById(lovId);
                newSulaman.setStatusId(statusLov);
            }

            if (waktuPenyulaman != null && !waktuPenyulaman.isEmpty()) {
                LocalDate date = LocalDate.parse(waktuPenyulaman);
                newSulaman.setWaktuPenyulaman(date);
            }

            if (jumlahBibit != null && !jumlahBibit.isEmpty()) {
                newSulaman.setJumlahBibit(Integer.parseInt(jumlahBibit));
            }

            if (jumlahTanamanHidup != null && !jumlahTanamanHidup.isEmpty()) {
                newSulaman.setJumlahTanamanHidup(Integer.parseInt(jumlahTanamanHidup));
            }

            if (jumlahHokPerempuan != null && !jumlahHokPerempuan.isEmpty()) {
                newSulaman.setJumlahHokPerempuan(Integer.parseInt(jumlahHokPerempuan));
            }

            if (jumlahHokLakiLaki != null && !jumlahHokLakiLaki.isEmpty()) {
                newSulaman.setJumlahHokLakiLaki(Integer.parseInt(jumlahHokLakiLaki));
            }

            newSulaman.setKeterangan(keterangan);

            KegiatanPemeliharaanSulaman savedSulaman = service.save(newSulaman);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSulaman);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace(); // Log stack trace to console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KegiatanPemeliharaanSulaman> updateKegiatanPemeliharaanSulaman(
            @PathVariable Long id,
            @RequestPart String kegiatanId,
            @RequestPart(required = false) String kategoriId,
            @RequestPart(required = false) String namaBibitId,
            @RequestPart(required = false) String sumberBibitId,
            @RequestPart(required = false) String kondisiTanamanId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String waktuPenyulaman,
            @RequestPart(required = false) String jumlahBibit,
            @RequestPart(required = false) String jumlahTanamanHidup,
            @RequestPart(required = false) String jumlahHokPerempuan,
            @RequestPart(required = false) String jumlahHokLakiLaki,
            @RequestPart(required = false) String keterangan) {

        try {
            KegiatanPemeliharaanSulaman existingSulaman = service.findById(id);

            // Update required field - kegiatan
            Long kegiatanIdLong = Long.parseLong(kegiatanId);
            Kegiatan kegiatan = kegiatanService.findById(kegiatanIdLong);
            existingSulaman.setKegiatan(kegiatan);

            // Update optional fields
            if (kategoriId != null && !kategoriId.isEmpty()) {
                Long lovId = Long.parseLong(kategoriId);
                Lov kategoriLov = lovService.findById(lovId);
                existingSulaman.setKategoriId(kategoriLov);
            }

            if (namaBibitId != null && !namaBibitId.isEmpty()) {
                Long lovId = Long.parseLong(namaBibitId);
                Lov namaBibitLov = lovService.findById(lovId);
                existingSulaman.setNamaBibitId(namaBibitLov);
            }

            if (sumberBibitId != null && !sumberBibitId.isEmpty()) {
                Long lovId = Long.parseLong(sumberBibitId);
                Lov sumberBibitLov = lovService.findById(lovId);
                existingSulaman.setSumberBibitId(sumberBibitLov);
            }

            if (kondisiTanamanId != null && !kondisiTanamanId.isEmpty()) {
                Long lovId = Long.parseLong(kondisiTanamanId);
                Lov kondisiTanamanLov = lovService.findById(lovId);
                existingSulaman.setKondisiTanamanId(kondisiTanamanLov);
            }

            if (statusId != null && !statusId.isEmpty()) {
                Long lovId = Long.parseLong(statusId);
                Lov statusLov = lovService.findById(lovId);
                existingSulaman.setStatusId(statusLov);
            }

            if (waktuPenyulaman != null && !waktuPenyulaman.isEmpty()) {
                LocalDate date = LocalDate.parse(waktuPenyulaman);
                existingSulaman.setWaktuPenyulaman(date);
            }

            if (jumlahBibit != null && !jumlahBibit.isEmpty()) {
                existingSulaman.setJumlahBibit(Integer.parseInt(jumlahBibit));
            }

            if (jumlahTanamanHidup != null && !jumlahTanamanHidup.isEmpty()) {
                existingSulaman.setJumlahTanamanHidup(Integer.parseInt(jumlahTanamanHidup));
            }

            if (jumlahHokPerempuan != null && !jumlahHokPerempuan.isEmpty()) {
                existingSulaman.setJumlahHokPerempuan(Integer.parseInt(jumlahHokPerempuan));
            }

            if (jumlahHokLakiLaki != null && !jumlahHokLakiLaki.isEmpty()) {
                existingSulaman.setJumlahHokLakiLaki(Integer.parseInt(jumlahHokLakiLaki));
            }

            existingSulaman.setKeterangan(keterangan);

            KegiatanPemeliharaanSulaman updatedSulaman = service.update(id, existingSulaman);
            return ResponseEntity.ok(updatedSulaman);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKegiatanPemeliharaanSulaman(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}