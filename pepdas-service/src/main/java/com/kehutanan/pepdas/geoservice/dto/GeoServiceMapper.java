package com.kehutanan.pepdas.geoservice.dto;

import com.kehutanan.pepdas.geoservice.model.GeoService;

import java.util.List;
import java.util.stream.Collectors;

public class GeoServiceMapper {

    public static GeoServiceResponseDto toResponseDto(GeoService geoService) {
        return GeoServiceResponseDto.builder()
                .id(geoService.getId())
                .direktorat(geoService.getDirektorat())
                .bpdas(geoService.getBpdas())
                .geoserviceId(geoService.getGeoserviceId())
                .url(geoService.getUrl())
                .tipe(geoService.getTipe())
                .service(geoService.getService())
                .status(geoService.getStatus())
                .build();
    }
    
    public static GeoService toEntity(GeoServiceRequestDto requestDto) {
        GeoService geoService = new GeoService();
        geoService.setDirektorat(requestDto.getDirektorat());
        geoService.setBpdas(requestDto.getBpdas());
        geoService.setGeoserviceId(requestDto.getGeoserviceId());
        geoService.setUrl(requestDto.getUrl());
        geoService.setTipe(requestDto.getTipe());
        geoService.setService(requestDto.getService());
        geoService.setStatus(requestDto.getStatus());
        return geoService;
    }
    
    public static void updateEntityFromDto(GeoServiceRequestDto requestDto, GeoService geoService) {
        geoService.setDirektorat(requestDto.getDirektorat());
        geoService.setBpdas(requestDto.getBpdas());
        geoService.setGeoserviceId(requestDto.getGeoserviceId());
        geoService.setUrl(requestDto.getUrl());
        geoService.setTipe(requestDto.getTipe());
        geoService.setService(requestDto.getService());
        geoService.setStatus(requestDto.getStatus());
    }
    
    public static List<GeoServiceResponseDto> toResponseDtoList(List<GeoService> geoServices) {
        return geoServices.stream()
                .map(GeoServiceMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}