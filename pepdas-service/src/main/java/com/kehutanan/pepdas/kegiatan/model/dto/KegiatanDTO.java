package com.kehutanan.pepdas.kegiatan.model.dto;

import com.kehutanan.pepdas.kegiatan.model.Kegiatan;
import com.kehutanan.pepdas.master.model.dto.BpdasDTO;
import com.kehutanan.pepdas.master.model.dto.DasDTO;
import com.kehutanan.pepdas.master.model.dto.Eselon2DTO;
import com.kehutanan.pepdas.master.model.dto.KabupatenKotaDTO;
import com.kehutanan.pepdas.master.model.dto.KecamatanDTO;
import com.kehutanan.pepdas.master.model.dto.KelurahanDesaDTO;
import com.kehutanan.pepdas.master.model.dto.LovDTO;
import com.kehutanan.pepdas.master.model.dto.ProvinsiDTO;
import com.kehutanan.pepdas.program.model.dto.ProgramDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class KegiatanDTO {
    
    private Long id;
    private Eselon2DTO eselon2;
    private ProgramDTO program;
    private LovDTO jenisKegiatan;
    private String namaKegiatan;
    private BpdasDTO bpdas;
    private DasDTO das;
    private ProvinsiDTO provinsi;
    private KabupatenKotaDTO kabupatenKota;
    private KecamatanDTO kecamatan;
    private KelurahanDesaDTO kelurahanDesa;
    private String alamat;
    private List<KegiatanLokusShpDTO> kegiatanLokusShps = new ArrayList<>();
    private LovDTO skema;
    private Integer tahunKegiatan;
    private LovDTO sumberAnggaran;
    private LovDTO penerimaManfaatKegiatan;
    private LovDTO detailPelaksana;
    private List<KegiatanRancanganTeknisPdfDTO> kegiatanRancanganTeknisPdfs = new ArrayList<>();
    private List<KegiatanRancanganTeknisFotoDTO> kegiatanRancanganTeknisFotos = new ArrayList<>();
    private List<KegiatanRancanganTeknisVideoDTO> kegiatanRancanganTeknisVideos = new ArrayList<>();
    private String nomorKontrak;
    private Integer nilaiKontrak;
    private LovDTO tipe;
    private LovDTO penerimaManfaatKontrak;
    private String tanggalKontrak;
    private List<KegiatanKontrakPdfDTO> kegiatanKontrakPdfs = new ArrayList<>();
    private LovDTO status;
    private List<KegiatanDokumentasiFotoDTO> kegiatanDokumentasiFotos = new ArrayList<>();
    private String dokumentasiCatatanFoto;
    private List<KegiatanDokumentasiVideoDTO> kegiatanDokumentasiVideos = new ArrayList<>();
    private String dokumentasiCatatanVideo;

    public KegiatanDTO(Kegiatan kegiatan) {
        this.id = kegiatan.getId();
        
        // Handle references to other entities using their respective DTOs
        if (kegiatan.getEselon2() != null) {
            this.eselon2 = new Eselon2DTO(kegiatan.getEselon2());
        }
        
        if (kegiatan.getProgram() != null) {
            this.program = new ProgramDTO(kegiatan.getProgram());
        }
        
        if (kegiatan.getJenisKegiatan() != null) {
            this.jenisKegiatan = new LovDTO(kegiatan.getJenisKegiatan());
        }
        
        this.namaKegiatan = kegiatan.getNamaKegiatan();
        
        if (kegiatan.getBpdas() != null) {
            this.bpdas = new BpdasDTO(kegiatan.getBpdas());
        }
        
        if (kegiatan.getDas() != null) {
            this.das = new DasDTO(kegiatan.getDas());
        }
        
        if (kegiatan.getProvinsi() != null) {
            this.provinsi = new ProvinsiDTO(kegiatan.getProvinsi());
        }
        
        if (kegiatan.getKabupatenKota() != null) {
            this.kabupatenKota = new KabupatenKotaDTO(kegiatan.getKabupatenKota());
        }
        
        if (kegiatan.getKecamatan() != null) {
            this.kecamatan = new KecamatanDTO(kegiatan.getKecamatan());
        }
        
        if (kegiatan.getKelurahanDesa() != null) {
            this.kelurahanDesa = new KelurahanDesaDTO(kegiatan.getKelurahanDesa());
        }
        
        this.alamat = kegiatan.getAlamat();
        
        // Convert list of related entities to list of DTOs
        if (kegiatan.getKegiatanLokusShps() != null) {
            this.kegiatanLokusShps = kegiatan.getKegiatanLokusShps().stream()
                    .map(KegiatanLokusShpDTO::new)
                    .collect(Collectors.toList());
        }
        
        if (kegiatan.getSkema() != null) {
            this.skema = new LovDTO(kegiatan.getSkema());
        }
        
        this.tahunKegiatan = kegiatan.getTahunKegiatan();
        
        if (kegiatan.getSumberAnggaran() != null) {
            this.sumberAnggaran = new LovDTO(kegiatan.getSumberAnggaran());
        }
        
        if (kegiatan.getPenerimaManfaatKegiatan() != null) {
            this.penerimaManfaatKegiatan = new LovDTO(kegiatan.getPenerimaManfaatKegiatan());
        }
        
        if (kegiatan.getDetailPelaksana() != null) {
            this.detailPelaksana = new LovDTO(kegiatan.getDetailPelaksana());
        }
        
        if (kegiatan.getKegiatanRancanganTeknisPdfs() != null) {
            this.kegiatanRancanganTeknisPdfs = kegiatan.getKegiatanRancanganTeknisPdfs().stream()
                    .map(KegiatanRancanganTeknisPdfDTO::new)
                    .collect(Collectors.toList());
        }
        
        if (kegiatan.getKegiatanRancanganTeknisFotos() != null) {
            this.kegiatanRancanganTeknisFotos = kegiatan.getKegiatanRancanganTeknisFotos().stream()
                    .map(KegiatanRancanganTeknisFotoDTO::new)
                    .collect(Collectors.toList());
        }
        
        if (kegiatan.getKegiatanRancanganTeknisVideos() != null) {
            this.kegiatanRancanganTeknisVideos = kegiatan.getKegiatanRancanganTeknisVideos().stream()
                    .map(KegiatanRancanganTeknisVideoDTO::new)
                    .collect(Collectors.toList());
        }
        
        this.nomorKontrak = kegiatan.getNomorKontrak();
        this.nilaiKontrak = kegiatan.getNilaiKontrak();
        
        if (kegiatan.getTipe() != null) {
            this.tipe = new LovDTO(kegiatan.getTipe());
        }
        
        if (kegiatan.getPenerimaManfaatKontrak() != null) {
            this.penerimaManfaatKontrak = new LovDTO(kegiatan.getPenerimaManfaatKontrak());
        }
        
        this.tanggalKontrak = kegiatan.getTanggalKontrak() != null ? 
                kegiatan.getTanggalKontrak().toString() : null;
        
        if (kegiatan.getKegiatanKontrakPdfs() != null) {
            this.kegiatanKontrakPdfs = kegiatan.getKegiatanKontrakPdfs().stream()
                    .map(KegiatanKontrakPdfDTO::new)
                    .collect(Collectors.toList());
        }
        
        if (kegiatan.getStatus() != null) {
            this.status = new LovDTO(kegiatan.getStatus());
        }
        
        if (kegiatan.getKegiatanDokumentasiFotos() != null) {
            this.kegiatanDokumentasiFotos = kegiatan.getKegiatanDokumentasiFotos().stream()
                    .map(KegiatanDokumentasiFotoDTO::new)
                    .collect(Collectors.toList());
        }
        
        this.dokumentasiCatatanFoto = kegiatan.getDokumentasiCatatanFoto();
        
        if (kegiatan.getKegiatanDokumentasiVideos() != null) {
            this.kegiatanDokumentasiVideos = kegiatan.getKegiatanDokumentasiVideos().stream()
                    .map(KegiatanDokumentasiVideoDTO::new)
                    .collect(Collectors.toList());
        }
        
        this.dokumentasiCatatanVideo = kegiatan.getDokumentasiCatatanVideo();
    }
}