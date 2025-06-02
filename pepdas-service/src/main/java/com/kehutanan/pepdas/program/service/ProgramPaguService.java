package com.kehutanan.pepdas.program.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.pepdas.program.dto.ProgramPaguDTO;
import com.kehutanan.pepdas.program.dto.ProgramPaguPageDTO;
import com.kehutanan.pepdas.program.model.ProgramPagu;

public interface ProgramPaguService {
    
    List<ProgramPagu> findAll();
    
    ProgramPaguDTO findDTOById(Long id);

    ProgramPagu findById(Long id);
    
    ProgramPagu save(ProgramPagu programPagu);
    
    ProgramPagu update(Long id, ProgramPagu programPagu);
    
    void deleteById(Long id);

    ProgramPaguPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    ProgramPaguPageDTO findByFiltersWithCache(Long programId, String kategori, String sumberAnggaran, 
            List<String> bpdas, Pageable pageable, String baseUrl);

    ProgramPaguPageDTO searchWithCache(Long programId, String keyWord, Pageable pageable, String baseUrl);
}