
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
public class KegiatanFungsiKawasanLahanPengganti {

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
    
    @Column(name = "luas")
    private Double luas;

        
    @ManyToOne
    @JoinColumn(name = "tumpang_tindih", referencedColumnName = "id")
    private Lov tumpangTindih;


    @Column(name = "rasio_tumpang_tindih")
    private Double rasioTumpangTindih;
    
    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;

        @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov statusId;


}