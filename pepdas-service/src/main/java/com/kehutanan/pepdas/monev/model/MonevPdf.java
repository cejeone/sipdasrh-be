package com.kehutanan.pepdas.monev.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kehutanan.pepdas.kegiatan.model.Kegiatan;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "trx_pepdas_monev_pdf")
@NoArgsConstructor
@AllArgsConstructor
public class MonevPdf {
    @Id
    private UUID id;
    
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monev_id", referencedColumnName = "id")
    private Monev monev;
    
    private String namaFile;
    
    private String namaAsli;

    private String pathFile;
    
    private Double ukuranMb;
    
    private String contentType;
    
    private LocalDateTime uploadedAt;

    private String viewUrl;

    private String downloadUrl;

}