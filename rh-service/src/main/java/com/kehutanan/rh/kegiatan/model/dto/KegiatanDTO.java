package com.kehutanan.rh.kegiatan.model.dto;

import com.kehutanan.rh.kegiatan.model.Kegiatan;
import com.kehutanan.rh.master.model.dto.Eselon3DTO;
import com.kehutanan.rh.master.model.dto.LovDTO;
import com.kehutanan.rh.program.model.dto.ProgramDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanDTO {
    private Long id;
    private Eselon3DTO eselon3;
    private ProgramDTO program;
    private LovDTO jenisKegiatanId;
    private LovDTO referensiP0Id;
    private LovDTO polaId;
    private LovDTO sumberAnggaranId;
    private LovDTO pelaksanaId;
    private List<KegiatanFungsiKawasanDTO> fungsiKawasans = new ArrayList<>();
    private LovDTO tipeId;
    private LovDTO penerimaManfaatId;
    private LovDTO statusId;
    private List<KegiatanPemeliharaanSulamanDTO> sulamanList = new ArrayList<>();
    private List<KegiatanPemeliharaanPemupukanDTO> pemupukanList = new ArrayList<>();
    private String pemangkuKawasan;
    private String namaKegiatan;
    private List<KegiatanLokusDTO> kegiatanLokus = new ArrayList<>();
    private Integer tahunKegiatan;
    private Integer totalBibit;
    private Double totalLuas;
    private List<KegiatanRancanganTeknisPdfDTO> kegiatanRancanganTeknisPdfs = new ArrayList<>();
    private List<KegiatanRancanganTeknisFotoDTO> kegiatanRancanganTeknisFotos = new ArrayList<>();
    private List<KegiatanRancanganTeknisVideoDTO> kegiatanRancanganTeknisVideos = new ArrayList<>();
    private String nomor;
    private Double nilai;
    private String tanggalKontrak;
    private List<KegiatanKontrakPdfDTO> kegiatanKontrakPdfs = new ArrayList<>();
    private List<KegiatanDokumentasiFotoDTO> kegiatanDokumentasiFotos = new ArrayList<>();
    private String dokumentasiCatatanFoto;
    private List<KegiatanDokumentasiVideoDTO> kegiatanDokumentasiVideos = new ArrayList<>();
    private String dokumentasiCatatanVideo;
    private List<KegiatanMonevDTO> monevList = new ArrayList<>();
    private List<KegiatanBastDTO> serahTerimaList = new ArrayList<>();
    
    public KegiatanDTO(Kegiatan entity) {
        this.id = entity.getId();
        
        if (entity.getEselon3() != null) {
            this.eselon3 = new Eselon3DTO(entity.getEselon3());
        }
        
        if (entity.getProgram() != null) {
            this.program = new ProgramDTO();
            this.program.setId(entity.getProgram().getId());
            // Only set essential fields to avoid circular dependency
            // Not setting full program details
        }
        
        if (entity.getJenisKegiatanId() != null) {
            this.jenisKegiatanId = new LovDTO(entity.getJenisKegiatanId());
        }
        
        if (entity.getReferensiP0Id() != null) {
            this.referensiP0Id = new LovDTO(entity.getReferensiP0Id());
        }
        
        if (entity.getPolaId() != null) {
            this.polaId = new LovDTO(entity.getPolaId());
        }
        
        if (entity.getSumberAnggaranId() != null) {
            this.sumberAnggaranId = new LovDTO(entity.getSumberAnggaranId());
        }
        
        if (entity.getPelaksanaId() != null) {
            this.pelaksanaId = new LovDTO(entity.getPelaksanaId());
        }
        
        if (entity.getFungsiKawasans() != null && !entity.getFungsiKawasans().isEmpty()) {
            this.fungsiKawasans = entity.getFungsiKawasans().stream()
                .map(KegiatanFungsiKawasanDTO::new)
                .collect(Collectors.toList());
        }
        
        if (entity.getTipeId() != null) {
            this.tipeId = new LovDTO(entity.getTipeId());
        }
        
        if (entity.getPenerimaManfaatId() != null) {
            this.penerimaManfaatId = new LovDTO(entity.getPenerimaManfaatId());
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = new LovDTO(entity.getStatusId());
        }
        
        if (entity.getSulamanList() != null && !entity.getSulamanList().isEmpty()) {
            this.sulamanList = entity.getSulamanList().stream()
                .map(KegiatanPemeliharaanSulamanDTO::new)
                .collect(Collectors.toList());
        }
        
        if (entity.getPemupukanList() != null && !entity.getPemupukanList().isEmpty()) {
            this.pemupukanList = entity.getPemupukanList().stream()
                .map(KegiatanPemeliharaanPemupukanDTO::new)
                .collect(Collectors.toList());
        }
        
        this.pemangkuKawasan = entity.getPemangkuKawasan();
        this.namaKegiatan = entity.getNamaKegiatan();
        
        if (entity.getKegiatanLokus() != null && !entity.getKegiatanLokus().isEmpty()) {
            this.kegiatanLokus = entity.getKegiatanLokus().stream()
                .map(KegiatanLokusDTO::new)
                .collect(Collectors.toList());
        }
        
        this.tahunKegiatan = entity.getTahunKegiatan();
        this.totalBibit = entity.getTotalBibit();
        this.totalLuas = entity.getTotalLuas();
        
        if (entity.getKegiatanRancanganTeknisPdfs() != null && !entity.getKegiatanRancanganTeknisPdfs().isEmpty()) {
            this.kegiatanRancanganTeknisPdfs = entity.getKegiatanRancanganTeknisPdfs().stream()
                .map(KegiatanRancanganTeknisPdfDTO::new)
                .collect(Collectors.toList());
        }
        
        if (entity.getKegiatanRancanganTeknisFotos() != null && !entity.getKegiatanRancanganTeknisFotos().isEmpty()) {
            this.kegiatanRancanganTeknisFotos = entity.getKegiatanRancanganTeknisFotos().stream()
                .map(KegiatanRancanganTeknisFotoDTO::new)
                .collect(Collectors.toList());
        }
        
        if (entity.getKegiatanRancanganTeknisVideos() != null && !entity.getKegiatanRancanganTeknisVideos().isEmpty()) {
            this.kegiatanRancanganTeknisVideos = entity.getKegiatanRancanganTeknisVideos().stream()
                .map(KegiatanRancanganTeknisVideoDTO::new)
                .collect(Collectors.toList());
        }
        
        this.nomor = entity.getNomor();
        this.nilai = entity.getNilai();
        this.tanggalKontrak = entity.getTanggalKontrak();
        
        if (entity.getKegiatanKontrakPdfs() != null && !entity.getKegiatanKontrakPdfs().isEmpty()) {
            this.kegiatanKontrakPdfs = entity.getKegiatanKontrakPdfs().stream()
                .map(KegiatanKontrakPdfDTO::new)
                .collect(Collectors.toList());
        }
        
        if (entity.getKegiatanDokumentasiFotos() != null && !entity.getKegiatanDokumentasiFotos().isEmpty()) {
            this.kegiatanDokumentasiFotos = entity.getKegiatanDokumentasiFotos().stream()
                .map(KegiatanDokumentasiFotoDTO::new)
                .collect(Collectors.toList());
        }
        
        this.dokumentasiCatatanFoto = entity.getDokumentasiCatatanFoto();
        
        if (entity.getKegiatanDokumentasiVideos() != null && !entity.getKegiatanDokumentasiVideos().isEmpty()) {
            this.kegiatanDokumentasiVideos = entity.getKegiatanDokumentasiVideos().stream()
                .map(KegiatanDokumentasiVideoDTO::new)
                .collect(Collectors.toList());
        }
        
        this.dokumentasiCatatanVideo = entity.getDokumentasiCatatanVideo();
        
        if (entity.getMonevList() != null && !entity.getMonevList().isEmpty()) {
            this.monevList = entity.getMonevList().stream()
                .map(KegiatanMonevDTO::new)
                .collect(Collectors.toList());
        }
        
        if (entity.getSerahTerimaList() != null && !entity.getSerahTerimaList().isEmpty()) {
            this.serahTerimaList = entity.getSerahTerimaList().stream()
                .map(KegiatanBastDTO::new)
                .collect(Collectors.toList());
        }
    }
}