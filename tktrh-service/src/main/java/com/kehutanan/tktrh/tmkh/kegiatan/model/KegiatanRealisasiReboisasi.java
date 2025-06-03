
package com.kehutanan.tktrh.tmkh.kegiatan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kehutanan.tktrh.master.model.Lov;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "trx_ppkh_kegiatan_fungsi_kawasan")
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanRealisasiReboisasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "kegiatan_id", referencedColumnName = "id")
    @JsonBackReference
    private Kegiatan kegiatan;
    
    @ManyToOne
    @JoinColumn(name = "fungsi_kawasan_id", referencedColumnName = "id")
    private Lov fungsiKawasanId;
    
    @Column(name = "target_luas")
    private Double targetLuas;
    
    @Column(name = "realisasi_luas")
    private Double realisasiLuas;
    
    @Column(name = "tahun_id")
    private Integer tahunId;
    
    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;

        @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov statusId;


}