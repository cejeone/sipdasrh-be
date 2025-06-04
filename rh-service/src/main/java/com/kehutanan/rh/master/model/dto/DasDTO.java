package com.kehutanan.rh.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.rh.master.model.Das;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DasDTO {
    private Long id;
    private String namaDas;
    private BpdasDTO bpdas;
    
    public DasDTO(Das das) {
        if (das != null) {
            this.id = das.getId();
            this.namaDas = das.getNamaDas();
            this.bpdas = das.getBpdas() != null ? 
                new BpdasDTO(das.getBpdas()) : null;
        }
    }
}