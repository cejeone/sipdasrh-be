package com.kehutanan.ppth.penghijauan.program.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.ppth.penghijauan.program.dto.ProgramPaguAnggaranPageDTO;
import com.kehutanan.ppth.penghijauan.program.model.ProgramPaguAnggaran;
import com.kehutanan.ppth.penghijauan.program.model.dto.ProgramPaguAnggaranDTO;

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