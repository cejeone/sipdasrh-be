package com.kehutanan.tktrh.perijinan.model.dto;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kehutanan.tktrh.master.model.dto.LovDTO;
import com.kehutanan.tktrh.perijinan.model.Perijinan;

import lombok.Data;

@Data
public class PerijinanDTO implements Serializable {

    private Long id;
    private LovDTO pelakuUsahaId;
    private LovDTO levelId;
    private LovDTO statusId;
    private String tanggalPengajuan;
    private String tanggalPenetapan;
    private List<PerijinanDokumenAwalPdfDTO> dokumenAwalPdfs = new ArrayList<>();
    private String catatanDokumenAwal;
    private List<PerijinanDokumenBastPdfDTO> dokumenBastPdfs = new ArrayList<>();
    private String catatanDokumenBast;
    private String keterangan;
    
    public PerijinanDTO(Perijinan entity) {
        if (entity != null) {
            this.id = entity.getId();
            
            if (entity.getPelakuUsahaId() != null) {
                this.pelakuUsahaId = new LovDTO(entity.getPelakuUsahaId());
            }
            
            if (entity.getLevelId() != null) {
                this.levelId = new LovDTO(entity.getLevelId());
            }
            
            if (entity.getStatusId() != null) {
                this.statusId = new LovDTO(entity.getStatusId());
            }
            
            this.tanggalPengajuan = entity.getTanggalPengajuan() != null ? 
                entity.getTanggalPengajuan().format(DateTimeFormatter.ISO_DATE_TIME) : null;
            
            this.tanggalPenetapan = entity.getTanggalPenetapan() != null ? 
                entity.getTanggalPenetapan().format(DateTimeFormatter.ISO_DATE_TIME) : null;
            
            if (entity.getDokumenAwalPdfs() != null && !entity.getDokumenAwalPdfs().isEmpty()) {
                this.dokumenAwalPdfs = entity.getDokumenAwalPdfs().stream()
                    .map(PerijinanDokumenAwalPdfDTO::new)
                    .collect(Collectors.toList());
            }
            
            this.catatanDokumenAwal = entity.getCatatanDokumenAwal();
            
            if (entity.getDokumenBastPdfs() != null && !entity.getDokumenBastPdfs().isEmpty()) {
                this.dokumenBastPdfs = entity.getDokumenBastPdfs().stream()
                    .map(PerijinanDokumenBastPdfDTO::new)
                    .collect(Collectors.toList());
            }
            
            this.catatanDokumenBast = entity.getCatatanDokumenBast();
            this.keterangan = entity.getKeterangan();
        }
    }
}
