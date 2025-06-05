package com.kehutanan.tktrh.bkta.kegiatan.model.dto;

import com.kehutanan.tktrh.bkta.kegiatan.model.Kegiatan;
import com.kehutanan.tktrh.bkta.program.model.dto.ProgramDTO;
import com.kehutanan.tktrh.master.model.dto.Eselon3DTO;
import com.kehutanan.tktrh.master.model.dto.LovDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class KegiatanDTO {

    private Long id;
    private Eselon3DTO subDirektorat;
    private ProgramDTO program;
    private String namaKegiatan;
    private LovDTO sumberAnggaran;
    private Double totalAnggaran;
    private List<KegiatanLokusDTO> lokus = new ArrayList<>();
    private Boolean fungsiKawasanHk;
    private Boolean fungsiKawasanHl;
    private Boolean fungsiKawasanApl;
    private Boolean fungsiKawasanHpt;
    private LovDTO skema;
    private String tahunKegiatan;
    private Double totalDpn;
    private Double totalGullyPlug;
    private String pemangkuKawasan;
    private List<KegiatanRancanganTeknisPdfDTO> kegiatanRancanganTeknisPdfs = new ArrayList<>();
    private List<KegiatanRancanganTeknisFotoDTO> kegiatanRancanganTeknisFotos = new ArrayList<>();
    private List<KegiatanRancanganTeknisShpDTO> kegiatanRancanganTeknisShps = new ArrayList<>();
    private String nomor;
    private String nilai;
    private String tanggalKontrak;
    private String tanggalBerakhirKontrak;
    private LovDTO pelaksana;
    private List<KegiatanKontrakPdfDTO> kegiatanKontrakPdfs = new ArrayList<>();
    private LovDTO status;
    private List<KegiatanDokumentasiFotoDTO> dokumentasiFotos = new ArrayList<>();
    private String catatanDokumentasiFoto;
    private List<KegiatanDokumentasiVideoDTO> dokumentasiVideos = new ArrayList<>();
    private List<KegiatanMonevDTO> monevs = new ArrayList<>();
    private List<KegiatanBastDTO> basts = new ArrayList<>();

    public KegiatanDTO(Kegiatan entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.subDirektorat = entity.getSubDirektorat() != null ? new Eselon3DTO(entity.getSubDirektorat()) : null;
            this.program = entity.getProgram() != null ? new ProgramDTO(entity.getProgram()) : null;
            this.namaKegiatan = entity.getNamaKegiatan();
            this.sumberAnggaran = entity.getSumberAnggaran() != null ? new LovDTO(entity.getSumberAnggaran()) : null;
            this.totalAnggaran = entity.getTotalAnggaran();
            
            if (entity.getLokus() != null) {
                this.lokus = entity.getLokus().stream()
                    .map(KegiatanLokusDTO::new)
                    .collect(Collectors.toList());
            }
            
            this.fungsiKawasanHk = entity.getFungsiKawasanHk();
            this.fungsiKawasanHl = entity.getFungsiKawasanHl();
            this.fungsiKawasanApl = entity.getFungsiKawasanApl();
            this.fungsiKawasanHpt = entity.getFungsiKawasanHpt();
            this.skema = entity.getSkema() != null ? new LovDTO(entity.getSkema()) : null;
            this.tahunKegiatan = entity.getTahunKegiatan();
            this.totalDpn = entity.getTotalDpn();
            this.totalGullyPlug = entity.getTotalGullyPlug();
            this.pemangkuKawasan = entity.getPemangkuKawasan();
            
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
            
            if (entity.getKegiatanRancanganTeknisShps() != null) {
                this.kegiatanRancanganTeknisShps = entity.getKegiatanRancanganTeknisShps().stream()
                    .map(KegiatanRancanganTeknisShpDTO::new)
                    .collect(Collectors.toList());
            }
            
            this.nomor = entity.getNomor();
            this.nilai = entity.getNilai();
            this.tanggalKontrak = entity.getTanggalKontrak() != null ? entity.getTanggalKontrak().toString() : null;
            this.tanggalBerakhirKontrak = entity.getTanggalBerakhirKontrak() != null ? entity.getTanggalBerakhirKontrak().toString() : null;
            this.pelaksana = entity.getPelaksana() != null ? new LovDTO(entity.getPelaksana()) : null;
            
            if (entity.getKegiatanKontrakPdfs() != null) {
                this.kegiatanKontrakPdfs = entity.getKegiatanKontrakPdfs().stream()
                    .map(KegiatanKontrakPdfDTO::new)
                    .collect(Collectors.toList());
            }
            
            this.status = entity.getStatus() != null ? new LovDTO(entity.getStatus()) : null;
            
            if (entity.getDokumentasiFotos() != null) {
                this.dokumentasiFotos = entity.getDokumentasiFotos().stream()
                    .map(KegiatanDokumentasiFotoDTO::new)
                    .collect(Collectors.toList());
            }
            
            this.catatanDokumentasiFoto = entity.getCatatanDokumentasiFoto();
            
            if (entity.getDokumentasiVideos() != null) {
                this.dokumentasiVideos = entity.getDokumentasiVideos().stream()
                    .map(KegiatanDokumentasiVideoDTO::new)
                    .collect(Collectors.toList());
            }
            
            if (entity.getMonevs() != null) {
                this.monevs = entity.getMonevs().stream()
                    .map(KegiatanMonevDTO::new)
                    .collect(Collectors.toList());
            }
            
            if (entity.getBasts() != null) {
                this.basts = entity.getBasts().stream()
                    .map(KegiatanBastDTO::new)
                    .collect(Collectors.toList());
            }
        }
    }
}
