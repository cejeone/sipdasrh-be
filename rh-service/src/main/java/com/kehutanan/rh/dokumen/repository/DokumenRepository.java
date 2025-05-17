package com.kehutanan.rh.dokumen.repository;

import com.kehutanan.rh.dokumen.model.Dokumen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DokumenRepository extends JpaRepository<Dokumen, Long> {
    Page<Dokumen> findByNamaDokumenContainingIgnoreCase(String search, Pageable pageable);
}