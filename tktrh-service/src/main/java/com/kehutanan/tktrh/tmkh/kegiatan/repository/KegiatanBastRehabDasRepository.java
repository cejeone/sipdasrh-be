package com.kehutanan.tktrh.tmkh.kegiatan.repository;

import com.kehutanan.tktrh.tmkh.kegiatan.model.KegiatanBastRehabDas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KegiatanBastRehabDasRepository extends JpaRepository<KegiatanBastRehabDas, Long>, JpaSpecificationExecutor<KegiatanBastRehabDas> {

}
