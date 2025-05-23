package com.kehutanan.pepdas.kegiatan.repository;

import com.kehutanan.pepdas.kegiatan.model.KegiatanFungsiKawasan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface KegiatanFungsiKawasanRepository extends JpaRepository<KegiatanFungsiKawasan, UUID>, 
                                                         JpaSpecificationExecutor<KegiatanFungsiKawasan> {
    // Custom query methods can be added here if needed
}