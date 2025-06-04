package com.kehutanan.tktrh.ppkh.program.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.ppkh.program.dto.ProgramPaguAnggaranDTO;
import com.kehutanan.tktrh.ppkh.program.dto.ProgramPaguAnggaranPageDTO;
import com.kehutanan.tktrh.ppkh.program.model.ProgramPaguAnggaran;

public interface ProgramPaguAnggaranService {
    
    List<ProgramPaguAnggaran> findAll();
    
    ProgramPaguAnggaranDTO findDTOById(Long id);

    ProgramPaguAnggaran findById(Long id);
    
    ProgramPaguAnggaran save(ProgramPaguAnggaran programPaguAnggaran);
    
    ProgramPaguAnggaran update(Long id, ProgramPaguAnggaran programPaguAnggaran);
    
    void deleteById(Long id);
    
    ProgramPaguAnggaranPageDTO findAllWithCache(Pageable pageable, String baseUrl);
    
    ProgramPaguAnggaranPageDTO findByFiltersWithCache(
        Long programId, 
        String sumberAnggaran, 
        String keterangan, 
        List<String> status, 
        Pageable pageable, 
        String baseUrl
    );
    
    ProgramPaguAnggaranPageDTO searchWithCache(
        Long programId, 
        String keyWord, 
        Pageable pageable, 
        String baseUrl
    );
}