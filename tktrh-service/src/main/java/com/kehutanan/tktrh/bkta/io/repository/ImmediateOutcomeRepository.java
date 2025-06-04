package com.kehutanan.tktrh.bkta.io.repository;

import com.kehutanan.tktrh.bkta.io.model.ImmediateOutcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ImmediateOutcomeRepository extends JpaRepository<ImmediateOutcome, Long>, JpaSpecificationExecutor<ImmediateOutcome> {

}