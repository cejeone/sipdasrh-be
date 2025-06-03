package com.kehutanan.pepdas.master.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.pepdas.master.model.Uptd;
import com.kehutanan.pepdas.master.model.UptdDokumentasiFoto;
import com.kehutanan.pepdas.master.model.UptdDokumentasiVideo;
import com.kehutanan.pepdas.master.model.UptdPetaShp;
import com.kehutanan.pepdas.master.model.UptdRantekPdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UptdDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String namaUptd;
    private String alamat;
    private String telepon;
    private Boolean sertifikasiSumberBenih;
    private Boolean sertifikasiMutuBenih;
    private Boolean sertifikasiMutuBibit;
    private Integer jumlahAsesorSumberBenih;
    private Integer jumlahAsesorMutuBenih;
    private Integer jumlahAsesorMutuBibit;
    private String nomorSkPenetapan;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String tanggal;
    
    private String namaKontak;
    private String nomorTeleponKontak;
    private String catatanDokumen;
    private String catatan;
    private BigDecimal lintang;
    private BigDecimal bujur;
    private BigDecimal luas;
    
    // Related entity IDs instead of full entities
    private Long bpdasId;
    private String bpdasNama;
    private Long provinsiId;
    private String provinsiNama;
    private Long kabupatenKotaId;
    private String kabupatenKotaNama;
    private Long kecamatanId;
    private String kecamatanNama;
    private Long kelurahanDesaId;
    private String kelurahanDesaNama;
    
    // Lists for file references
    private List<UptdFileDTO> uptdPdfList = new ArrayList<>();
    private List<UptdFileDTO> uptdFotoList = new ArrayList<>();
    private List<UptdFileDTO> uptdVideoList = new ArrayList<>();
    private List<UptdFileDTO> uptdShpList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public UptdDTO(Uptd entity) {
        System.out.println("Converting Uptd entity to DTO: " + entity.getId());
        this.id = entity.getId();
        this.namaUptd = entity.getNamaUptd();
        this.alamat = entity.getAlamat();
        this.telepon = entity.getTelepon();
        this.sertifikasiSumberBenih = entity.getSertifikasiSumberBenih();
        this.sertifikasiMutuBenih = entity.getSertifikasiMutuBenih();
        this.sertifikasiMutuBibit = entity.getSertifikasiMutuBibit();
        this.jumlahAsesorSumberBenih = entity.getJumlahAsesorSumberBenih();
        this.jumlahAsesorMutuBenih = entity.getJumlahAsesorMutuBenih();
        this.jumlahAsesorMutuBibit = entity.getJumlahAsesorMutuBibit();
        this.nomorSkPenetapan = entity.getNomorSkPenetapan();
        
        // Convert LocalDateTime to String
        if (entity.getTanggal() != null) {
            this.tanggal = entity.getTanggal().toString();
        }
        
        this.namaKontak = entity.getNamaKontak();
        this.nomorTeleponKontak = entity.getNomorTeleponKontak();
        this.catatanDokumen = entity.getCatatanDokumen();
        this.catatan = entity.getCatatan();
        this.lintang = entity.getLintang();
        this.bujur = entity.getBujur();
        this.luas = entity.getLuas();
        
        // Set related entity IDs and names
        if (entity.getBpdas() != null) {
            this.bpdasId = entity.getBpdas().getId();
            this.bpdasNama = entity.getBpdas().getNamaBpdas();
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
        
        // Convert file lists
        if (entity.getUptdRantekPdfs() != null) {
            for (UptdRantekPdf pdf : entity.getUptdRantekPdfs()) {
                this.uptdPdfList.add(new UptdFileDTO(pdf));
            }
        }
        
        if (entity.getUptdDokumentasiFotos() != null) {
            for (UptdDokumentasiFoto foto : entity.getUptdDokumentasiFotos()) {
                this.uptdFotoList.add(new UptdFileDTO(foto));
            }
        }
        
        if (entity.getUptdDokumentasiVideos() != null) {
            for (UptdDokumentasiVideo video : entity.getUptdDokumentasiVideos()) {
                this.uptdVideoList.add(new UptdFileDTO(video));
            }
        }
        
        if (entity.getUptdPetaShps() != null) {
            for (UptdPetaShp shp : entity.getUptdPetaShps()) {
                this.uptdShpList.add(new UptdFileDTO(shp));
            }
        }
    }

}