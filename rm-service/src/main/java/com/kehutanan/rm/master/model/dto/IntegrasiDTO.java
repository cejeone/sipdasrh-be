package com.kehutanan.rm.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.rm.master.model.Integrasi;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntegrasiDTO {
    private Long id;
    private String url;
    private String apiKey;
    private String tipe;
    private String deskripsi;
    private LovDTO status;
    
    public IntegrasiDTO(Integrasi integrasi) {
        if (integrasi != null) {
            this.id = integrasi.getId();
            this.url = integrasi.getUrl();
            this.apiKey = integrasi.getApiKey();
            this.tipe = integrasi.getTipe();
            this.deskripsi = integrasi.getDeskripsi();
            this.status = integrasi.getStatus() != null ? 
                new LovDTO(integrasi.getStatus()) : null;
        }
    }
}