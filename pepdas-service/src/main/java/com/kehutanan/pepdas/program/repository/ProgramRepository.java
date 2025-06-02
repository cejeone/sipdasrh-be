package com.kehutanan.pepdas.program.repository;

import com.kehutanan.pepdas.program.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long>, JpaSpecificationExecutor<Program> {

}