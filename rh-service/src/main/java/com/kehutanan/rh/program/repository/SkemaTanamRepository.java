package com.kehutanan.rh.program.repository;

import com.kehutanan.rh.program.model.PaguAnggaran;
import com.kehutanan.rh.program.model.SkemaTanam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface SkemaTanamRepository extends JpaRepository<SkemaTanam, UUID>,JpaSpecificationExecutor<SkemaTanam>  {
    List<SkemaTanam> findByProgramId(UUID programId);
}