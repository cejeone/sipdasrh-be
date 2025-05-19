package com.kehutanan.rh.kegiatan.repository;

import com.kehutanan.rh.kegiatan.model.KegiatanFungsiKawasan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KegiatanFungsiKawasanRepository extends JpaRepository<KegiatanFungsiKawasan, UUID>, JpaSpecificationExecutor<KegiatanFungsiKawasan> {
    List<KegiatanFungsiKawasan> findByKegiatanId(UUID kegiatanId);
}