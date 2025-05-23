package com.kehutanan.pepdas.kegiatan.repository;

import com.kehutanan.pepdas.kegiatan.model.KegiatanLokus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KegiatanLokusRepository extends JpaRepository<KegiatanLokus, UUID>, JpaSpecificationExecutor<KegiatanLokus> {
    List<KegiatanLokus> findByKegiatanId(UUID kegiatanId);
}