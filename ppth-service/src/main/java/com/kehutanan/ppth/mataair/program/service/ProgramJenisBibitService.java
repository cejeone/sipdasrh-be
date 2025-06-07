package com.kehutanan.ppth.mataair.program.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.ppth.mataair.program.dto.ProgramJenisBibitPageDTO;
import com.kehutanan.ppth.mataair.program.model.ProgramJenisBibit;
import com.kehutanan.ppth.mataair.program.model.dto.ProgramJenisBibitDTO;

public interface ProgramJenisBibitService {

    List<ProgramJenisBibit> findAll();

    ProgramJenisBibitDTO findDTOById(Long id);

    ProgramJenisBibit findById(Long id);

    ProgramJenisBibit save(ProgramJenisBibit programJenisBibit);

    ProgramJenisBibit update(Long id, ProgramJenisBibit programJenisBibit);

    void deleteById(Long id);

    ProgramJenisBibitPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    ProgramJenisBibitPageDTO findByFiltersWithCache(Long programId, String namaBibit, List<String> kategori,
            Pageable pageable, String baseUrl);

    ProgramJenisBibitPageDTO searchWithCache(Long programId, String keyWord, Pageable pageable, String baseUrl);
}