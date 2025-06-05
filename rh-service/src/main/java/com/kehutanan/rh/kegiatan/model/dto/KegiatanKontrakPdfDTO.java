package com.kehutanan.rh.kegiatan.model.dto;

import com.kehutanan.rh.kegiatan.model.KegiatanKontrakPdf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanKontrakPdfDTO extends FileDTO {
    private Long kegiatanId;
    
    public KegiatanKontrakPdfDTO(KegiatanKontrakPdf entity) {
        super(entity.getId(), 
              entity.getNamaFile(), 
              entity.getNamaAsli(), 
              entity.getPathFile(), 
              entity.getUkuranMb(), 
              entity.getContentType(), 
              entity.getUploadedAt() != null ? entity.getUploadedAt().toString() : null,
              entity.getViewUrl(),
              entity.getDownloadUrl());
        
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
        }
    }
}