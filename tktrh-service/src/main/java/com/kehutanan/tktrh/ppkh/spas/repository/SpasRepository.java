package com.kehutanan.tktrh.ppkh.spas.repository;

import com.kehutanan.tktrh.ppkh.spas.model.Spas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository("ppkhSpasRepository")
public interface SpasRepository extends JpaRepository<Spas, Long>, JpaSpecificationExecutor<Spas> {

}
