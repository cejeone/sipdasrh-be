package com.kehutanan.tktrh.bkta.program.repository;

import com.kehutanan.tktrh.bkta.program.model.ProgramPaguAnggaran;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository("bktaProgramPaguAnggaranRepository")
public interface ProgramPaguAnggaranRepository extends JpaRepository<ProgramPaguAnggaran, Long>, JpaSpecificationExecutor<ProgramPaguAnggaran> {

}