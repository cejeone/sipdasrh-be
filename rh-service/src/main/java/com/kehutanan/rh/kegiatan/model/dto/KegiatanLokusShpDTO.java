package com.kehutanan.rh.kegiatan.model.dto;

import com.kehutanan.rh.kegiatan.model.KegiatanLokusShp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanLokusShpDTO extends FileDTO {
    private Long kegiatanLokusId;
    
    public KegiatanLokusShpDTO(KegiatanLokusShp entity) {
        super(entity.getId(), 
              entity.getNamaFile(), 
              entity.getNamaAsli(), 
              entity.getPathFile(), 
              entity.getUkuranMb(), 
              entity.getContentType(), 
              entity.getUploadedAt() != null ? entity.getUploadedAt().toString() : null,
              entity.getViewUrl(),
              entity.getDownloadUrl());
        
        if (entity.getKegiatanLokus() != null) {
            this.kegiatanLokusId = entity.getKegiatanLokus().getId();
        }
    }
}