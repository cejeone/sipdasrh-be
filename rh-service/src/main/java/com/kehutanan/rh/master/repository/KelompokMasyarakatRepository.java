package com.kehutanan.rh.master.repository;

import com.kehutanan.rh.master.model.KelompokMasyarakat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KelompokMasyarakatRepository extends JpaRepository<KelompokMasyarakat, Long>, JpaSpecificationExecutor<KelompokMasyarakat> {
    // Repository methods
}