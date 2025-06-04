package com.kehutanan.rh.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.rh.master.model.Peran;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeranDTO {
    private Long id;
    private String nama;
    private String deskripsi;
    private LovDTO status;
    
    public PeranDTO(Peran peran) {
        if (peran != null) {
            this.id = peran.getId();
            this.nama = peran.getNama();
            this.deskripsi = peran.getDeskripsi();
            this.status = peran.getStatus() != null ? 
                new LovDTO(peran.getStatus()) : null;
        }
    }
}