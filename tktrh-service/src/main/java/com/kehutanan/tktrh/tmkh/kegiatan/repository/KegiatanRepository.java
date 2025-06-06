package com.kehutanan.tktrh.tmkh.kegiatan.repository;

import com.kehutanan.tktrh.tmkh.kegiatan.model.Kegiatan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository("tmkhKegiatanRepository")
public interface KegiatanRepository extends JpaRepository<Kegiatan, Long>, JpaSpecificationExecutor<Kegiatan> {
    
}
