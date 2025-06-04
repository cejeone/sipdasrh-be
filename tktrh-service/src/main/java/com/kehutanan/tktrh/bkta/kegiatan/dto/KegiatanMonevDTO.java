package com.kehutanan.tktrh.bkta.kegiatan.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanMonev;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanMonevKriteria;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanMonevPdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanMonevDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String nomor;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String tanggal;
    
    private String deskripsi;
    
    // Related entity IDs instead of full entities
    private Long kegiatanId;
    private String kegiatanNama;
    private Long statusId;
    private String statusNama;
    
    // Lists for related entities
    private List<KegiatanMonevKriteriaDTO> kriterias = new ArrayList<>();
    private List<KegiatanMonevFileDTO> monevPdfList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public KegiatanMonevDTO(KegiatanMonev entity) {
        this.id = entity.getId();
        this.nomor = entity.getNomor();
        
        // Convert Date to String
        if (entity.getTanggal() != null) {
            this.tanggal = entity.getTanggal().toString();
        }
        
        this.deskripsi = entity.getDeskripsi();
        
        // Set related entity IDs and names
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
            this.kegiatanNama = entity.getKegiatan().getNilai(); // Assuming Kegiatan has a 'nama' field
        }
        
        if (entity.getStatus() != null) {
            this.statusId = entity.getStatus().getId();
            this.statusNama = entity.getStatus().getNilai(); // Assuming Lov has a 'nama' field
        }
        
        // Convert kriteria list
        if (entity.getKriterias() != null) {
            for (KegiatanMonevKriteria kriteria : entity.getKriterias()) {
                this.kriterias.add(new KegiatanMonevKriteriaDTO(kriteria));
            }
        }
        
        // Convert PDF files list
        if (entity.getKegiatanMonevPdfs() != null) {
            for (KegiatanMonevPdf pdf : entity.getKegiatanMonevPdfs()) {
                this.monevPdfList.add(new KegiatanMonevFileDTO(pdf));
            }
        }
    }
}