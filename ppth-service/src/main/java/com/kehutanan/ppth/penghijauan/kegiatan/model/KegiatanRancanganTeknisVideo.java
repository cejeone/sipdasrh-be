package com.kehutanan.ppth.penghijauan.kegiatan.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "penghijauanKegiatanRancanganTeknisVideo")
@Table(name = "trx_ppth_kegiatan_penghijauan_rancangan_teknis_video")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanRancanganTeknisVideo {

    @Id
    private UUID id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kegiatan_id", referencedColumnName = "id")
    private Kegiatan kegiatan;

    private String namaFile;

    private String namaAsli;

    private String pathFile;

    private Double ukuranMb;

    private String contentType;

    private LocalDateTime uploadedAt;

    private String viewUrl;

    private String downloadUrl;
}