package com.kehutanan.rh.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kehutanan.rh.master.model.Lov;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "trx_rh_kegiatan_p2_bast")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanBast {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "kegiatan_id", referencedColumnName = "id")
    @JsonBackReference
    private Kegiatan kegiatan;
    
    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov statusId;
    
    @ManyToOne
    @JoinColumn(name = "tahap_id", referencedColumnName = "id")
    private Lov tahapId;
    
    private String tahun;
    
    @Column(name = "target_luas")
    private String targetLuas;
    
    @Column(name = "realisasi_luas")
    private String realisasiLuas;
    
    @Column(name = "jenis_tanaman_id")
    private String jenisTanamanId;
    
    @Column(name = "kelompok_masyarakat_id")
    private String kelompokMasyarakatId;
    

    @OneToMany(mappedBy = "kegiatanBast", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KegiatanBastPdf> kegiatanBastPdfs = new ArrayList<>();

}