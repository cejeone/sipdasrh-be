package com.kehutanan.rh.repository;

import com.kehutanan.rh.model.SkemaTanam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface SkemaTanamRepository extends JpaRepository<SkemaTanam, UUID> {
    List<SkemaTanam> findByProgram_Id(UUID programId);
}