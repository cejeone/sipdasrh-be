package com.kehutanan.tktrh.bkta.kegiatan.repository;

import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanMonevKriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository("bktaKegiatanMonevKriteriaRepository")
public interface KegiatanMonevKriteriaRepository extends JpaRepository<KegiatanMonevKriteria, Long>, JpaSpecificationExecutor<KegiatanMonevKriteria> {

}