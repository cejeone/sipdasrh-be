package com.kehutanan.tktrh.ppkh.monev.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.tktrh.bkta.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.bkta.program.model.Program;
import com.kehutanan.tktrh.master.model.Bpdas;
import com.kehutanan.tktrh.master.model.Lov;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "trx_ppkh_monev")
@NoArgsConstructor
@AllArgsConstructor
public class Monev implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "program_id", referencedColumnName = "id")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "bpdas_id", referencedColumnName = "id")
    private Bpdas bpdas;

        @Column(name = "tanggal")
    private LocalDate tanggal;

    @Column(name = "subjek", columnDefinition = "TEXT")
    private String subjek;

        @Column(name = "audiensi", columnDefinition = "TEXT")
    private String audiensi;

    @Column(name = "isu_tindak_lanjut", columnDefinition = "TEXT")
    private String isuTindakLanjut;

    
    @OneToMany(mappedBy = "monev", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MonevPdf> monevPdfs = new ArrayList<>();


}