package com.kehutanan.rh.monev.repository;

import com.kehutanan.rh.monev.model.Monev;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface MonevRepository extends JpaRepository<Monev, UUID>,JpaSpecificationExecutor<Monev> {
    List<Monev> findByProgram(String program);
    List<Monev> findByBpdas(String bpdas);
    List<Monev> findByKeteranganContaining(String keterangan);
    
    // Combined search
    List<Monev> findByProgramAndBpdas(String program, String bpdas);
}