package com.kehutanan.tktrh.tmkh.kegiatan.model.dto;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kehutanan.tktrh.bkta.program.model.dto.ProgramDTO;
import com.kehutanan.tktrh.master.model.dto.BpdasDTO;
import com.kehutanan.tktrh.master.model.dto.Eselon3DTO;
import com.kehutanan.tktrh.master.model.dto.KabupatenKotaDTO;
import com.kehutanan.tktrh.master.model.dto.KecamatanDTO;
import com.kehutanan.tktrh.master.model.dto.LovDTO;
import com.kehutanan.tktrh.master.model.dto.ProvinsiDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanBastRehabDas;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanBastZip;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanFungsiKawasanLahanPengganti;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanFungsiKawasanRehab;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanMonev;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanPbakPdfShp;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanPerijinanPdf;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanRealisasiReboisasi;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanRehabPdf;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanRiwayatSk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanDTO implements Serializable {

    private Long id;

    // Informasi Umum Kegiatan
    private ProgramDTO program;
    private String namaKegiatan;
    private Integer tahun;
    private ProvinsiDTO provinsi;
    private KabupatenKotaDTO kabupatenKota;
    private KecamatanDTO kecamatan;
    private BpdasDTO bpdas;
    private LovDTO status;

    // Perijinan
    private LovDTO pemegangIjinId;
    private LovDTO peruntukanId;
    private String namaPeruntukan;
    private LovDTO jenisIjin;
    private String nomorSkPerijinan;
    private String tanggalSkPerijinan;
    private String tanggalBerakhirSkPerijinan;
    private Double luasSesuaiSkPerijinanHa;
    private List<KegiatanPerijinanPdfDTO> kegiatanPerijinanPdfs = new ArrayList<>();
    private List<KegiatanRiwayatSkDTO> riwayatSks = new ArrayList<>();

    // PBAK
    private String nomorSkPbak;
    private String tanggalSkPbak;
    private String tanggalBerakhirSkPbak;
    private Double luasSesuaiSkPbakHa;
    private List<KegiatanPbakPdfShpDTO> kegiatanPakPdfShps = new ArrayList<>();

    // Lahan Pengganti
    private String nomorSkLahanPengganti;
    private String tanggalSkLahanPengganti;
    private String tanggalBerakhirSkLahanPengganti;
    private Double luasSkLahanPengganti;
    private String keteranganLahanPengganti;
    private List<KegiatanFungsiKawasanLahanPenggantiDTO> fungsiKawasanLahanPenggantis = new ArrayList<>();

    // Rehabilitasi
    private String nomorSkRehabilitasi;
    private String tanggalSkRehabilitasi;
    private String tanggalBerakhirSkRehabilitasi;
    private Double luasSkRehabilitasi;
    private BpdasDTO bpdasRehabId;
    private List<KegiatanFungsiKawasanRehabDTO> fungsiKawasanRehabs = new ArrayList<>();
    private List<KegiatanRehabPdfDTO> kegiatanRehabPdfs = new ArrayList<>();

    // Kinerja
    private List<KegiatanRealisasiReboisasiDTO> kegiatanRealisasiReboisasis = new ArrayList<>();

    // Monev
    private List<KegiatanMonevDTO> kegiatanMonevs = new ArrayList<>();

    // BAST
    private List<KegiatanBastRehabDasDTO> bastReboRehabs = new ArrayList<>();
    private String nomorBastPpkhKeDirjen;
    private String tanggalBastPpkhKeDirjen;
    private String nomorBastDirjenKeDishut;
    private String tanggalBastDirjenKeDishut;
    private String nomorBastDishutKePengelola;
    private String tanggalBastDishutKePengelola;
    private List<KegiatanBastZipDTO> kegiatanBastZips = new ArrayList<>();

    private String namaPic;
    private String nomorPic;
    private String emailPic;

    // Constructor that takes the entity as a parameter
    public KegiatanDTO(Kegiatan kegiatan) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        this.id = kegiatan.getId();
        
        // Informasi Umum Kegiatan
        if (kegiatan.getProgram() != null) {
            this.program = new ProgramDTO(kegiatan.getProgram());
        }
        this.namaKegiatan = kegiatan.getNamaKegiatan();
        this.tahun = kegiatan.getTahun();
        
        if (kegiatan.getProvinsi() != null) {
            this.provinsi = new ProvinsiDTO(kegiatan.getProvinsi());
        }
        
        if (kegiatan.getKabupatenKota() != null) {
            this.kabupatenKota = new KabupatenKotaDTO(kegiatan.getKabupatenKota());
        }
        
        if (kegiatan.getKecamatan() != null) {
            this.kecamatan = new KecamatanDTO(kegiatan.getKecamatan());
        }
        
        if (kegiatan.getBpdas() != null) {
            this.bpdas = new BpdasDTO(kegiatan.getBpdas());
        }
        
        if (kegiatan.getStatus() != null) {
            this.status = new LovDTO(kegiatan.getStatus());
        }
        
        // Perijinan
        if (kegiatan.getPemegangIjinId() != null) {
            this.pemegangIjinId = new LovDTO(kegiatan.getPemegangIjinId());
        }
        
        if (kegiatan.getPeruntukanId() != null) {
            this.peruntukanId = new LovDTO(kegiatan.getPeruntukanId());
        }
        
        this.namaPeruntukan = kegiatan.getNamaPeruntukan();
        
        if (kegiatan.getJenisIjin() != null) {
            this.jenisIjin = new LovDTO(kegiatan.getJenisIjin());
        }
        
        this.nomorSkPerijinan = kegiatan.getNomorSkPerijinan();
        if (kegiatan.getTanggalSkPerijinan() != null) {
            this.tanggalSkPerijinan = kegiatan.getTanggalSkPerijinan().format(formatter);
        }
        if (kegiatan.getTanggalBerakhirSkPerijinan() != null) {
            this.tanggalBerakhirSkPerijinan = kegiatan.getTanggalBerakhirSkPerijinan().format(formatter);
        }
        this.luasSesuaiSkPerijinanHa = kegiatan.getLuasSesuaiSkPerijinanHa();
        
        if (kegiatan.getKegiatanPerijinanPdfs() != null) {
            this.kegiatanPerijinanPdfs = kegiatan.getKegiatanPerijinanPdfs().stream()
                .map(KegiatanPerijinanPdfDTO::new)
                .collect(Collectors.toList());
        }
        
        if (kegiatan.getRiwayatSks() != null) {
            this.riwayatSks = kegiatan.getRiwayatSks().stream()
                .map(KegiatanRiwayatSkDTO::new)
                .collect(Collectors.toList());
        }
        
        // PBAK
        this.nomorSkPbak = kegiatan.getNomorSkPbak();
        if (kegiatan.getTanggalSkPbak() != null) {
            this.tanggalSkPbak = kegiatan.getTanggalSkPbak().format(formatter);
        }
        if (kegiatan.getTanggalBerakhirSkPbak() != null) {
            this.tanggalBerakhirSkPbak = kegiatan.getTanggalBerakhirSkPbak().format(formatter);
        }
        this.luasSesuaiSkPbakHa = kegiatan.getLuasSesuaiSkPbakHa();
        
        if (kegiatan.getKegiatanPakPdfShps() != null) {
            this.kegiatanPakPdfShps = kegiatan.getKegiatanPakPdfShps().stream()
                .map(KegiatanPbakPdfShpDTO::new)
                .collect(Collectors.toList());
        }
        
        // Lahan Pengganti
        this.nomorSkLahanPengganti = kegiatan.getNomorSkLahanPengganti();
        if (kegiatan.getTanggalSkLahanPengganti() != null) {
            this.tanggalSkLahanPengganti = kegiatan.getTanggalSkLahanPengganti().format(formatter);
        }
        if (kegiatan.getTanggalBerakhirSkLahanPengganti() != null) {
            this.tanggalBerakhirSkLahanPengganti = kegiatan.getTanggalBerakhirSkLahanPengganti().format(formatter);
        }
        this.luasSkLahanPengganti = kegiatan.getLuasSkLahanPengganti();
        this.keteranganLahanPengganti = kegiatan.getKeteranganLahanPengganti();
        
        if (kegiatan.getFungsiKawasanLahanPenggantis() != null) {
            this.fungsiKawasanLahanPenggantis = kegiatan.getFungsiKawasanLahanPenggantis().stream()
                .map(KegiatanFungsiKawasanLahanPenggantiDTO::new)
                .collect(Collectors.toList());
        }
        
        // Rehabilitasi
        this.nomorSkRehabilitasi = kegiatan.getNomorSkRehabilitasi();
        if (kegiatan.getTanggalSkRehabilitasi() != null) {
            this.tanggalSkRehabilitasi = kegiatan.getTanggalSkRehabilitasi().format(formatter);
        }
        if (kegiatan.getTanggalBerakhirSkRehabilitasi() != null) {
            this.tanggalBerakhirSkRehabilitasi = kegiatan.getTanggalBerakhirSkRehabilitasi().format(formatter);
        }
        this.luasSkRehabilitasi = kegiatan.getLuasSkRehabilitasi();
        
        if (kegiatan.getBpdasRehabId() != null) {
            this.bpdasRehabId = new BpdasDTO(kegiatan.getBpdasRehabId());
        }
        
        if (kegiatan.getFungsiKawasanRehabs() != null) {
            this.fungsiKawasanRehabs = kegiatan.getFungsiKawasanRehabs().stream()
                .map(KegiatanFungsiKawasanRehabDTO::new)
                .collect(Collectors.toList());
        }
        
        if (kegiatan.getKegiatanRehabPdfs() != null) {
            this.kegiatanRehabPdfs = kegiatan.getKegiatanRehabPdfs().stream()
                .map(KegiatanRehabPdfDTO::new)
                .collect(Collectors.toList());
        }
        
        // Kinerja
        if (kegiatan.getKegiatanRealisasiReboisasis() != null) {
            this.kegiatanRealisasiReboisasis = kegiatan.getKegiatanRealisasiReboisasis().stream()
                .map(KegiatanRealisasiReboisasiDTO::new)
                .collect(Collectors.toList());
        }
        
        // Monev
        if (kegiatan.getKegiatanMonevs() != null) {
            this.kegiatanMonevs = kegiatan.getKegiatanMonevs().stream()
                .map(KegiatanMonevDTO::new)
                .collect(Collectors.toList());
        }
        
        // BAST
        if (kegiatan.getBastReboRehabs() != null) {
            this.bastReboRehabs = kegiatan.getBastReboRehabs().stream()
                .map(KegiatanBastRehabDasDTO::new)
                .collect(Collectors.toList());
        }
        
        this.nomorBastPpkhKeDirjen = kegiatan.getNomorBastPpkhKeDirjen();
        if (kegiatan.getTanggalBastPpkhKeDirjen() != null) {
            this.tanggalBastPpkhKeDirjen = kegiatan.getTanggalBastPpkhKeDirjen().format(formatter);
        }
        this.nomorBastDirjenKeDishut = kegiatan.getNomorBastDirjenKeDishut();
        if (kegiatan.getTanggalBastDirjenKeDishut() != null) {
            this.tanggalBastDirjenKeDishut = kegiatan.getTanggalBastDirjenKeDishut().format(formatter);
        }
        this.nomorBastDishutKePengelola = kegiatan.getNomorBastDishutKePengelola();
        if (kegiatan.getTanggalBastDishutKePengelola() != null) {
            this.tanggalBastDishutKePengelola = kegiatan.getTanggalBastDishutKePengelola().format(formatter);
        }
        
        if (kegiatan.getKegiatanBastZips() != null) {
            this.kegiatanBastZips = kegiatan.getKegiatanBastZips().stream()
                .map(KegiatanBastZipDTO::new)
                .collect(Collectors.toList());
        }
        
        this.namaPic = kegiatan.getNamaPic();
        this.nomorPic = kegiatan.getNomorPic();
        this.emailPic = kegiatan.getEmailPic();
    }
}
