package com.kehutanan.ppth.penghijauan.kegiatan.model.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kehutanan.ppth.penghijauan.kegiatan.model.KegiatanMonev;
import com.kehutanan.ppth.master.model.dto.LovDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KegiatanMonevDTO {
    
    private Long id;
    private String nomor;
    private String tanggal;
    private String deskripsi;
    private LovDTO statusId;
    private List<KegiatanMonevKriteriaDTO> kriterias = new ArrayList<>();
    private List<KegiatanMonevPdfDTO> kegiatanMonevPdfs = new ArrayList<>();
    
    public KegiatanMonevDTO(KegiatanMonev entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.nomor = entity.getNomor();
            this.tanggal = entity.getTanggal() != null ? entity.getTanggal().toString() : null;
            this.deskripsi = entity.getDeskripsi();
            this.statusId = entity.getStatusId() != null ? new LovDTO(entity.getStatusId()) : null;
            
            if (entity.getKriterias() != null) {
                this.kriterias = entity.getKriterias().stream()
                    .map(KegiatanMonevKriteriaDTO::new)
                    .collect(Collectors.toList());
            }
            
            if (entity.getKegiatanMonevPdfs() != null) {
                this.kegiatanMonevPdfs = entity.getKegiatanMonevPdfs().stream()
                    .map(KegiatanMonevPdfDTO::new)
                    .collect(Collectors.toList());
            }
        }
    }
}
