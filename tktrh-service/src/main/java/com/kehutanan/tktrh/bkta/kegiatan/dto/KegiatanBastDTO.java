package com.kehutanan.tktrh.bkta.kegiatan.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanBast;
import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanBastPdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanBastDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Integer tahun;
    
    // Related entity IDs instead of full entities
    private Long kegiatanId;
    private String kegiatanNama;
    
    private Long identitasBktaId;
    private String identitasBktaNama;
    
    private Long jenisBktaId;
    private String jenisBktaNama;
    
    private Long kelompokMasyarakatId;
    private String kelompokMasyarakatNama;
    
    private Long statusId;
    private String statusNama;
    
    // Lists for file references
    private List<KegiatanBastFileDTO> kegiatanBastPdfList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public KegiatanBastDTO(KegiatanBast entity) {
        this.id = entity.getId();
        this.tahun = entity.getTahun();
        
        // Set related entity IDs and names
        if (entity.getKegiatan() != null) {
            this.kegiatanId = entity.getKegiatan().getId();
            // Assuming Kegiatan has a name field, replace with actual field
            // this.kegiatanNama = entity.getKegiatan().getNama();
        }
        
        if (entity.getIdentitasBkta() != null) {
            this.identitasBktaId = entity.getIdentitasBkta().getId();
            this.identitasBktaNama = entity.getIdentitasBkta().getNilai();
        }
        
        if (entity.getJenisBkta() != null) {
            this.jenisBktaId = entity.getJenisBkta().getId();
            this.jenisBktaNama = entity.getJenisBkta().getNilai();
        }
        
        if (entity.getKelompokMasyarakat() != null) {
            this.kelompokMasyarakatId = entity.getKelompokMasyarakat().getId();
            this.kelompokMasyarakatNama = entity.getKelompokMasyarakat().getNilai();
        }
        
        if (entity.getStatus() != null) {
            this.statusId = entity.getStatus().getId();
            this.statusNama = entity.getStatus().getNilai();
        }
        
        // Convert file lists
        if (entity.getKegiatanKontrakPdfs() != null) {
            for (KegiatanBastPdf pdf : entity.getKegiatanKontrakPdfs()) {
                this.kegiatanBastPdfList.add(new KegiatanBastFileDTO(pdf));
            }
        }
    }
}