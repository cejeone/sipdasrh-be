package com.kehutanan.pepdas.konten.repository;

import com.kehutanan.pepdas.konten.model.KontenGambar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KontenGambarRepository extends JpaRepository<KontenGambar, UUID> {
    List<KontenGambar> findByKontenId(UUID kontenId);
}
