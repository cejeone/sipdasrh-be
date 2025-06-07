package com.kehutanan.ppth.mataair.program.model;

import java.util.ArrayList;
import java.util.List;

import com.kehutanan.ppth.master.model.Lov;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "mataairProgram")
@Table(name = "trx_ppth_program_mata_air")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nama;

    @JoinColumn(name = "kategori_id", referencedColumnName = "id")
    private Lov kategoriId;

    
    @JoinColumn(name = "fungsi_kawasan_id", referencedColumnName = "id")
    private Lov fungsiKawasan;

        @Column(name = "tahun_rencana")
    private String tahunRencana;

    @Column(name = "total_anggaran")
    private Double totalAnggaran;


    @Column(name = "total_bibit")
    private Double totalBibit;

    @Column(name = "target_luas")
    private Double targetLuas;

    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov statusId;

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