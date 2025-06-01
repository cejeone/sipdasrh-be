package com.kehutanan.rh.program.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.kehutanan.rh.bimtek.model.Bimtek;
import com.kehutanan.rh.kegiatan.model.Kegiatan;
import com.kehutanan.rh.master.model.Eselon1;
import com.kehutanan.rh.master.model.Eselon2;
import com.kehutanan.rh.master.model.Lov;

@Entity
@Table(name = "trx_rh_program")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "direktorat_id", referencedColumnName = "id")
    private Eselon2 eselon2;

    @JoinColumn(name = "kategori_id", referencedColumnName = "id")
    private Lov kategoriId;
    
    @JoinColumn(name = "kategori_id", referencedColumnName = "id")
    @Column(name = "status_id")
    private Lov statusId;

    private String nama;

    @Column(name = "tahun_pelaksana")
    private String tahunPelaksana;

    @Column(name = "total_anggaran")
    private Double totalAnggaran;

    @Column(name = "target_luas")
    private Double targetLuas;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<ProgramSkema> skemas = new ArrayList<>();

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<ProgramPaguAnggaran> paguAnggarans = new ArrayList<>();

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<ProgramJenisBibit> jenisBibits = new ArrayList<>();

    // @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    // private List<Kegiatan> kegiatans = new ArrayList<>();

    // @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    // private List<BastPusat> bastPusats = new ArrayList<>();

    // @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    // private List<MonevPusat> monevPusats = new ArrayList<>();

    // @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    // private List<Bimtek> bimteks = new ArrayList<>();
}