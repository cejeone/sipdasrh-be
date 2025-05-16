package com.kehutanan.rh.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "rh_program")
@NoArgsConstructor
@AllArgsConstructor
public class Program {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String direktorat;
    
    @Column(nullable = false)
    private String kategori;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String nama;
    
    @Column(name = "tahun_pelaksanaan", nullable = false)
    private Integer tahunPelaksanaan;
    
    @Column(name = "total_anggaran", precision = 19, scale = 2)
    private BigDecimal totalAnggaran;
    
    @Column(name = "target_luas")
    private Integer targetLuas;
    
    @Column(nullable = false)
    private String status;

    @JsonManagedReference
    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<JenisBibit> jenisBibits;

    @JsonManagedReference
    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<PaguAnggaran> paguAnggarans;

    @JsonManagedReference
    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<SkemaTanam> skemaTanams;
}