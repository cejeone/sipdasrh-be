package com.kehutanan.pepdas.dokumen.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.kehutanan.pepdas.dokumen.model.Dokumen;
import com.kehutanan.pepdas.dokumen.model.DokumenFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DokumenDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String namaDokumen;
    private Double ukuranDokumen;
    private String keterangan;
    
    // Related entity IDs and names
    private Long tipeId;
    private String tipeNama;
    private Long statusId;
    private String statusNama;
    
    // Lists for file references
    private List<FileDTO> dokumenFiles = new ArrayList<>();
    
    // Constructor to convert from Entity
    public DokumenDTO(Dokumen entity) {
        this.id = entity.getId();
        this.namaDokumen = entity.getNamaDokumen();
        this.ukuranDokumen = entity.getUkuranDokumen();
        this.keterangan = entity.getKeterangan();
        
        // Set related entity IDs and names
        if (entity.getTipe() != null) {
            this.tipeId = entity.getTipe().getId();
            this.tipeNama = entity.getTipe().getNilai();
        }
        
        if (entity.getStatus() != null) {
            this.statusId = entity.getStatus().getId();
            this.statusNama = entity.getStatus().getNilai();
        }
        
        // Convert file lists
        if (entity.getDokumenFiles() != null) {
            for (DokumenFile file : entity.getDokumenFiles()) {
                this.dokumenFiles.add(new FileDTO(file));
            }
        }
    }
}