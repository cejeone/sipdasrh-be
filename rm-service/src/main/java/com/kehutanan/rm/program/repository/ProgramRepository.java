package com.kehutanan.rm.program.repository;

import com.kehutanan.rm.program.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long>, JpaSpecificationExecutor<Program> {

}