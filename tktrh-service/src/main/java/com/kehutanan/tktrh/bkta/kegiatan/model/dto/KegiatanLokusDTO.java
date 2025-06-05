package com.kehutanan.tktrh.bkta.kegiatan.model.dto;

import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanLokus;
import com.kehutanan.tktrh.master.model.dto.BpdasDTO;
import com.kehutanan.tktrh.master.model.dto.DasDTO;
import com.kehutanan.tktrh.master.model.dto.KabupatenKotaDTO;
import com.kehutanan.tktrh.master.model.dto.KecamatanDTO;
import com.kehutanan.tktrh.master.model.dto.KelurahanDesaDTO;
import com.kehutanan.tktrh.master.model.dto.LovDTO;
import com.kehutanan.tktrh.master.model.dto.ProvinsiDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class KegiatanLokusDTO {

    private Long id;
    private ProvinsiDTO provinsi;
    private KabupatenKotaDTO kabupatenKota;
    private KecamatanDTO kecamatan;
    private KelurahanDesaDTO kelurahanDesa;
    private BpdasDTO bpdas;
    private DasDTO das;
    private String subDas;
    private LovDTO status;
    private String identitasBkta;
    private LovDTO jenisBangunan;
    private Double koordinatX;
    private Double koordinatY;
    private Double lebarAlurAtas;
    private Double lebarAlurBawah;
    private Double panjang;
    private Double tinggi;
    private Double lebar;
    private Double volumeBangunan;
    private Double dayaTampungSedimen;
    private LovDTO penerimaManfaat;
    private Double anggaran;
    private List<KegiatanLokusProposalPdfDTO> lokusProposalPdfs = new ArrayList<>();
    private String keterangan;
    private Boolean informasiTambahan;
    private Boolean integrasiRhl;
    private Boolean rawanBencana;
    private Boolean dasPrioritas;
    private Boolean perlindunganMataAir;
    private Boolean danauPrioritas;
    private Boolean pengendalianErosiDanSedimentasi;
    private String catatan;
    private List<KegiatanLokusLokasiPdfDTO> lokusLokasiPdfs = new ArrayList<>();
    private List<KegiatanLokusBangunanPdfDTO> lokusBangunanPdfs = new ArrayList<>();

    public KegiatanLokusDTO(KegiatanLokus entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.provinsi = entity.getProvinsi() != null ? new ProvinsiDTO(entity.getProvinsi()) : null;
            this.kabupatenKota = entity.getKabupatenKota() != null ? new KabupatenKotaDTO(entity.getKabupatenKota()) : null;
            this.kecamatan = entity.getKecamatan() != null ? new KecamatanDTO(entity.getKecamatan()) : null;
            this.kelurahanDesa = entity.getKelurahanDesa() != null ? new KelurahanDesaDTO(entity.getKelurahanDesa()) : null;
            this.bpdas = entity.getBpdas() != null ? new BpdasDTO(entity.getBpdas()) : null;
            this.das = entity.getDas() != null ? new DasDTO(entity.getDas()) : null;
            this.subDas = entity.getSubDas();
            this.status = entity.getStatus() != null ? new LovDTO(entity.getStatus()) : null;
            this.identitasBkta = entity.getIdentitasBkta();
            this.jenisBangunan = entity.getJenisBangunan() != null ? new LovDTO(entity.getJenisBangunan()) : null;
            this.koordinatX = entity.getKoordinatX();
            this.koordinatY = entity.getKoordinatY();
            this.lebarAlurAtas = entity.getLebarAlurAtas();
            this.lebarAlurBawah = entity.getLebarAlurBawah();
            this.panjang = entity.getPanjang();
            this.tinggi = entity.getTinggi();
            this.lebar = entity.getLebar();
            this.volumeBangunan = entity.getVolumeBangunan();
            this.dayaTampungSedimen = entity.getDayaTampungSedimen();
            this.penerimaManfaat = entity.getPenerimaManfaat() != null ? new LovDTO(entity.getPenerimaManfaat()) : null;
            this.anggaran = entity.getAnggaran();
            
            if (entity.getLokusProposalPdfs() != null) {
                this.lokusProposalPdfs = entity.getLokusProposalPdfs().stream()
                    .map(KegiatanLokusProposalPdfDTO::new)
                    .collect(Collectors.toList());
            }
            
            this.keterangan = entity.getKeterangan();
            this.informasiTambahan = entity.getInformasiTambahan();
            this.integrasiRhl = entity.getIntegrasiRhl();
            this.rawanBencana = entity.getRawanBencana();
            this.dasPrioritas = entity.getDasPrioritas();
            this.perlindunganMataAir = entity.getPerlindunganMataAir();
            this.danauPrioritas = entity.getDanauPrioritas();
            this.pengendalianErosiDanSedimentasi = entity.getPengendalianErosiDanSedimentasi();
            this.catatan = entity.getCatatan();
            
            if (entity.getLokusLokasiPdfs() != null) {
                this.lokusLokasiPdfs = entity.getLokusLokasiPdfs().stream()
                    .map(KegiatanLokusLokasiPdfDTO::new)
                    .collect(Collectors.toList());
            }
            
            if (entity.getLokusBangunanPdfs() != null) {
                this.lokusBangunanPdfs = entity.getLokusBangunanPdfs().stream()
                    .map(KegiatanLokusBangunanPdfDTO::new)
                    .collect(Collectors.toList());
            }
        }
    }
}
