package com.kehutanan.rh.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "rh_kegiatan_serah_terima")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanSerahTerima {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private Integer tahun;
    
    private String tahap;
    
    @Column(name = "target_luas")
    private Integer targetLuas;
    
    @Column(name = "realisasi_luas")
    private Integer realisasiLuas;
    
    @Column(name = "jenis_tanaman")
    private String jenisTanaman;
    
    @Column(name = "kelompok_masyarakat")
    private String kelompokMasyarakat;
    
    private String status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kegiatan_id", nullable = false)
    @JsonBackReference
    private Kegiatan kegiatan;

    
    @OneToMany(mappedBy = "kegiatanSerahTerima", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanSerahTerimaPdf> kegiatanSerahTerimaPdfs = new ArrayList<>();

}