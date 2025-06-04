package com.kehutanan.rm.konten.repository;

import com.kehutanan.rm.konten.model.Konten;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KontenRepository extends JpaRepository<Konten, Long>, JpaSpecificationExecutor<Konten> {

}