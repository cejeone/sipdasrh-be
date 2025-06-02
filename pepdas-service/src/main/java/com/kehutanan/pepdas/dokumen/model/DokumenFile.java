package com.kehutanan.pepdas.dokumen.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "trx_pepdas_dokumen_file")
@NoArgsConstructor
@AllArgsConstructor
public class DokumenFile {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "dokumen_id", referencedColumnName = "id")
    @JsonBackReference
    private Dokumen dokumen;

    @Column(name = "nama_asli")
    private String namaAsli;

    @Column(name = "nama_file")
    private String namaFile;

    @Column(name = "path_file")
    private String pathFile;

    @Column(name = "view_url")
    private String viewUrl;

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "ukuran_mb")
    private Double ukuranMb;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;
}