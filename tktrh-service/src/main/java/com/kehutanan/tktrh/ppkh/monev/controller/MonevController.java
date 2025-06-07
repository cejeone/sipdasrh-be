package com.kehutanan.tktrh.ppkh.monev.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.tktrh.bkta.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.bkta.kegiatan.service.KegiatanService;
import com.kehutanan.tktrh.bkta.program.model.Program;
import com.kehutanan.tktrh.bkta.program.service.ProgramService;
import com.kehutanan.tktrh.master.model.Bpdas;
import com.kehutanan.tktrh.master.service.BpdasService;
import com.kehutanan.tktrh.ppkh.monev.dto.MonevPageDTO;
import com.kehutanan.tktrh.ppkh.monev.model.Monev;
import com.kehutanan.tktrh.ppkh.monev.model.dto.MonevDTO;
import com.kehutanan.tktrh.ppkh.monev.service.MonevService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/ppkh/ppkh/monev")
public class MonevController {

    private final MonevService service;
    private final BpdasService bpdasService;
    private final ProgramService programService;
    private final PagedResourcesAssembler<Monev> pagedResourcesAssembler;
    
    @Autowired
    public MonevController(
            MonevService service,
            BpdasService bpdasService,
            ProgramService programService,
            PagedResourcesAssembler<Monev> pagedResourcesAssembler) {
        this.service = service;
        this.bpdasService = bpdasService;
        this.programService = programService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }
    
    @GetMapping
    public ResponseEntity<MonevPageDTO> getAllMonev(
            @RequestParam(required = false) String namaProgram,
            @RequestParam(required = false) String audiensi,
            @RequestParam(required = false) List<String> bpdasList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        MonevPageDTO monevPage;
        
        String baseUrl = request.getRequestURL().toString();
        
        // Check if any filter is provided
        if ((namaProgram != null && !namaProgram.isEmpty()) ||
                (audiensi != null && !audiensi.isEmpty()) ||
                (bpdasList != null && !bpdasList.isEmpty())) {
            monevPage = service.findByFiltersWithCache(namaProgram, audiensi, bpdasList, pageable, baseUrl);
        } else {
            monevPage = service.findAllWithCache(pageable, baseUrl);
        }
        
        return ResponseEntity.ok(monevPage);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search Monev by keyword")
    public ResponseEntity<MonevPageDTO> searchMonev(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        MonevPageDTO monevPage = service.searchWithCache(keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(monevPage);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MonevDTO> getMonevById(@PathVariable Long id) {
        try {
            MonevDTO monevDTO = service.findDTOById(id);
            return ResponseEntity.ok(monevDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Monev> createMonev(
            @RequestPart(required = true) String programId,
            @RequestPart(required = true) String bpdasId,
            @RequestPart(required = false) String tanggal,
            @RequestPart(required = false) String subjek,
            @RequestPart(required = false) String audiensi,
            @RequestPart(required = false) String isuTindakLanjut) {
        
        try {
            Monev newMonev = new Monev();
            
            // Set required fields
            if (programId != null && !programId.isEmpty()) {
                Long programIdLong = Long.parseLong(programId);
                Program program = programService.findById(programIdLong);
                newMonev.setProgram(program);
            }
            
            if (bpdasId != null && !bpdasId.isEmpty()) {
                Long bpdasIdLong = Long.parseLong(bpdasId);
                Bpdas bpdas = bpdasService.findById(bpdasIdLong);
                newMonev.setBpdas(bpdas);
            }
            
            // Set optional fields
            if (tanggal != null && !tanggal.isEmpty()) {
                LocalDate date = LocalDate.parse(tanggal);
                newMonev.setTanggal(date);
            }
            
            newMonev.setSubjek(subjek);
            newMonev.setAudiensi(audiensi);
            newMonev.setIsuTindakLanjut(isuTindakLanjut);
            
            Monev savedMonev = service.save(newMonev);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMonev);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Monev> updateMonev(
            @PathVariable Long id,
            @RequestPart(required = false) String programId,
            @RequestPart(required = false) String bpdasId,
            @RequestPart(required = false) String tanggal,
            @RequestPart(required = false) String subjek,
            @RequestPart(required = false) String audiensi,
            @RequestPart(required = false) String isuTindakLanjut) {
        
        try {
            Monev existingMonev = service.findById(id);
            
            // Update fields if provided
            if (programId != null && !programId.isEmpty()) {
                Long programIdLong = Long.parseLong(programId);
                Program program = programService.findById(programIdLong);
                existingMonev.setProgram(program);
            }
            
            if (bpdasId != null && !bpdasId.isEmpty()) {
                Long bpdasIdLong = Long.parseLong(bpdasId);
                Bpdas bpdas = bpdasService.findById(bpdasIdLong);
                existingMonev.setBpdas(bpdas);
            }
            
            if (tanggal != null && !tanggal.isEmpty()) {
                LocalDate date = LocalDate.parse(tanggal);
                existingMonev.setTanggal(date);
            }
            
            if (subjek != null) {
                existingMonev.setSubjek(subjek);
            }
            
            if (audiensi != null) {
                existingMonev.setAudiensi(audiensi);
            }
            
            if (isuTindakLanjut != null) {
                existingMonev.setIsuTindakLanjut(isuTindakLanjut);
            }
            
            Monev updatedMonev = service.update(id, existingMonev);
            return ResponseEntity.ok(updatedMonev);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonev(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping(value = "/{id}/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload PDF files for Monev")
    public ResponseEntity<?> uploadPdf(
            @PathVariable Long id,
            @RequestPart(value = "files", required = true) List<MultipartFile> files) {
        try {
            Monev monev = service.uploadMonevPdf(id, files);
            return ResponseEntity.ok(monev);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload PDF files: " + e.getMessage());
        }
    }
    
    @DeleteMapping(value = "/{id}/pdf")
    @Operation(summary = "Delete PDF files from Monev")
    public ResponseEntity<?> deletePdf(
            @PathVariable Long id,
            @RequestParam(required = true) List<String> pdfIds) {
        try {
            Monev monev = service.deleteMonevPdf(id, pdfIds);
            return ResponseEntity.ok(monev);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete PDF files: " + e.getMessage());
        }
    }
}