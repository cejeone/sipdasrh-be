package com.kehutanan.pepdas.kegiatan.repository;

import com.kehutanan.pepdas.kegiatan.model.Kegiatan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KegiatanRepository extends JpaRepository<Kegiatan, Long>, JpaSpecificationExecutor<Kegiatan> {

}