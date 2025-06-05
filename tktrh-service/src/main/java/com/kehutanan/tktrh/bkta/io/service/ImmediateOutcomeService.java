package com.kehutanan.tktrh.bkta.io.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.bkta.io.dto.ImmediateOutcomePageDTO;
import com.kehutanan.tktrh.bkta.io.model.ImmediateOutcome;
import com.kehutanan.tktrh.bkta.io.model.dto.ImmediateOutcomeDTO;

public interface ImmediateOutcomeService {
    
    List<ImmediateOutcome> findAll();
    
    ImmediateOutcomeDTO findDTOById(Long id);

    ImmediateOutcome findById(Long id);
    
    ImmediateOutcome save(ImmediateOutcome immediateOutcome);
    
    ImmediateOutcome update(Long id, ImmediateOutcome immediateOutcome);
    
    void deleteById(Long id);
    
    ImmediateOutcomePageDTO findAllWithCache(Pageable pageable, String baseUrl);

    ImmediateOutcomePageDTO findByFiltersWithCache(String kegiatan, Integer tahun, List<String> bpdas, 
                                                 Pageable pageable, String baseUrl);

    ImmediateOutcomePageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}