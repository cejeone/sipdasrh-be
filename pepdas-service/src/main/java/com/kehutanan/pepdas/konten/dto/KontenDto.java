package com.kehutanan.pepdas.konten.dto;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KontenDto {

    private String tipe;

    private String judul;

    private String konten;


    private List<String> kataKunci;


    private LocalDateTime waktuAwalTayang;

    private LocalDateTime waktuAkhirTayang;

    private String status;


}