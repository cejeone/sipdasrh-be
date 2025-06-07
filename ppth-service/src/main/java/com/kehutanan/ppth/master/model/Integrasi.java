package com.kehutanan.ppth.master.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

/**
 * Entity class representing the mst_integrasi table
 * This table stores integration configuration for external APIs and third-party services
 */
@Data
@Entity
@Table(name = "mst_integrasi")
@NoArgsConstructor
@AllArgsConstructor
public class Integrasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", length = 255)
    private String url;

    @Column(name = "api_key", length = 255)
    private String apiKey;

    @Column(name = "tipe")
    private String tipe;

    @Column(name = "deskripsi", columnDefinition = "TEXT")
    private String deskripsi;

        @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;
}