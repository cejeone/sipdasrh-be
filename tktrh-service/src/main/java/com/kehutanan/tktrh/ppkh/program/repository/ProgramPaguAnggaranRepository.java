package com.kehutanan.tktrh.ppkh.program.repository;

import com.kehutanan.tktrh.ppkh.program.model.ProgramPaguAnggaran;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramPaguAnggaranRepository extends JpaRepository<ProgramPaguAnggaran, Long>, JpaSpecificationExecutor<ProgramPaguAnggaran> {
    Page<ProgramPaguAnggaran> findByProgramId(Long programId, Pageable pageable);
}