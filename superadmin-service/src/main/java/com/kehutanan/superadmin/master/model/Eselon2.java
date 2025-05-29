package com.kehutanan.superadmin.master.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "mst_eselon_2")
@NoArgsConstructor
@AllArgsConstructor
public class Eselon2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama", length = 255)
    private String nama;

    @Column(name = "pejabat", length = 255)
    private String pejabat;

    @Column(name = "tugas_dan_fungsi", columnDefinition = "TEXT")
    private String tugasDanFungsi;

    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;

    @ManyToOne
    @JoinColumn(name = "eselon_1_id", referencedColumnName = "id")
    private Eselon1 eselon1;

    // @OneToMany(mappedBy = "eselon2", cascade = CascadeType.ALL)
    // private List<Eselon3> eselon3List;
}