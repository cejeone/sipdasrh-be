package com.kehutanan.rh.program.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.rh.program.dto.ProgramDTO;
import com.kehutanan.rh.program.dto.ProgramPageDTO;
import com.kehutanan.rh.program.model.Program;

public interface ProgramService {
    
    List<Program> findAll();
    
    ProgramDTO findDTOById(Long id);

    Program findById(Long id);
    
    Program save(Program program);
    
    Program update(Long id, Program program);
    
    void deleteById(Long id);

    ProgramPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    ProgramPageDTO findByFiltersWithCache(String nama, List<String> eselon,
            Pageable pageable, String baseUrl);

    ProgramPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}