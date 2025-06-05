package com.kehutanan.rm.program.repository;

import com.kehutanan.rm.program.model.ProgramPaguAnggaran;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramPaguAnggaranRepository extends JpaRepository<ProgramPaguAnggaran, Long>, JpaSpecificationExecutor<ProgramPaguAnggaran> {

}