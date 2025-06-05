package com.kehutanan.tktrh.ppkh.monev.repository;

import com.kehutanan.tktrh.ppkh.monev.model.Monev;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MonevRepository extends JpaRepository<Monev, Long>, JpaSpecificationExecutor<Monev> {

}
