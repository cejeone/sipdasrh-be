package com.kehutanan.ppth.penghijauan.kegiatan.repository;

import com.kehutanan.ppth.penghijauan.kegiatan.model.KegiatanPemeliharaanPemupukan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KegiatanPemeliharaanPemupukanRepository extends JpaRepository<KegiatanPemeliharaanPemupukan, Long>, JpaSpecificationExecutor<KegiatanPemeliharaanPemupukan> {
    // You can add custom query methods here if needed
}