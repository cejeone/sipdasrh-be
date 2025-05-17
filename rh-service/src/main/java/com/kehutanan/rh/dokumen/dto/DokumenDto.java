
package com.kehutanan.rh.dokumen.dto;

import java.util.List;

import lombok.Data;

@Data
public class DokumenDto {
    private String tipe;
    private String namaDokumen;
    private String status; 
    private String keterangan;
    private List<Long> deleteFileIds;
}