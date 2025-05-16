package com.kehutanan.rh.repository;

import com.kehutanan.rh.model.PaguAnggaran;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface PaguAnggaranRepository extends JpaRepository<PaguAnggaran, UUID> {
    List<PaguAnggaran> findByProgram_Id(UUID programId);
}