package com.kehutanan.pepdas.master.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "mst_eselon_1")
@NoArgsConstructor
@AllArgsConstructor
public class Eselon1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nama", length = 255)
    private String nama;
    
    @Column(name = "pejabat", length = 255)
    private String pejabat;
    
    @Column(name = "tugas_dan_fungsi", columnDefinition = "TEXT")
    private String tugasDanFungsi;
    
    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;
    
    // @OneToMany(mappedBy = "eselon1", cascade = CascadeType.ALL)
    // private List<Eselon2> eselon2List;
}