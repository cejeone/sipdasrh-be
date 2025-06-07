package com.kehutanan.tktrh.bkta.kegiatan.repository;

import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanMonev;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository("bktaKegiatanMonevRepository")
public interface KegiatanMonevRepository extends JpaRepository<KegiatanMonev, Long>, JpaSpecificationExecutor<KegiatanMonev> {

}