package com.kehutanan.ppth.mataair.program.repository;

import com.kehutanan.ppth.mataair.program.model.ProgramPaguAnggaran;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramPaguAnggaranRepository extends JpaRepository<ProgramPaguAnggaran, Long>, JpaSpecificationExecutor<ProgramPaguAnggaran> {

}