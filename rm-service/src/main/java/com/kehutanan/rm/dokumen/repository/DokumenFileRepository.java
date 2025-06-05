package com.kehutanan.rm.dokumen.repository;

import com.kehutanan.rm.dokumen.model.DokumenFile;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DokumenFileRepository extends JpaRepository<DokumenFile, UUID> {
}