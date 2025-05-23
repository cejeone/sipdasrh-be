package com.kehutanan.pepdas.kegiatan.repository;

import com.kehutanan.pepdas.kegiatan.model.KegiatanMonev;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KegiatanMonevRepository extends JpaRepository<KegiatanMonev, UUID>, JpaSpecificationExecutor<KegiatanMonev> {
    List<KegiatanMonev> findByKegiatanId(UUID kegiatanId);
}