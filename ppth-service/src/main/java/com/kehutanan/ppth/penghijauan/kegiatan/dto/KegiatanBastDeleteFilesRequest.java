package com.kehutanan.ppth.penghijauan.kegiatan.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanBastDeleteFilesRequest {
    private List<String> bastPdfIds;
}