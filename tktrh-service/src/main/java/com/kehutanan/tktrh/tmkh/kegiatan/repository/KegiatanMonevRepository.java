package com.kehutanan.tktrh.tmkh.kegiatan.repository;

import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanMonev;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KegiatanMonevRepository extends JpaRepository<KegiatanMonev, Long>, JpaSpecificationExecutor<KegiatanMonev> {

}
