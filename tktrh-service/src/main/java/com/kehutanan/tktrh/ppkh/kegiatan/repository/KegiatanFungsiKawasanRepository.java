package com.kehutanan.tktrh.ppkh.kegiatan.repository;

import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanFungsiKawasan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KegiatanFungsiKawasanRepository extends JpaRepository<KegiatanFungsiKawasan, Long>, JpaSpecificationExecutor<KegiatanFungsiKawasan> {

}
