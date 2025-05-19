package com.kehutanan.rh.kegiatan.repository;

import com.kehutanan.rh.kegiatan.model.KegiatanDokumentasiFoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KegiatanDokumentasiFotoRepository extends JpaRepository<KegiatanDokumentasiFoto, UUID>, JpaSpecificationExecutor<KegiatanDokumentasiFoto> {
    List<KegiatanDokumentasiFoto> findByKegiatanId(UUID kegiatanId);
}