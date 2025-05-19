package com.kehutanan.rh.kegiatan.repository;

import com.kehutanan.rh.kegiatan.model.KegiatanMonevPdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KegiatanMonevPdfRepository extends JpaRepository<KegiatanMonevPdf, UUID>, JpaSpecificationExecutor<KegiatanMonevPdf> {
    List<KegiatanMonevPdf> findByKegiatanMonevId(UUID kegiatanMonevId);
}