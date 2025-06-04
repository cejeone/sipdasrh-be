package com.kehutanan.pepdas.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.pepdas.master.model.Konfigurasi;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KonfigurasiDTO {
    private Long id;
    private LovDTO tipe;
    private Integer key;
    private String value;
    private String deskripsi;
    
    public KonfigurasiDTO(Konfigurasi konfigurasi) {
        if (konfigurasi != null) {
            this.id = konfigurasi.getId();
            this.tipe = konfigurasi.getTipe() != null ? 
                new LovDTO(konfigurasi.getTipe()) : null;
            this.key = konfigurasi.getKey();
            this.value = konfigurasi.getValue();
            this.deskripsi = konfigurasi.getDeskripsi();
        }
    }
}