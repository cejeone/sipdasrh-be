package com.kehutanan.tktrh.ppkh.kegiatan.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KegiatanRiwayatSkDeleteFilesRequest {
    private List<String> riwayatSkShpIds;
}