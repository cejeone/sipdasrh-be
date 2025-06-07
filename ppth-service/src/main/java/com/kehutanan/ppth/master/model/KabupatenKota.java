package com.kehutanan.ppth.master.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "mst_kabupaten_kota")
@NoArgsConstructor
@AllArgsConstructor
public class KabupatenKota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kabupaten_kota")
    private String kabupatenKota;

    @Column(name = "kode_depdagri")
    private String kodeDepdagri;

    @ManyToOne
    @JoinColumn(name = "provinsi_id", referencedColumnName = "id")
    private Provinsi provinsi;

    // @OneToMany(mappedBy = "kabupatenKota", cascade = CascadeType.ALL)
    // private List<Kecamatan> kecamatanList;
}