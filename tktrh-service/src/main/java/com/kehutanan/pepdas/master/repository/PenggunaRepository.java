package com.kehutanan.pepdas.master.repository;

import com.kehutanan.pepdas.master.model.Pengguna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PenggunaRepository extends JpaRepository<Pengguna, Long>, JpaSpecificationExecutor<Pengguna> {

}