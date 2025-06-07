package com.kehutanan.ppth.dokumen.repository;

import com.kehutanan.ppth.dokumen.model.DokumenFile;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DokumenFileRepository extends JpaRepository<DokumenFile, UUID> {
}