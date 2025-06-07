package com.kehutanan.ppth.mataair.kegiatan.repository;

import com.kehutanan.ppth.mataair.kegiatan.model.KegiatanMonevKriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KegiatanMonevKriteriaRepository extends JpaRepository<KegiatanMonevKriteria, Long>, JpaSpecificationExecutor<KegiatanMonevKriteria> {
    List<KegiatanMonevKriteria> findByKegiatanMonevId(Long kegiatanMonevId);
}