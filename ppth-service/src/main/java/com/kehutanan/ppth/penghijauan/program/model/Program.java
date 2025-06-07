package com.kehutanan.ppth.penghijauan.program.model;

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

@Entity(name = "penghijauanProgram")
@Table(name = "trx_ppth_program_penghijauan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "kategori_id", referencedColumnName = "id")
    private Lov kategoriId;

    private String nama;

    @JoinColumn(name = "fungsi_kawasan_id", referencedColumnName = "id")
    private Lov fungsiKawasan;

    @Column(name = "tahun_rencana")
    private Integer tahunRencana;

    @Column(name = "total_anggaran")
    private Double totalAnggaran;

        @Column(name = "total_bibit")
    private Integer totalBibit;

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

}