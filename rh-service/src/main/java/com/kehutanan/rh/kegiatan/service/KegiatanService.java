package com.kehutanan.rh.kegiatan.service;

import com.kehutanan.rh.bimtek.model.Bimtek;
import com.kehutanan.rh.bimtek.model.BimtekFoto;
import com.kehutanan.rh.kegiatan.model.Kegiatan;
import com.kehutanan.rh.kegiatan.model.KegiatanRancanganTeknisFoto;
import com.kehutanan.rh.kegiatan.repository.KegiatanRepository;
import com.kehutanan.rh.kegiatan.repository.KegiatanRancanganTeknisFotoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KegiatanService {

    private final KegiatanRepository kegiatanRepository;
    private final KegiatanRancanganTeknisFotoRepository kegiatanRancanganTeknisFotoRepository;

    public Page<Kegiatan> findAll(Pageable pageable) {
        return kegiatanRepository.findAll(pageable);
    }

    public Kegiatan findById(UUID id) {
        return kegiatanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan tidak ditemukan dengan id: " + id));
    }

    @Transactional
    public Kegiatan create(Kegiatan kegiatan) {
        return kegiatanRepository.save(kegiatan);
    }

    @Transactional
    public Kegiatan update(UUID id, Kegiatan kegiatan) {
        Kegiatan existing = findById(id);

        existing.setSubDirektorat(kegiatan.getSubDirektorat());
        existing.setProgram(kegiatan.getProgram());
        existing.setJenisKegiatan(kegiatan.getJenisKegiatan());
        existing.setRefPo(kegiatan.getRefPo());
        existing.setNamaKegiatan(kegiatan.getNamaKegiatan());
        existing.setDetailPola(kegiatan.getDetailPola());
        existing.setDetailTahunKegiatan(kegiatan.getDetailTahunKegiatan());
        existing.setDetailSumberAnggaran(kegiatan.getDetailSumberAnggaran());
        existing.setDetailTotalBibit(kegiatan.getDetailTotalBibit());
        existing.setDetailTotalLuasHa(kegiatan.getDetailTotalLuasHa());
        existing.setDetailPemangkuKawasan(kegiatan.getDetailPemangkuKawasan());
        existing.setDetailPelaksanaan(kegiatan.getDetailPelaksanaan());
        existing.setKontrakNomor(kegiatan.getKontrakNomor());
        existing.setKontrakNilai(kegiatan.getKontrakNilai());
        existing.setKontrakTipe(kegiatan.getKontrakTipe());
        existing.setKontrakPelaksanaan(kegiatan.getKontrakPelaksanaan());
        existing.setKontrakTanggalKontrak(kegiatan.getKontrakTanggalKontrak());
        existing.setKontrakStatus(kegiatan.getKontrakStatus());
        existing.setDokumentasiCatatanFoto(kegiatan.getDokumentasiCatatanFoto());
        existing.setDokumentasiCatatanVideo(kegiatan.getDokumentasiCatatanVideo());

        return kegiatanRepository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        Kegiatan kegiatan = kegiatanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kegiatan tidak ditemukan dengan id: " + id));

        try {
            kegiatanRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Gagal menghapus Kegiatan dan data terkait: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal menghapus Kegiatan dan data terkait: " + e.getMessage(), e);
        }
    }

    public List<KegiatanRancanganTeknisFoto> uploadKegiatanRancanganTeknisFotos(UUID id, List<MultipartFile> files) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'uploadKegiatanRancanganTeknisFotos'");
    }

    // public List<KegiatanRancanganTeknisFoto> uploadKegiatanRancanganTeknisFotos(UUID id,
    //         List<MultipartFile> files) throws Exception {
    //     Kegiatan kegiatan = kegiatanRepository.findById(id)
    //             .orElseThrow(() -> new EntityNotFoundException(
    //                     "uploadKegiatanRancanganTeknisFotos tidak ditemukan dengan id: " + id));

    //     return fileUploadUtil.uploadFiles(
    //             kegiatan,
    //             files,
    //             "Kegiatan", // folderPrefix di Minio, bisa disesuaikan sesuai struktur kamu
    //             (parent, fileName) -> {
    //                 KegiatanRancanganTeknisFoto foto = new KegiatanRancanganTeknisFoto();
    //                 foto.setKegiatan(parent);
    //                 foto.setNamaFile(fileName);
    //                 return foto;
    //             },
    //             (entity, file) -> {
    //                 entity.setNamaAsli(file.getOriginalFilename());
    //                 entity.setUkuranMb((double) file.getSize() / (1024 * 1024));
    //                 entity.setContentType(file.getContentType());
    //                 entity.setUploadedAt(LocalDateTime.now());
    //             },
    //             kegiatanRancanganTeknisFotoRepository::save);
    // }
}
