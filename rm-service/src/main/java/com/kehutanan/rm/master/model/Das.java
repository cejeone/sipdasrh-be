package com.kehutanan.rm.master.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

/**
 * Entity class representing the mst_das table
 * This table stores information about watersheds (Daerah Aliran Sungai)
 */
@Data
@Entity
@Table(name = "mst_das")
@NoArgsConstructor
@AllArgsConstructor
public class Das {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_das", length = 255)
    private String namaDas;

    @ManyToOne
    @JoinColumn(name = "bpdas_id", referencedColumnName = "id")
    private Bpdas bpdas;
}