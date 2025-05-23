package com.kehutanan.pepdas.kegiatan.repository;

import com.kehutanan.pepdas.kegiatan.model.KegiatanMonevKriteriaEvaluasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KegiatanMonevKriteriaEvaluasiRepository extends JpaRepository<KegiatanMonevKriteriaEvaluasi, UUID>, JpaSpecificationExecutor<KegiatanMonevKriteriaEvaluasi> {
    List<KegiatanMonevKriteriaEvaluasi> findByKegiatanMonevId(UUID kegiatanMonevId);
}