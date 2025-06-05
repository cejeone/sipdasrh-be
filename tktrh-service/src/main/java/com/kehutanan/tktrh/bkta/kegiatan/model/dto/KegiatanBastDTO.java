package com.kehutanan.tktrh.bkta.kegiatan.model.dto;

import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanBast;
import com.kehutanan.tktrh.master.model.dto.LovDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class KegiatanBastDTO {

    private Long id;
    private Integer tahun;
    private LovDTO identitasBkta;
    private LovDTO jenisBkta;
    private LovDTO kelompokMasyarakat;
    private List<KegiatanBastPdfDTO> kegiatanKontrakPdfs = new ArrayList<>();
    private LovDTO status;

    public KegiatanBastDTO(KegiatanBast entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.tahun = entity.getTahun();
            this.identitasBkta = entity.getIdentitasBkta() != null ? new LovDTO(entity.getIdentitasBkta()) : null;
            this.jenisBkta = entity.getJenisBkta() != null ? new LovDTO(entity.getJenisBkta()) : null;
            this.kelompokMasyarakat = entity.getKelompokMasyarakat() != null ? new LovDTO(entity.getKelompokMasyarakat()) : null;
            
            if (entity.getKegiatanKontrakPdfs() != null) {
                this.kegiatanKontrakPdfs = entity.getKegiatanKontrakPdfs().stream()
                    .map(KegiatanBastPdfDTO::new)
                    .collect(Collectors.toList());
            }
            
            this.status = entity.getStatus() != null ? new LovDTO(entity.getStatus()) : null;
        }
    }
}
