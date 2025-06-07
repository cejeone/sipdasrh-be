package com.kehutanan.ppth.penghijauan.program.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.ppth.penghijauan.program.dto.ProgramSkemaPageDTO;
import com.kehutanan.ppth.penghijauan.program.model.ProgramSkema;
import com.kehutanan.ppth.penghijauan.program.model.dto.ProgramSkemaDTO;

public interface ProgramSkemaService {
    
    List<ProgramSkema> findAll();
    
    ProgramSkemaDTO findDTOById(Long id);

    ProgramSkema findById(Long id);
    
    ProgramSkema save(ProgramSkema programSkema);
    
    ProgramSkema update(Long id, ProgramSkema programSkema);
    
    void deleteById(Long id);
    
    ProgramSkemaPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    ProgramSkemaPageDTO findByFiltersWithCache(Long programId, String nama, List<String> kategori,
            Pageable pageable, String baseUrl);

    ProgramSkemaPageDTO searchWithCache(Long programId, String keyWord, Pageable pageable, String baseUrl);
}