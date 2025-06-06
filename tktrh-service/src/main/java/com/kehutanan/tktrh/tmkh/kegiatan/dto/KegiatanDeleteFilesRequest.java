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
    private List<String> rehabPdfIds;
    private List<String> bastZipIds;
}
