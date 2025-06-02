package com.kehutanan.rh.kegiatan.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.rh.kegiatan.model.Kegiatan;
import com.kehutanan.rh.kegiatan.model.KegiatanBast;
import com.kehutanan.rh.kegiatan.model.KegiatanDokumentasiFoto;
import com.kehutanan.rh.kegiatan.model.KegiatanDokumentasiVideo;
import com.kehutanan.rh.kegiatan.model.KegiatanFungsiKawasan;
import com.kehutanan.rh.kegiatan.model.KegiatanKontrakPdf;
import com.kehutanan.rh.kegiatan.model.KegiatanLokus;
import com.kehutanan.rh.kegiatan.model.KegiatanMonev;
import com.kehutanan.rh.kegiatan.model.KegiatanPemeliharaanPemupukan;
import com.kehutanan.rh.kegiatan.model.KegiatanPemeliharaanSulaman;
import com.kehutanan.rh.kegiatan.model.KegiatanRancanganTeknisFoto;
import com.kehutanan.rh.kegiatan.model.KegiatanRancanganTeknisPdf;
import com.kehutanan.rh.kegiatan.model.KegiatanRancanganTeknisVideo;

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
    private String pemangkuKawasan;
    private Integer tahunKegiatan;
    private Integer totalBibit;
    private Double totalLuas;
    private String nomor;
    private Double nilai;
    private String tanggalKontrak;
    private String dokumentasiCatatanFoto;
    private String dokumentasiCatatanVideo;
    
    // Related entity IDs instead of full entities
    private Long eselon3Id;
    private String eselon3Nama;
    private Long programId;
    private String programNama;
    private Long jenisKegiatanId;
    private String jenisKegiatanNama;
    private Long referensiP0Id;
    private String referensiP0Nama;
    private Long polaId;
    private String polaNama;
    private Long sumberAnggaranId;
    private String sumberAnggaranNama;
    private Long pelaksanaId;
    private String pelaksanaNama;
    private Long tipeId;
    private String tipeNama;
    private Long penerimaManfaatId;
    private String penerimaManfaatNama;
    private Long statusId;
    private String statusNama;
    
    // Lists for related entities
    private List<KegiatanFileDTO> kegiatanRancanganTeknisPdfs = new ArrayList<>();
    private List<KegiatanFileDTO> kegiatanRancanganTeknisFotos = new ArrayList<>();
    private List<KegiatanFileDTO> kegiatanRancanganTeknisVideos = new ArrayList<>();
    private List<KegiatanFileDTO> kegiatanKontrakPdfs = new ArrayList<>();
    private List<KegiatanFileDTO> kegiatanDokumentasiFotos = new ArrayList<>();
    private List<KegiatanFileDTO> kegiatanDokumentasiVideos = new ArrayList<>();
    
    private List<KegiatanFungsiKawasanDTO> fungsiKawasans = new ArrayList<>();
    private List<KegiatanLokusDTO> kegiatanLokus = new ArrayList<>();
    private List<KegiatanPemeliharaanSulamanDTO> sulamanList = new ArrayList<>();
    private List<KegiatanPemeliharaanPemupukanDTO> pemupukanList = new ArrayList<>();
    private List<KegiatanMonevDTO> monevList = new ArrayList<>();
    private List<KegiatanBastDTO> serahTerimaList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public KegiatanDTO(Kegiatan entity) {
        System.out.println("Converting Kegiatan entity to DTO: " + entity.getId());
        this.id = entity.getId();
        this.namaKegiatan = entity.getNamaKegiatan();
        this.pemangkuKawasan = entity.getPemangkuKawasan();
        this.tahunKegiatan = entity.getTahunKegiatan();
        this.totalBibit = entity.getTotalBibit();
        this.totalLuas = entity.getTotalLuas();
        this.nomor = entity.getNomor();
        this.nilai = entity.getNilai();
        this.tanggalKontrak = entity.getTanggalKontrak();
        this.dokumentasiCatatanFoto = entity.getDokumentasiCatatanFoto();
        this.dokumentasiCatatanVideo = entity.getDokumentasiCatatanVideo();
        
        // Set related entity IDs and names
        if (entity.getEselon3() != null) {
            this.eselon3Id = entity.getEselon3().getId();
            this.eselon3Nama = entity.getEselon3().getNama(); // Assuming Eselon3 has a namaEselon3 field
        }
        
        if (entity.getProgram() != null) {
            this.programId = entity.getProgram().getId();
            this.programNama = entity.getProgram().getNama(); // Assuming Program has a namaProgram field
        }
        
        if (entity.getJenisKegiatanId() != null) {
            this.jenisKegiatanId = entity.getJenisKegiatanId().getId();
            this.jenisKegiatanNama = entity.getJenisKegiatanId().getNilai(); // Assuming Lov has a namaLov field
        }
        
        if (entity.getReferensiP0Id() != null) {
            this.referensiP0Id = entity.getReferensiP0Id().getId();
            this.referensiP0Nama = entity.getReferensiP0Id().getNilai();
        }
        
        if (entity.getPolaId() != null) {
            this.polaId = entity.getPolaId().getId();
            this.polaNama = entity.getPolaId().getNilai();
        }
        
        if (entity.getSumberAnggaranId() != null) {
            this.sumberAnggaranId = entity.getSumberAnggaranId().getId();
            this.sumberAnggaranNama = entity.getSumberAnggaranId().getNilai();
        }
        
        if (entity.getPelaksanaId() != null) {
            this.pelaksanaId = entity.getPelaksanaId().getId();
            this.pelaksanaNama = entity.getPelaksanaId().getNilai();
        }
        
        if (entity.getTipeId() != null) {
            this.tipeId = entity.getTipeId().getId();
            this.tipeNama = entity.getTipeId().getNilai();
        }
        
        if (entity.getPenerimaManfaatId() != null) {
            this.penerimaManfaatId = entity.getPenerimaManfaatId().getId();
            this.penerimaManfaatNama = entity.getPenerimaManfaatId().getNilai();
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = entity.getStatusId().getId();
            this.statusNama = entity.getStatusId().getNilai();
        }
        
        // Convert file lists
        if (entity.getKegiatanRancanganTeknisPdfs() != null) {
            for (KegiatanRancanganTeknisPdf pdf : entity.getKegiatanRancanganTeknisPdfs()) {
                this.kegiatanRancanganTeknisPdfs.add(new KegiatanFileDTO(pdf));
            }
        }
        
        if (entity.getKegiatanRancanganTeknisFotos() != null) {
            for (KegiatanRancanganTeknisFoto foto : entity.getKegiatanRancanganTeknisFotos()) {
                this.kegiatanRancanganTeknisFotos.add(new KegiatanFileDTO(foto));
            }
        }
        
        if (entity.getKegiatanRancanganTeknisVideos() != null) {
            for (KegiatanRancanganTeknisVideo video : entity.getKegiatanRancanganTeknisVideos()) {
                this.kegiatanRancanganTeknisVideos.add(new KegiatanFileDTO(video));
            }
        }
        
        if (entity.getKegiatanKontrakPdfs() != null) {
            for (KegiatanKontrakPdf pdf : entity.getKegiatanKontrakPdfs()) {
                this.kegiatanKontrakPdfs.add(new KegiatanFileDTO(pdf));
            }
        }
        
        if (entity.getKegiatanDokumentasiFotos() != null) {
            for (KegiatanDokumentasiFoto foto : entity.getKegiatanDokumentasiFotos()) {
                this.kegiatanDokumentasiFotos.add(new KegiatanFileDTO(foto));
            }
        }
        
        if (entity.getKegiatanDokumentasiVideos() != null) {
            for (KegiatanDokumentasiVideo video : entity.getKegiatanDokumentasiVideos()) {
                this.kegiatanDokumentasiVideos.add(new KegiatanFileDTO(video));
            }
        }
        
        // Convert related entities
        if (entity.getFungsiKawasans() != null) {
            for (KegiatanFungsiKawasan fungsiKawasan : entity.getFungsiKawasans()) {
                this.fungsiKawasans.add(new KegiatanFungsiKawasanDTO(fungsiKawasan));
            }
        }
        
        if (entity.getKegiatanLokus() != null) {
            for (KegiatanLokus lokus : entity.getKegiatanLokus()) {
                this.kegiatanLokus.add(new KegiatanLokusDTO(lokus));
            }
        }
        
        if (entity.getSulamanList() != null) {
            for (KegiatanPemeliharaanSulaman sulaman : entity.getSulamanList()) {
                this.sulamanList.add(new KegiatanPemeliharaanSulamanDTO(sulaman));
            }
        }
        
        if (entity.getPemupukanList() != null) {
            for (KegiatanPemeliharaanPemupukan pemupukan : entity.getPemupukanList()) {
                this.pemupukanList.add(new KegiatanPemeliharaanPemupukanDTO(pemupukan));
            }
        }
        
        if (entity.getMonevList() != null) {
            for (KegiatanMonev monev : entity.getMonevList()) {
                this.monevList.add(new KegiatanMonevDTO(monev));
            }
        }
        
        if (entity.getSerahTerimaList() != null) {
            for (KegiatanBast bast : entity.getSerahTerimaList()) {
                this.serahTerimaList.add(new KegiatanBastDTO(bast));
            }
        }
    }
}