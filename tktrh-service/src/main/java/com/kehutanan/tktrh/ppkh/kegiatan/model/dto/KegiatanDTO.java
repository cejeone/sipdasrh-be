package com.kehutanan.tktrh.ppkh.kegiatan.model.dto;

import com.kehutanan.tktrh.bkta.program.model.dto.ProgramDTO;
import com.kehutanan.tktrh.master.model.dto.BpdasDTO;
import com.kehutanan.tktrh.master.model.dto.Eselon3DTO;
import com.kehutanan.tktrh.master.model.dto.KabupatenKotaDTO;
import com.kehutanan.tktrh.master.model.dto.KecamatanDTO;
import com.kehutanan.tktrh.master.model.dto.LovDTO;
import com.kehutanan.tktrh.master.model.dto.ProvinsiDTO;
import com.kehutanan.tktrh.ppkh.kegiatan.model.Kegiatan;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanDTO {
    private Long id;
    private ProgramDTO program;
    private Eselon3DTO subDirektorat;
    private String namaKegiatan;
    private Integer tahun;
    private ProvinsiDTO provinsi;
    private KabupatenKotaDTO kabupatenKota;
    private KecamatanDTO kecamatan;
    private LovDTO status;
    private LovDTO pemegangIjinId;
    private LovDTO peruntukanId;
    private String namaPeruntukan;
    private LovDTO jenisIjinId;
    private String nomorSkPerijinan;
    private String tanggalSkPerijinan;
    private String tanggalBerakhirSkPerijinan;
    private Integer luasSesuaiSkPerijinanHa;
    private List<KegiatanPerijinanPdfDTO> kegiatanPerijinanPdfs;
    private List<KegiatanRiwayatSkDTO> riwayatSks;
    private String nomorSkPak;
    private String tanggalSkPak;
    private String tanggalBerakhirSkPak;
    private Integer luasSesuaiSkPakHa;
    private List<KegiatanPakPdfShpDTO> kegiatanPakPdfShps;
    private String nomorSkRehabilitasi;
    private String tanggalSkRehabilitasi;
    private String tanggalBerakhirSkRehabilitasi;
    private Integer luasSkRehabDas;
    private BpdasDTO bpdasRehab;
    private List<KegiatanFungsiKawasanDTO> fungsiKawasans;
    private List<KegiatanRantekPdfDTO> kegiatanRantekPdfs;
    private List<KegiatanRencanaRealisasiDTO> rencanaRealisasis;
    private List<KegiatanBastReboRehabDTO> bastReboRehabs;
    private String nomorBastPpkhKeDirjen;
    private String tanggalBastPpkhKeDirjen;
    private String nomorBastDirjenKeDishut;
    private String tanggalBastDirjenKeDishut;
    private String nomorBastDishutKePengelola;
    private String tanggalBastDishutKePengelola;
    private List<KegiatanBastZipDTO> kegiatanBastZips;
    private String namaPic;
    private String nomorPic;
    private String emailPic;

    public KegiatanDTO(Kegiatan entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.program = entity.getProgram() != null ? new ProgramDTO(entity.getProgram()) : null;
            this.subDirektorat = entity.getSubDirektorat() != null ? new Eselon3DTO(entity.getSubDirektorat()) : null;
            this.namaKegiatan = entity.getNamaKegiatan();
            this.tahun = entity.getTahun();
            this.provinsi = entity.getProvinsi() != null ? new ProvinsiDTO(entity.getProvinsi()) : null;
            this.kabupatenKota = entity.getKabupatenKota() != null ? new KabupatenKotaDTO(entity.getKabupatenKota()) : null;
            this.kecamatan = entity.getKecamatan() != null ? new KecamatanDTO(entity.getKecamatan()) : null;
            this.status = entity.getStatus() != null ? new LovDTO(entity.getStatus()) : null;
            this.pemegangIjinId = entity.getPemegangIjinId() != null ? new LovDTO(entity.getPemegangIjinId()) : null;
            this.peruntukanId = entity.getPeruntukanId() != null ? new LovDTO(entity.getPeruntukanId()) : null;
            this.namaPeruntukan = entity.getNamaPeruntukan();
            this.jenisIjinId = entity.getJenisIjinId() != null ? new LovDTO(entity.getJenisIjinId()) : null;
            this.nomorSkPerijinan = entity.getNomorSkPerijinan();
            
            this.tanggalSkPerijinan = entity.getTanggalSkPerijinan() != null ? 
                entity.getTanggalSkPerijinan().format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
            
            this.tanggalBerakhirSkPerijinan = entity.getTanggalBerakhirSkPerijinan() != null ? 
                entity.getTanggalBerakhirSkPerijinan().format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
            
            this.luasSesuaiSkPerijinanHa = entity.getLuasSesuaiSkPerijinanHa();
            
            if (entity.getKegiatanPerijinanPdfs() != null) {
                this.kegiatanPerijinanPdfs = entity.getKegiatanPerijinanPdfs().stream()
                    .map(KegiatanPerijinanPdfDTO::new)
                    .collect(Collectors.toList());
            } else {
                this.kegiatanPerijinanPdfs = new ArrayList<>();
            }
            
            if (entity.getRiwayatSks() != null) {
                this.riwayatSks = entity.getRiwayatSks().stream()
                    .map(KegiatanRiwayatSkDTO::new)
                    .collect(Collectors.toList());
            } else {
                this.riwayatSks = new ArrayList<>();
            }
            
            this.nomorSkPak = entity.getNomorSkPak();
            
            this.tanggalSkPak = entity.getTanggalSkPak() != null ? 
                entity.getTanggalSkPak().format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
                
            this.tanggalBerakhirSkPak = entity.getTanggalBerakhirSkPak() != null ? 
                entity.getTanggalBerakhirSkPak().format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
                
            this.luasSesuaiSkPakHa = entity.getLuasSesuaiSkPakHa();
            
            if (entity.getKegiatanPakPdfShps() != null) {
                this.kegiatanPakPdfShps = entity.getKegiatanPakPdfShps().stream()
                    .map(KegiatanPakPdfShpDTO::new)
                    .collect(Collectors.toList());
            } else {
                this.kegiatanPakPdfShps = new ArrayList<>();
            }
            
            this.nomorSkRehabilitasi = entity.getNomorSkRehabilitasi();
            
            this.tanggalSkRehabilitasi = entity.getTanggalSkRehabilitasi() != null ? 
                entity.getTanggalSkRehabilitasi().format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
                
            this.tanggalBerakhirSkRehabilitasi = entity.getTanggalBerakhirSkRehabilitasi() != null ? 
                entity.getTanggalBerakhirSkRehabilitasi().format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
                
            this.luasSkRehabDas = entity.getLuasSkRehabDas();
            this.bpdasRehab = entity.getBpdasRehab() != null ? new BpdasDTO(entity.getBpdasRehab()) : null;
            
            if (entity.getFungsiKawasans() != null) {
                this.fungsiKawasans = entity.getFungsiKawasans().stream()
                    .map(KegiatanFungsiKawasanDTO::new)
                    .collect(Collectors.toList());
            } else {
                this.fungsiKawasans = new ArrayList<>();
            }
            
            if (entity.getKegiatanRantekPdfs() != null) {
                this.kegiatanRantekPdfs = entity.getKegiatanRantekPdfs().stream()
                    .map(KegiatanRantekPdfDTO::new)
                    .collect(Collectors.toList());
            } else {
                this.kegiatanRantekPdfs = new ArrayList<>();
            }
            
            if (entity.getRencanaRealisasis() != null) {
                this.rencanaRealisasis = entity.getRencanaRealisasis().stream()
                    .map(KegiatanRencanaRealisasiDTO::new)
                    .collect(Collectors.toList());
            } else {
                this.rencanaRealisasis = new ArrayList<>();
            }
            
            if (entity.getBastReboRehabs() != null) {
                this.bastReboRehabs = entity.getBastReboRehabs().stream()
                    .map(KegiatanBastReboRehabDTO::new)
                    .collect(Collectors.toList());
            } else {
                this.bastReboRehabs = new ArrayList<>();
            }
            
            this.nomorBastPpkhKeDirjen = entity.getNomorBastPpkhKeDirjen();
            
            this.tanggalBastPpkhKeDirjen = entity.getTanggalBastPpkhKeDirjen() != null ? 
                entity.getTanggalBastPpkhKeDirjen().format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
                
            this.nomorBastDirjenKeDishut = entity.getNomorBastDirjenKeDishut();
            
            this.tanggalBastDirjenKeDishut = entity.getTanggalBastDirjenKeDishut() != null ? 
                entity.getTanggalBastDirjenKeDishut().format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
                
            this.nomorBastDishutKePengelola = entity.getNomorBastDishutKePengelola();
            
            this.tanggalBastDishutKePengelola = entity.getTanggalBastDishutKePengelola() != null ? 
                entity.getTanggalBastDishutKePengelola().format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
                
            if (entity.getKegiatanBastZips() != null) {
                this.kegiatanBastZips = entity.getKegiatanBastZips().stream()
                    .map(KegiatanBastZipDTO::new)
                    .collect(Collectors.toList());
            } else {
                this.kegiatanBastZips = new ArrayList<>();
            }
            
            this.namaPic = entity.getNamaPic();
            this.nomorPic = entity.getNomorPic();
            this.emailPic = entity.getEmailPic();
        }
    }
}
