package com.kehutanan.ppth.mataair.kegiatan.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanMonevDeleteFilesRequest {
    private List<String> pdfIds;
}