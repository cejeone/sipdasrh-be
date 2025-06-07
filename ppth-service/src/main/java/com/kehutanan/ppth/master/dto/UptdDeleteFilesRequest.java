package com.kehutanan.ppth.master.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UptdDeleteFilesRequest {
    private List<String> rantekPdfIds;
    private List<String> dokumentasiFotoIds;
    private List<String> dokumentasiVideoIds;
    private List<String> lokasiMapShpIds;
}