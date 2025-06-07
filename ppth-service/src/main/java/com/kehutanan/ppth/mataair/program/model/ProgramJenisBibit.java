package com.kehutanan.ppth.mataair.program.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kehutanan.ppth.master.model.Lov;

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

@Entity(name = "mataairProgramJenisBibit")
@Table(name = "trx_ppth_program_mata_air_bibit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramJenisBibit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "program_id")
    @JsonBackReference
    private Program program;
    
    @JoinColumn(name = "kategori_id", referencedColumnName = "id")
    private Lov kategoriId;
    
    @JoinColumn(name = "nama_bibit_id", referencedColumnName = "id")
    private Lov namaBibitId;
    
    @JoinColumn(name = "sumber_bibit_id", referencedColumnName = "id")
    private Lov sumberBibitId;
    
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov statusId;
    
    private Integer jumlah;
    
    private String keterangan;
}