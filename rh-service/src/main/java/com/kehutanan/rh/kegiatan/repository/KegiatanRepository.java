package com.kehutanan.rh.kegiatan.repository;

import com.kehutanan.rh.kegiatan.model.Kegiatan;
import com.kehutanan.rh.kegiatan.model.KegiatanDokumentasiFoto;
import com.kehutanan.rh.kegiatan.model.KegiatanDokumentasiVideo;
import com.kehutanan.rh.kegiatan.model.KegiatanKontrakPdf;
import com.kehutanan.rh.kegiatan.model.KegiatanRancanganTeknisFoto;
import com.kehutanan.rh.kegiatan.model.KegiatanRancanganTeknisPdf;
import com.kehutanan.rh.kegiatan.model.KegiatanRancanganTeknisVideo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KegiatanRepository extends JpaRepository<Kegiatan, UUID> {
    @Query("SELECT k FROM Kegiatan k WHERE k.id = :id")
    Optional<Kegiatan> findByIdAllChild(@Param("id") UUID id);
    
    @Query("SELECT k.kegiatanRancanganTeknisFotos FROM Kegiatan k WHERE k.id = :id")
    List<KegiatanRancanganTeknisFoto> findRancanganTeknisFotosById(@Param("id") UUID id);
    
    @Query("SELECT k.kegiatanRancanganTeknisPdfs FROM Kegiatan k WHERE k.id = :id")
    List<KegiatanRancanganTeknisPdf> findRancanganTeknisPdfsById(@Param("id") UUID id);

     
    @Query("SELECT k.kegiatanRancanganTeknisVideos FROM Kegiatan k WHERE k.id = :id")
    List<KegiatanRancanganTeknisVideo> findRancanganTeknisVideosById(@Param("id") UUID id);
    
    @Query("SELECT k.kegiatanKontrakPdfs FROM Kegiatan k WHERE k.id = :id")
    List<KegiatanKontrakPdf> findKegiatanKontrakPdffsById(@Param("id") UUID id);

     
    @Query("SELECT k.kegiatanDokumentasiFotos FROM Kegiatan k WHERE k.id = :id")
    List<KegiatanDokumentasiFoto> findKegiatanDokumentasiFotoById(@Param("id") UUID id);
    
    @Query("SELECT k.kegiatanDokumentasiVideos FROM Kegiatan k WHERE k.id = :id")
    List<KegiatanDokumentasiVideo> findKegiatanDokumentasiVideoPdfsById(@Param("id") UUID id);
}