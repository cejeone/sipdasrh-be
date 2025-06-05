package com.kehutanan.tktrh.bkta.program.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.bkta.program.dto.ProgramPageDTO;
import com.kehutanan.tktrh.bkta.program.model.Program;
import com.kehutanan.tktrh.bkta.program.model.dto.ProgramDTO;

public interface ProgramService {
    
    List<Program> findAll();
    
    ProgramDTO findDTOById(Long id);

    Program findById(Long id);
    
    Program save(Program program);
    
    Program update(Long id, Program program);
    
    void deleteById(Long id);
    
    ProgramPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    ProgramPageDTO findByFiltersWithCache(String namaProgram, String tahun, List<String> status,
            Pageable pageable, String baseUrl);

    ProgramPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}