package com.kehutanan.rh.kegiatan.repository;

import com.kehutanan.rh.kegiatan.model.KegiatanRancanganTeknisVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KegiatanRancanganTeknisVideoRepository extends JpaRepository<KegiatanRancanganTeknisVideo, UUID>, JpaSpecificationExecutor<KegiatanRancanganTeknisVideo> {
    List<KegiatanRancanganTeknisVideo> findByKegiatanId(UUID kegiatanId);
}