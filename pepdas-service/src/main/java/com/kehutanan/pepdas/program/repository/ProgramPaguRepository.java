package com.kehutanan.pepdas.program.repository;

import com.kehutanan.pepdas.program.model.ProgramPagu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramPaguRepository extends JpaRepository<ProgramPagu, Long>, JpaSpecificationExecutor<ProgramPagu> {

}