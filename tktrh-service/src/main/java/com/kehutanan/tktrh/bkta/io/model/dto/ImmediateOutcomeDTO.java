package com.kehutanan.tktrh.bkta.io.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.kehutanan.tktrh.bkta.io.model.ImmediateOutcome;
import com.kehutanan.tktrh.master.model.dto.BpdasDTO;
import com.kehutanan.tktrh.master.model.dto.ProvinsiDTO;
import com.kehutanan.tktrh.master.model.dto.KabupatenKotaDTO;
import com.kehutanan.tktrh.master.model.dto.KecamatanDTO;
import com.kehutanan.tktrh.master.model.dto.LovDTO;

import java.io.Serializable;

/**
 * DTO class for ImmediateOutcome entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImmediateOutcomeDTO implements Serializable {
    
    private Long id;
    private BpdasDTO bpdas;
    private ProvinsiDTO provinsi;
    private KabupatenKotaDTO kabupatenKota;
    private KecamatanDTO kecamatan;
    private String dta;
    private LovDTO status;
    private LovDTO kondisi;
    private LovDTO efektivitas;
    private LovDTO rancanganTeknis;
    private String namaKegiatan;
    private Integer tahunKegiatan;
    private Double spatialX;
    private Double spatialY;
    private Double volumeSedimen;
    private String keterangan;
    
    /**
     * Constructor that takes an ImmediateOutcome entity and converts it to DTO
     * @param immediateOutcome The ImmediateOutcome entity
     */
    public ImmediateOutcomeDTO(ImmediateOutcome immediateOutcome) {
        this.id = immediateOutcome.getId();
        this.bpdas = immediateOutcome.getBpdas() != null ? new BpdasDTO(immediateOutcome.getBpdas()) : null;
        this.provinsi = immediateOutcome.getProvinsi() != null ? new ProvinsiDTO(immediateOutcome.getProvinsi()) : null;
        this.kabupatenKota = immediateOutcome.getKabupatenKota() != null ? new KabupatenKotaDTO(immediateOutcome.getKabupatenKota()) : null;
        this.kecamatan = immediateOutcome.getKecamatan() != null ? new KecamatanDTO(immediateOutcome.getKecamatan()) : null;
        this.dta = immediateOutcome.getDta();
        this.status = immediateOutcome.getStatus() != null ? new LovDTO(immediateOutcome.getStatus()) : null;
        this.kondisi = immediateOutcome.getKondisi() != null ? new LovDTO(immediateOutcome.getKondisi()) : null;
        this.efektivitas = immediateOutcome.getEfektivitas() != null ? new LovDTO(immediateOutcome.getEfektivitas()) : null;
        this.rancanganTeknis = immediateOutcome.getRancanganTeknis() != null ? new LovDTO(immediateOutcome.getRancanganTeknis()) : null;
        this.namaKegiatan = immediateOutcome.getNamaKegiatan();
        this.tahunKegiatan = immediateOutcome.getTahunKegiatan();
        this.spatialX = immediateOutcome.getSpatialX();
        this.spatialY = immediateOutcome.getSpatialY();
        this.volumeSedimen = immediateOutcome.getVolumeSedimen();
        this.keterangan = immediateOutcome.getKeterangan();
    }
}
