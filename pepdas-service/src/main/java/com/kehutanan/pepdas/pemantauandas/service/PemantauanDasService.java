package com.kehutanan.pepdas.pemantauandas.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.kehutanan.pepdas.pemantauandas.dto.PemantauanDasDTO;
import com.kehutanan.pepdas.pemantauandas.dto.PemantauanDasPageDTO;
import com.kehutanan.pepdas.pemantauandas.model.PemantauanDas;

public interface PemantauanDasService {
    
    List<PemantauanDas> findAll();
    
    PemantauanDasDTO findDTOById(Long id);

    PemantauanDas findById(Long id);
    
    PemantauanDas save(PemantauanDas pemantauanDas);
    
    PemantauanDas update(Long id, PemantauanDas pemantauanDas);
    
    void deleteById(Long id);

    PemantauanDasPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    PemantauanDasPageDTO findByFiltersWithCache(String das, String spasId, List<String> bpdas,
            Pageable pageable, String baseUrl);

    PemantauanDasPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}