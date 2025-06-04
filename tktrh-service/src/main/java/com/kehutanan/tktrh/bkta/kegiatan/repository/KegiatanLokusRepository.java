package com.kehutanan.tktrh.bkta.kegiatan.repository;

import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanLokus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KegiatanLokusRepository extends JpaRepository<KegiatanLokus, Long>, JpaSpecificationExecutor<KegiatanLokus> {

}