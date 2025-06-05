package com.kehutanan.rh.kegiatan.model.dto;

import com.kehutanan.rh.kegiatan.model.KegiatanMonev;
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
public class KegiatanMonevDTO {
    private Long id;
    private Long kegiatanId;
    private LovDTO statusId;
    private String nomor;
    private String tanggal;
    private String deskripsi;
    private List<KegiatanMonevKriteriaDTO> kriterias = new ArrayList<>();
    private List<KegiatanMonevPdfDTO> kegiatanMonevPdfs = new ArrayList<>();
    
    public KegiatanMonevDTO(KegiatanMonev entity) {
        this.id = entity.getId();
        
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
        }
        
        if (entity.getStatusId() != null) {
            this.statusId = new LovDTO(entity.getStatusId());
        }
        
        this.nomor = entity.getNomor();
        this.tanggal = entity.getTanggal() != null ? entity.getTanggal().toString() : null;
        this.deskripsi = entity.getDeskripsi();
        
        if (entity.getKriterias() != null && !entity.getKriterias().isEmpty()) {
            this.kriterias = entity.getKriterias().stream()
                .map(KegiatanMonevKriteriaDTO::new)
                .collect(Collectors.toList());
        }
        
        if (entity.getKegiatanMonevPdfs() != null && !entity.getKegiatanMonevPdfs().isEmpty()) {
            this.kegiatanMonevPdfs = entity.getKegiatanMonevPdfs().stream()
                .map(KegiatanMonevPdfDTO::new)
                .collect(Collectors.toList());
        }
    }
}