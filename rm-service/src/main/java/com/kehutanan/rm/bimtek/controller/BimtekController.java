package com.kehutanan.rm.bimtek.controller;

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

import com.kehutanan.rm.bimtek.dto.BimtekDTO;
import com.kehutanan.rm.bimtek.dto.BimtekDeleteFilesRequest;
import com.kehutanan.rm.bimtek.dto.BimtekPageDTO;
import com.kehutanan.rm.bimtek.model.Bimtek;
import com.kehutanan.rm.bimtek.service.BimtekService;
import com.kehutanan.rm.master.model.Bpdas;
import com.kehutanan.rm.master.service.BpdasService;
import com.kehutanan.rm.program.model.Program;
import com.kehutanan.rm.program.service.ProgramService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/bimtek")
public class BimtekController {

    private final BimtekService service;
    private final BpdasService bpdasService;
    private final ProgramService programService;
    private final PagedResourcesAssembler<Bimtek> pagedResourcesAssembler;

    @Autowired
    public BimtekController(
            BimtekService service,
            BpdasService bpdasService,
            ProgramService programService,
            PagedResourcesAssembler<Bimtek> pagedResourcesAssembler) {
        this.service = service;
        this.bpdasService = bpdasService;
        this.programService = programService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<BimtekPageDTO> getAllBimtek(
            @RequestParam(required = false) String namaBimtek,
            @RequestParam(required = false) List<String> bpdasList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        BimtekPageDTO bimtekPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((namaBimtek != null && !namaBimtek.isEmpty()) ||
                (bpdasList != null && !bpdasList.isEmpty())) {
            bimtekPage = service.findByFiltersWithCache(namaBimtek, bpdasList, pageable, baseUrl);
        } else {
            bimtekPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(bimtekPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Bimtek by keyword")
    public ResponseEntity<BimtekPageDTO> searchBimtek(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        BimtekPageDTO bimtekPage = service.searchWithCache(keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(bimtekPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bimtek> getBimtekById(@PathVariable Long id) {
        try {
            Bimtek bimtek = service.findById(id);
            return ResponseEntity.ok(bimtek);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Bimtek> createBimtek(
            @RequestPart String namaBimtek,
            @RequestPart(required = false) String subjek,
            @RequestPart(required = false) String tempat,
            @RequestPart(required = false) String tanggal,
            @RequestPart(required = false) String audience,
            @RequestPart(required = false) String evaluasi,
            @RequestPart(required = false) String catatan,
            @RequestPart(required = false) String programId,
            @RequestPart(required = false) String bpdasId) {

        try {
            Bimtek newBimtek = new Bimtek();
            newBimtek.setNamaBimtek(namaBimtek);
            newBimtek.setSubjek(subjek);
            newBimtek.setTempat(tempat);

            if (tanggal != null && !tanggal.isEmpty()) {
                // Assuming tanggal is in ISO format (yyyy-MM-dd)
                newBimtek.setTanggal(LocalDate.parse(tanggal));
            }

            newBimtek.setAudience(audience);
            newBimtek.setEvaluasi(evaluasi);
            newBimtek.setCatatan(catatan);

            // Set relations if IDs are provided
            if (programId != null && !programId.isEmpty()) {
                Long programIdLong = Long.parseLong(programId);
                Program program = programService.findById(programIdLong);
                newBimtek.setProgram(program);
            }

            if (bpdasId != null && !bpdasId.isEmpty()) {
                Long bpdasIdLong = Long.parseLong(bpdasId);
                Bpdas bpdas = bpdasService.findById(bpdasIdLong);
                newBimtek.setBpdasId(bpdas);
            }

            Bimtek savedBimtek = service.save(newBimtek);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBimtek);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Add detailed logging for troubleshooting
            e.printStackTrace(); // Log stack trace to console

            // Return more specific error details in response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Consider sending a structured error response instead of null
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Bimtek> updateBimtek(
            @PathVariable Long id,
            @RequestPart String namaBimtek,
            @RequestPart(required = false) String subjek,
            @RequestPart(required = false) String tempat,
            @RequestPart(required = false) String tanggal,
            @RequestPart(required = false) String audience,
            @RequestPart(required = false) String evaluasi,
            @RequestPart(required = false) String catatan,
            @RequestPart(required = false) String programId,
            @RequestPart(required = false) String bpdasId) {

        try {
            Bimtek existingBimtek = service.findById(id);

            existingBimtek.setNamaBimtek(namaBimtek);
            existingBimtek.setSubjek(subjek);
            existingBimtek.setTempat(tempat);

            if (tanggal != null && !tanggal.isEmpty()) {
                existingBimtek.setTanggal(LocalDate.parse(tanggal));
            }

            existingBimtek.setAudience(audience);
            existingBimtek.setEvaluasi(evaluasi);
            existingBimtek.setCatatan(catatan);

            // Update relations if IDs are provided
            if (programId != null && !programId.isEmpty()) {
                Long programIdLong = Long.parseLong(programId);
                Program program = programService.findById(programIdLong);
                existingBimtek.setProgram(program);
            }

            if (bpdasId != null && !bpdasId.isEmpty()) {
                Long bpdasIdLong = Long.parseLong(bpdasId);
                Bpdas bpdas = bpdasService.findById(bpdasIdLong);
                existingBimtek.setBpdasId(bpdas);
            }

            Bimtek updatedBimtek = service.update(id, existingBimtek);
            return ResponseEntity.ok(updatedBimtek);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBimtek(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/{id}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple files for Bimtek")
    public ResponseEntity<?> uploadFiles(
            @PathVariable Long id,
            @RequestPart(value = "bimtekPdfs", required = false) List<MultipartFile> bimtekPdfs,
            @RequestPart(value = "bimtekFotos", required = false) List<MultipartFile> bimtekFotos,
            @RequestPart(value = "bimtekVideos", required = false) List<MultipartFile> bimtekVideos) {
        try {
            if (bimtekPdfs != null) {
                service.uploadBimtekPdf(id, bimtekPdfs);
            }
            if (bimtekFotos != null) {
                service.uploadBimtekFoto(id, bimtekFotos);
            }
            if (bimtekVideos != null) {
                service.uploadBimtekVideo(id, bimtekVideos);
            }

            Bimtek bimtek = service.findById(id);
            return ResponseEntity.ok(bimtek);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal mengupload file: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/files")
    @Operation(summary = "Delete multiple files for Bimtek")
    public ResponseEntity<?> deleteFiles(
            @PathVariable Long id,
            @RequestBody(required = false) BimtekDeleteFilesRequest filesRequest) {
        try {
            // Handle each file type list if provided
            if (filesRequest.getBimtekPdfIds() != null && !filesRequest.getBimtekPdfIds().isEmpty()) {
                service.deleteBimtekPdf(id, filesRequest.getBimtekPdfIds());
            }

            if (filesRequest.getBimtekFotoIds() != null && !filesRequest.getBimtekFotoIds().isEmpty()) {
                service.deleteBimtekFoto(id, filesRequest.getBimtekFotoIds());
            }

            if (filesRequest.getBimtekVideoIds() != null && !filesRequest.getBimtekVideoIds().isEmpty()) {
                service.deleteBimtekVideo(id, filesRequest.getBimtekVideoIds());
            }

            // Fetch and return the updated Bimtek entity
            Bimtek bimtek = service.findById(id);
            return ResponseEntity.ok(bimtek);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal menghapus file: " + e.getMessage());
        }
    }
}