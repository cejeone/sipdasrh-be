package com.kehutanan.pepdas.kegiatan.model;

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
@Table(name = "pepdas_kegiatan_monev_pdf")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanMonevPdf {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kegiatan_monev_id", nullable = false)
    private KegiatanMonev kegiatanMonev;

    @Column(nullable = false)
    private String namaFile;

    private String namaAsli;

    private Double ukuranMb;

    private String contentType;

    private LocalDateTime uploadedAt;

}