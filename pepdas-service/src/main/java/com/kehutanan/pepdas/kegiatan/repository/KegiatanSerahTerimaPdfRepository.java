package com.kehutanan.pepdas.kegiatan.repository;

import com.kehutanan.pepdas.kegiatan.model.KegiatanSerahTerimaPdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KegiatanSerahTerimaPdfRepository extends JpaRepository<KegiatanSerahTerimaPdf, UUID>, JpaSpecificationExecutor<KegiatanSerahTerimaPdf> {
    List<KegiatanSerahTerimaPdf> findByKegiatanSerahTerimaId(UUID kegiatanSerahTerimaId);
}