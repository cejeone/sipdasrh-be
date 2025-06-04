package com.kehutanan.pepdas.monev.model.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.kehutanan.pepdas.kegiatan.model.dto.KegiatanDTO;
import com.kehutanan.pepdas.master.model.dto.LovDTO;
import com.kehutanan.pepdas.monev.model.Monev;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonevDTO {
    
    private Long id;
    private KegiatanDTO kegiatan;
    private LovDTO status;
    private String nomor;
    private String tanggal;
    private String deskripsi;
    private List<MonevPdfDTO> monevPdfs = new ArrayList<>();
    
    public MonevDTO(Monev monev) {
        this.id = monev.getId();
        this.nomor = monev.getNomor();
        this.tanggal = monev.getTanggal() != null ? monev.getTanggal().toString() : null;
        this.deskripsi = monev.getDeskripsi();
        
        // Use existing DTOs for related entities
        if (monev.getKegiatan() != null) {
            this.kegiatan = new KegiatanDTO(monev.getKegiatan());
        }
        
        if (monev.getStatus() != null) {
            this.status = new LovDTO(monev.getStatus());
        }
        
        // Convert list of MonevPdf to list of MonevPdfDTO
        if (monev.getMonevPdfs() != null) {
            this.monevPdfs = monev.getMonevPdfs().stream()
                .map(MonevPdfDTO::new)
                .collect(Collectors.toList());
        }
    }
}