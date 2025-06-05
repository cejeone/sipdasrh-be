package com.kehutanan.rm.program.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.rm.program.dto.ProgramPaguAnggaranPageDTO;
import com.kehutanan.rm.program.model.ProgramPaguAnggaran;
import com.kehutanan.rm.program.model.dto.ProgramPaguAnggaranDTO;

public interface ProgramPaguAnggaranService {
    
    List<ProgramPaguAnggaran> findAll();
    
    ProgramPaguAnggaranDTO findDTOById(Long id);

    ProgramPaguAnggaran findById(Long id);
    
    ProgramPaguAnggaran save(ProgramPaguAnggaran programPaguAnggaran);
    
    ProgramPaguAnggaran update(Long id, ProgramPaguAnggaran programPaguAnggaran);
    
    void deleteById(Long id);

    ProgramPaguAnggaranPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    ProgramPaguAnggaranPageDTO findByFiltersWithCache(Long programId, String sumberAnggaran, 
            List<String> kategori, Pageable pageable, String baseUrl);

    ProgramPaguAnggaranPageDTO searchWithCache(Long programId, String keyWord, Pageable pageable, String baseUrl);
}