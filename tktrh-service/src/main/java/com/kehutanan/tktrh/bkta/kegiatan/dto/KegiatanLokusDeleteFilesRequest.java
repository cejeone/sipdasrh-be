package com.kehutanan.tktrh.bkta.kegiatan.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanLokusDeleteFilesRequest {
    private List<String> proposalPdfIds;
    private List<String> lokasiPdfIds;
    private List<String> bangunanPdfIds;
}