package com.kehutanan.pepdas.serahterima.model;

import com.fasterxml.jackson.annotation.JsonBackReference;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "pepdas_serah_terima_pdf")
@NoArgsConstructor
@AllArgsConstructor
public class SerahTerimaPdf {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serah_terima_id", nullable = false)
    private SerahTerima serahTerima;

    @Column(nullable = false)
    private String namaFile;

    private String namaAsli;

    private Double ukuranMb;

    private String contentType;

    private LocalDateTime uploadedAt;

}