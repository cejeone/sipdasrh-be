package com.kehutanan.pepdas.kegiatan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanFileDto {
    private UUID id;
    private String fileName;
    private String contentType;
    private String url;
}