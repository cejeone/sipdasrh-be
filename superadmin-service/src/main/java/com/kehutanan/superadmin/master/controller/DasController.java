package com.kehutanan.superadmin.master.controller;

import com.kehutanan.superadmin.master.service.DasService;
import com.kehutanan.superadmin.master.model.Das;
import com.kehutanan.superadmin.master.service.BpdasService;
import com.kehutanan.superadmin.master.model.Bpdas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.web.PagedResourcesAssembler;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/das")
public class DasController {

    private final DasService service;
    private final BpdasService bpdasService;
    private final PagedResourcesAssembler<Das> pagedResourcesAssembler;

    @Autowired
    public DasController(DasService service,
                        BpdasService bpdasService,
                        PagedResourcesAssembler<Das> pagedResourcesAssembler) {
        this.service = service;
        this.bpdasService = bpdasService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Das>>> getAllDas(
            @RequestParam(required = false) String namaDas,
            @RequestParam(required = false) Long bpdasId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Das> dasPage;
        
        // Check if any filter is provided
        if ((namaDas != null && !namaDas.isEmpty()) || 
            (bpdasId != null)) {
            dasPage = service.findByFilters(namaDas, bpdasId, pageable);
        } else {
            dasPage = service.findAll(pageable);
        }

        PagedModel<EntityModel<Das>> pagedModel = pagedResourcesAssembler.toModel(dasPage);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Das> getDasById(@PathVariable Long id) {
        try {
            Das das = service.findById(id);
            return ResponseEntity.ok(das);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Das> createDas(
            @RequestPart String bpdasId,
            @RequestPart String namaDas) {

        try {
            Long bpdasIdLong = Long.parseLong(bpdasId);
            Bpdas bpdas = bpdasService.findById(bpdasIdLong);
            
            Das newDas = new Das();
            newDas.setBpdas(bpdas);
            newDas.setNamaDas(namaDas);

            Das savedDas = service.save(newDas);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Das> updateDas(
            @PathVariable Long id,
            @RequestPart String bpdasId,
            @RequestPart String namaDas) {

        try {
            Long bpdasIdLong = Long.parseLong(bpdasId);
            Das existingDas = service.findById(id);
            Bpdas bpdas = bpdasService.findById(bpdasIdLong);
            
            // Update the existing das with new values
            existingDas.setBpdas(bpdas);
            existingDas.setNamaDas(namaDas);

            Das updatedDas = service.update(id, existingDas);
            return ResponseEntity.ok(updatedDas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDas(@PathVariable Long id) {
        try {
            service.findById(id); // Check if exists
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}