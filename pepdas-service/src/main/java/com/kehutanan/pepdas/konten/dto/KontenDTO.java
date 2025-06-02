package com.kehutanan.pepdas.konten.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.pepdas.konten.model.Konten;
import com.kehutanan.pepdas.konten.model.KontenGambar;
import com.kehutanan.pepdas.konten.model.KontenGambarUtama;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KontenDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String judul;
    private String konten;
    private List<String> kataKunci;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime waktuAwalTayang;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime waktuAkhirTayang;
    
    // Related entity IDs instead of full entities
    private Long tipeId;
    private String tipeNama;
    private Long statusId;
    private String statusNama;
    
    // Lists for file references
    private List<KontenFileDTO> kontenGambarList = new ArrayList<>();
    private List<KontenFileDTO> kontenGambarUtamaList = new ArrayList<>();
    
    // Constructor to convert from Entity
    public KontenDTO(Konten entity) {
        this.id = entity.getId();
        this.judul = entity.getJudul();
        this.konten = entity.getKonten();
        this.kataKunci = entity.getKataKunci();
        this.waktuAwalTayang = entity.getWaktuAwalTayang();
        this.waktuAkhirTayang = entity.getWaktuAkhirTayang();
        
        // Set related entity IDs and names
        if (entity.getTipe() != null) {
            this.tipeId = entity.getTipe().getId();
            this.tipeNama = entity.getTipe().getNilai(); // Assuming this field exists
        }
        
        if (entity.getStatus() != null) {
            this.statusId = entity.getStatus().getId();
            this.statusNama = entity.getStatus().getNilai(); // Assuming this field exists
        }
        
        // Convert file lists
        if (entity.getKontenGambars() != null) {
            for (KontenGambar gambar : entity.getKontenGambars()) {
                this.kontenGambarList.add(new KontenFileDTO(gambar));
            }
        }
        
        if (entity.getKontenGambarUtamas() != null) {
            for (KontenGambarUtama gambarUtama : entity.getKontenGambarUtamas()) {
                this.kontenGambarUtamaList.add(new KontenFileDTO(gambarUtama));
            }
        }
    }
}