package com.kehutanan.ppth.perijinan.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerijinanDeleteFilesRequest {
    private List<String> dokumenAwalPdfIds;
    private List<String> dokumenBastPdfIds;
}