package com.kehutanan.tktrh.bkta.program.model.dto;

import com.kehutanan.tktrh.bkta.program.model.Program;
import com.kehutanan.tktrh.master.model.dto.Eselon2DTO;
import com.kehutanan.tktrh.master.model.dto.LovDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDTO implements Serializable {

    private Long id;
    private Eselon2DTO eselon2;
    private LovDTO status;
    private String nama;
    private Integer tahunPelaksana;
    private Double totalAnggaran;
    private Integer targetGp;
    private Integer targetDpn;
    private List<ProgramPaguAnggaranDTO> paguAnggarans = new ArrayList<>();

    public ProgramDTO(Program entity) {
        if (entity != null) {
            this.id = entity.getId();
            if (entity.getEselon2() != null) {
                this.eselon2 = new Eselon2DTO(entity.getEselon2());
            }
            if (entity.getStatus() != null) {
                this.status = new LovDTO(entity.getStatus());
            }
            this.nama = entity.getNama();
            this.tahunPelaksana = entity.getTahunPelaksana();
            this.totalAnggaran = entity.getTotalAnggaran();
            this.targetGp = entity.getTargetGp();
            this.targetDpn = entity.getTargetDpn();
            if (entity.getPaguAnggarans() != null) {
                this.paguAnggarans = entity.getPaguAnggarans().stream()
                        .map(ProgramPaguAnggaranDTO::new)
                        .collect(Collectors.toList());
            }
        }
    }
}
