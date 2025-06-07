package com.kehutanan.ppth.mataair.program.model;

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

@Entity(name = "mataairProgramPaguAnggaran")
@Table(name = "trx_ppth_program_mata_air_pagu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramPaguAnggaran {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "program_id",referencedColumnName = "id")
    @JsonBackReference
    private Program program;
    
    @JoinColumn(name = "kategori_id",referencedColumnName = "id")
    private Lov kategoriId;
    
    @JoinColumn(name = "sumber_anggaran_id",referencedColumnName = "id")
    private Lov sumberAnggaranId;
    
    
    @Column(name = "tahun_anggaran")
    private Integer tahunAnggaran;
    
    private Double pagu;

        @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov statusId;
    
    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;
}
