package com.kehutanan.ppth.master.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.kehutanan.ppth.master.model.Pengguna;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PenggunaDTO {
    private Long id;
    private String username;
    private String namaLengkap;
    private String noHp;
    private String email;
    private PeranDTO peran;
    private LovDTO status;
    private List<PenggunaFotoDTO> penggunaFotoList;
    
    public PenggunaDTO(Pengguna pengguna) {
        if (pengguna != null) {
            this.id = pengguna.getId();
            this.username = pengguna.getUsername();
            this.namaLengkap = pengguna.getNamaLengkap();
            this.noHp = pengguna.getNoHp();
            this.email = pengguna.getEmail();
            this.peran = pengguna.getPeran() != null ? 
                new PeranDTO(pengguna.getPeran()) : null;
            this.status = pengguna.getStatus() != null ? 
                new LovDTO(pengguna.getStatus()) : null;
            
            if (pengguna.getPenggunaFotoList() != null) {
                this.penggunaFotoList = pengguna.getPenggunaFotoList().stream()
                    .map(PenggunaFotoDTO::new)
                    .collect(Collectors.toList());
            }
        }
    }
}