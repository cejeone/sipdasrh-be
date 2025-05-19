package com.kehutanan.rh.kegiatan.repository;

import com.kehutanan.rh.kegiatan.model.Kegiatan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface KegiatanRepository extends JpaRepository<Kegiatan, UUID>, JpaSpecificationExecutor<Kegiatan> {
}