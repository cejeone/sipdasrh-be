package com.kehutanan.tktrh.ppkh.kegiatan.repository;

import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRiwayatSk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository("ppkhKegiatanRiwayatSkRepository")
public interface KegiatanRiwayatSkRepository extends JpaRepository<KegiatanRiwayatSk, Long>, JpaSpecificationExecutor<KegiatanRiwayatSk> {

}