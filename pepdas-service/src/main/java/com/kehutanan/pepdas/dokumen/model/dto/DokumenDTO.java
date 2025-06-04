package com.kehutanan.pepdas.dokumen.model.dto;

import com.kehutanan.pepdas.dokumen.model.Dokumen;
import com.kehutanan.pepdas.master.model.dto.LovDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DokumenDTO {
    
    private Long id;
    private LovDTO tipe;
    private LovDTO status;
    private String namaDokumen;
    private List<DokumenFileDto> dokumenFiles = new ArrayList<>();
    private Double ukuranDokumen;
    private String keterangan;
    
    public DokumenDTO(Dokumen dokumen) {
        if (dokumen != null) {
            this.id = dokumen.getId();
            this.tipe = dokumen.getTipe() != null ? new LovDTO(dokumen.getTipe()) : null;
            this.status = dokumen.getStatus() != null ? new LovDTO(dokumen.getStatus()) : null;
            this.namaDokumen = dokumen.getNamaDokumen();
            this.ukuranDokumen = dokumen.getUkuranDokumen();
            this.keterangan = dokumen.getKeterangan();
            
            if (dokumen.getDokumenFiles() != null && !dokumen.getDokumenFiles().isEmpty()) {
                this.dokumenFiles = dokumen.getDokumenFiles().stream()
                    .map(DokumenFileDto::new)
                    .collect(Collectors.toList());
            }
        }
    }
}