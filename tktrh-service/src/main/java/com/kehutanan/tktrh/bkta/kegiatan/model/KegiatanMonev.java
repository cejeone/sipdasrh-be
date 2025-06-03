package com.kehutanan.tktrh.bkta.kegiatan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.tktrh.master.model.Lov;

/**
 * Entity class representing the trx_bkta_kegiatan_monev table
 * This table stores information about monitoring and evaluation of BKTA activities
 */
@Data
@Entity
@Table(name = "trx_bkta_kegiatan_monev")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanMonev implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kegiatan_id", referencedColumnName = "id")
    private Kegiatan kegiatan;

    
    @Column(name = "nomor")
    private String nomor;

    
    @Column(name = "tanggal")
    private Date tanggal;

    
    @Column(name = "deskripsi", columnDefinition = "TEXT")
    private String deskripsi;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;

        
    @OneToMany(mappedBy = "monitoringEvaluasi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KegiatanMonevKriteria> kriterias = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatanMonev", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KegiatanMonevPdf> kegiatanMonevPdfs = new ArrayList<>();
}