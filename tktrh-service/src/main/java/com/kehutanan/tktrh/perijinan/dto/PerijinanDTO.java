package com.kehutanan.tktrh.perijinan.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.tktrh.perijinan.model.Perijinan;
import com.kehutanan.tktrh.perijinan.model.PerijinanDokumenAwalPdf;
import com.kehutanan.tktrh.perijinan.model.PerijinanDokumenBastPdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerijinanDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    // Related entity IDs instead of full entities
    private Long pelakuUsahaId;
    private String pelakuUsahaNama;
    private Long levelId;
    private String levelNama;
    private Long statusId;
    private String statusNama;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String tanggalPengajuan;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String tanggalPenetapan;
    
    private String catatanDokumenAwal;
    private String catatanDokumenBast;
    private String keterangan;
    
    // Lists for file references
    private List<PerijinanFileDTO> dokumenAwalPdfList = new ArrayList<>();
    private List<PerijinanFileDTO> dokumenBastPdfList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public PerijinanDTO(Perijinan entity) {
        System.out.println("Converting Perijinan entity to DTO: " + entity.getId());
        this.id = entity.getId();
        
        // Set related entity IDs and names
        if (entity.getPelakuUsahaId() != null) {
            this.pelakuUsahaId = entity.getPelakuUsahaId().getId();
            this.pelakuUsahaNama = entity.getPelakuUsahaId().getNilai();
        }
        
        if (entity.getLevelId() != null) {
            this.levelId = entity.getLevelId().getId();
            this.levelNama = entity.getLevelId().getNilai();
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = entity.getStatusId().getId();
            this.statusNama = entity.getStatusId().getNilai();
        }
        
        // Convert LocalDateTime to String
        if (entity.getTanggalPengajuan() != null) {
            this.tanggalPengajuan = entity.getTanggalPengajuan().toString();
        }
        
        if (entity.getTanggalPenetapan() != null) {
            this.tanggalPenetapan = entity.getTanggalPenetapan().toString();
        }
        
        this.catatanDokumenAwal = entity.getCatatanDokumenAwal();
        this.catatanDokumenBast = entity.getCatatanDokumenBast();
        this.keterangan = entity.getKeterangan();
        
        // Convert file lists
        if (entity.getDokumenAwalPdfs() != null) {
            for (PerijinanDokumenAwalPdf pdf : entity.getDokumenAwalPdfs()) {
                this.dokumenAwalPdfList.add(new PerijinanFileDTO(pdf));
            }
        }
        
        if (entity.getDokumenBastPdfs() != null) {
            for (PerijinanDokumenBastPdf pdf : entity.getDokumenBastPdfs()) {
                this.dokumenBastPdfList.add(new PerijinanFileDTO(pdf));
            }
        }
    }
}