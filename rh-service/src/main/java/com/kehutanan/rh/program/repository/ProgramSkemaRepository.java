package com.kehutanan.rh.program.repository;

import com.kehutanan.rh.program.model.ProgramSkema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramSkemaRepository extends JpaRepository<ProgramSkema, Long>, JpaSpecificationExecutor<ProgramSkema> {

}