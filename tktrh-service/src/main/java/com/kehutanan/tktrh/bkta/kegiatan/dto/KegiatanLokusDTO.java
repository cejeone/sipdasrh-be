package com.kehutanan.tktrh.bkta.kegiatan.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanLokus;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanLokusBangunanPdf;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanLokusLokasiPdf;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanLokusProposalPdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanLokusDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long kegiatanId;
    private String identitasBkta;
    
    // Location information
    private Long provinsiId;
    private String provinsiNama;
    private Long kabupatenKotaId;
    private String kabupatenKotaNama;
    private Long kecamatanId;
    private String kecamatanNama;
    private Long kelurahanDesaId;
    private String kelurahanDesaNama;
    private Long bpdasId;
    private String bpdasNama;
    private Long dasId;
    private String dasNama;
    private String subDas;
    
    // Building details
    private Long jenisBangunanId;
    private String jenisBangunanNama;
    private Double koordinatX;
    private Double koordinatY;
    private Double lebarAlurAtas;
    private Double lebarAlurBawah;
    private Double panjang;
    private Double tinggi;
    private Double lebar;
    private Double volumeBangunan;
    private Double dayaTampungSedimen;
    
    // Status and beneficiaries
    private Long statusId;
    private String statusNama;
    private Long penerimaManfaatId;
    private String penerimaManfaatNama;
    private Double anggaran;
    
    // Additional information
    private String keterangan;
    private String catatan;
    private Boolean informasiTambahan;
    private Boolean integrasiRhl;
    private Boolean rawanBencana;
    private Boolean dasPrioritas;
    private Boolean perlindunganMataAir;
    private Boolean danauPrioritas;
    private Boolean pengendalianErosiDanSedimentasi;
    
    // File lists
    private List<KegiatanLokusFileDTO> lokusProposalPdfList = new ArrayList<>();
    private List<KegiatanLokusFileDTO> lokusLokasiPdfList = new ArrayList<>();
    private List<KegiatanLokusFileDTO> lokusBangunanPdfList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public KegiatanLokusDTO(KegiatanLokus entity) {
        this.id = entity.getId();
        this.identitasBkta = entity.getIdentitasBkta();
        
        // Set kegiatan ID
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
        }
        
        // Set location information
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
        
        if (entity.getBpdas() != null) {
            this.bpdasId = entity.getBpdas().getId();
            this.bpdasNama = entity.getBpdas().getNamaBpdas();
        }
        
        if (entity.getDas() != null) {
            this.dasId = entity.getDas().getId();
            this.dasNama = entity.getDas().getNamaDas();
        }
        
        this.subDas = entity.getSubDas();
        
        // Set building details
        if (entity.getJenisBangunan() != null) {
            this.jenisBangunanId = entity.getJenisBangunan().getId();
            this.jenisBangunanNama = entity.getJenisBangunan().getNilai();
        }
        
        this.koordinatX = entity.getKoordinatX();
        this.koordinatY = entity.getKoordinatY();
        this.lebarAlurAtas = entity.getLebarAlurAtas();
        this.lebarAlurBawah = entity.getLebarAlurBawah();
        this.panjang = entity.getPanjang();
        this.tinggi = entity.getTinggi();
        this.lebar = entity.getLebar();
        this.volumeBangunan = entity.getVolumeBangunan();
        this.dayaTampungSedimen = entity.getDayaTampungSedimen();
        
        // Set status and beneficiaries
        if (entity.getStatus() != null) {
            this.statusId = entity.getStatus().getId();
            this.statusNama = entity.getStatus().getNilai();
        }
        
        if (entity.getPenerimaManfaat() != null) {
            this.penerimaManfaatId = entity.getPenerimaManfaat().getId();
            this.penerimaManfaatNama = entity.getPenerimaManfaat().getNilai();
        }
        
        this.anggaran = entity.getAnggaran();
        
        // Set additional information
        this.keterangan = entity.getKeterangan();
        this.catatan = entity.getCatatan();
        this.informasiTambahan = entity.getInformasiTambahan();
        this.integrasiRhl = entity.getIntegrasiRhl();
        this.rawanBencana = entity.getRawanBencana();
        this.dasPrioritas = entity.getDasPrioritas();
        this.perlindunganMataAir = entity.getPerlindunganMataAir();
        this.danauPrioritas = entity.getDanauPrioritas();
        this.pengendalianErosiDanSedimentasi = entity.getPengendalianErosiDanSedimentasi();
        
        // Convert file lists
        if (entity.getLokusProposalPdfs() != null) {
            for (KegiatanLokusProposalPdf pdf : entity.getLokusProposalPdfs()) {
                this.lokusProposalPdfList.add(new KegiatanLokusFileDTO(pdf));
            }
        }
        
        if (entity.getLokusLokasiPdfs() != null) {
            for (KegiatanLokusLokasiPdf pdf : entity.getLokusLokasiPdfs()) {
                this.lokusLokasiPdfList.add(new KegiatanLokusFileDTO(pdf));
            }
        }
        
        if (entity.getLokusBangunanPdfs() != null) {
            for (KegiatanLokusBangunanPdf pdf : entity.getLokusBangunanPdfs()) {
                this.lokusBangunanPdfList.add(new KegiatanLokusFileDTO(pdf));
            }
        }
    }
}