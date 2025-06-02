package com.kehutanan.pepdas.geoservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

import com.kehutanan.pepdas.master.model.Bpdas;
import com.kehutanan.pepdas.master.model.Eselon2;
import com.kehutanan.pepdas.master.model.Lov;


@Data
@Entity
@Table(name = "trx_pepdas_geoservice")
@NoArgsConstructor
@AllArgsConstructor
public class GeoService implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "eselon_2_id", referencedColumnName = "id")
    private Eselon2 eselon2Id;

    @ManyToOne
    @JoinColumn(name = "bpdas_id", referencedColumnName = "id")
    private Bpdas bpdasId;

    @ManyToOne
    @JoinColumn(name = "tipe_id", referencedColumnName = "id")
    private Lov tipeId;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Lov statusId;

    @Column(name = "geoservice_id")
    private Integer geoserviceId;

    @Column(name = "url", columnDefinition = "TEXT")
    private String url;

    @Column(name = "service", columnDefinition = "TEXT")
    private String service;
}