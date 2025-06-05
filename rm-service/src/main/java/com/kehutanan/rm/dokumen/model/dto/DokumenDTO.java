package com.kehutanan.rm.dokumen.model.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kehutanan.rm.dokumen.model.Dokumen;
import com.kehutanan.rm.master.model.dto.LovDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DokumenDTO {
    private Long id;
    private LovDTO tipe;
    private LovDTO status;
    private String namaDokumen;
    private List<DokumenFileDTO> dokumenFiles = new ArrayList<>();
    private Double ukuranDokumen;
    private String keterangan;
    
    // Constructor that takes Dokumen entity
    public DokumenDTO(Dokumen dokumen) {
        this.id = dokumen.getId();
        this.namaDokumen = dokumen.getNamaDokumen();
        this.ukuranDokumen = dokumen.getUkuranDokumen();
        this.keterangan = dokumen.getKeterangan();
        
        // Convert tipe Lov to LovDTO
        if (dokumen.getTipe() != null) {
            this.tipe = new LovDTO(dokumen.getTipe());
        }
        
        // Convert status Lov to LovDTO
        if (dokumen.getStatus() != null) {
            this.status = new LovDTO(dokumen.getStatus());
        }
        
        // Convert dokumenFiles to DokumenFileDTO list
        if (dokumen.getDokumenFiles() != null) {
            this.dokumenFiles = dokumen.getDokumenFiles().stream()
                .map(DokumenFileDTO::new)
                .collect(Collectors.toList());
        }
    }
}
