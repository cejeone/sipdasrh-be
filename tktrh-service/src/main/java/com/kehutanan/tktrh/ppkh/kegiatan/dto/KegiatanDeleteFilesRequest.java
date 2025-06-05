package com.kehutanan.tktrh.ppkh.kegiatan.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanDeleteFilesRequest {
    private List<String> perijinanPdfIds;
    private List<String> riwayatSkIds;
    private List<String> pakPdfShpIds;
    private List<String> fungsiKawasanIds;
    private List<String> rantekPdfIds;
    private List<String> rencanaRealisasiIds;
    private List<String> bastReboRehabIds;
    private List<String> bastZipIds;
}
