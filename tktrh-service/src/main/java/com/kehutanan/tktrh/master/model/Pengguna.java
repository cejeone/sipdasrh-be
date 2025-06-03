package com.kehutanan.tktrh.master.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

/**
 * Entity class representing the mst_pengguna table
 * This table stores user accounts in the forestry management system
 */
@Data
@Entity
@Table(name = "mst_pengguna")
@NoArgsConstructor
@AllArgsConstructor
public class Pengguna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 255, unique = true)
    private String username;

    @Column(name = "nama_lengkap", length = 255)
    private String namaLengkap;

    @Column(name = "no_hp", length = 20)
    private String noHp;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "kata_sandi", length = 255)
    private String kataSandi;

    @ManyToOne
    @JoinColumn(name = "peran_id", referencedColumnName = "id")
    private Peran peran;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;

    @OneToMany(mappedBy = "pengguna", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<PenggunaFoto> penggunaFotoList = new ArrayList<>();
    
}