package com.kehutanan.pepdas.serahterima.repository;

import com.kehutanan.pepdas.serahterima.model.SerahTerima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.UUID;

public interface SerahTerimaRepository extends JpaRepository<SerahTerima, UUID>, JpaSpecificationExecutor<SerahTerima> {
    // existing methods...
}