package com.kehutanan.tktrh.ppkh.kegiatan.repository;

import com.kehutanan.tktrh.ppkh.kegiatan.model.Kegiatan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository("ppkhKegiatanRepository")
public interface KegiatanRepository extends JpaRepository<Kegiatan, Long>, JpaSpecificationExecutor<Kegiatan> {

}
