package com.kehutanan.tktrh.bkta.kegiatan.model.dto;

import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanMonev;
import com.kehutanan.tktrh.master.model.dto.LovDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class KegiatanMonevDTO {

    private Long id;
    private String nomor;
    private String tanggal;
    private String deskripsi;
    private LovDTO status;
    private List<KegiatanMonevKriteriaDTO> kriterias = new ArrayList<>();
    private List<KegiatanMonevPdfDTO> kegiatanMonevPdfs = new ArrayList<>();

    public KegiatanMonevDTO(KegiatanMonev entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.nomor = entity.getNomor();
            this.tanggal = entity.getTanggal() != null ? entity.getTanggal().toString() : null;
            this.deskripsi = entity.getDeskripsi();
            this.status = entity.getStatus() != null ? new LovDTO(entity.getStatus()) : null;
            
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
