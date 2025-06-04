package com.kehutanan.tktrh.ppkh.kegiatan.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "trx_ppkh_kegiatan_rancangan_teknis_pdf")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanRantekPdf {

    @Id
    private UUID id;
    
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kegiatan_id", referencedColumnName = "id")
    private Kegiatan kegiatan;
    
    private String namaFile;
    
    private String namaAsli;

    private String pathFile;
    
    private Double ukuranMb;
    
    private String contentType;
    
    private LocalDateTime uploadedAt;

    private String viewUrl;

    private String downloadUrl;

}