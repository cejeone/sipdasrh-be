package com.kehutanan.rm.program.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kehutanan.rm.master.model.Lov;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "trx_rm_program_skema")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramSkema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "program_id", referencedColumnName = "id")
    @JsonBackReference
    private Program program;

    @JoinColumn(name = "kategori_id", referencedColumnName = "id")
    private Lov kategoriId;

    @Column(name = "skema_batang_ha")
    private Double skemaBatangHa;

    @Column(name = "target_luas")
    private Double targetLuas;

    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov statusId;

    private String keterangan;
}
