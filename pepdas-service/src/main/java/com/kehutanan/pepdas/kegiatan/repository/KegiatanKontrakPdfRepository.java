package com.kehutanan.pepdas.kegiatan.repository;

import com.kehutanan.pepdas.kegiatan.model.KegiatanKontrakPdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KegiatanKontrakPdfRepository extends JpaRepository<KegiatanKontrakPdf, UUID>, JpaSpecificationExecutor<KegiatanKontrakPdf> {
    List<KegiatanKontrakPdf> findByKegiatanId(UUID kegiatanId);
}