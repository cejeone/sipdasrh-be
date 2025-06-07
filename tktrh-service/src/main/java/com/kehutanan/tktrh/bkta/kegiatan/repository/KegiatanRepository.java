package com.kehutanan.tktrh.bkta.kegiatan.repository;

import com.kehutanan.tktrh.bkta.kegiatan.model.Kegiatan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository("bktaKegiatanRepository")
public interface KegiatanRepository extends JpaRepository<Kegiatan, Long>, JpaSpecificationExecutor<Kegiatan> {

}