package com.kehutanan.ppth.master.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

/**
 * Entity class that represents system configuration settings
 * This table stores various configuration parameters used across the application
 */
@Data
@Entity
@Table(name = "cfg_konfigurasi")
@NoArgsConstructor
@AllArgsConstructor
public class Konfigurasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "type", referencedColumnName = "id")
    private Lov tipe;

    @Column(name = "key")
    private Integer key;

    @Column(name = "value", length = 255)
    private String value;

    @Column(name = "deskripsi", length = 255)
    private String deskripsi;
    
}