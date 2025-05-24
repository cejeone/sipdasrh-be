package com.kehutanan.pepdas.serahterima.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kehutanan.pepdas.serahterima.model.SerahTerimaPdf;

public interface SerahTerimaPdfRepository extends JpaRepository<SerahTerimaPdf, UUID> {
}