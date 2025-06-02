package com.kehutanan.rh.kegiatan.repository;

import com.kehutanan.rh.kegiatan.model.KegiatanPemeliharaanSulaman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KegiatanPemeliharaanSulamanRepository extends JpaRepository<KegiatanPemeliharaanSulaman, Long>, JpaSpecificationExecutor<KegiatanPemeliharaanSulaman> {

}