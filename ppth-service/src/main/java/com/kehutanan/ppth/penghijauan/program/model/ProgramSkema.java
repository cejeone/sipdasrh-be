package com.kehutanan.ppth.penghijauan.program.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kehutanan.ppth.master.model.Lov;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "penghijauanProgramSkema")
@Table(name = "trx_ppth_program_penghijauan_skema")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramSkema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "program_id",referencedColumnName = "id")
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
