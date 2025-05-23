package com.kehutanan.pepdas.kegiatan.repository;

import com.kehutanan.pepdas.kegiatan.model.KegiatanPemeliharaanSulaman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KegiatanPemeliharaanSulamanRepository extends JpaRepository<KegiatanPemeliharaanSulaman, UUID>, JpaSpecificationExecutor<KegiatanPemeliharaanSulaman> {
    List<KegiatanPemeliharaanSulaman> findByKegiatanId(UUID kegiatanId);
}