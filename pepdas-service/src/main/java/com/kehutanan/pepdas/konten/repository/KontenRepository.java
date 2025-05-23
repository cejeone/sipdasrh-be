package com.kehutanan.pepdas.konten.repository;

import com.kehutanan.pepdas.konten.model.Konten;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KontenRepository extends JpaRepository<Konten, UUID>, JpaSpecificationExecutor<Konten> {
    // Add custom query methods here if needed
}