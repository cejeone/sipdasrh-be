package com.kehutanan.rh.kegiatan.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.rh.kegiatan.model.KegiatanMonev;
import com.kehutanan.rh.kegiatan.model.KegiatanMonevKriteria;
import com.kehutanan.rh.kegiatan.model.KegiatanMonevPdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanMonevDTOx implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long kegiatanId;
    private String kegiatanNama;
    private Long statusId;
    private String statusNama;
    private String nomor;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggal;
    
    private String deskripsi;
    
    // Lists for related entities
    private List<KegiatanMonevKriteriaDTO> kriteriaList = new ArrayList<>();
    private List<KegiatanMonevFileDTO> pdfList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public KegiatanMonevDTO(KegiatanMonev entity) {
        this.id = entity.getId();
        
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
            this.kegiatanNama = entity.getKegiatan().getNamaKegiatan(); // Assuming Kegiatan has a 'nama' field
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = entity.getStatusId().getId();
            this.statusNama = entity.getStatusId().getNilai(); // Assuming Lov has a 'nama' field
        }
        
        this.nomor = entity.getNomor();
        this.tanggal = entity.getTanggal();
        this.deskripsi = entity.getDeskripsi();
        
        // Convert kriteria list
        if (entity.getKriterias() != null) {
            for (KegiatanMonevKriteria kriteria : entity.getKriterias()) {
                this.kriteriaList.add(new KegiatanMonevKriteriaDTO(kriteria));
            }
        }
        
        // Convert PDF list
        if (entity.getKegiatanMonevPdfs() != null) {
            for (KegiatanMonevPdf pdf : entity.getKegiatanMonevPdfs()) {
                this.pdfList.add(new KegiatanMonevFileDTO(pdf));
            }
        }
    }
}