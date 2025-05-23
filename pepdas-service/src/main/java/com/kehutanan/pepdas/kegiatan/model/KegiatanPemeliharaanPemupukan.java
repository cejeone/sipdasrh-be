package com.kehutanan.pepdas.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "pepdas_kegiatan_pemeliharaan_pemupukan")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanPemeliharaanPemupukan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private String jenis;
    
    @Column(name = "waktu_pemupukan")
    private LocalDate waktuPemupukan;
    
    @Column(name = "jumlah_pupuk")
    private Integer jumlahPupuk;
    
    private String satuan;
    
    @Column(name = "jumlah_hok_perempuan")
    private Integer jumlahHokPerempuan;
    
    @Column(name = "jumlah_hok_laki_laki")
    private Integer jumlahHokLakiLaki;
    
    @Column(columnDefinition = "TEXT")
    private String keterangan;
    
    private String status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kegiatan_id", nullable = false)
    @JsonBackReference
    private Kegiatan kegiatan;
}