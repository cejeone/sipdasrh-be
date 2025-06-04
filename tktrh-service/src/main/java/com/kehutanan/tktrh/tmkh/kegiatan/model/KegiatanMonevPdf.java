package com.kehutanan.tktrh.tmkh.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "trx_tmkh_kegiatan_monev_pdf")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanMonevPdf {

    @Id
    private UUID id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "kegiatan_monev_id", nullable = false)
    private KegiatanMonev kegiatanMonev;

    
    private String namaFile;
    
    private String namaAsli;

    private String pathFile;
    
    private Double ukuranMb;
    
    private String contentType;
    
    private LocalDateTime uploadedAt;

    private String viewUrl;

    private String downloadUrl;

}