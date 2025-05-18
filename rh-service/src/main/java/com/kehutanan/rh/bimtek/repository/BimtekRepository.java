package com.kehutanan.rh.bimtek.repository;

import com.kehutanan.rh.bimtek.model.Bimtek;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface BimtekRepository extends JpaRepository<Bimtek, UUID> {
    Page<Bimtek> findByNamaBimtekContainingIgnoreCase(String search, Pageable pageable);
}
