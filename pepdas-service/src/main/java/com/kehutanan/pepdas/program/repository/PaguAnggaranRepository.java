package com.kehutanan.pepdas.program.repository;

import com.kehutanan.pepdas.program.model.PaguAnggaran;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface PaguAnggaranRepository extends JpaRepository<PaguAnggaran, UUID>,JpaSpecificationExecutor<PaguAnggaran> {
    List<PaguAnggaran> findByProgramId(UUID programId);
}