package com.kehutanan.pepdas.geoservice.controller;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kehutanan.pepdas.geoservice.dto.GeoServicePageDTO;
import com.kehutanan.pepdas.geoservice.model.GeoService;
import com.kehutanan.pepdas.geoservice.model.dto.GeoServiceDTO;
import com.kehutanan.pepdas.geoservice.service.GeoServiceService;
import com.kehutanan.pepdas.master.model.Bpdas;
import com.kehutanan.pepdas.master.model.Eselon2;
import com.kehutanan.pepdas.master.model.Lov;
import com.kehutanan.pepdas.master.service.BpdasService;
import com.kehutanan.pepdas.master.service.Eselon2Service;
import com.kehutanan.pepdas.master.service.LovService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/geoservice")
public class GeoServiceController {

    private final GeoServiceService service;
    private final Eselon2Service eselon2Service;
    private final BpdasService bpdasService;
    private final LovService lovService;

    @Autowired
    public GeoServiceController(
            GeoServiceService service,
            Eselon2Service eselon2Service,
            BpdasService bpdasService,
            LovService lovService) {
        this.service = service;
        this.eselon2Service = eselon2Service;
        this.bpdasService = bpdasService;
        this.lovService = lovService;
    }

    @GetMapping
    public ResponseEntity<GeoServicePageDTO> getAllGeoService(
            @RequestParam(required = false) String serviceParam,
            @RequestParam(required = false) List<String> bpdas,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        GeoServicePageDTO geoServicePage;

        String baseUrl = request.getRequestURL().toString();

        if ((serviceParam != null && !serviceParam.isEmpty()) || (bpdas != null && !bpdas.isEmpty())) {
            geoServicePage = service.findByFiltersWithCache(serviceParam, bpdas, pageable, baseUrl);
        } else {
            geoServicePage = service.findAllWithCache(pageable, baseUrl);
        }

        return ResponseEntity.ok(geoServicePage);
    }

    @GetMapping("/search")
    public ResponseEntity<GeoServicePageDTO> searchGeoService(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        GeoServicePageDTO geoServicePage = service.searchWithCache(keyWord, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(geoServicePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeoServiceDTO> getGeoServiceById(@PathVariable Long id) {
        try {
            GeoServiceDTO dto = service.findDTOById(id);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GeoService> createGeoService(
            @RequestPart Long eselon2Id,
            @RequestPart Long bpdasId,
            @RequestPart Long tipeId,
            @RequestPart Long statusId,
            @RequestPart(required = false) Integer geoserviceId,
            @RequestPart(required = false) String url,
            @RequestPart(required = false) String serviceParam
    ) {
        try {
            GeoService geoService = new GeoService();

            if (eselon2Id != null) {
                Eselon2 eselon2 = eselon2Service.findById(eselon2Id);
                geoService.setEselon2Id(eselon2);
            }
            if (bpdasId != null) {
                Bpdas bpdas = bpdasService.findById(bpdasId);
                geoService.setBpdasId(bpdas);
            }
            if (tipeId != null) {
                Lov tipe = lovService.findById(tipeId);
                geoService.setTipeId(tipe);
            }
            if (statusId != null) {
                Lov status = lovService.findById(statusId);
                geoService.setStatusId(status);
            }
            geoService.setGeoserviceId(geoserviceId);
            geoService.setUrl(url);
            geoService.setService(serviceParam);

            GeoService saved = service.save(geoService);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GeoService> updateGeoService(
            @PathVariable Long id,
            @RequestPart Long eselon2Id,
            @RequestPart Long bpdasId,
            @RequestPart Long tipeId,
            @RequestPart Long statusId,
            @RequestPart(required = false) Integer geoserviceId,
            @RequestPart(required = false) String url,
            @RequestPart(required = false) String serviceParam
    ) {
        try {
            GeoService geoService = service.findById(id);

            if (eselon2Id != null) {
                Eselon2 eselon2 = eselon2Service.findById(eselon2Id);
                geoService.setEselon2Id(eselon2);
            }
            if (bpdasId != null) {
                Bpdas bpdas = bpdasService.findById(bpdasId);
                geoService.setBpdasId(bpdas);
            }
            if (tipeId != null) {
                Lov tipe = lovService.findById(tipeId);
                geoService.setTipeId(tipe);
            }
            if (statusId != null) {
                Lov status = lovService.findById(statusId);
                geoService.setStatusId(status);
            }
            geoService.setGeoserviceId(geoserviceId);
            geoService.setUrl(url);
            geoService.setService(serviceParam);

            GeoService updated = service.update(id, geoService);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGeoService(@PathVariable Long id) {
        try {
            service.findById(id);
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
