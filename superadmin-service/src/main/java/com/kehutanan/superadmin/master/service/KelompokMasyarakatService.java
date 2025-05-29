package com.kehutanan.superadmin.master.service;

import com.kehutanan.superadmin.master.model.KelompokMasyarakat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.time.LocalDate;

public interface KelompokMasyarakatService {
    
    Page<KelompokMasyarakat> findAll(Pageable pageable);
    
    List<KelompokMasyarakat> findAll();
    
    KelompokMasyarakat findById(Long id);
    
    KelompokMasyarakat save(KelompokMasyarakat kelompokMasyarakat);
    
    KelompokMasyarakat update(Long id, KelompokMasyarakat kelompokMasyarakat);
    
    void deleteById(Long id);
    
    Page<KelompokMasyarakat> findByFilters(String namaKelompokMasyarakat, String nomorSkPenetapan, LocalDate tanggalSkPenetapan, 
            Long provinsiId, Long kabupatenKotaId, Long kecamatanId, Long kelurahanDesaId, Pageable pageable);
}