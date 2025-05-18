package com.kehutanan.rh.dokumen.repository;

import com.kehutanan.rh.dokumen.model.DokumenFile;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DokumenFileRepository extends JpaRepository<DokumenFile, UUID> {
}