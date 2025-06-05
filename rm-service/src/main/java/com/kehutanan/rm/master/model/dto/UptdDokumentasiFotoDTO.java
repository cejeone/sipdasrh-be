package com.kehutanan.rm.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.rm.master.model.UptdDokumentasiFoto;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UptdDokumentasiFotoDTO {
    private UUID id;
    private String namaFile;
    private String namaAsli;
    private String pathFile;
    private Double ukuranMb;
    private String contentType;
    private LocalDateTime uploadedAt;
    private String viewUrl;
    private String downloadUrl;
    
    public UptdDokumentasiFotoDTO(UptdDokumentasiFoto uptdDokumentasiFoto) {
        if (uptdDokumentasiFoto != null) {
            this.id = uptdDokumentasiFoto.getId();
            this.namaFile = uptdDokumentasiFoto.getNamaFile();
            this.namaAsli = uptdDokumentasiFoto.getNamaAsli();
            this.pathFile = uptdDokumentasiFoto.getPathFile();
            this.ukuranMb = uptdDokumentasiFoto.getUkuranMb();
            this.contentType = uptdDokumentasiFoto.getContentType();
            this.uploadedAt = uptdDokumentasiFoto.getUploadedAt();
            this.viewUrl = uptdDokumentasiFoto.getViewUrl();
            this.downloadUrl = uptdDokumentasiFoto.getDownloadUrl();
        }
    }
}