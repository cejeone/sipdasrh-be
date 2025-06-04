package com.kehutanan.rh.kegiatan.model.dto;

import com.kehutanan.rh.kegiatan.model.KegiatanBast;
import com.kehutanan.rh.master.model.dto.LovDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanBastDTO {
    private Long id;
    private Long kegiatanId;
    private LovDTO statusId;
    private LovDTO tahapId;
    private String tahun;
    private String targetLuas;
    private String realisasiLuas;
    private String jenisTanamanId;
    private String kelompokMasyarakatId;
    private List<KegiatanBastPdfDTO> kegiatanBastPdfs = new ArrayList<>();
    
    public KegiatanBastDTO(KegiatanBast entity) {
        this.id = entity.getId();
        
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = new LovDTO(entity.getStatusId());
        }
        
        if (entity.getTahapId() != null) {
            this.tahapId = new LovDTO(entity.getTahapId());
        }
        
        this.tahun = entity.getTahun();
        this.targetLuas = entity.getTargetLuas();
        this.realisasiLuas = entity.getRealisasiLuas();
        this.jenisTanamanId = entity.getJenisTanamanId();
        this.kelompokMasyarakatId = entity.getKelompokMasyarakatId();
        
        if (entity.getKegiatanBastPdfs() != null && !entity.getKegiatanBastPdfs().isEmpty()) {
            this.kegiatanBastPdfs = entity.getKegiatanBastPdfs().stream()
                .map(KegiatanBastPdfDTO::new)
                .collect(Collectors.toList());
        }
    }
}