package com.kehutanan.rh.dokumen.model.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kehutanan.rh.dokumen.model.Dokumen;
import com.kehutanan.rh.master.model.dto.LovDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DokumenDTO {
    private Long id;
    private LovDTO tipe;
    private LovDTO status;
    private String namaDokumen;
    private List<DokumenFileDTO> dokumenFiles = new ArrayList<>();
    private Double ukuranDokumen;
    private String keterangan;
    
    // Constructor from entity
    public DokumenDTO(Dokumen dokumen) {
        if (dokumen != null) {
            this.id = dokumen.getId();
            this.tipe = dokumen.getTipe() != null ? new LovDTO(dokumen.getTipe()) : null;
            this.status = dokumen.getStatus() != null ? new LovDTO(dokumen.getStatus()) : null;
            this.namaDokumen = dokumen.getNamaDokumen();
            if (dokumen.getDokumenFiles() != null) {
                this.dokumenFiles = dokumen.getDokumenFiles().stream()
                    .map(DokumenFileDTO::new)
                    .collect(Collectors.toList());
            }
            this.ukuranDokumen = dokumen.getUkuranDokumen();
            this.keterangan = dokumen.getKeterangan();
        }
    }
}