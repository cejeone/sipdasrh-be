package com.kehutanan.ppth.penghijauan.serahterima.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "pehijauanBastPdf")
@Table(name = "trx_ppth_bast_penghijauan_pdf")
@NoArgsConstructor
@AllArgsConstructor
public class BastPdf {

    @Id
    private UUID id;
    
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "konten_id", referencedColumnName = "id")
    private Bast bast;
    
    private String namaFile;
    
    private String namaAsli;

    private String pathFile;
    
    private Double ukuranMb;
    
    private String contentType;
    
    private LocalDateTime uploadedAt;

    private String viewUrl;

    private String downloadUrl;

}