package com.kehutanan.pepdas.dokumen.repository;

import com.kehutanan.pepdas.dokumen.model.DokumenFile;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DokumenFileRepository extends JpaRepository<DokumenFile, UUID> {
}