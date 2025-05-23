package com.kehutanan.rh.bimtek.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BimtekDto {


    private String namaBimtek;


    private String subjek;

    private String program;

    private String bpdas;

    private String tempat;

    private LocalDate tanggal;


    private String audience;

    private String evaluasi;

    private String keterangan;

}