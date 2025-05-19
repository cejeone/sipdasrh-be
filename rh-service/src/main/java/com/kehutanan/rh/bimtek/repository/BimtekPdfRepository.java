package com.kehutanan.rh.bimtek.repository;

import com.kehutanan.rh.bimtek.model.BimtekPdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BimtekPdfRepository extends JpaRepository<BimtekPdf, UUID> {
    List<BimtekPdf> findByBimtekId(UUID bimtekId);
    Optional<BimtekPdf> findByNamaFile(String namaFile);
}