package com.kehutanan.pepdas.monev.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonevDeleteFilesRequest {
    private List<String> pdfIds;
}