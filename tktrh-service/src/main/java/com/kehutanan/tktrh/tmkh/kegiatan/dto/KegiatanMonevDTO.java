package com.kehutanan.tktrh.tmkh.kegiatan.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.tktrh.tmkh.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanMonev;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanMonevKriteria;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanMonevPdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanMonevDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long kegiatanId;
    private String kegiatanNomor;
    private Long statusId;
    private String statusNama;
    private String nomor;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggal;
    
    private String deskripsi;
    
    // Lists for file references and criteria
    private List<KegiatanMonevFileDTO> kegiatanMonevPdfList = new ArrayList<>();
    private List<KegiatanMonevKriteriaDTO> kriteriaList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public KegiatanMonevDTO(KegiatanMonev entity) {
        this.id = entity.getId();
        this.nomor = entity.getNomor();
        this.tanggal = entity.getTanggal();
        this.deskripsi = entity.getDeskripsi();
        
        // Set related entity IDs and names
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
            this.kegiatanNomor = entity.getKegiatan().getNomor();
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = entity.getStatusId().getId();
            this.statusNama = entity.getStatusId().getNama();
        }
        
        // Convert file lists
        if (entity.getKegiatanMonevPdfs() != null) {
            for (KegiatanMonevPdf pdf : entity.getKegiatanMonevPdfs()) {
                this.kegiatanMonevPdfList.add(new KegiatanMonevFileDTO(pdf));
            }
        }
        
        // Convert criteria list
        if (entity.getKriterias() != null) {
            for (KegiatanMonevKriteria kriteria : entity.getKriterias()) {
                this.kriteriaList.add(new KegiatanMonevKriteriaDTO(kriteria));
            }
        }
    }
}
