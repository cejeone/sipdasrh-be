package com.kehutanan.tktrh.tmkh.kegiatan.repository;

import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanMonevKriteria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository("tmkhKegiatanMonevKriteriaRepository")
public interface KegiatanMonevKriteriaRepository extends JpaRepository<KegiatanMonevKriteria, Long>, JpaSpecificationExecutor<KegiatanMonevKriteria> {

}
