package com.kehutanan.ppth.perijinan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.kehutanan.ppth.master.model.Bpdas;
import com.kehutanan.ppth.master.model.KabupatenKota;
import com.kehutanan.ppth.master.model.Kecamatan;
import com.kehutanan.ppth.master.model.KelurahanDesa;
import com.kehutanan.ppth.master.model.Lov;
import com.kehutanan.ppth.master.model.PelakuUsaha;
import com.kehutanan.ppth.master.model.Provinsi;
import com.kehutanan.ppth.master.model.UptdDokumentasiFoto;
import com.kehutanan.ppth.master.model.UptdDokumentasiVideo;
import com.kehutanan.ppth.master.model.UptdPetaShp;
import com.kehutanan.ppth.master.model.UptdRantekPdf;

/**
 * Entity class representing the mst_uptd table
 * This table stores information about Regional Technical Implementation Units (UPTD)
 * responsible for forestry activities
 */
@Data
@Entity
@Table(name = "trx_ppth_perijinan")
@NoArgsConstructor
@AllArgsConstructor
public class Perijinan implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pelaku_usaha_id", referencedColumnName = "id")
    private PelakuUsaha pelakuUsahaId;


    
    @Column(name = "tanggal_pengajuan")
    private LocalDateTime tanggalPengajuan;

    
    @Column(name = "tanggal_penetapan")
    private LocalDateTime tanggalPenetapan;

    @OneToMany(mappedBy = "perijinan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PerijinanDokumenPdf> perijinanDokumenPdfs = new ArrayList<>();
    
    @Column(name = "catatan_dokumen_perijinan", columnDefinition = "TEXT")
    private String catatanDokumenPerijinan;

        @OneToMany(mappedBy = "perijinan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PerijinanDokumenSertifikatPdf> perijinanDokumenSertifikatPdfs = new ArrayList<>();

    @Column(name = "catatan_dokumen_bast", columnDefinition = "TEXT")
    private String catatanDokumenSertifikat;

    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;

    
    @ManyToOne
    @JoinColumn(name = "level_id", referencedColumnName = "id")
    private Lov levelId;

    @ManyToOne
    @JoinColumn(name = "satus_id", referencedColumnName = "id")
    private Lov statusId;

}