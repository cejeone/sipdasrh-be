package com.kehutanan.rm.program.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RestController;

import com.kehutanan.rm.master.model.Eselon2;
import com.kehutanan.rm.master.service.Eselon2Service;
import com.kehutanan.rm.master.service.LovService;
import com.kehutanan.rm.program.dto.ProgramPageDTO;
import com.kehutanan.rm.program.model.Program;
import com.kehutanan.rm.program.service.ProgramService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/program")
public class ProgramController {

    private final ProgramService service;
    private final Eselon2Service eselon2Service;
    private final LovService lovService;

    @Autowired
    public ProgramController(ProgramService service, Eselon2Service eselon2Service, LovService lovService) {
        this.service = service;
        this.eselon2Service = eselon2Service;
        this.lovService = lovService;
    }

    @GetMapping
    public ResponseEntity<ProgramPageDTO> getAllProgram(
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) List<String> eselon,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        ProgramPageDTO programPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((nama != null && !nama.isEmpty()) || (eselon != null && !eselon.isEmpty())) {
            programPage = service.findByFiltersWithCache(nama, eselon, pageable, baseUrl);
        } else {
            programPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(programPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Program by keyword")
    public ResponseEntity<ProgramPageDTO> searchProgram(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        ProgramPageDTO programPage = service.searchWithCache(keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(programPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Program> getProgramById(@PathVariable Long id) {
        try {
            Program program = service.findById(id);
            return ResponseEntity.ok(program);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Program> createProgram(
            @RequestPart String nama,
            @RequestPart(required = false) String tahunRencana,
            @RequestPart(required = false) String totalAnggaran,
            @RequestPart(required = false) String totalBibit,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String eselon2Id,
            @RequestPart(required = false) String kategoriId,
            @RequestPart(required = false) String fungsiKawasanId,
            @RequestPart(required = false) String statusId) {
        try {
            Program program = new Program();
            program.setNama(nama);
            program.setTahunRencana(tahunRencana);
            if (totalAnggaran != null) program.setTotalAnggaran(Double.parseDouble(totalAnggaran));
            if (totalBibit != null) program.setTotalBibit(Double.parseDouble(totalBibit));
            if (targetLuas != null) program.setTargetLuas(Double.parseDouble(targetLuas));

            if (eselon2Id != null) {
                Eselon2 eselon2 = eselon2Service.findById(Long.parseLong(eselon2Id));
                program.setEselon2(eselon2);
            }
            if (kategoriId != null) program.setKategoriId(lovService.findById(Long.parseLong(kategoriId)));
            if (fungsiKawasanId != null) program.setFungsiKawasan(lovService.findById(Long.parseLong(fungsiKawasanId)));
            if (statusId != null) program.setStatusId(lovService.findById(Long.parseLong(statusId)));

            Program savedProgram = service.save(program);
            return ResponseEntity.ok(savedProgram);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Program> updateProgram(
            @PathVariable Long id,
            @RequestPart String nama,
            @RequestPart(required = false) String tahunRencana,
            @RequestPart(required = false) String totalAnggaran,
            @RequestPart(required = false) String totalBibit,
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String eselon2Id,
            @RequestPart(required = false) String kategoriId,
            @RequestPart(required = false) String fungsiKawasanId,
            @RequestPart(required = false) String statusId) {

        try {
            Program program = service.findById(id);

            program.setNama(nama);
            program.setTahunRencana(tahunRencana);
            if (totalAnggaran != null) program.setTotalAnggaran(Double.parseDouble(totalAnggaran));
            if (totalBibit != null) program.setTotalBibit(Double.parseDouble(totalBibit));
            if (targetLuas != null) program.setTargetLuas(Double.parseDouble(targetLuas));

            if (eselon2Id != null) {
                Eselon2 eselon2 = eselon2Service.findById(Long.parseLong(eselon2Id));
                program.setEselon2(eselon2);
            }
            if (kategoriId != null) program.setKategoriId(lovService.findById(Long.parseLong(kategoriId)));
            if (fungsiKawasanId != null) program.setFungsiKawasan(lovService.findById(Long.parseLong(fungsiKawasanId)));
            if (statusId != null) program.setStatusId(lovService.findById(Long.parseLong(statusId)));

            Program updatedProgram = service.update(id, program);
            return ResponseEntity.ok(updatedProgram);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}