package com.kehutanan.pepdas.monev.model;

import com.fasterxml.jackson.annotation.JsonBackReference;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "pepdas_monev_pdf")
@NoArgsConstructor
@AllArgsConstructor
public class MonevPdf {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monev_id", nullable = false)
    private Monev monev;

    @Column(nullable = false)
    private String namaFile;

    private String namaAsli;

    private Double ukuranMb;

    private String contentType;

    private LocalDateTime uploadedAt;

}