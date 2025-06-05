package com.kehutanan.rh.konten.model.dto;

import com.kehutanan.rh.konten.model.Konten;
import com.kehutanan.rh.master.model.dto.LovDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
        
        // Handle potential null values
        if (konten.getTipe() != null) {
            this.tipe = new LovDTO(konten.getTipe());
        }
        
        if (konten.getStatus() != null) {
            this.status = new LovDTO(konten.getStatus());
        }
        
        this.judul = konten.getJudul();
        this.konten = konten.getKonten();
        
        if (konten.getKataKunci() != null) {
            this.kataKunci = new ArrayList<>(konten.getKataKunci());
        }
        
        this.waktuAwalTayang = konten.getWaktuAwalTayang() != null ? 
                konten.getWaktuAwalTayang().toString() : null;
        
        this.waktuAkhirTayang = konten.getWaktuAkhirTayang() != null ? 
                konten.getWaktuAkhirTayang().toString() : null;
        
        // Map related entities to their respective DTOs
        if (konten.getKontenGambars() != null) {
            this.kontenGambars = konten.getKontenGambars().stream()
                    .map(KontenGambarDTO::new)
                    .collect(Collectors.toList());
        }
        
        if (konten.getKontenGambarUtamas() != null) {
            this.kontenGambarUtamas = konten.getKontenGambarUtamas().stream()
                    .map(KontenGambarUtamaDTO::new)
                    .collect(Collectors.toList());
        }
    }
}