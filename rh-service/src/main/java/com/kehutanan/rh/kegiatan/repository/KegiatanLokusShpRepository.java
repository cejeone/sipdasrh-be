package com.kehutanan.rh.kegiatan.repository;

import com.kehutanan.rh.kegiatan.model.KegiatanLokusShp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KegiatanLokusShpRepository extends JpaRepository<KegiatanLokusShp, UUID>, JpaSpecificationExecutor<KegiatanLokusShp> {
    List<KegiatanLokusShp> findByKegiatanId(UUID kegiatanId);
}