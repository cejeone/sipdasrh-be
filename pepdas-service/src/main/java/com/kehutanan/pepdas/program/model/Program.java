package com.kehutanan.pepdas.program.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.pepdas.dokumen.model.DokumenFile;
import com.kehutanan.pepdas.master.model.Eselon1;
import com.kehutanan.pepdas.master.model.Lov;

@Data
@Entity
@Table(name = "trx_pepdas_program")
@NoArgsConstructor
@AllArgsConstructor
public class Program implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "eselon_1_id", referencedColumnName = "id")
    private Eselon1 eselon1;

    @ManyToOne
    @JoinColumn(name = "kategori_id", referencedColumnName = "id")
    private Lov kategori;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;

    @Column(name = "nama", columnDefinition = "TEXT")
    private String nama;

    @Column(name = "tahun_rencana")
    private Integer tahunRencana;

    @Column(name = "total_anggaran")
    private Double totalAnggaran;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramPagu> pagus;

}