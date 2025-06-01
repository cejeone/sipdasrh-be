package com.kehutanan.rh.bimtek.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BimtekDeleteFilesRequest {
    private List<String> bimtekPdfIds;
    private List<String> bimtekFotoIds;
    private List<String> bimtekVideoIds;
}