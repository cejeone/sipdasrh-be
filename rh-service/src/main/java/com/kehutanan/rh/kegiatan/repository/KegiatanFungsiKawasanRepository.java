package com.kehutanan.rh.kegiatan.repository;

import com.kehutanan.rh.kegiatan.model.KegiatanFungsiKawasan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KegiatanFungsiKawasanRepository extends JpaRepository<KegiatanFungsiKawasan, Long>, JpaSpecificationExecutor<KegiatanFungsiKawasan> {
    
}