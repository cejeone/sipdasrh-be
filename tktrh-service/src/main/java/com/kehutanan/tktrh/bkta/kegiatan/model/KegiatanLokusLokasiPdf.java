package com.kehutanan.tktrh.bkta.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "trx_bkta_kegiatan_lokus_lokasi_pdf")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanLokusLokasiPdf {


    @Id
    private UUID id;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "kegiatan_lokus_id", referencedColumnName = "id")
    private KegiatanLokus kegiatanLokus;
    
    private String namaFile;
    
    private String namaAsli;

    private String pathFile;
    
    private Double ukuranMb;
    
    private String contentType;
    
    private LocalDateTime uploadedAt;

    private String viewUrl;

    private String downloadUrl;

}