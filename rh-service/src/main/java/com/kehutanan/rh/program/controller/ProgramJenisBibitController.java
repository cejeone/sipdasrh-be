package com.kehutanan.rh.program.controller;

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

import com.kehutanan.rh.master.repository.LovRepository;
import com.kehutanan.rh.master.service.LovService;
import com.kehutanan.rh.program.dto.ProgramJenisBibitDTO;
import com.kehutanan.rh.program.dto.ProgramJenisBibitPageDTO;
import com.kehutanan.rh.program.model.ProgramJenisBibit;
import com.kehutanan.rh.program.repository.ProgramRepository;
import com.kehutanan.rh.program.service.ProgramJenisBibitService;
import com.kehutanan.rh.program.service.ProgramService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/program-jenis-bibit")
public class ProgramJenisBibitController {

    private final ProgramJenisBibitService service;
    private final PagedResourcesAssembler<ProgramJenisBibit> pagedResourcesAssembler;
    private final LovService lovService;
    private final ProgramService programService;

    @Autowired
    public ProgramJenisBibitController(
            ProgramJenisBibitService service,
            LovService lovService,
            PagedResourcesAssembler<ProgramJenisBibit> pagedResourcesAssembler, ProgramService programService) {
        this.service = service;
        this.lovService = lovService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.programService = programService;
    }

    @GetMapping
    public ResponseEntity<ProgramJenisBibitPageDTO> getAllProgramJenisBibit(
            @RequestParam(required = false) Long programId,
            @RequestParam(required = false) String namaBibit,
            @RequestParam(required = false) List<String> kategori,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        ProgramJenisBibitPageDTO programJenisBibitPage;

        String baseUrl = request.getRequestURL().toString();

        // Check if any filter is provided
        if ((programId != null) ||
                (namaBibit != null && !namaBibit.isEmpty()) ||
                (kategori != null && !kategori.isEmpty())) {
            programJenisBibitPage = service.findByFiltersWithCache(programId, namaBibit, kategori, pageable, baseUrl);
        } else {
            programJenisBibitPage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(programJenisBibitPage);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Program Jenis Bibit")
    public ResponseEntity<ProgramJenisBibitPageDTO> searchProgramJenisBibit(
            @RequestParam(required = false) Long programId,
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        ProgramJenisBibitPageDTO programJenisBibitPage = service.searchWithCache(
                programId, keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(programJenisBibitPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramJenisBibitDTO> getProgramJenisBibitById(@PathVariable Long id) {
        try {
            ProgramJenisBibitDTO programJenisBibitDTO = service.findDTOById(id);
            return ResponseEntity.ok(programJenisBibitDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProgramJenisBibit> createProgramJenisBibit(
            @RequestPart(required = true) String programId,
            @RequestPart(required = false) String kategoriId,
            @RequestPart(required = true) String namaBibitId,
            @RequestPart(required = false) String sumberBibitId,
            @RequestPart(required = false) String jumlah,
            @RequestPart(required = false) String keterangan) {

        try {

            ProgramJenisBibit newProgramJenisBibit = new ProgramJenisBibit();

            newProgramJenisBibit.setProgram(programService.findById(Long.parseLong(programId)));
            newProgramJenisBibit.setKategoriId(lovService.findById(Long.parseLong(kategoriId)));
            newProgramJenisBibit.setNamaBibitId((lovService.findById(Long.parseLong(namaBibitId))));

            newProgramJenisBibit.setSumberBibitId(lovService.findById(Long.parseLong(sumberBibitId)));

            newProgramJenisBibit.setJumlah(Integer.parseInt(jumlah));

            newProgramJenisBibit.setKeterangan(keterangan);

            ProgramJenisBibit savedProgramJenisBibit = service.save(newProgramJenisBibit);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProgramJenisBibit);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProgramJenisBibit> updateProgramJenisBibit(
            @PathVariable Long id,
            @RequestPart(required = true) String programId,
            @RequestPart(required = false) String kategoriId,
            @RequestPart(required = true) String namaBibitId,
            @RequestPart(required = false) String sumberBibitId,
            @RequestPart(required = false) String jumlah,
            @RequestPart(required = false) String keterangan) {

        try {
            
            ProgramJenisBibit existingProgramJenisBibit = service.findById(id);
            existingProgramJenisBibit.setProgram(programService.findById(Long.parseLong(programId)));
            existingProgramJenisBibit.setKategoriId(lovService.findById(Long.parseLong(kategoriId)));
            existingProgramJenisBibit.setNamaBibitId((lovService.findById(Long.parseLong(namaBibitId))));

            existingProgramJenisBibit.setSumberBibitId(lovService.findById(Long.parseLong(sumberBibitId)));

            existingProgramJenisBibit.setJumlah(Integer.parseInt(jumlah));

            existingProgramJenisBibit.setKeterangan(keterangan);

            ProgramJenisBibit updatedProgramJenisBibit = service.update(id, existingProgramJenisBibit);
            return ResponseEntity.ok(updatedProgramJenisBibit);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgramJenisBibit(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}