package com.kehutanan.pepdas.konten.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KontenDeleteFilesRequest {
    private List<String> kontenGambarIds;
    private List<String> kontenGambarUtamaIds;
}