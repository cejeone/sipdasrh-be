package com.kehutanan.pepdas.serahterima.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BastDeleteFilesRequest {
    private List<String> bastPdfIds;
}