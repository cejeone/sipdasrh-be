package com.kehutanan.tktrh.tmkh.kegiatan.dto;

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
    private List<String> pbakPdfShpIds;
    private List<String> fungsiKawasanLahanPenggantiIds;
    private List<String> fungsiKawasanRehabIds;
    private List<String> rehabPdfIds;
    private List<String> realisasiReboisasiIds;
    private List<String> monevIds;
    private List<String> bastRehabDasIds;
    private List<String> bastZipIds;
}
