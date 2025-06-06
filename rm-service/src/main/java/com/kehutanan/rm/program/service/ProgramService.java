package com.kehutanan.rm.program.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.rm.program.dto.ProgramPageDTO;
import com.kehutanan.rm.program.model.Program;
import com.kehutanan.rm.program.model.dto.ProgramDTO;

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