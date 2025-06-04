package com.kehutanan.tktrh.bkta.kegiatan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.kehutanan.tktrh.master.model.Bpdas;
import com.kehutanan.tktrh.master.model.Das;
import com.kehutanan.tktrh.master.model.KabupatenKota;
import com.kehutanan.tktrh.master.model.Kecamatan;
import com.kehutanan.tktrh.master.model.KelurahanDesa;
import com.kehutanan.tktrh.master.model.Lov;
import com.kehutanan.tktrh.master.model.Provinsi;

/**
 * Entity class representing the trx_bkta_kegiatan_lokus table
 * This table stores information about locations of BKTA activities
 */
@Data
@Entity
@Table(name = "trx_bkta_kegiatan_lokus")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanLokus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kegiatan_id", referencedColumnName = "id")
    private Kegiatan kegiatan;

    @ManyToOne
    @JoinColumn(name = "provinsi_id", referencedColumnName = "id")
    private Provinsi provinsi;

    @ManyToOne
    @JoinColumn(name = "kabupaten_kota_id", referencedColumnName = "id")
    private KabupatenKota kabupatenKota;

    @ManyToOne
    @JoinColumn(name = "kecamatan_id", referencedColumnName = "id")
    private Kecamatan kecamatan;

    @ManyToOne
    @JoinColumn(name = "kelurahan_desa_id", referencedColumnName = "id")
    private KelurahanDesa kelurahanDesa;

    @ManyToOne
    @JoinColumn(name = "bpdas_id", referencedColumnName = "id")
    private Bpdas bpdas;

    @ManyToOne
    @JoinColumn(name = "das_id", referencedColumnName = "id")
    private Das das;

    @Column(name = "sub_das", columnDefinition = "TEXT")
    private String subDas;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov status;

    @Column(name = "identitas_bkta", columnDefinition = "TEXT", unique = true)
    private String identitasBkta;

    @ManyToOne
    @JoinColumn(name = "jenis_bangunan_id", referencedColumnName = "id")
    private Lov jenisBangunan;

    @Column(name = "koordinat_x")
    private Double koordinatX;

    @Column(name = "koordinat_y")
    private Double koordinatY;

    @Column(name = "lebar_alur_atas")
    private Double lebarAlurAtas;

    @Column(name = "lebar_alur_bawah")
    private Double lebarAlurBawah;

    @Column(name = "panjang")
    private Double panjang;

    @Column(name = "tinggi")
    private Double tinggi;

    @Column(name = "lebar")
    private Double lebar;

    @Column(name = "volume_bangunan")
    private Double volumeBangunan;

    @Column(name = "daya_tampung_sedimen")
    private Double dayaTampungSedimen;

    @Column(name = "penerima_manfaat", columnDefinition = "TEXT")
    private Lov penerimaManfaat;

    @Column(name = "anggaran")
    private Double anggaran;

    @OneToMany(mappedBy = "kegiatanLokus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KegiatanLokusProposalPdf> lokusProposalPdfs = new ArrayList<>();

    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;

    @Column(name = "informasi_tambahan")
    private Boolean informasiTambahan;

        @Column(name = "integrasi_rhl")
    private Boolean integrasiRhl;

        @Column(name = "rawan_bencana")
    private Boolean rawanBencana;

        @Column(name = "das_prioritas")
    private Boolean dasPrioritas;

        @Column(name = "perlindungan_mata_air")
    private Boolean perlindunganMataAir;

        @Column(name = "danau_prioritas")
    private Boolean danauPrioritas;

        @Column(name = "pengendalian_erosi_dan_sedimentasi")
    private Boolean pengendalianErosiDanSedimentasi;

    @Column(name = "catatan", columnDefinition = "TEXT")
    private String catatan;

    @OneToMany(mappedBy = "kegiatanLokus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KegiatanLokusLokasiPdf> lokusLokasiPdfs = new ArrayList<>();

    @OneToMany(mappedBy = "kegiatanLokus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KegiatanLokusBangunanPdf> lokusBangunanPdfs = new ArrayList<>();

}