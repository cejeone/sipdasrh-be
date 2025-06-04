package com.kehutanan.tktrh.bkta.kegiatan.repository;

import com.kehutanan.tktrh.bkta.kegiatan.model.KegiatanBast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KegiatanBastRepository extends JpaRepository<KegiatanBast, Long>, JpaSpecificationExecutor<KegiatanBast> {

}