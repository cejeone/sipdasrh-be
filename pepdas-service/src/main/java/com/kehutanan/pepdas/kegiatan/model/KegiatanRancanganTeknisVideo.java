package com.kehutanan.pepdas.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "trx_pepdas_kegiatan_rancangan_teknis_video")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanRancanganTeknisVideo {

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