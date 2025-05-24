package com.kehutanan.pepdas.monev.repository;

import com.kehutanan.pepdas.monev.model.MonevPdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MonevPdfRepository extends JpaRepository<MonevPdf, UUID> {}