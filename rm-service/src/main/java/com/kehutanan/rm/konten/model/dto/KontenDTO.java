package com.kehutanan.rm.konten.model.dto;

import com.kehutanan.rm.konten.model.Konten;
import com.kehutanan.rm.master.model.dto.LovDTO;

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
    private List<String> kataKunci = new ArrayList<>();
    private String waktuAwalTayang;
    private String waktuAkhirTayang;
    private List<KontenGambarDTO> kontenGambars = new ArrayList<>();
    private List<KontenGambarUtamaDTO> kontenGambarUtamas = new ArrayList<>();
    
    // Constructor that takes Konten entity
    public KontenDTO(Konten konten) {
        this.id = konten.getId();
        this.judul = konten.getJudul();
        this.konten = konten.getKonten();
        
        // Convert Lov objects to LovDTOs
        if (konten.getTipe() != null) {
            this.tipe = new LovDTO(konten.getTipe());
        }
        
        if (konten.getStatus() != null) {
            this.status = new LovDTO(konten.getStatus());
        }
        
        // Convert date/time to strings
        this.waktuAwalTayang = konten.getWaktuAwalTayang() != null ? 
                              konten.getWaktuAwalTayang().toString() : null;
        
        this.waktuAkhirTayang = konten.getWaktuAkhirTayang() != null ? 
                               konten.getWaktuAkhirTayang().toString() : null;
        
        // Copy kata kunci list
        if (konten.getKataKunci() != null) {
            this.kataKunci.addAll(konten.getKataKunci());
        }
        
        // Convert KontenGambar list to DTOs
        if (konten.getKontenGambars() != null) {
            this.kontenGambars = konten.getKontenGambars().stream()
                .map(KontenGambarDTO::new)
                .collect(Collectors.toList());
        }
        
        // Convert KontenGambarUtama list to DTOs
        if (konten.getKontenGambarUtamas() != null) {
            this.kontenGambarUtamas = konten.getKontenGambarUtamas().stream()
                .map(KontenGambarUtamaDTO::new)
                .collect(Collectors.toList());
        }
    }
}
