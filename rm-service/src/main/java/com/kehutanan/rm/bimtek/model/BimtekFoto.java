package com.kehutanan.rm.bimtek.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kehutanan.rm.master.model.Bpdas;
import com.kehutanan.rm.master.model.Uptd;
import com.kehutanan.rm.program.model.Program;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "trx_rm_bimtek_foto")
@NoArgsConstructor
@AllArgsConstructor
public class BimtekFoto implements Serializable{

    @Id
    private UUID id;
    
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bimtek_id", referencedColumnName = "id")
    private Bimtek bimtek;
    
    private String namaFile;
    
    private String namaAsli;

    private String pathFile;
    
    private Double ukuranMb;
    
    private String contentType;
    
    private LocalDateTime uploadedAt;

    private String viewUrl;

    private String downloadUrl;

}