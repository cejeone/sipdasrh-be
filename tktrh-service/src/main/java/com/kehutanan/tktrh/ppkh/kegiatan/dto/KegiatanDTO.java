package com.kehutanan.tktrh.ppkh.kegiatan.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.tktrh.ppkh.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanBastReboRehab;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanBastZip;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanFungsiKawasan;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanPakPdfShp;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanPerijinanPdf;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRantekPdf;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRencanaRealisasi;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRiwayatSk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String namaKegiatan;
    private Integer tahun;
    private String nomorSkPerijinan;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggalSkPerijinan;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggalBerakhirSkPerijinan;
    
    private Integer luasSesuaiSkPerijinanHa;
    private String nomorSkPak;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggalSkPak;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggalBerakhirSkPak;
    
    private Integer luasSesuaiSkPakHa;
    private String nomorSkRehabilitasi;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggalSkRehabilitasi;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggalBerakhirSkRehabilitasi;
    
    private Integer luasSkRehabDas;
    private String nomorBastPpkhKeDirjen;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggalBastPpkhKeDirjen;
    
    private String nomorBastDirjenKeDishut;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggalBastDirjenKeDishut;
    
    private String nomorBastDishutKePengelola;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tanggalBastDishutKePengelola;
    
    private String namaPeruntukan;
    private String namaPic;
    private String nomorPic;
    private String emailPic;
    
    // Related entity IDs and names
    private Long programId;
    private String programNama;
    private Long subDirektoratId;
    private String subDirektoratNama;
    private Long provinsiId;
    private String provinsiNama;
    private Long kabupatenKotaId;
    private String kabupatenKotaNama;
    private Long kecamatanId;
    private String kecamatanNama;
    private Long statusId;
    private String statusNama;
    private Long pemegangIjinId;
    private String pemegangIjinNama;
    private Long peruntukanId;
    private String peruntukanNama;
    private Long jenisIjinId;
    private String jenisIjinNama;
    private Long bpdasRehabId;
    private String bpdasRehabNama;
    
    // Lists for file references
    private List<KegiatanFileDTO> kegiatanPerijinanPdfsList = new ArrayList<>();
    private List<KegiatanFileDTO> kegiatanRiwayatSkList = new ArrayList<>();
    private List<KegiatanFileDTO> kegiatanPakPdfShpsList = new ArrayList<>();
    private List<KegiatanFileDTO> kegiatanFungsiKawasansList = new ArrayList<>();
    private List<KegiatanFileDTO> kegiatanRantekPdfsList = new ArrayList<>();
    private List<KegiatanFileDTO> kegiatanRencanaRealisasisList = new ArrayList<>();
    private List<KegiatanFileDTO> kegiatanBastReboRehabsList = new ArrayList<>();
    private List<KegiatanFileDTO> kegiatanBastZipsList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public KegiatanDTO(Kegiatan entity) {
        this.id = entity.getId();
        this.namaKegiatan = entity.getNamaKegiatan();
        this.tahun = entity.getTahun();
        this.nomorSkPerijinan = entity.getNomorSkPerijinan();
        this.tanggalSkPerijinan = entity.getTanggalSkPerijinan();
        this.tanggalBerakhirSkPerijinan = entity.getTanggalBerakhirSkPerijinan();
        this.luasSesuaiSkPerijinanHa = entity.getLuasSesuaiSkPerijinanHa();
        this.nomorSkPak = entity.getNomorSkPak();
        this.tanggalSkPak = entity.getTanggalSkPak();
        this.tanggalBerakhirSkPak = entity.getTanggalBerakhirSkPak();
        this.luasSesuaiSkPakHa = entity.getLuasSesuaiSkPakHa();
        this.nomorSkRehabilitasi = entity.getNomorSkRehabilitasi();
        this.tanggalSkRehabilitasi = entity.getTanggalSkRehabilitasi();
        this.tanggalBerakhirSkRehabilitasi = entity.getTanggalBerakhirSkRehabilitasi();
        this.luasSkRehabDas = entity.getLuasSkRehabDas();
        this.nomorBastPpkhKeDirjen = entity.getNomorBastPpkhKeDirjen();
        this.tanggalBastPpkhKeDirjen = entity.getTanggalBastPpkhKeDirjen();
        this.nomorBastDirjenKeDishut = entity.getNomorBastDirjenKeDishut();
        this.tanggalBastDirjenKeDishut = entity.getTanggalBastDirjenKeDishut();
        this.nomorBastDishutKePengelola = entity.getNomorBastDishutKePengelola();
        this.tanggalBastDishutKePengelola = entity.getTanggalBastDishutKePengelola();
        this.namaPeruntukan = entity.getNamaPeruntukan();
        this.namaPic = entity.getNamaPic();
        this.nomorPic = entity.getNomorPic();
        this.emailPic = entity.getEmailPic();
        
        // Set related entity IDs and names
        if (entity.getProgram() != null) {
            this.programId = entity.getProgram().getId();
            this.programNama = entity.getProgram().getNamaProgram();
        }
        
        if (entity.getSubDirektorat() != null) {
            this.subDirektoratId = entity.getSubDirektorat().getId();
            this.subDirektoratNama = entity.getSubDirektorat().getNamaEselon3();
        }
        
        if (entity.getProvinsi() != null) {
            this.provinsiId = entity.getProvinsi().getId();
            this.provinsiNama = entity.getProvinsi().getNamaProvinsi();
        }
        
        if (entity.getKabupatenKota() != null) {
            this.kabupatenKotaId = entity.getKabupatenKota().getId();
            this.kabupatenKotaNama = entity.getKabupatenKota().getKabupatenKota();
        }
        
        if (entity.getKecamatan() != null) {
            this.kecamatanId = entity.getKecamatan().getId();
            this.kecamatanNama = entity.getKecamatan().getKecamatan();
        }
        
        if (entity.getStatus() != null) {
            this.statusId = entity.getStatus().getId();
            this.statusNama = entity.getStatus().getNama();
        }
        
        if (entity.getPemegangIjinId() != null) {
            this.pemegangIjinId = entity.getPemegangIjinId().getId();
            this.pemegangIjinNama = entity.getPemegangIjinId().getNama();
        }
        
        if (entity.getPeruntukanId() != null) {
            this.peruntukanId = entity.getPeruntukanId().getId();
            this.peruntukanNama = entity.getPeruntukanId().getNama();
        }
        
        if (entity.getJenisIjinId() != null) {
            this.jenisIjinId = entity.getJenisIjinId().getId();
            this.jenisIjinNama = entity.getJenisIjinId().getNama();
        }
        
        if (entity.getBpdasRehab() != null) {
            this.bpdasRehabId = entity.getBpdasRehab().getId();
            this.bpdasRehabNama = entity.getBpdasRehab().getNamaBpdas();
        }
        
        // Convert file lists
        if (entity.getKegiatanPerijinanPdfs() != null) {
            for (KegiatanPerijinanPdf pdf : entity.getKegiatanPerijinanPdfs()) {
                this.kegiatanPerijinanPdfsList.add(new KegiatanFileDTO(pdf));
            }
        }
        
        if (entity.getRiwayatSks() != null) {
            for (KegiatanRiwayatSk sk : entity.getRiwayatSks()) {
                this.kegiatanRiwayatSkList.add(new KegiatanFileDTO(sk));
            }
        }
        
        if (entity.getKegiatanPakPdfShps() != null) {
            for (KegiatanPakPdfShp pdfShp : entity.getKegiatanPakPdfShps()) {
                this.kegiatanPakPdfShpsList.add(new KegiatanFileDTO(pdfShp));
            }
        }
        
        if (entity.getFungsiKawasans() != null) {
            for (KegiatanFungsiKawasan kawasan : entity.getFungsiKawasans()) {
                this.kegiatanFungsiKawasansList.add(new KegiatanFileDTO(kawasan));
            }
        }
        
        if (entity.getKegiatanRantekPdfs() != null) {
            for (KegiatanRantekPdf rantek : entity.getKegiatanRantekPdfs()) {
                this.kegiatanRantekPdfsList.add(new KegiatanFileDTO(rantek));
            }
        }
        
        if (entity.getRencanaRealisasis() != null) {
            for (KegiatanRencanaRealisasi realisasi : entity.getRencanaRealisasis()) {
                this.kegiatanRencanaRealisasisList.add(new KegiatanFileDTO(realisasi));
            }
        }
        
        if (entity.getBastReboRehabs() != null) {
            for (KegiatanBastReboRehab reboRehab : entity.getBastReboRehabs()) {
                this.kegiatanBastReboRehabsList.add(new KegiatanFileDTO(reboRehab));
            }
        }
        
        if (entity.getKegiatanBastZips() != null) {
            for (KegiatanBastZip zip : entity.getKegiatanBastZips()) {
                this.kegiatanBastZipsList.add(new KegiatanFileDTO(zip));
            }
        }
    }
}
