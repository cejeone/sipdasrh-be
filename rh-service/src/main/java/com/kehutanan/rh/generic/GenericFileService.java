package com.kehutanan.rh.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.web.multipart.MultipartFile;

import com.kehutanan.rh.dokumen.service.MinioService;

public class GenericFileService {
    private final MinioService minioGenericService;

    public GenericFileService(MinioService minioService) {
        this.minioGenericService = minioService;

    }

    // Method utama, reusable untuk berbagai entity
    public <E, F> List<F> uploadPictures(
            E entity,
            List<MultipartFile> files,
            TriFunction<E,  MultipartFile, String, F> pictureCreator,
            Function<F, F> savePictureFunc) throws Exception {
        List<F> uploadedPictures = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioGenericService.uploadFile(fileName, file.getInputStream(), file.getContentType());

            // Tambahkan set property lainnya di fotoCreator lambda

        }

        return uploadedPictures;
    }

    @FunctionalInterface
    public interface TriFunction<A, B, C, R> {
        R apply(A a, B b, C c);
    }

}
