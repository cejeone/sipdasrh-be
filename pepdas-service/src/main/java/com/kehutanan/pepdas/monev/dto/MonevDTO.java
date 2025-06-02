package com.kehutanan.pepdas.monev.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.pepdas.monev.model.Monev;
import com.kehutanan.pepdas.monev.model.MonevPdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonevDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String nomor;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggal;
    
    private String deskripsi;
    
    // Related entity IDs instead of full entities
    private Long kegiatanId;
    private String kegiatanNama;
    private Long statusId;
    private String statusNama;
    
    // Lists for file references
    private List<MonevFileDTO> monevPdfList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public MonevDTO(Monev entity) {
        this.id = entity.getId();
        this.nomor = entity.getNomor();
        this.tanggal = entity.getTanggal();
        this.deskripsi = entity.getDeskripsi();
        
        // Set related entity IDs and names
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
            this.kegiatanNama = entity.getKegiatan().getNamaKegiatan();
        }
        
        if (entity.getStatus() != null) {
            this.statusId = entity.getStatus().getId();
            this.statusNama = entity.getStatus().getNilai();
        }
        
        // Convert file lists
        if (entity.getMonevPdfs() != null) {
            for (MonevPdf pdf : entity.getMonevPdfs()) {
                this.monevPdfList.add(new MonevFileDTO(pdf));
            }
        }
    }
}