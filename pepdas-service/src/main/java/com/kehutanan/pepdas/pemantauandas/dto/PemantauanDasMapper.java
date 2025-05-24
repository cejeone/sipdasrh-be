package com.kehutanan.pepdas.pemantauandas.dto;

import com.kehutanan.pepdas.pemantauandas.model.PemantauanDas;

import java.util.List;
import java.util.stream.Collectors;

public class PemantauanDasMapper {

    public static PemantauanDasResponseDto toResponseDto(PemantauanDas pemantauanDas) {
        return PemantauanDasResponseDto.builder()
                .id(pemantauanDas.getId())
                .bpdas(pemantauanDas.getBpdas())
                .das(pemantauanDas.getDas())
                .spasId(pemantauanDas.getSpasId())
                .tanggalWaktu(pemantauanDas.getTanggalWaktu())
                .nilaiTma(pemantauanDas.getNilaiTma())
                .nilaiCurahHujan(pemantauanDas.getNilaiCurahHujan())
                .teganganBaterai(pemantauanDas.getTeganganBaterai())
                .build();
    }
    
    public static PemantauanDas toEntity(PemantauanDasRequestDto requestDto) {
        PemantauanDas pemantauanDas = new PemantauanDas();
        pemantauanDas.setBpdas(requestDto.getBpdas());
        pemantauanDas.setDas(requestDto.getDas());
        pemantauanDas.setSpasId(requestDto.getSpasId());
        pemantauanDas.setTanggalWaktu(requestDto.getTanggalWaktu());
        pemantauanDas.setNilaiTma(requestDto.getNilaiTma());
        pemantauanDas.setNilaiCurahHujan(requestDto.getNilaiCurahHujan());
        pemantauanDas.setTeganganBaterai(requestDto.getTeganganBaterai());
        return pemantauanDas;
    }
    
    public static void updateEntityFromDto(PemantauanDasRequestDto requestDto, PemantauanDas pemantauanDas) {
        pemantauanDas.setBpdas(requestDto.getBpdas());
        pemantauanDas.setDas(requestDto.getDas());
        pemantauanDas.setSpasId(requestDto.getSpasId());
        pemantauanDas.setTanggalWaktu(requestDto.getTanggalWaktu());
        pemantauanDas.setNilaiTma(requestDto.getNilaiTma());
        pemantauanDas.setNilaiCurahHujan(requestDto.getNilaiCurahHujan());
        pemantauanDas.setTeganganBaterai(requestDto.getTeganganBaterai());
    }
    
    public static List<PemantauanDasResponseDto> toResponseDtoList(List<PemantauanDas> pemantauanDasList) {
        return pemantauanDasList.stream()
                .map(PemantauanDasMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}