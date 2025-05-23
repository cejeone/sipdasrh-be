package com.kehutanan.pepdas.konten.repository;

import com.kehutanan.pepdas.konten.model.KontenGambarUtama;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface KontenGambarUtamaRepository extends JpaRepository<KontenGambarUtama, UUID> {
}
