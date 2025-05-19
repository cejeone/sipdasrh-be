package com.kehutanan.rh.dokumen.repository;

import com.kehutanan.rh.dokumen.model.Dokumen;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DokumenRepository extends JpaRepository<Dokumen, UUID>, JpaSpecificationExecutor<Dokumen> {
    Page<Dokumen> findByNamaDokumenContainingIgnoreCase(String search, Pageable pageable);
}