package com.kehutanan.tktrh.tmkh.kegiatan.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.tktrh.tmkh.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanPerijinanPdf;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanRiwayatSk;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanPbakPdfShp;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanFungsiKawasanLahanPengganti;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanFungsiKawasanRehab;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanRehabPdf;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanRealisasiReboisasi;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanMonev;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanBastRehabDas;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanBastZip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    // Informasi Umum Kegiatan
    private Long programId;
    private String programNama;
    private String namaKegiatan;
    private Integer tahun;
    private Long provinsiId;
    private String provinsiNama;
    private Long kabupatenKotaId;
    private String kabupatenKotaNama;
    private Long kecamatanId;
    private String kecamatanNama;
    private Long bpdasId;
    private String bpdasNama;
    private Long statusId;
    private String statusNama;
    
    // Perijinan
    private Long pemegangIjinId;
    private String pemegangIjinNama;
    private Long peruntukanId;
    private String peruntukanNama;
    private Long namaPeruntukanId;
    private String namaPeruntukanNama;
    private Long jenisIjinId;
    private String jenisIjinNama;
    private String nomorSkPerijinan;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalSkPerijinan;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalBerakhirSkPerijinan;
    
    private Double luasSesuaiSkPerijinanHa;
    
    // PBAK
    private String nomorSkPbak;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalSkPbak;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalBerakhirSkPbak;
    
    private Double luasSesuaiSkPbakHa;
    
    // Lahan Pengganti
    private String nomorSkLahanPengganti;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalSkLahanPengganti;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalBerakhirSkLahanPengganti;
    
    private Double luasSkLahanPengganti;
    private String keteranganLahanPengganti;
    
    // Rehabilitasi
    private String nomorSkRehabilitasi;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalSkRehabilitasi;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalBerakhirSkRehabilitasi;
    
    private Double luasSkRehabilitasi;
    private Long bpdasRehabId;
    private String bpdasRehabNama;
    
    // BAST
    private String nomorBastPpkhKeDirjen;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalBastPpkhKeDirjen;
    
    private String nomorBastDirjenKeDishut;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalBastDirjenKeDishut;
    
    private String nomorBastDishutKePengelola;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalBastDishutKePengelola;
    
    private String namaPic;
    private String nomorPic;
    private String emailPic;
    
    // Lists for file references
    private List<KegiatanFileDTO> perijinanPdfList = new ArrayList<>();
    private List<KegiatanFileDTO> riwayatSkList = new ArrayList<>();
    private List<KegiatanFileDTO> pbakPdfShpList = new ArrayList<>();
    private List<KegiatanFileDTO> fungsiKawasanLahanPenggantiList = new ArrayList<>();
    private List<KegiatanFileDTO> fungsiKawasanRehabList = new ArrayList<>();
    private List<KegiatanFileDTO> rehabPdfList = new ArrayList<>();
    private List<KegiatanFileDTO> realisasiReboisasiList = new ArrayList<>();
    private List<KegiatanFileDTO> monevList = new ArrayList<>();
    private List<KegiatanFileDTO> bastRehabDasList = new ArrayList<>();
    private List<KegiatanFileDTO> bastZipList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public KegiatanDTO(Kegiatan entity) {
        this.id = entity.getId();
        
        // Informasi Umum
        if (entity.getProgram() != null) {
            this.programId = entity.getProgram().getId();
            this.programNama = entity.getProgram().getNamaProgram(); // Assuming getProgram() has getNamaProgram()
        }
        
        this.namaKegiatan = entity.getNamaKegiatan();
        this.tahun = entity.getTahun();
        
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
        
        if (entity.getBpdas() != null) {
            this.bpdasId = entity.getBpdas().getId();
            this.bpdasNama = entity.getBpdas().getNamaBpdas();
        }
        
        if (entity.getStatus() != null) {
            this.statusId = entity.getStatus().getId();
            this.statusNama = entity.getStatus().getNama();
        }
        
        // Perijinan
        if (entity.getPemegangIjinId() != null) {
            this.pemegangIjinId = entity.getPemegangIjinId().getId();
            this.pemegangIjinNama = entity.getPemegangIjinId().getNama();
        }
        
        if (entity.getPeruntukan() != null) {
            this.peruntukanId = entity.getPeruntukan().getId();
            this.peruntukanNama = entity.getPeruntukan().getNama();
        }
        
        if (entity.getNamaPeruntukan() != null) {
            this.namaPeruntukanId = entity.getNamaPeruntukan().getId();
            this.namaPeruntukanNama = entity.getNamaPeruntukan().getNama();
        }
        
        if (entity.getJenisIjin() != null) {
            this.jenisIjinId = entity.getJenisIjin().getId();
            this.jenisIjinNama = entity.getJenisIjin().getNama();
        }
        
        this.nomorSkPerijinan = entity.getNomorSkPerijinan();
        this.tanggalSkPerijinan = entity.getTanggalSkPerijinan();
        this.tanggalBerakhirSkPerijinan = entity.getTanggalBerakhirSkPerijinan();
        this.luasSesuaiSkPerijinanHa = entity.getLuasSesuaiSkPerijinanHa();
        
        // PBAK
        this.nomorSkPbak = entity.getNomorSkPbak();
        this.tanggalSkPbak = entity.getTanggalSkPbak();
        this.tanggalBerakhirSkPbak = entity.getTanggalBerakhirSkPbak();
        this.luasSesuaiSkPbakHa = entity.getLuasSesuaiSkPbakHa();
        
        // Lahan Pengganti
        this.nomorSkLahanPengganti = entity.getNomorSkLahanPengganti();
        this.tanggalSkLahanPengganti = entity.getTanggalSkLahanPengganti();
        this.tanggalBerakhirSkLahanPengganti = entity.getTanggalBerakhirSkLahanPengganti();
        this.luasSkLahanPengganti = entity.getLuasSkLahanPengganti();
        this.keteranganLahanPengganti = entity.getKeteranganLahanPengganti();
        
        // Rehabilitasi
        this.nomorSkRehabilitasi = entity.getNomorSkRehabilitasi();
        this.tanggalSkRehabilitasi = entity.getTanggalSkRehabilitasi();
        this.tanggalBerakhirSkRehabilitasi = entity.getTanggalBerakhirSkRehabilitasi();
        this.luasSkRehabilitasi = entity.getLuasSkRehabilitasi();
        
        if (entity.getBpdasRehabId() != null) {
            this.bpdasRehabId = entity.getBpdasRehabId().getId();
            this.bpdasRehabNama = entity.getBpdasRehabId().getNamaBpdas();
        }
        
        // BAST
        this.nomorBastPpkhKeDirjen = entity.getNomorBastPpkhKeDirjen();
        this.tanggalBastPpkhKeDirjen = entity.getTanggalBastPpkhKeDirjen();
        this.nomorBastDirjenKeDishut = entity.getNomorBastDirjenKeDishut();
        this.tanggalBastDirjenKeDishut = entity.getTanggalBastDirjenKeDishut();
        this.nomorBastDishutKePengelola = entity.getNomorBastDishutKePengelola();
        this.tanggalBastDishutKePengelola = entity.getTanggalBastDishutKePengelola();
        
        this.namaPic = entity.getNamaPic();
        this.nomorPic = entity.getNomorPic();
        this.emailPic = entity.getEmailPic();
        
        // Convert file lists
        if (entity.getKegiatanPerijinanPdfs() != null) {
            entity.getKegiatanPerijinanPdfs().forEach(pdf -> 
                this.perijinanPdfList.add(new KegiatanFileDTO(pdf))
            );
        }
        
        if (entity.getRiwayatSks() != null) {
            entity.getRiwayatSks().forEach(sk -> 
                this.riwayatSkList.add(new KegiatanFileDTO(sk))
            );
        }
        
        if (entity.getKegiatanPakPdfShps() != null) {
            entity.getKegiatanPakPdfShps().forEach(pdfShp -> 
                this.pbakPdfShpList.add(new KegiatanFileDTO(pdfShp))
            );
        }
        
        if (entity.getFungsiKawasanLahanPenggantis() != null) {
            entity.getFungsiKawasanLahanPenggantis().forEach(pengganti -> 
                this.fungsiKawasanLahanPenggantiList.add(new KegiatanFileDTO(pengganti))
            );
        }
        
        if (entity.getFungsiKawasanRehabs() != null) {
            entity.getFungsiKawasanRehabs().forEach(rehab -> 
                this.fungsiKawasanRehabList.add(new KegiatanFileDTO(rehab))
            );
        }
        
        if (entity.getKegiatanRehabPdfs() != null) {
            entity.getKegiatanRehabPdfs().forEach(pdf -> 
                this.rehabPdfList.add(new KegiatanFileDTO(pdf))
            );
        }
        
        if (entity.getKegiatanRealisasiReboisasis() != null) {
            entity.getKegiatanRealisasiReboisasis().forEach(reboisasi -> 
                this.realisasiReboisasiList.add(new KegiatanFileDTO(reboisasi))
            );
        }
        
        if (entity.getKegiatanMonevs() != null) {
            entity.getKegiatanMonevs().forEach(monev -> 
                this.monevList.add(new KegiatanFileDTO(monev))
            );
        }
        
        if (entity.getBastReboRehabs() != null) {
            entity.getBastReboRehabs().forEach(bast -> 
                this.bastRehabDasList.add(new KegiatanFileDTO(bast))
            );
        }
        
        if (entity.getKegiatanBastZips() != null) {
            entity.getKegiatanBastZips().forEach(zip -> 
                this.bastZipList.add(new KegiatanFileDTO(zip))
            );
        }
    }
}
