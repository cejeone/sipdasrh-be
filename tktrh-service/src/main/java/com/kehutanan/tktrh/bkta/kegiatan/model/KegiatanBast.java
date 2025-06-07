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
 * Entity class representing the trx_bkta_kegiatan_bast table
 * This table stores information about handover documents for BKTA activities
 */
@Data
@Entity
@Table(name = "trx_bkta_kegiatan_bast")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanBast implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kegiatan_id", referencedColumnName = "id")
    private Kegiatan kegiatan;

    @Column(name = "tahun")
    private Integer tahun;

        @ManyToOne
    @JoinColumn(name = "identitas_bkta_id", referencedColumnName = "id")
    private Lov identitasBkta;

        @ManyToOne
    @JoinColumn(name = "jenis_bkta_id", referencedColumnName = "id")
    private Lov jenisBkta;

    @ManyToOne
    @JoinColumn(name = "kelompok_masyarakat_id", referencedColumnName = "id")
    private Lov kelompokMasyarakat;


    @OneToMany(mappedBy = "kegiatanBast", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanBastPdf> kegiatanKontrakPdfs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;





}