package com.kehutanan.tktrh.ppkh.spas.model.dto;

import com.kehutanan.tktrh.master.model.dto.BpdasDTO;
import com.kehutanan.tktrh.master.model.dto.DasDTO;
import com.kehutanan.tktrh.master.model.dto.LovDTO;
import com.kehutanan.tktrh.ppkh.spas.model.Spas;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpasDTO {
    private Long id;
    private BpdasDTO bpdas;
    private DasDTO das;
    private LovDTO spasId;
    private String tanggal;
    private Double nilaiTma;
    private Double nilaiCurahHujan;
    private Double teganganBaterai;

    public SpasDTO(Spas spas) {
        if (spas != null) {
            this.id = spas.getId();
            
            if (spas.getBpdas() != null) {
                this.bpdas = new BpdasDTO(spas.getBpdas());
            }
            
            if (spas.getDas() != null) {
                this.das = new DasDTO(spas.getDas());
            }
            
            if (spas.getSpasId() != null) {
                this.spasId = new LovDTO(spas.getSpasId());
            }
            
            if (spas.getTanggal() != null) {
                this.tanggal = spas.getTanggal().format(DateTimeFormatter.ISO_DATE_TIME);
            }
            
            this.nilaiTma = spas.getNilaiTma();
            this.nilaiCurahHujan = spas.getNilaiCurahHujan();
            this.teganganBaterai = spas.getTeganganBaterai();
        }
    }
}
