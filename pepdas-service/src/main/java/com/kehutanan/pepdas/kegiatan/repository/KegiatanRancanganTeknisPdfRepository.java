package com.kehutanan.pepdas.kegiatan.repository;

import com.kehutanan.pepdas.kegiatan.model.KegiatanRancanganTeknisPdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KegiatanRancanganTeknisPdfRepository extends JpaRepository<KegiatanRancanganTeknisPdf, UUID>, JpaSpecificationExecutor<KegiatanRancanganTeknisPdf> {
    List<KegiatanRancanganTeknisPdf> findByKegiatanId(UUID kegiatanId);
}