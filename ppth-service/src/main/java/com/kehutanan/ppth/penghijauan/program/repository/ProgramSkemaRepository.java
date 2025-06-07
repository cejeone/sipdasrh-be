package com.kehutanan.ppth.penghijauan.program.repository;

import com.kehutanan.ppth.penghijauan.program.model.ProgramSkema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramSkemaRepository extends JpaRepository<ProgramSkema, Long>, JpaSpecificationExecutor<ProgramSkema> {

}