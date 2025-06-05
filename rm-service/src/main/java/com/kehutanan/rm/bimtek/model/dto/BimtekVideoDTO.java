package com.kehutanan.rm.bimtek.model.dto;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.rm.bimtek.model.BimtekVideo;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BimtekVideoDTO {

    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private String uploadedAt;
    private String viewUrl;
    private String downloadUrl;

    public BimtekVideoDTO(BimtekVideo bimtekVideo) {
        this.id = bimtekVideo.getId();
        this.namaFile = bimtekVideo.getNamaFile();
        this.namaAsli = bimtekVideo.getNamaAsli();
        this.pathFile = bimtekVideo.getPathFile();
        this.ukuranMb = bimtekVideo.getUkuranMb();
        this.contentType = bimtekVideo.getContentType();
        this.uploadedAt = bimtekVideo.getUploadedAt() != null ? bimtekVideo.getUploadedAt().toString() : null;
        this.viewUrl = bimtekVideo.getViewUrl();
        this.downloadUrl = bimtekVideo.getDownloadUrl();
    }
}
