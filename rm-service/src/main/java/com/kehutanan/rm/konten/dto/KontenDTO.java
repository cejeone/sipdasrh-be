package com.kehutanan.rm.konten.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kehutanan.rm.konten.model.Konten;
import com.kehutanan.rm.konten.model.KontenGambar;
import com.kehutanan.rm.konten.model.KontenGambarUtama;

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
    private List<String> kataKunci = new ArrayList<>();
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String waktuAwalTayang;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String waktuAkhirTayang;
    
    // Related entity IDs instead of full entities
    private Long tipeId;
    private String tipeName;
    private Long statusId;
    private String statusName;
    
    // Lists for file references
    private List<KontenFileDTO> kontenGambars = new ArrayList<>();
    private List<KontenFileDTO> kontenGambarUtamas = new ArrayList<>();
    
    // Constructor to convert from Entity
    public KontenDTO(Konten entity) {
        this.id = entity.getId();
        this.judul = entity.getJudul();
        this.konten = entity.getKonten();
        
        if (entity.getKataKunci() != null) {
            this.kataKunci = new ArrayList<>(entity.getKataKunci());
        }
        
        // Convert LocalDateTime to String
        if (entity.getWaktuAwalTayang() != null) {
            this.waktuAwalTayang = entity.getWaktuAwalTayang().toString();
        }
        
        if (entity.getWaktuAkhirTayang() != null) {
            this.waktuAkhirTayang = entity.getWaktuAkhirTayang().toString();
        }
        
        // Set related entity IDs and names
        if (entity.getTipe() != null) {
            this.tipeId = entity.getTipe().getId();
            this.tipeName = entity.getTipe().getNilai();
        }
        
        if (entity.getStatus() != null) {
            this.statusId = entity.getStatus().getId();
            this.statusName = entity.getStatus().getNilai();
        }
        
        // Convert file lists
        if (entity.getKontenGambars() != null) {
            for (KontenGambar gambar : entity.getKontenGambars()) {
                this.kontenGambars.add(new KontenFileDTO(gambar));
            }
        }
        
        if (entity.getKontenGambarUtamas() != null) {
            for (KontenGambarUtama gambarUtama : entity.getKontenGambarUtamas()) {
                this.kontenGambarUtamas.add(new KontenFileDTO(gambarUtama));
            }
        }
    }
}