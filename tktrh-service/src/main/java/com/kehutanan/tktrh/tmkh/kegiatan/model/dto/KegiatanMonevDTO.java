package com.kehutanan.tktrh.tmkh.kegiatan.model.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kehutanan.tktrh.master.model.dto.LovDTO;
import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanMonev;

import lombok.Data;

@Data
public class KegiatanMonevDTO {
    
    private Long id;
    private LovDTO statusId;
    private String nomor;
    private String tanggal;
    private String deskripsi;
    private List<KegiatanMonevKriteriaDTO> kriterias = new ArrayList<>();
    private List<KegiatanMonevPdfDTO> kegiatanMonevPdfs = new ArrayList<>();
    
    public KegiatanMonevDTO(KegiatanMonev entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.statusId = entity.getStatusId() != null ? new LovDTO(entity.getStatusId()) : null;
            this.nomor = entity.getNomor();
            this.tanggal = entity.getTanggal() != null ? entity.getTanggal().toString() : null;
            this.deskripsi = entity.getDeskripsi();
            
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
