package com.kehutanan.superadmin.master.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PenggunaDeleteFilesRequest {
    private List<String> penggunaFotoIds;
}