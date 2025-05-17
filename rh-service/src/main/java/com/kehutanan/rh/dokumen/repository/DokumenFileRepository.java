package com.kehutanan.rh.dokumen.repository;

import com.kehutanan.rh.dokumen.model.DokumenFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DokumenFileRepository extends JpaRepository<DokumenFile, Long> {
}