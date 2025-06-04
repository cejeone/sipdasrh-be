package com.kehutanan.pepdas.konten.model.dto;

import com.kehutanan.pepdas.konten.model.Konten;
import com.kehutanan.pepdas.master.model.dto.LovDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<String> kataKunci;
    private String waktuAwalTayang;
    private String waktuAkhirTayang;
    private List<KontenGambarDTO> kontenGambars = new ArrayList<>();
    private List<KontenGambarUtamaDTO> kontenGambarUtamas = new ArrayList<>();
    
    // Constructor from entity
    public KontenDTO(Konten konten) {
        this.id = konten.getId();
        this.tipe = konten.getTipe() != null ? new LovDTO(konten.getTipe()) : null;
        this.status = konten.getStatus() != null ? new LovDTO(konten.getStatus()) : null;
        this.judul = konten.getJudul();
        this.konten = konten.getKonten();
        this.kataKunci = konten.getKataKunci();
        this.waktuAwalTayang = konten.getWaktuAwalTayang() != null ? 
                konten.getWaktuAwalTayang().toString() : null;
        this.waktuAkhirTayang = konten.getWaktuAkhirTayang() != null ? 
                konten.getWaktuAkhirTayang().toString() : null;
                
        // Convert KontenGambar list to DTO
        if (konten.getKontenGambars() != null) {
            this.kontenGambars = konten.getKontenGambars().stream()
                    .map(KontenGambarDTO::new)
                    .collect(Collectors.toList());
        }
        
        // Convert KontenGambarUtama list to DTO
        if (konten.getKontenGambarUtamas() != null) {
            this.kontenGambarUtamas = konten.getKontenGambarUtamas().stream()
                    .map(KontenGambarUtamaDTO::new)
                    .collect(Collectors.toList());
        }
    }
}