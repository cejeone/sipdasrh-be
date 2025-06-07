package com.kehutanan.ppth.mataair.kegiatan.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "mataairKegiatanMonevPdf")
@Table(name = "trx_rm_kegiatan_monev_pdf")
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