package com.kehutanan.rh.monev.repository;

import com.kehutanan.rh.monev.model.Monev;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface MonevRepository extends JpaRepository<Monev, UUID> {
}