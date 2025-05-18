package com.kehutanan.rh.serahterima.repository;

import com.kehutanan.rh.serahterima.model.SerahTerima;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SerahTerimaRepository extends JpaRepository<SerahTerima, UUID> {
}