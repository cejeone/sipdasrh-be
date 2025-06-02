package com.kehutanan.pepdas.serahterima.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.pepdas.serahterima.model.Bast;
import com.kehutanan.pepdas.serahterima.model.BastPdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BastDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String nomorBast;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggal;
    
    private String deskripsi;
    
    // Related entity IDs instead of full entities
    private Long kegiatanId;
    private String kegiatanNama;
    private Long statusId;
    private String statusNama;
    
    // Lists for file references
    private List<BastFileDTO> bastPdfList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public BastDTO(Bast entity) {
        this.id = entity.getId();
        this.nomorBast = entity.getNomorBast();
        this.tanggal = entity.getTanggal();
        this.deskripsi = entity.getDeskripsi();
        
        // Set related entity IDs and names
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
            this.kegiatanNama = entity.getKegiatan().getNamaKegiatan(); // Assuming this field exists
        }
        
        if (entity.getStatus() != null) {
            this.statusId = entity.getStatus().getId();
            this.statusNama = entity.getStatus().getNilai(); // Assuming this field exists
        }
        
        // Convert file lists
        if (entity.getBastPdfs() != null) {
            for (BastPdf pdf : entity.getBastPdfs()) {
                this.bastPdfList.add(new BastFileDTO(pdf));
            }
        }
    }
}