package com.kehutanan.pepdas.geoservice.model.dto;

import com.kehutanan.pepdas.master.model.dto.BpdasDTO;
import com.kehutanan.pepdas.master.model.dto.Eselon2DTO;
import com.kehutanan.pepdas.master.model.dto.LovDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoServiceDTO implements Serializable {

    private Long id;
    private Eselon2DTO eselon2Id;
    private BpdasDTO bpdasId;
    private LovDTO tipeId;
    private LovDTO statusId;
    private Integer geoserviceId;
    private String url;
    private String service;

    // Constructor from entity
    public GeoServiceDTO(com.kehutanan.pepdas.geoservice.model.GeoService geoService) {
        if (geoService != null) {
            this.id = geoService.getId();
            
            // Convert Eselon2 to Eselon2DTO
            if (geoService.getEselon2Id() != null) {
                this.eselon2Id = new Eselon2DTO(geoService.getEselon2Id());
            }
            
            // Convert Bpdas to BpdasDTO
            if (geoService.getBpdasId() != null) {
                this.bpdasId = new BpdasDTO(geoService.getBpdasId());
            }
            
            // Convert Lov to LovDTO for tipeId
            if (geoService.getTipeId() != null) {
                this.tipeId = new LovDTO(geoService.getTipeId());
            }
            
            // Convert Lov to LovDTO for statusId
            if (geoService.getStatusId() != null) {
                this.statusId = new LovDTO(geoService.getStatusId());
            }
            
            this.geoserviceId = geoService.getGeoserviceId();
            this.url = geoService.getUrl();
            this.service = geoService.getService();
        }
    }
}