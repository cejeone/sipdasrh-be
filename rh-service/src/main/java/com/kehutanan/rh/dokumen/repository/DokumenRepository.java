package com.kehutanan.rh.dokumen.repository;

import com.kehutanan.rh.dokumen.model.Dokumen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DokumenRepository extends JpaRepository<Dokumen, Long>, JpaSpecificationExecutor<Dokumen> {

}