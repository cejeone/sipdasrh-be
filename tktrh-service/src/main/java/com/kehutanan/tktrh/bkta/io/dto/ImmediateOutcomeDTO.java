package com.kehutanan.tktrh.bkta.io.dto;

import java.io.Serializable;

import com.kehutanan.tktrh.bkta.io.model.ImmediateOutcome;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImmediateOutcomeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String dta;
    private String namaKegiatan;
    private Integer tahunKegiatan;
    private Double spatialX;
    private Double spatialY;
    private Double volumeSedimen;
    private String keterangan;

    // Related entity IDs instead of full entities
    private Long bpdasId;
    private String bpdasNama;
    private Long provinsiId;
    private String provinsiNama;
    private Long kabupatenKotaId;
    private String kabupatenKotaNama;
    private Long kecamatanId;
    private String kecamatanNama;
    
    // Status, kondisi, efektivitas, and rancangan teknis
    private Long statusId;
    private String statusNama;
    private Long kondisiId;
    private String kondisiNama;
    private Long efektivitasId;
    private String efektivitasNama;
    private Long rancanganTeknisId;
    private String rancanganTeknisNama;

    // Constructor to convert from Entity
    public ImmediateOutcomeDTO(ImmediateOutcome entity) {
        this.id = entity.getId();
        this.dta = entity.getDta();
        this.namaKegiatan = entity.getNamaKegiatan();
        this.tahunKegiatan = entity.getTahunKegiatan();
        this.spatialX = entity.getSpatialX();
        this.spatialY = entity.getSpatialY();
        this.volumeSedimen = entity.getVolumeSedimen();
        this.keterangan = entity.getKeterangan();

        // Set related entity IDs and names
        if (entity.getBpdas() != null) {
            this.bpdasId = entity.getBpdas().getId();
            this.bpdasNama = entity.getBpdas().getNamaBpdas();
        }

        if (entity.getProvinsi() != null) {
            this.provinsiId = entity.getProvinsi().getId();
            this.provinsiNama = entity.getProvinsi().getNamaProvinsi();
        }

        if (entity.getKabupatenKota() != null) {
            this.kabupatenKotaId = entity.getKabupatenKota().getId();
            this.kabupatenKotaNama = entity.getKabupatenKota().getKabupatenKota();
        }

        if (entity.getKecamatan() != null) {
            this.kecamatanId = entity.getKecamatan().getId();
            this.kecamatanNama = entity.getKecamatan().getKecamatan();
        }

        if (entity.getStatus() != null) {
            this.statusId = entity.getStatus().getId();
            this.statusNama = entity.getStatus().getNilai();
        }

        if (entity.getKondisi() != null) {
            this.kondisiId = entity.getKondisi().getId();
            this.kondisiNama = entity.getKondisi().getNilai();
        }

        if (entity.getEfektivitas() != null) {
            this.efektivitasId = entity.getEfektivitas().getId();
            this.efektivitasNama = entity.getEfektivitas().getNilai();
        }

        if (entity.getRancanganTeknis() != null) {
            this.rancanganTeknisId = entity.getRancanganTeknis().getId();
            this.rancanganTeknisNama = entity.getRancanganTeknis().getNilai();
        }
    }
}