package com.kehutanan.pepdas.pemantauandas.model.dto;

import com.kehutanan.pepdas.master.model.dto.BpdasDTO;
import com.kehutanan.pepdas.master.model.dto.DasDTO;
import com.kehutanan.pepdas.master.model.dto.SpasDTO;
import com.kehutanan.pepdas.pemantauandas.model.PemantauanDas;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PemantauanDasDTO {
    
    private Long id;
    private BpdasDTO bpdas;
    private DasDTO das;
    private SpasDTO spas;
    private String tanggalDanWaktu;
    private Integer nilaiTma;
    private Integer nilaiCurahHujan;
    private Integer teganganBaterai;
    
    public PemantauanDasDTO(PemantauanDas pemantauanDas) {
        if (pemantauanDas != null) {
            this.id = pemantauanDas.getId();
            
            if (pemantauanDas.getBpdas() != null) {
                this.bpdas = new BpdasDTO(pemantauanDas.getBpdas());
            }
            
            if (pemantauanDas.getDas() != null) {
                this.das = new DasDTO(pemantauanDas.getDas());
            }
            
            if (pemantauanDas.getSpas() != null) {
                this.spas = new SpasDTO(pemantauanDas.getSpas());
            }
            
            if (pemantauanDas.getTanggalDanWaktu() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                this.tanggalDanWaktu = pemantauanDas.getTanggalDanWaktu().format(formatter);
            }
            
            this.nilaiTma = pemantauanDas.getNilaiTma();
            this.nilaiCurahHujan = pemantauanDas.getNilaiCurahHujan();
            this.teganganBaterai = pemantauanDas.getTeganganBaterai();
        }
    }
}