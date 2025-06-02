package com.kehutanan.pepdas.geoservice.dto;

import java.io.Serializable;

import com.kehutanan.pepdas.geoservice.model.GeoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoServiceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long eselon2Id;
    private String eselon2Nama;
    private Long bpdasId;
    private String bpdasNama;
    private Long tipeId;
    private String tipeNama;
    private Long statusId;
    private String statusNama;
    private Integer geoserviceId;
    private String url;
    private String service;

    public GeoServiceDTO(GeoService entity) {
        this.id = entity.getId();
        if (entity.getEselon2Id() != null) {
            this.eselon2Id = entity.getEselon2Id().getId();
            this.eselon2Nama = entity.getEselon2Id().getNama();
        }
        if (entity.getBpdasId() != null) {
            this.bpdasId = entity.getBpdasId().getId();
            this.bpdasNama = entity.getBpdasId().getNamaBpdas();
        }
        if (entity.getTipeId() != null) {
            this.tipeId = entity.getTipeId().getId();
            this.tipeNama = entity.getTipeId().getNilai();
        }
        if (entity.getStatusId() != null) {
            this.statusId = entity.getStatusId().getId();
            this.statusNama = entity.getStatusId().getNilai();
        }
        this.geoserviceId = entity.getGeoserviceId();
        this.url = entity.getUrl();
        this.service = entity.getService();
    }
}
