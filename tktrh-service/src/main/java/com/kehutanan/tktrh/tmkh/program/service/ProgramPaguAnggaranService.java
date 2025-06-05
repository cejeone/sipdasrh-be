package com.kehutanan.tktrh.tmkh.program.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.tmkh.program.dto.ProgramPaguAnggaranPageDTO;
import com.kehutanan.tktrh.tmkh.program.model.ProgramPaguAnggaran;
import com.kehutanan.tktrh.tmkh.program.model.dto.ProgramPaguAnggaranDTO;

public interface ProgramPaguAnggaranService {
    
    List<ProgramPaguAnggaran> findAll();
    
    ProgramPaguAnggaranDTO findDTOById(Long id);

    ProgramPaguAnggaran findById(Long id);
    
    ProgramPaguAnggaran save(ProgramPaguAnggaran paguAnggaran);
    
    ProgramPaguAnggaran update(Long id, ProgramPaguAnggaran paguAnggaran);
    
    void deleteById(Long id);
    
    ProgramPaguAnggaranPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    ProgramPaguAnggaranPageDTO findByFiltersWithCache(String programId, String sumberAnggaran, String keterangan, 
            List<String> status, Pageable pageable, String baseUrl);

    ProgramPaguAnggaranPageDTO searchWithCache(String programId, String keyWord, Pageable pageable, String baseUrl);
}