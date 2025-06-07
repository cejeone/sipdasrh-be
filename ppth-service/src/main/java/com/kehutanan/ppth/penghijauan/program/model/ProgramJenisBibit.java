package com.kehutanan.ppth.penghijauan.program.model;

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

@Entity(name = "penghijauanProgramJenisBibit")
@Table(name = "trx_ppth_program_penghijauan_jenis_bibit")
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

    
    @JoinColumn(name = "nama_bibit_id", referencedColumnName = "id")
    private Lov namaBibitId;
    
    @JoinColumn(name = "sumber_bibit_id", referencedColumnName = "id")
    private Lov sumberBibitId;
    
    private Integer jumlah;
    
        @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov statusId;

    private String keterangan;
}