package com.kehutanan.rm.kegiatan.model.dto;

import com.kehutanan.rm.kegiatan.model.Kegiatan;
import com.kehutanan.rm.master.model.dto.Eselon3DTO;
import com.kehutanan.rm.master.model.dto.KabupatenKotaDTO;
import com.kehutanan.rm.master.model.dto.KecamatanDTO;
import com.kehutanan.rm.master.model.dto.KelurahanDesaDTO;
import com.kehutanan.rm.master.model.dto.LovDTO;
import com.kehutanan.rm.master.model.dto.ProvinsiDTO;
import com.kehutanan.rm.program.model.dto.ProgramDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class KegiatanDTO {
    private Long id;
    private Eselon3DTO eselon3;
    private Long programId; // Only store the ID to avoid circular reference
    private LovDTO jenisKegiatanId;
    private LovDTO referensiP0Id;
    private String namaKegiatan;
    private LovDTO bpdasId;
    private LovDTO dasId;
    private ProvinsiDTO provinsiId;
    private KabupatenKotaDTO kabupatenKota;
    private KecamatanDTO kecamatanId;
    private KelurahanDesaDTO kelurahanDesaId;
    private String alamat;
    private List<KegiatanLokusShpDTO> kegiatanLokusShps = new ArrayList<>();
    private LovDTO fungsiKawasanId;
    private LovDTO skemaId;
    private Integer tahunKegiatan;
    private LovDTO sumberAnggaranId;
    private Integer totalBibit;
    private Double totalLuas;
    private LovDTO penerimaManfaatId;
    private LovDTO pelaksanaId;
    private List<KegiatanRancanganTeknisPdfDTO> kegiatanRancanganTeknisPdfs = new ArrayList<>();
    private List<KegiatanRancanganTeknisFotoDTO> kegiatanRancanganTeknisFotos = new ArrayList<>();
    private List<KegiatanRancanganTeknisVideoDTO> kegiatanRancanganTeknisVideos = new ArrayList<>();
    private String nomor;
    private Double nilai;
    private LovDTO tipeId;
    private LovDTO penerimaManfaatKegiatanId;
    private String tanggalKontrak;
    private List<KegiatanKontrakPdfDTO> kegiatanKontrakPdfs = new ArrayList<>();
    private LovDTO statusId;
    private List<KegiatanPemeliharaanSulamanDTO> sulamanList = new ArrayList<>();
    private List<KegiatanPemeliharaanPemupukanDTO> pemupukanList = new ArrayList<>();
    private List<KegiatanDokumentasiFotoDTO> kegiatanDokumentasiFotos = new ArrayList<>();
    private String dokumentasiCatatanFoto;
    private List<KegiatanDokumentasiVideoDTO> kegiatanDokumentasiVideos = new ArrayList<>();
    private String dokumentasiCatatanVideo;
    private List<KegiatanMonevDTO> monevList = new ArrayList<>();

    public KegiatanDTO(Kegiatan entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.eselon3 = entity.getEselon3() != null ? new Eselon3DTO(entity.getEselon3()) : null;
            this.programId = entity.getProgram() != null ? entity.getProgram().getId() : null;
            this.jenisKegiatanId = entity.getJenisKegiatanId() != null ? new LovDTO(entity.getJenisKegiatanId()) : null;
            this.referensiP0Id = entity.getReferensiP0Id() != null ? new LovDTO(entity.getReferensiP0Id()) : null;
            this.namaKegiatan = entity.getNamaKegiatan();
            this.bpdasId = entity.getBpdasId() != null ? new LovDTO(entity.getBpdasId()) : null;
            this.dasId = entity.getDasId() != null ? new LovDTO(entity.getDasId()) : null;
            this.provinsiId = entity.getProvinsiId() != null ? new ProvinsiDTO(entity.getProvinsiId()) : null;
            this.kabupatenKota = entity.getKabupatenKota() != null ? new KabupatenKotaDTO(entity.getKabupatenKota()) : null;
            this.kecamatanId = entity.getKecamatanId() != null ? new KecamatanDTO(entity.getKecamatanId()) : null;
            this.kelurahanDesaId = entity.getKelurahanDesaId() != null ? new KelurahanDesaDTO(entity.getKelurahanDesaId()) : null;
            this.alamat = entity.getAlamat();
            
            if (entity.getKegiatanLokusShps() != null) {
                this.kegiatanLokusShps = entity.getKegiatanLokusShps().stream()
                    .map(KegiatanLokusShpDTO::new)
                    .collect(Collectors.toList());
            }
            
            this.fungsiKawasanId = entity.getFungsiKawasanId() != null ? new LovDTO(entity.getFungsiKawasanId()) : null;
            this.skemaId = entity.getSkemaId() != null ? new LovDTO(entity.getSkemaId()) : null;
            this.tahunKegiatan = entity.getTahunKegiatan();
            this.sumberAnggaranId = entity.getSumberAnggaranId() != null ? new LovDTO(entity.getSumberAnggaranId()) : null;
            this.totalBibit = entity.getTotalBibit();
            this.totalLuas = entity.getTotalLuas();
            this.penerimaManfaatId = entity.getPenerimaManfaatId() != null ? new LovDTO(entity.getPenerimaManfaatId()) : null;
            this.pelaksanaId = entity.getPelaksanaId() != null ? new LovDTO(entity.getPelaksanaId()) : null;
            
            if (entity.getKegiatanRancanganTeknisPdfs() != null) {
                this.kegiatanRancanganTeknisPdfs = entity.getKegiatanRancanganTeknisPdfs().stream()
                    .map(KegiatanRancanganTeknisPdfDTO::new)
                    .collect(Collectors.toList());
            }
            
            if (entity.getKegiatanRancanganTeknisFotos() != null) {
                this.kegiatanRancanganTeknisFotos = entity.getKegiatanRancanganTeknisFotos().stream()
                    .map(KegiatanRancanganTeknisFotoDTO::new)
                    .collect(Collectors.toList());
            }
            
            if (entity.getKegiatanRancanganTeknisVideos() != null) {
                this.kegiatanRancanganTeknisVideos = entity.getKegiatanRancanganTeknisVideos().stream()
                    .map(KegiatanRancanganTeknisVideoDTO::new)
                    .collect(Collectors.toList());
            }
            
            this.nomor = entity.getNomor();
            this.nilai = entity.getNilai();
            this.tipeId = entity.getTipeId() != null ? new LovDTO(entity.getTipeId()) : null;
            this.penerimaManfaatKegiatanId = entity.getPenerimaManfaatKegiatanId() != null ? 
                new LovDTO(entity.getPenerimaManfaatKegiatanId()) : null;
            this.tanggalKontrak = entity.getTanggalKontrak();
            
            if (entity.getKegiatanKontrakPdfs() != null) {
                this.kegiatanKontrakPdfs = entity.getKegiatanKontrakPdfs().stream()
                    .map(KegiatanKontrakPdfDTO::new)
                    .collect(Collectors.toList());
            }
            
            this.statusId = entity.getStatusId() != null ? new LovDTO(entity.getStatusId()) : null;
            
            if (entity.getSulamanList() != null) {
                this.sulamanList = entity.getSulamanList().stream()
                    .map(KegiatanPemeliharaanSulamanDTO::new)
                    .collect(Collectors.toList());
            }
            
            if (entity.getPemupukanList() != null) {
                this.pemupukanList = entity.getPemupukanList().stream()
                    .map(KegiatanPemeliharaanPemupukanDTO::new)
                    .collect(Collectors.toList());
            }
            
            if (entity.getKegiatanDokumentasiFotos() != null) {
                this.kegiatanDokumentasiFotos = entity.getKegiatanDokumentasiFotos().stream()
                    .map(KegiatanDokumentasiFotoDTO::new)
                    .collect(Collectors.toList());
            }
            
            this.dokumentasiCatatanFoto = entity.getDokumentasiCatatanFoto();
            
            if (entity.getKegiatanDokumentasiVideos() != null) {
                this.kegiatanDokumentasiVideos = entity.getKegiatanDokumentasiVideos().stream()
                    .map(KegiatanDokumentasiVideoDTO::new)
                    .collect(Collectors.toList());
            }
            
            this.dokumentasiCatatanVideo = entity.getDokumentasiCatatanVideo();
            
            if (entity.getMonevList() != null) {
                this.monevList = entity.getMonevList().stream()
                    .map(KegiatanMonevDTO::new)
                    .collect(Collectors.toList());
            }
        }
    }
}