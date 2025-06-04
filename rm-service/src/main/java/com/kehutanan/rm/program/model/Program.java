package com.kehutanan.rm.program.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.kehutanan.rm.bimtek.model.Bimtek;
import com.kehutanan.rm.kegiatan.model.Kegiatan;
import com.kehutanan.rm.master.model.Eselon1;
import com.kehutanan.rm.master.model.Eselon2;
import com.kehutanan.rm.master.model.Lov;

@Entity
@Table(name = "trx_rm_program")
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

    @Column(name = "nama")
    private String nama;

    @JoinColumn(name = "fungsi_kawasan_id", referencedColumnName = "id")
    @Column(name = "fungsi_kawasan")
    private Lov fungsiKawasan;

    @Column(name = "tahun_rencana")
    private String tahunRencana;

    @Column(name = "total_anggaran")
    private Double totalAnggaran;


    @Column(name = "total_bibit")
    private Double totalBibit;

    @Column(name = "target_luas")
    private Double targetLuas;

    @JoinColumn(name = "kategori_id", referencedColumnName = "id")
    @Column(name = "status_id")
    private Lov statusId;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<ProgramSkema> skemas = new ArrayList<>();

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<ProgramPaguAnggaran> paguAnggarans = new ArrayList<>();

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<ProgramJenisBibit> jenisBibits = new ArrayList<>();

}