package com.kehutanan.tktrh.bkta.kegiatan.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.tktrh.bkta.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanBast;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanDokumentasiFoto;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanDokumentasiVideo;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanKontrakPdf;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanLokus;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanMonev;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanRancanganTeknisFoto;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanRancanganTeknisPdf;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanRancanganTeknisShp;

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
    private Double totalAnggaran;
    private Boolean fungsiKawasanHk;
    private Boolean fungsiKawasanHl;
    private Boolean fungsiKawasanApl;
    private Boolean fungsiKawasanHpt;
    private String tahunKegiatan;
    private Double totalDpn;
    private Double totalGullyPlug;
    private String pemangkuKawasan;
    private String nomor;
    private String nilai;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date tanggalKontrak;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date tanggalBerakhirKontrak;
    
    private String catatanDokumentasiFoto;
    
    // Related entity IDs instead of full entities
    private Long subDirektoratId;
    private String subDirektoratNama;
    private Long programId;
    private String programNama;
    private Long sumberAnggaranId;
    private String sumberAnggaranNama;
    private Long skemaId;
    private String skemaNama;
    private Long pelaksanaId;
    private String pelaksanaNama;
    private Long statusId;
    private String statusNama;
    
    // Lists for related entities
    private List<KegiatanLokusDTO> lokusList = new ArrayList<>();
    private List<KegiatanFileDTO> rancanganTeknisPdfList = new ArrayList<>();
    private List<KegiatanFileDTO> rancanganTeknisFotoList = new ArrayList<>();
    private List<KegiatanFileDTO> rancanganTeknisShpList = new ArrayList<>();
    private List<KegiatanFileDTO> kontrakPdfList = new ArrayList<>();
    private List<KegiatanFileDTO> dokumentasiFotoList = new ArrayList<>();
    private List<KegiatanFileDTO> dokumentasiVideoList = new ArrayList<>();
    private List<KegiatanMonevDTO> monevList = new ArrayList<>();
    private List<KegiatanBastDTO> bastList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public KegiatanDTO(Kegiatan entity) {
        this.id = entity.getId();
        this.namaKegiatan = entity.getNamaKegiatan();
        this.totalAnggaran = entity.getTotalAnggaran();
        this.fungsiKawasanHk = entity.getFungsiKawasanHk();
        this.fungsiKawasanHl = entity.getFungsiKawasanHl();
        this.fungsiKawasanApl = entity.getFungsiKawasanApl();
        this.fungsiKawasanHpt = entity.getFungsiKawasanHpt();
        this.tahunKegiatan = entity.getTahunKegiatan();
        this.totalDpn = entity.getTotalDpn();
        this.totalGullyPlug = entity.getTotalGullyPlug();
        this.pemangkuKawasan = entity.getPemangkuKawasan();
        this.nomor = entity.getNomor();
        this.nilai = entity.getNilai();
        this.tanggalKontrak = entity.getTanggalKontrak();
        this.tanggalBerakhirKontrak = entity.getTanggalBerakhirKontrak();
        this.catatanDokumentasiFoto = entity.getCatatanDokumentasiFoto();
        
        // Set related entity IDs and names
        if (entity.getSubDirektorat() != null) {
            this.subDirektoratId = entity.getSubDirektorat().getId();
            this.subDirektoratNama = entity.getSubDirektorat().getNama();
        }
        
        if (entity.getProgram() != null) {
            this.programId = entity.getProgram().getId();
            this.programNama = entity.getProgram().getNama();
        }
        
        if (entity.getSumberAnggaran() != null) {
            this.sumberAnggaranId = entity.getSumberAnggaran().getId();
            this.sumberAnggaranNama = entity.getSumberAnggaran().getNilai();
        }
        
        if (entity.getSkema() != null) {
            this.skemaId = entity.getSkema().getId();
            this.skemaNama = entity.getSkema().getNilai();
        }
        
        if (entity.getPelaksana() != null) {
            this.pelaksanaId = entity.getPelaksana().getId();
            this.pelaksanaNama = entity.getPelaksana().getNilai();
        }
        
        if (entity.getStatus() != null) {
            this.statusId = entity.getStatus().getId();
            this.statusNama = entity.getStatus().getNilai();
        }
        
        // Convert lokus list
        if (entity.getLokus() != null) {
            for (KegiatanLokus lokus : entity.getLokus()) {
                this.lokusList.add(new KegiatanLokusDTO(lokus));
            }
        }
        
        // Convert file lists
        if (entity.getKegiatanRancanganTeknisPdfs() != null) {
            for (KegiatanRancanganTeknisPdf pdf : entity.getKegiatanRancanganTeknisPdfs()) {
                this.rancanganTeknisPdfList.add(new KegiatanFileDTO(pdf));
            }
        }
        
        if (entity.getKegiatanRancanganTeknisFotos() != null) {
            for (KegiatanRancanganTeknisFoto foto : entity.getKegiatanRancanganTeknisFotos()) {
                this.rancanganTeknisFotoList.add(new KegiatanFileDTO(foto));
            }
        }
        
        if (entity.getKegiatanRancanganTeknisShps() != null) {
            for (KegiatanRancanganTeknisShp shp : entity.getKegiatanRancanganTeknisShps()) {
                this.rancanganTeknisShpList.add(new KegiatanFileDTO(shp));
            }
        }
        
        if (entity.getKegiatanKontrakPdfs() != null) {
            for (KegiatanKontrakPdf pdf : entity.getKegiatanKontrakPdfs()) {
                this.kontrakPdfList.add(new KegiatanFileDTO(pdf));
            }
        }
        
        if (entity.getDokumentasiFotos() != null) {
            for (KegiatanDokumentasiFoto foto : entity.getDokumentasiFotos()) {
                this.dokumentasiFotoList.add(new KegiatanFileDTO(foto));
            }
        }
        
        if (entity.getDokumentasiVideos() != null) {
            for (KegiatanDokumentasiVideo video : entity.getDokumentasiVideos()) {
                this.dokumentasiVideoList.add(new KegiatanFileDTO(video));
            }
        }
        
        // Convert monev list
        if (entity.getMonevs() != null) {
            for (KegiatanMonev monev : entity.getMonevs()) {
                this.monevList.add(new KegiatanMonevDTO(monev));
            }
        }
        
        // Convert bast list
        if (entity.getBasts() != null) {
            for (KegiatanBast bast : entity.getBasts()) {
                this.bastList.add(new KegiatanBastDTO(bast));
            }
        }
    }
}