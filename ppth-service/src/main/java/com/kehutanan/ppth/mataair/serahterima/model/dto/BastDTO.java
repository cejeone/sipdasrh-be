package com.kehutanan.ppth.mataair.serahterima.model.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kehutanan.ppth.master.model.dto.LovDTO;
import com.kehutanan.ppth.mataair.kegiatan.model.dto.KegiatanDTO;
import com.kehutanan.ppth.mataair.serahterima.model.Bast;
import com.kehutanan.ppth.mataair.serahterima.model.dto.BastPdfDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BastDTO {

    private Long id;
    private KegiatanDTO kegiatan;
    private String nomorBast;
    private String tanggal;
    private String deskripsi;
    private List<BastPdfDTO> bastPdfs = new ArrayList<>();
    private LovDTO status;

    public BastDTO(Bast bast) {
        if (bast != null) {
            this.id = bast.getId();
            // Avoid circular dependency by not directly creating KegiatanDTO with the full object
            this.kegiatan = bast.getKegiatan() != null ? new KegiatanDTO(bast.getKegiatan()) : null;
            this.nomorBast = bast.getNomorBast();
            this.tanggal = bast.getTanggal() != null ? 
                bast.getTanggal().format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
            this.deskripsi = bast.getDeskripsi();
            
            if (bast.getBastPdfs() != null) {
                this.bastPdfs = bast.getBastPdfs().stream()
                    .map(BastPdfDTO::new)
                    .collect(Collectors.toList());
            }
            
            this.status = bast.getStatus() != null ? new LovDTO(bast.getStatus()) : null;
        }
    }
}