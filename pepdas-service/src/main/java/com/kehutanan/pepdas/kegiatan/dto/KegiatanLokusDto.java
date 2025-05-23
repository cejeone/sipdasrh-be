package com.kehutanan.pepdas.kegiatan.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.pepdas.kegiatan.model.Kegiatan;
import com.kehutanan.pepdas.kegiatan.model.KegiatanLokusShp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanLokusDto {
    


    private String provinsi;

    private String kabupatenKota;
    
    private String kecamatan;
    

    private String kelurahanDesa;

    private String alamat;

    private UUID kegiatanId;

}