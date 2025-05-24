package com.kehutanan.pepdas.serahterima.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.pepdas.serahterima.model.SerahTerimaPdf;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SerahTerimaDto {
    

    private String nomor;
    private String kegiatan;
    private String kontrak;
    private LocalDate tanggal;
    private String deskripsi;
    private String status;
}