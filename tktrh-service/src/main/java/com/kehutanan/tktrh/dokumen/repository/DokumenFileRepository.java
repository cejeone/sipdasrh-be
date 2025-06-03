package com.kehutanan.tktrh.dokumen.repository;

import com.kehutanan.tktrh.dokumen.model.DokumenFile;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DokumenFileRepository extends JpaRepository<DokumenFile, UUID> {
}