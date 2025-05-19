package com.kehutanan.rh.kegiatan.model;

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
@Table(name = "rh_kegiatan_lokus_shp")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanLokusShp {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kegiatan_lokus_id", nullable = false)
    private KegiatanLokus   kegiatanLokus;

    @Column(nullable = false)
    private String namaFile;

    private String namaAsli;

    private Double ukuranMb;

    private String contentType;

    private LocalDateTime uploadedAt;

}