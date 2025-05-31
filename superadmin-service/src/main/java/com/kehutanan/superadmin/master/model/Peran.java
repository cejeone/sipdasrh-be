package com.kehutanan.superadmin.master.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

/**
 * Entity class representing the mst_peran table
 * This table stores user roles/positions in the forestry management system
 */
@Data
@Entity
@Table(name = "mst_peran")
@NoArgsConstructor
@AllArgsConstructor
public class Peran {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama", length = 255)
    private String nama;

    @Column(name = "deskripsi", columnDefinition = "TEXT")
    private String deskripsi;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;
}