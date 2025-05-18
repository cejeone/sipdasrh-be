package com.kehutanan.rh.bimtek.repository;

import com.kehutanan.rh.bimtek.model.BimtekFoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface BimtekFotoRepository extends JpaRepository<BimtekFoto, UUID> {
    List<BimtekFoto> findByBimtekId(UUID bimtekId);
}
