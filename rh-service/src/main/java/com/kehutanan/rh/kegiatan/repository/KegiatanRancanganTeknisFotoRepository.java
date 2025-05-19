package com.kehutanan.rh.kegiatan.repository;

import com.kehutanan.rh.kegiatan.model.KegiatanRancanganTeknisFoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KegiatanRancanganTeknisFotoRepository extends JpaRepository<KegiatanRancanganTeknisFoto, UUID>, JpaSpecificationExecutor<KegiatanRancanganTeknisFoto> {
    List<KegiatanRancanganTeknisFoto> findByKegiatanId(UUID kegiatanId);
}