package com.kehutanan.tktrh.ppkh.program.controller;

import java.util.ArrayList;
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

import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.service.LovService;
import com.kehutanan.tktrh.ppkh.program.dto.ProgramPageDTO;
import com.kehutanan.tktrh.ppkh.program.model.Program;
import com.kehutanan.tktrh.ppkh.program.model.ProgramPaguAnggaran;
import com.kehutanan.tktrh.ppkh.program.model.dto.ProgramDTO;
import com.kehutanan.tktrh.ppkh.program.service.ProgramService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/program")
public class ProgramController {

    private final ProgramService service;
    private final LovService lovService;
    private final PagedResourcesAssembler<Program> pagedResourcesAssembler;

    @Autowired
    public ProgramController(
            ProgramService service,
            LovService lovService,
            PagedResourcesAssembler<Program> pagedResourcesAssembler) {
        this.service = service;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<ProgramPageDTO> getAllPrograms(
            @RequestParam(required = false) String namaProgram,
            @RequestParam(required = false) String anggaran,
            @RequestParam(required = false) List<String> status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        ProgramPageDTO programPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((namaProgram != null && !namaProgram.isEmpty()) ||
                (anggaran != null && !anggaran.isEmpty()) ||
                (status != null && !status.isEmpty())) {
            programPage = service.findByFiltersWithCache(namaProgram, anggaran, status, pageable, baseUrl);
        } else {
            programPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(programPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Program by keywords")
    public ResponseEntity<ProgramPageDTO> searchPrograms(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        ProgramPageDTO programPage = service.searchWithCache(keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(programPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramDTO> getProgramById(@PathVariable Long id) {
        try {
            ProgramDTO programDTO = service.findDTOById(id);
            return ResponseEntity.ok(programDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Program> createProgram(
            @RequestPart String nama,
            @RequestPart(required = false) String tahunPelaksana,
            @RequestPart(required = false) String totalAnggaran,
            @RequestPart(required = false) String totalLuas,
            @RequestPart(required = false) String statusId) {

        try {
            Program newProgram = new Program();
            newProgram.setNama(nama);
            
            if (tahunPelaksana != null && !tahunPelaksana.isEmpty()) {
                newProgram.setTahunPelaksana(Integer.parseInt(tahunPelaksana));
            }
            
            if (totalAnggaran != null && !totalAnggaran.isEmpty()) {
                newProgram.setTotalAnggaran(Double.parseDouble(totalAnggaran));
            }
            
            if (totalLuas != null && !totalLuas.isEmpty()) {
                newProgram.setTotalLuas(Integer.parseInt(totalLuas));
            }

            // Set status if ID is provided
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                newProgram.setStatus(status);
            }
            
            // Initialize empty pagu anggaran list
            newProgram.setPaguAnggarans(new ArrayList<>());

            Program savedProgram = service.save(newProgram);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProgram);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Program> updateProgram(
            @PathVariable Long id,
            @RequestPart String nama,
            @RequestPart(required = false) String tahunPelaksana,
            @RequestPart(required = false) String totalAnggaran,
            @RequestPart(required = false) String totalLuas,
            @RequestPart(required = false) String statusId) {

        try {
            Program existingProgram = service.findById(id);
            
            existingProgram.setNama(nama);
            
            if (tahunPelaksana != null && !tahunPelaksana.isEmpty()) {
                existingProgram.setTahunPelaksana(Integer.parseInt(tahunPelaksana));
            }
            
            if (totalAnggaran != null && !totalAnggaran.isEmpty()) {
                existingProgram.setTotalAnggaran(Double.parseDouble(totalAnggaran));
            }
            
            if (totalLuas != null && !totalLuas.isEmpty()) {
                existingProgram.setTotalLuas(Integer.parseInt(totalLuas));
            }

            // Update status if ID is provided
            if (statusId != null && !statusId.isEmpty()) {
                Long statusIdLong = Long.parseLong(statusId);
                Lov status = lovService.findById(statusIdLong);
                existingProgram.setStatus(status);
            }

            Program updatedProgram = service.update(id, existingProgram);
            return ResponseEntity.ok(updatedProgram);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
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