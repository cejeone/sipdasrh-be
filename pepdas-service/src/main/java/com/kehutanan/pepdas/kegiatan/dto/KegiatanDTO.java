package com.kehutanan.pepdas.kegiatan.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.pepdas.kegiatan.model.Kegiatan;
import com.kehutanan.pepdas.kegiatan.model.KegiatanDokumentasiFoto;
import com.kehutanan.pepdas.kegiatan.model.KegiatanDokumentasiVideo;
import com.kehutanan.pepdas.kegiatan.model.KegiatanKontrakPdf;
import com.kehutanan.pepdas.kegiatan.model.KegiatanLokusShp;
import com.kehutanan.pepdas.kegiatan.model.KegiatanRancanganTeknisFoto;
import com.kehutanan.pepdas.kegiatan.model.KegiatanRancanganTeknisPdf;
import com.kehutanan.pepdas.kegiatan.model.KegiatanRancanganTeknisVideo;

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
    private Integer tahunKegiatan;
    private String alamat;
    private String nomorKontrak;
    private Integer nilaiKontrak;
    private String dokumentasiCatatanFoto;
    private String catatanVideo;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String tanggalKontrak;
    
    // Related entity IDs with their names
    private Long eselon2Id;
    private String eselon2Nama;
    private Long programId;
    private String programNama;
    private Long jenisKegiatanId;
    private String jenisKegiatanNama;
    private Long bpdasId;
    private String bpdasNama;
    private Long dasId;
    private String dasNama;
    private Long provinsiId;
    private String provinsiNama;
    private Long kabupatenKotaId;
    private String kabupatenKotaNama;
    private Long kecamatanId;
    private String kecamatanNama;
    private Long kelurahanDesaId;
    private String kelurahanDesaNama;
    private Long skemaId;
    private String skemanNama;
    private Long sumberAnggaranId;
    private String sumberAnggaranNama;
    private Long penerimaManfaatKegiatanId;
    private String penerimaManfaatKegiatanNama;
    private Long detailPelaksanaId;
    private String detailPelaksanaNama;
    private Long tipeId;
    private String tipeNama;
    private Long penerimaManfaatKontrakId;
    private String penerimaManfaatKontrakNama;
    private Long statusId;
    private String statusNama;
    
    // Lists for file references
    private List<KegiatanFileDTO> rancanganTeknisPdfList = new ArrayList<>();
    private List<KegiatanFileDTO> rancanganTeknisFotoList = new ArrayList<>();
    private List<KegiatanFileDTO> rancanganTeknisVideoList = new ArrayList<>();
    private List<KegiatanFileDTO> kontrakPdfList = new ArrayList<>();
    private List<KegiatanFileDTO> dokumentasiFotoList = new ArrayList<>();
    private List<KegiatanFileDTO> dokumentasiVideoList = new ArrayList<>();
    private List<KegiatanFileDTO> lokusShpList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public KegiatanDTO(Kegiatan entity) {
        this.id = entity.getId();
        this.namaKegiatan = entity.getNamaKegiatan();
        this.tahunKegiatan = entity.getTahunKegiatan();
        this.alamat = entity.getAlamat();
        this.nomorKontrak = entity.getNomorKontrak();
        this.nilaiKontrak = entity.getNilaiKontrak();
        this.dokumentasiCatatanFoto = entity.getDokumentasiCatatanFoto();
        this.catatanVideo = entity.getDokumentasiCatatanVideo();
        
        // Convert LocalDate to String
        if (entity.getTanggalKontrak() != null) {
            this.tanggalKontrak = entity.getTanggalKontrak().toString();
        }
        
        // Set related entity IDs and names
        if (entity.getEselon2() != null) {
            this.eselon2Id = entity.getEselon2().getId();
            this.eselon2Nama = entity.getEselon2().getNama();
        }
        
        if (entity.getProgram() != null) {
            this.programId = entity.getProgram().getId();
            this.programNama = entity.getProgram().getNama();
        }
        
        if (entity.getJenisKegiatan() != null) {
            this.jenisKegiatanId = entity.getJenisKegiatan().getId();
            this.jenisKegiatanNama = entity.getJenisKegiatan().getNilai();
        }
        
        if (entity.getBpdas() != null) {
            this.bpdasId = entity.getBpdas().getId();
            this.bpdasNama = entity.getBpdas().getNamaBpdas();
        }
        
        if (entity.getDas() != null) {
            this.dasId = entity.getDas().getId();
            this.dasNama = entity.getDas().getNamaDas();
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
        
        if (entity.getKelurahanDesa() != null) {
            this.kelurahanDesaId = entity.getKelurahanDesa().getId();
            this.kelurahanDesaNama = entity.getKelurahanDesa().getKelurahan();
        }
        
        if (entity.getSkema() != null) {
            this.skemaId = entity.getSkema().getId();
            this.skemanNama = entity.getSkema().getNilai();
        }
        
        if (entity.getSumberAnggaran() != null) {
            this.sumberAnggaranId = entity.getSumberAnggaran().getId();
            this.sumberAnggaranNama = entity.getSumberAnggaran().getNilai();
        }
        
        if (entity.getPenerimaManfaatKegiatan() != null) {
            this.penerimaManfaatKegiatanId = entity.getPenerimaManfaatKegiatan().getId();
            this.penerimaManfaatKegiatanNama = entity.getPenerimaManfaatKegiatan().getNilai();
        }
        
        if (entity.getDetailPelaksana() != null) {
            this.detailPelaksanaId = entity.getDetailPelaksana().getId();
            this.detailPelaksanaNama = entity.getDetailPelaksana().getNilai();
        }
        
        if (entity.getTipe() != null) {
            this.tipeId = entity.getTipe().getId();
            this.tipeNama = entity.getTipe().getNilai();
        }
        
        if (entity.getPenerimaManfaatKontrak() != null) {
            this.penerimaManfaatKontrakId = entity.getPenerimaManfaatKontrak().getId();
            this.penerimaManfaatKontrakNama = entity.getPenerimaManfaatKontrak().getNilai();
        }
        
        if (entity.getStatus() != null) {
            this.statusId = entity.getStatus().getId();
            this.statusNama = entity.getStatus().getNilai();
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
        
        if (entity.getKegiatanRancanganTeknisVideos() != null) {
            for (KegiatanRancanganTeknisVideo video : entity.getKegiatanRancanganTeknisVideos()) {
                this.rancanganTeknisVideoList.add(new KegiatanFileDTO(video));
            }
        }
        
        if (entity.getKegiatanKontrakPdfs() != null) {
            for (KegiatanKontrakPdf pdf : entity.getKegiatanKontrakPdfs()) {
                this.kontrakPdfList.add(new KegiatanFileDTO(pdf));
            }
        }
        
        if (entity.getKegiatanDokumentasiFotos() != null) {
            for (KegiatanDokumentasiFoto foto : entity.getKegiatanDokumentasiFotos()) {
                this.dokumentasiFotoList.add(new KegiatanFileDTO(foto));
            }
        }
        
        if (entity.getKegiatanDokumentasiVideos() != null) {
            for (KegiatanDokumentasiVideo video : entity.getKegiatanDokumentasiVideos()) {
                this.dokumentasiVideoList.add(new KegiatanFileDTO(video));
            }
        }
        
        if (entity.getKegiatanLokusShps() != null) {
            for (KegiatanLokusShp shp : entity.getKegiatanLokusShps()) {
                this.lokusShpList.add(new KegiatanFileDTO(shp));
            }
        }
    }
}