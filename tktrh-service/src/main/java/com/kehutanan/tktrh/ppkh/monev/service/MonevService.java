package com.kehutanan.tktrh.ppkh.monev.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.tktrh.ppkh.monev.dto.MonevPageDTO;
import com.kehutanan.tktrh.ppkh.monev.model.Monev;
import com.kehutanan.tktrh.ppkh.monev.model.dto.MonevDTO;

public interface MonevService {
    
    List<Monev> findAll();
    
    MonevDTO findDTOById(Long id);

    Monev findById(Long id);
    
    Monev save(Monev monev);
    
    Monev update(Long id, Monev monev);
    
    void deleteById(Long id);
    
    Monev uploadMonevPdf(Long id, List<MultipartFile> pdf);

    Monev deleteMonevPdf(Long id, List<String> uuidPdf);

    MonevPageDTO findAllWithCache(Pageable pageable, String baseUrl);

    MonevPageDTO findByFiltersWithCache(String namaProgram, String audiensi, List<String> bpdasList,
            Pageable pageable, String baseUrl);

    MonevPageDTO searchWithCache(String keyWord, Pageable pageable, String baseUrl);
}
