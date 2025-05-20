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
@Table(name = "rh_kegiatan_lokus")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanLokus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String provinsi;
    
    @Column(name = "kabupaten_kota")
    private String kabupatenKota;
    
    private String kecamatan;
    
    @Column(name = "kelurahan_desa")
    private String kelurahanDesa;
    
    @Column(columnDefinition = "TEXT")
    private String alamat;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kegiatan_id", nullable = false)
    @JsonBackReference
    private Kegiatan kegiatan;

    @OneToMany(mappedBy = "kegiatanLokus", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanLokusShp> kegiatanLokusShps = new ArrayList<>();
}