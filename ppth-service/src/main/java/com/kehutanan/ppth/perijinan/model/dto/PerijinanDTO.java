package com.kehutanan.ppth.perijinan.model.dto;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kehutanan.ppth.master.model.dto.LovDTO;
import com.kehutanan.ppth.master.model.dto.PelakuUsahaDTO;
import com.kehutanan.ppth.perijinan.model.Perijinan;

import lombok.Data;

@Data
public class PerijinanDTO implements Serializable {

    private Long id;
    private PelakuUsahaDTO pelakuUsahaId;
    private LovDTO levelId;
    private LovDTO statusId;
    private String tanggalPengajuan;
    private String tanggalPenetapan;
    private List<PerijinanDokumenPdfDTO> perijinanDokumenPdfDTOs = new ArrayList<>();
    private String catatanDokumenPerijinan;
    private List<PerijinanDokumenSertifikatPdfDTO> perijinanDokumenSertifikatPdfDTOs = new ArrayList<>();
    private String catatanDokumenSertifikat;
    private String keterangan;
    
    public PerijinanDTO(Perijinan entity) {
        if (entity != null) {
            this.id = entity.getId();
            
            if (entity.getPelakuUsahaId() != null) {
                this.pelakuUsahaId = new PelakuUsahaDTO(entity.getPelakuUsahaId());
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
            
            if (entity.getPerijinanDokumenPdfs() != null && !entity.getPerijinanDokumenPdfs().isEmpty()) {
                this.perijinanDokumenPdfDTOs = entity.getPerijinanDokumenPdfs().stream()
                    .map(PerijinanDokumenPdfDTO::new)
                    .collect(Collectors.toList());
            }
            
            this.catatanDokumenPerijinan = entity.getCatatanDokumenPerijinan();
            
            if (entity.getPerijinanDokumenSertifikatPdfs() != null && !entity.getPerijinanDokumenSertifikatPdfs().isEmpty()) {
                this.perijinanDokumenSertifikatPdfDTOs = entity.getPerijinanDokumenSertifikatPdfs().stream()
                    .map(PerijinanDokumenSertifikatPdfDTO::new)
                    .collect(Collectors.toList());
            }
            
            this.catatanDokumenSertifikat = entity.getCatatanDokumenSertifikat();
            this.keterangan = entity.getKeterangan();
        }
    }
}
