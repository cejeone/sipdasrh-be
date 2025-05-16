package com.kehutanan.rh.program.repository;

import com.kehutanan.rh.program.model.JenisBibit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface JenisBibitRepository extends JpaRepository<JenisBibit, UUID> {
    List<JenisBibit> findByProgram_Id(UUID programId);
}