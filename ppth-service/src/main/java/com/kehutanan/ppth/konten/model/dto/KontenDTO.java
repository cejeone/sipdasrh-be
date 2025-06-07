package com.kehutanan.ppth.konten.model.dto;

import com.kehutanan.ppth.konten.model.Konten;
import com.kehutanan.ppth.konten.model.KontenGambar;
import com.kehutanan.ppth.konten.model.KontenGambarUtama;
import com.kehutanan.ppth.master.model.dto.LovDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class KontenDTO {
    
    private Long id;
    private LovDTO tipe;
    private LovDTO status;
    private String judul;
    private String konten;
    private List<String> kataKunci;
    private String waktuAwalTayang;
    private String waktuAkhirTayang;
    private List<KontenGambarDTO> kontenGambars;
    private List<KontenGambarUtamaDTO> kontenGambarUtamas;
    
    public KontenDTO(Konten konten) {
        if (konten != null) {
            this.id = konten.getId();
            this.tipe = konten.getTipe() != null ? new LovDTO(konten.getTipe()) : null;
            this.status = konten.getStatus() != null ? new LovDTO(konten.getStatus()) : null;
            this.judul = konten.getJudul();
            this.konten = konten.getKonten();
            this.kataKunci = konten.getKataKunci() != null ? 
                             new ArrayList<>(konten.getKataKunci()) : new ArrayList<>();
            this.waktuAwalTayang = konten.getWaktuAwalTayang() != null ? 
                                   konten.getWaktuAwalTayang().toString() : null;
            this.waktuAkhirTayang = konten.getWaktuAkhirTayang() != null ? 
                                    konten.getWaktuAkhirTayang().toString() : null;
            
            this.kontenGambars = konten.getKontenGambars() != null ?
                                konten.getKontenGambars().stream()
                                .map(KontenGambarDTO::new)
                                .collect(Collectors.toList()) : new ArrayList<>();
            
            this.kontenGambarUtamas = konten.getKontenGambarUtamas() != null ?
                                     konten.getKontenGambarUtamas().stream()
                                     .map(KontenGambarUtamaDTO::new)
                                     .collect(Collectors.toList()) : new ArrayList<>();
        }
    }
}
