package com.kehutanan.tktrh.bkta.io.controller;

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

import com.kehutanan.tktrh.bkta.io.dto.ImmediateOutcomePageDTO;
import com.kehutanan.tktrh.bkta.io.model.ImmediateOutcome;
import com.kehutanan.tktrh.bkta.io.model.dto.ImmediateOutcomeDTO;
import com.kehutanan.tktrh.bkta.io.service.ImmediateOutcomeService;
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

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/bkta/immediate-outcome")
public class ImmediateOutcomeController {

    private final ImmediateOutcomeService service;
    private final BpdasService bpdasService;
    private final ProvinsiService provinsiService;
    private final KabupatenKotaService kabupatenKotaService;
    private final KecamatanService kecamatanService;
    private final LovService lovService;
    private final PagedResourcesAssembler<ImmediateOutcome> pagedResourcesAssembler;

    @Autowired
    public ImmediateOutcomeController(
            ImmediateOutcomeService service,
            BpdasService bpdasService,
            ProvinsiService provinsiService,
            KabupatenKotaService kabupatenKotaService,
            KecamatanService kecamatanService,
            LovService lovService,
            PagedResourcesAssembler<ImmediateOutcome> pagedResourcesAssembler) {
        this.service = service;
        this.bpdasService = bpdasService;
        this.provinsiService = provinsiService;
        this.kabupatenKotaService = kabupatenKotaService;
        this.kecamatanService = kecamatanService;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<ImmediateOutcomePageDTO> getAllImmediateOutcomes(
            @RequestParam(required = false) String kegiatan,
            @RequestParam(required = false) Integer tahun,
            @RequestParam(required = false) List<String> bpdas,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        ImmediateOutcomePageDTO outcomePageDTO;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((kegiatan != null && !kegiatan.isEmpty()) || (tahun != null) || (bpdas != null && !bpdas.isEmpty())) {
            outcomePageDTO = service.findByFiltersWithCache(kegiatan, tahun, bpdas, pageable, baseUrl);
        } else {
            outcomePageDTO = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(outcomePageDTO);
    }

    @GetMapping("/search")
    @Operation(summary = "Search ImmediateOutcome by keyword")
    public ResponseEntity<ImmediateOutcomePageDTO> searchImmediateOutcomes(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        ImmediateOutcomePageDTO outcomePageDTO = service.searchWithCache(keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(outcomePageDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImmediateOutcomeDTO> getImmediateOutcomeById(@PathVariable Long id) {
        try {
            ImmediateOutcomeDTO immediateOutcomeDTO = service.findDTOById(id);
            return ResponseEntity.ok(immediateOutcomeDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ImmediateOutcome> createImmediateOutcome(
            @RequestPart(required = false) String dta,
            @RequestPart String namaKegiatan,
            @RequestPart Integer tahunKegiatan,
            @RequestPart(required = false) String spatialX,
            @RequestPart(required = false) String spatialY,
            @RequestPart(required = false) String volumeSedimen,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String bpdasId,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String kondisiId,
            @RequestPart(required = false) String efektivitasId,
            @RequestPart(required = false) String rancanganTeknisId) {

        try {
            ImmediateOutcome newImmediateOutcome = new ImmediateOutcome();
            newImmediateOutcome.setDta(dta);
            newImmediateOutcome.setNamaKegiatan(namaKegiatan);
            newImmediateOutcome.setTahunKegiatan(tahunKegiatan);
            
            if (spatialX != null && !spatialX.isEmpty()) {
                newImmediateOutcome.setSpatialX(Double.parseDouble(spatialX));
            }
            
            if (spatialY != null && !spatialY.isEmpty()) {
                newImmediateOutcome.setSpatialY(Double.parseDouble(spatialY));
            }
            
            if (volumeSedimen != null && !volumeSedimen.isEmpty()) {
                newImmediateOutcome.setVolumeSedimen(Double.parseDouble(volumeSedimen));
            }
            
            newImmediateOutcome.setKeterangan(keterangan);
            
            // Set relations if IDs are provided
            if (bpdasId != null && !bpdasId.isEmpty()) {
                Bpdas bpdas = bpdasService.findById(Long.parseLong(bpdasId));
                newImmediateOutcome.setBpdas(bpdas);
            }
            
            if (provinsiId != null && !provinsiId.isEmpty()) {
                Provinsi provinsi = provinsiService.findById(Long.parseLong(provinsiId));
                newImmediateOutcome.setProvinsi(provinsi);
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(Long.parseLong(kabupatenKotaId));
                newImmediateOutcome.setKabupatenKota(kabupatenKota);
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Kecamatan kecamatan = kecamatanService.findById(Long.parseLong(kecamatanId));
                newImmediateOutcome.setKecamatan(kecamatan);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Lov status = lovService.findById(Long.parseLong(statusId));
                newImmediateOutcome.setStatus(status);
            }
            
            if (kondisiId != null && !kondisiId.isEmpty()) {
                Lov kondisi = lovService.findById(Long.parseLong(kondisiId));
                newImmediateOutcome.setKondisi(kondisi);
            }
            
            if (efektivitasId != null && !efektivitasId.isEmpty()) {
                Lov efektivitas = lovService.findById(Long.parseLong(efektivitasId));
                newImmediateOutcome.setEfektivitas(efektivitas);
            }
            
            if (rancanganTeknisId != null && !rancanganTeknisId.isEmpty()) {
                Lov rancanganTeknis = lovService.findById(Long.parseLong(rancanganTeknisId));
                newImmediateOutcome.setRancanganTeknis(rancanganTeknis);
            }

            ImmediateOutcome savedImmediateOutcome = service.save(newImmediateOutcome);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedImmediateOutcome);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImmediateOutcome> updateImmediateOutcome(
            @PathVariable Long id,
            @RequestPart(required = false) String dta,
            @RequestPart String namaKegiatan,
            @RequestPart Integer tahunKegiatan,
            @RequestPart(required = false) String spatialX,
            @RequestPart(required = false) String spatialY,
            @RequestPart(required = false) String volumeSedimen,
            @RequestPart(required = false) String keterangan,
            @RequestPart(required = false) String bpdasId,
            @RequestPart(required = false) String provinsiId,
            @RequestPart(required = false) String kabupatenKotaId,
            @RequestPart(required = false) String kecamatanId,
            @RequestPart(required = false) String statusId,
            @RequestPart(required = false) String kondisiId,
            @RequestPart(required = false) String efektivitasId,
            @RequestPart(required = false) String rancanganTeknisId) {

        try {
            ImmediateOutcome existingImmediateOutcome = service.findById(id);
            
            existingImmediateOutcome.setDta(dta);
            existingImmediateOutcome.setNamaKegiatan(namaKegiatan);
            existingImmediateOutcome.setTahunKegiatan(tahunKegiatan);
            
            if (spatialX != null && !spatialX.isEmpty()) {
                existingImmediateOutcome.setSpatialX(Double.parseDouble(spatialX));
            }
            
            if (spatialY != null && !spatialY.isEmpty()) {
                existingImmediateOutcome.setSpatialY(Double.parseDouble(spatialY));
            }
            
            if (volumeSedimen != null && !volumeSedimen.isEmpty()) {
                existingImmediateOutcome.setVolumeSedimen(Double.parseDouble(volumeSedimen));
            }
            
            existingImmediateOutcome.setKeterangan(keterangan);
            
            // Update relations if IDs are provided
            if (bpdasId != null && !bpdasId.isEmpty()) {
                Bpdas bpdas = bpdasService.findById(Long.parseLong(bpdasId));
                existingImmediateOutcome.setBpdas(bpdas);
            }
            
            if (provinsiId != null && !provinsiId.isEmpty()) {
                Provinsi provinsi = provinsiService.findById(Long.parseLong(provinsiId));
                existingImmediateOutcome.setProvinsi(provinsi);
            }
            
            if (kabupatenKotaId != null && !kabupatenKotaId.isEmpty()) {
                KabupatenKota kabupatenKota = kabupatenKotaService.findById(Long.parseLong(kabupatenKotaId));
                existingImmediateOutcome.setKabupatenKota(kabupatenKota);
            }
            
            if (kecamatanId != null && !kecamatanId.isEmpty()) {
                Kecamatan kecamatan = kecamatanService.findById(Long.parseLong(kecamatanId));
                existingImmediateOutcome.setKecamatan(kecamatan);
            }
            
            if (statusId != null && !statusId.isEmpty()) {
                Lov status = lovService.findById(Long.parseLong(statusId));
                existingImmediateOutcome.setStatus(status);
            }
            
            if (kondisiId != null && !kondisiId.isEmpty()) {
                Lov kondisi = lovService.findById(Long.parseLong(kondisiId));
                existingImmediateOutcome.setKondisi(kondisi);
            }
            
            if (efektivitasId != null && !efektivitasId.isEmpty()) {
                Lov efektivitas = lovService.findById(Long.parseLong(efektivitasId));
                existingImmediateOutcome.setEfektivitas(efektivitas);
            }
            
            if (rancanganTeknisId != null && !rancanganTeknisId.isEmpty()) {
                Lov rancanganTeknis = lovService.findById(Long.parseLong(rancanganTeknisId));
                existingImmediateOutcome.setRancanganTeknis(rancanganTeknis);
            }

            ImmediateOutcome updatedImmediateOutcome = service.update(id, existingImmediateOutcome);
            return ResponseEntity.ok(updatedImmediateOutcome);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImmediateOutcome(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
