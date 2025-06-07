package com.kehutanan.ppth.penghijauan.kegiatan.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanDeleteFilesRequest {
    private List<String> rancanganTeknisPdfIds;
    private List<String> rancanganTeknisFotoIds;
    private List<String> rancanganTeknisVideoIds;
    private List<String> kontrakPdfIds;
    private List<String> dokumentasiFotoIds;
    private List<String> dokumentasiVideoIds;
    private List<Long> lokusShpIds;
}