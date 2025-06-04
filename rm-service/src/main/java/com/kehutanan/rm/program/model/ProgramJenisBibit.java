package com.kehutanan.rm.program.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kehutanan.rm.master.model.Lov;

import java.util.UUID;

@Entity
@Table(name = "trx_rm_program_jenis_bibit")
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

    @Column(name = "jumlah_bibit")
    private Integer jumlahBibit;
    
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov statusId;
    
    private String keterangan;
}