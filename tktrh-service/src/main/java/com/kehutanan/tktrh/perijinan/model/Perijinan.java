package com.kehutanan.tktrh.perijinan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.kehutanan.tktrh.master.model.Bpdas;
import com.kehutanan.tktrh.master.model.KabupatenKota;
import com.kehutanan.tktrh.master.model.Kecamatan;
import com.kehutanan.tktrh.master.model.KelurahanDesa;
import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.model.Provinsi;
import com.kehutanan.tktrh.master.model.UptdDokumentasiFoto;
import com.kehutanan.tktrh.master.model.UptdDokumentasiVideo;
import com.kehutanan.tktrh.master.model.UptdPetaShp;
import com.kehutanan.tktrh.master.model.UptdRantekPdf;

/**
 * Entity class representing the mst_uptd table
 * This table stores information about Regional Technical Implementation Units (UPTD)
 * responsible for forestry activities
 */
@Data
@Entity
@Table(name = "trx_tkrth_perijinan")
@NoArgsConstructor
@AllArgsConstructor
public class Perijinan implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pelaku_usaha_id", referencedColumnName = "id")
    private Lov pelakuUsahaId;

    @ManyToOne
    @JoinColumn(name = "level_id", referencedColumnName = "id")
    private Lov levelId;

    @ManyToOne
    @JoinColumn(name = "satus_id", referencedColumnName = "id")
    private Lov statusId;

    
    @Column(name = "tanggal_pengajuan")
    private LocalDateTime tanggalPengajuan;

    
    @Column(name = "tanggal_penetapan")
    private LocalDateTime tanggalPenetapan;

    @OneToMany(mappedBy = "perijinan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PerijinanDokumenAwalPdf> dokumenAwalPdfs = new ArrayList<>();
    
    @Column(name = "catatan_dokumen_awal", columnDefinition = "TEXT")
    private String catatanDokumenAwal;

        @OneToMany(mappedBy = "perijinan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PerijinanDokumenBastPdf> dokumenBastPdfs = new ArrayList<>();

    @Column(name = "catatan_dokumen_bast", columnDefinition = "TEXT")
    private String catatanDokumenBast;

    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;

}