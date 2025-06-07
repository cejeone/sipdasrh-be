package com.kehutanan.ppth.penghijauan.program.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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

import com.kehutanan.ppth.master.model.Eselon2;
import com.kehutanan.ppth.master.service.Eselon2Service;
import com.kehutanan.ppth.master.service.LovService;
import com.kehutanan.ppth.penghijauan.program.dto.ProgramPageDTO;
import com.kehutanan.ppth.penghijauan.program.model.Program;
import com.kehutanan.ppth.penghijauan.program.service.ProgramService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController("penghijauanProgramController")
@RequestMapping("/api/program")
public class ProgramController {

    private final ProgramService service;
    private final Eselon2Service eselon2Service;
    private final LovService lovService;
    private final PagedResourcesAssembler<Program> pagedResourcesAssembler;

    @Autowired
    public ProgramController(ProgramService service, Eselon2Service eselon2Service, LovService lovService,
            PagedResourcesAssembler<Program> pagedResourcesAssembler) {
        this.service = service;
        this.eselon2Service = eselon2Service;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
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

    @GetMapping("/s")
    public ResponseEntity<PagedModel<EntityModel<Program>>> getAllProgramByHateoas(
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) List<String> eselon,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Program> programPage = service.findAll(pageable);

        PagedModel<EntityModel<Program>> pagedModel = pagedResourcesAssembler.toModel(programPage);
        return ResponseEntity.ok(pagedModel);
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
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String eselon2Id,
            @RequestPart(required = false) String kategoriId,
            @RequestPart(required = false) String statusId) {
        try {
            Program program = new Program();
            program.setNama(nama);
            program.setTahunRencana(Integer.parseInt(tahunRencana));
            program.setTotalAnggaran(Double.parseDouble(totalAnggaran));
            program.setTargetLuas(Double.parseDouble(targetLuas));

            program.setKategoriId(lovService.findById(Long.parseLong(kategoriId)));
            program.setStatusId(lovService.findById(Long.parseLong(statusId)));

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
            @RequestPart(required = false) String targetLuas,
            @RequestPart(required = false) String eselon2Id,
            @RequestPart(required = false) String kategoriId,
            @RequestPart(required = false) String statusId) {

        try {
            Program program = service.findById(id);

            program.setNama(nama);
            program.setTahunRencana(Integer.parseInt(tahunRencana));

            program.setTotalAnggaran(Double.parseDouble(totalAnggaran));
            program.setTargetLuas(Double.parseDouble(targetLuas));

            program.setKategoriId(lovService.findById(Long.parseLong(kategoriId)));
            program.setStatusId(lovService.findById(Long.parseLong(statusId)));

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