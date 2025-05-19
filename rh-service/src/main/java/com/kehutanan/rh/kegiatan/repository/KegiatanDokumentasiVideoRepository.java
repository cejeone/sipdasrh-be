package com.kehutanan.rh.kegiatan.repository;

import com.kehutanan.rh.kegiatan.model.KegiatanDokumentasiVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KegiatanDokumentasiVideoRepository extends JpaRepository<KegiatanDokumentasiVideo, UUID>, JpaSpecificationExecutor<KegiatanDokumentasiVideo> {
    List<KegiatanDokumentasiVideo> findByKegiatanId(UUID kegiatanId);
}