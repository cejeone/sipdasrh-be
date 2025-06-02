package com.kehutanan.rh.kegiatan.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.rh.kegiatan.model.KegiatanBast;
import com.kehutanan.rh.kegiatan.model.KegiatanBastPdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanBastDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long kegiatanId;
    private String kegiatanNama;
    private Long statusId;
    private String statusNama;
    private Long tahapId;
    private String tahapNama;
    private String tahun;
    private String targetLuas;
    private String realisasiLuas;
    private String jenisTanamanId;
    private String kelompokMasyarakatId;
    
    // Lists for file references
    private List<KegiatanBastFileDTO> kegiatanBastPdfList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public KegiatanBastDTO(KegiatanBast entity) {
        this.id = entity.getId();
        
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
            // Note: You might need to adjust this based on what field contains the name in Kegiatan
            // this.kegiatanNama = entity.getKegiatan().getNamaKegiatan();
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = entity.getStatusId().getId();
            this.statusNama = entity.getStatusId().getNilai();
        }
        
        if (entity.getTahapId() != null) {
            this.tahapId = entity.getTahapId().getId();
            this.tahapNama = entity.getTahapId().getNilai();
        }
        
        this.tahun = entity.getTahun();
        this.targetLuas = entity.getTargetLuas();
        this.realisasiLuas = entity.getRealisasiLuas();
        this.jenisTanamanId = entity.getJenisTanamanId();
        this.kelompokMasyarakatId = entity.getKelompokMasyarakatId();
        
        // Convert file lists
        if (entity.getKegiatanBastPdfs() != null) {
            for (KegiatanBastPdf pdf : entity.getKegiatanBastPdfs()) {
                this.kegiatanBastPdfList.add(new KegiatanBastFileDTO(pdf));
            }
        }
    }
}