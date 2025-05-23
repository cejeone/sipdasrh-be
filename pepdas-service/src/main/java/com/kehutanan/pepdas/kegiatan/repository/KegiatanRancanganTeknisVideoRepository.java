package com.kehutanan.pepdas.kegiatan.repository;

import com.kehutanan.pepdas.kegiatan.model.KegiatanRancanganTeknisVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KegiatanRancanganTeknisVideoRepository extends JpaRepository<KegiatanRancanganTeknisVideo, UUID>, JpaSpecificationExecutor<KegiatanRancanganTeknisVideo> {
    List<KegiatanRancanganTeknisVideo> findByKegiatanId(UUID kegiatanId);
}