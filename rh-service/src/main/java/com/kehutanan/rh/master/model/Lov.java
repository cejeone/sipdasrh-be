package com.kehutanan.rh.master.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "mst_lov")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lov implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nama_kategori", nullable = false)
    private String namaKategori;

    @Column(name = "nilai", nullable = false)
    private String nilai;

    @Column(name = "kelas")
    private String kelas;

    @Column(name = "deskripsi")
    private String deskripsi;

    @Column(name = "status")
    private String status;
}