package com.kehutanan.ppth.penghijauan.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "penghijauanKegiatanKontrakPdf")
@Table(name = "trx_ppth_kegiatan_penghijauan_kontrak_pdf")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanKontrakPdf {


    @Id
    private UUID id;
    
    @JsonBackReference
    @ManyToOne
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