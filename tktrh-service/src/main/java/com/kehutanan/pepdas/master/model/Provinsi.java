package com.kehutanan.pepdas.master.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "mst_provinsi")
@NoArgsConstructor
@AllArgsConstructor
public class Provinsi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_provinsi")
    private String namaProvinsi;

    @Column(name = "kode_depdagri")
    private String kodeDepdagri;
    
    
    // @OneToMany(mappedBy = "provinsi", cascade = CascadeType.ALL)
    // private List<KabupatenKota> kabupatenKotaList;
}