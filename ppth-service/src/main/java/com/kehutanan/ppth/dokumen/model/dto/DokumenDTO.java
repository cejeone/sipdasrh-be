package com.kehutanan.ppth.dokumen.model.dto;

import com.kehutanan.ppth.dokumen.model.Dokumen;
import com.kehutanan.ppth.master.model.dto.LovDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class DokumenDTO {

    private Long id;
    private LovDTO tipe;
    private LovDTO status;
    private String namaDokumen;
    private List<DokumenFileDTO> dokumenFiles = new ArrayList<>();
    private Double ukuranDokumen;
    private String keterangan;

    public DokumenDTO(Dokumen dokumen) {
        if (dokumen != null) {
            this.id = dokumen.getId();
            this.namaDokumen = dokumen.getNamaDokumen();
            this.ukuranDokumen = dokumen.getUkuranDokumen();
            this.keterangan = dokumen.getKeterangan();
            
            if (dokumen.getTipe() != null) {
                this.tipe = new LovDTO(dokumen.getTipe());
            }
            
            if (dokumen.getStatus() != null) {
                this.status = new LovDTO(dokumen.getStatus());
            }
            
            if (dokumen.getDokumenFiles() != null && !dokumen.getDokumenFiles().isEmpty()) {
                this.dokumenFiles = dokumen.getDokumenFiles().stream()
                        .map(DokumenFileDTO::new)
                        .collect(Collectors.toList());
            }
        }
    }
}
