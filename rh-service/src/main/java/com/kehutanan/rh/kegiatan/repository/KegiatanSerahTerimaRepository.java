package com.kehutanan.rh.kegiatan.repository;

import com.kehutanan.rh.kegiatan.model.KegiatanSerahTerima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KegiatanSerahTerimaRepository extends JpaRepository<KegiatanSerahTerima, UUID>, JpaSpecificationExecutor<KegiatanSerahTerima> {
    List<KegiatanSerahTerima> findByKegiatanId(UUID kegiatanId);
}