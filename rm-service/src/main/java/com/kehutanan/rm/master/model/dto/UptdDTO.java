package com.kehutanan.rm.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.rm.master.model.Uptd;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UptdDTO {
    private Long id;
    private String alamat;
    private String telepon;
    private Boolean sertifikasiSumberBenih;
    private Boolean sertifikasiMutuBenih;
    private Boolean sertifikasiMutuBibit;
    private Integer jumlahAsesorSumberBenih;
    private Integer jumlahAsesorMutuBenih;
    private Integer jumlahAsesorMutuBibit;
    private String nomorSkPenetapan;
    private LocalDateTime tanggal;
    private String namaKontak;
    private String nomorTeleponKontak;
    private String catatanDokumen;
    private String catatan;
    private BigDecimal lintang;
    private BigDecimal bujur;
    private BigDecimal luas;
    private String namaUptd;
    private BpdasDTO bpdas;
    private ProvinsiDTO provinsi;
    private KabupatenKotaDTO kabupatenKota;
    private KecamatanDTO kecamatan;
    private KelurahanDesaDTO kelurahanDesa;
    private List<UptdRantekPdfDTO> uptdRantekPdfs;
    private List<UptdDokumentasiFotoDTO> uptdDokumentasiFotos;
    private List<UptdDokumentasiVideoDTO> uptdDokumentasiVideos;
    private List<UptdPetaShpDTO> uptdPetaShps;
    
    public UptdDTO(Uptd uptd) {
        if (uptd != null) {
            this.id = uptd.getId();
            this.alamat = uptd.getAlamat();
            this.telepon = uptd.getTelepon();
            this.sertifikasiSumberBenih = uptd.getSertifikasiSumberBenih();
            this.sertifikasiMutuBenih = uptd.getSertifikasiMutuBenih();
            this.sertifikasiMutuBibit = uptd.getSertifikasiMutuBibit();
            this.jumlahAsesorSumberBenih = uptd.getJumlahAsesorSumberBenih();
            this.jumlahAsesorMutuBenih = uptd.getJumlahAsesorMutuBenih();
            this.jumlahAsesorMutuBibit = uptd.getJumlahAsesorMutuBibit();
            this.nomorSkPenetapan = uptd.getNomorSkPenetapan();
            this.tanggal = uptd.getTanggal();
            this.namaKontak = uptd.getNamaKontak();
            this.nomorTeleponKontak = uptd.getNomorTeleponKontak();
            this.catatanDokumen = uptd.getCatatanDokumen();
            this.catatan = uptd.getCatatan();
            this.lintang = uptd.getLintang();
            this.bujur = uptd.getBujur();
            this.luas = uptd.getLuas();
            this.namaUptd = uptd.getNamaUptd();
            
            this.bpdas = uptd.getBpdas() != null ? 
                new BpdasDTO(uptd.getBpdas()) : null;
            this.provinsi = uptd.getProvinsi() != null ? 
                new ProvinsiDTO(uptd.getProvinsi()) : null;
            this.kabupatenKota = uptd.getKabupatenKota() != null ? 
                new KabupatenKotaDTO(uptd.getKabupatenKota()) : null;
            this.kecamatan = uptd.getKecamatan() != null ? 
                new KecamatanDTO(uptd.getKecamatan()) : null;
            this.kelurahanDesa = uptd.getKelurahanDesa() != null ? 
                new KelurahanDesaDTO(uptd.getKelurahanDesa()) : null;
            
            if (uptd.getUptdRantekPdfs() != null) {
                this.uptdRantekPdfs = uptd.getUptdRantekPdfs().stream()
                    .map(UptdRantekPdfDTO::new)
                    .collect(Collectors.toList());
            }
            
            if (uptd.getUptdDokumentasiFotos() != null) {
                this.uptdDokumentasiFotos = uptd.getUptdDokumentasiFotos().stream()
                    .map(UptdDokumentasiFotoDTO::new)
                    .collect(Collectors.toList());
            }
            
            if (uptd.getUptdDokumentasiVideos() != null) {
                this.uptdDokumentasiVideos = uptd.getUptdDokumentasiVideos().stream()
                    .map(UptdDokumentasiVideoDTO::new)
                    .collect(Collectors.toList());
            }
            
            if (uptd.getUptdPetaShps() != null) {
                this.uptdPetaShps = uptd.getUptdPetaShps().stream()
                    .map(UptdPetaShpDTO::new)
                    .collect(Collectors.toList());
            }
        }
    }
}