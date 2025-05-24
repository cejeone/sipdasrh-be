package com.kehutanan.pepdas.monev.repository;

import com.kehutanan.pepdas.monev.model.Monev;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface MonevRepository extends JpaRepository<Monev, UUID>,JpaSpecificationExecutor<Monev> {

}