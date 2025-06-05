package com.kehutanan.tktrh.ppkh.spas.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.tktrh.ppkh.spas.dto.SpasDTO;
import com.kehutanan.tktrh.ppkh.spas.dto.SpasPageDTO;
import com.kehutanan.tktrh.ppkh.spas.model.Spas;

public interface SpasService {
    
    List<Spas> findAll();
    
    SpasDTO findDTOById(Long id);

    Spas findById(Long id);
    
    Spas save(Spas spas);
    
    Spas update(Long id, Spas spas);
    
    void deleteById(Long id);
    
    SpasPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    SpasPageDTO findByFiltersWithCache(String spasId, String namaDas, List<String> bpdasList,
            Pageable pageable, String baseUrl);

    SpasPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}
