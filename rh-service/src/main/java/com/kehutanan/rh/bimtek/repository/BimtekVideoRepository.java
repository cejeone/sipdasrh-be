package com.kehutanan.rh.bimtek.repository;

import com.kehutanan.rh.bimtek.model.BimtekVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BimtekVideoRepository extends JpaRepository<BimtekVideo, UUID> {
    List<BimtekVideo> findByBimtekId(UUID bimtekId);
    Optional<BimtekVideo> findByNamaFile(String namaFile);
}