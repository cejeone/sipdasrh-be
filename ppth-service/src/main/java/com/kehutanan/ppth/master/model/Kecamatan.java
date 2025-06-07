package com.kehutanan.ppth.master.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "mst_kecamatan")
@NoArgsConstructor
@AllArgsConstructor
public class Kecamatan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kecamatan")
    private String kecamatan;

    @Column(name = "kode_depdagri")
    private String kodeDepdagri;

    @ManyToOne
    @JoinColumn(name = "kabupaten_kota_id", referencedColumnName = "id")
    private KabupatenKota kabupatenKota;

    // @OneToMany(mappedBy = "kecamatan", cascade = CascadeType.ALL)
    // private List<KelurahanDesa> kelurahanDesaList;
}