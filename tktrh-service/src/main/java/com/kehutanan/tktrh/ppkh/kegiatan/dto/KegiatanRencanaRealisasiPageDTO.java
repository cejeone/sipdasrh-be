package com.kehutanan.tktrh.ppkh.kegiatan.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanRencanaRealisasi;
import com.kehutanan.tktrh.ppkh.kegiatan.model.dto.KegiatanRencanaRealisasiDTO;

import lombok.Data;

@Data
public class KegiatanRencanaRealisasiPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @JsonProperty("_embedded")
    private Embedded embedded;
    
    @JsonProperty("_links")
    private Map<String, LinkItem> links;
    
    @JsonProperty("page")
    private PageMetadata page;
    
    /**
     * Default constructor required for JSON deserialization
     */
    public KegiatanRencanaRealisasiPageDTO() {
        this.embedded = new Embedded(new ArrayList<>());
        this.links = new HashMap<>();
        this.page = new PageMetadata(0, 0, 0, 0);
    }
    
    public KegiatanRencanaRealisasiPageDTO(Page<KegiatanRencanaRealisasi> page, PagedModel.PageMetadata pageMetadata, List<Link> links) {
        // Set embedded content
        List<KegiatanRencanaRealisasiDTO> kegiatanRencanaRealisasiList = page.getContent().stream()
                .map(KegiatanRencanaRealisasiDTO::new)
                .collect(Collectors.toList());
        this.embedded = new Embedded(kegiatanRencanaRealisasiList);
        
        // Set page metadata
        this.page = new PageMetadata(
                pageMetadata.getSize(),
                pageMetadata.getNumber(),
                pageMetadata.getTotalElements(),
                pageMetadata.getTotalPages()
        );
        
        // Set links
        this.links = new HashMap<>();
        for (Link link : links) {
            this.links.put(link.getRel().value(), new LinkItem(link.getHref()));
        }
    }
    
    @Data
    public static class Embedded implements Serializable {
        private static final long serialVersionUID = 1L;
        
        @JsonProperty("kegiatanRencanaRealisasiList")
        private List<KegiatanRencanaRealisasiDTO> kegiatanRencanaRealisasiList;
        
        public Embedded() {
            this.kegiatanRencanaRealisasiList = new ArrayList<>();
        }
        
        public Embedded(List<KegiatanRencanaRealisasiDTO> kegiatanRencanaRealisasiList) {
            this.kegiatanRencanaRealisasiList = kegiatanRencanaRealisasiList;
        }
    }
    
    @Data
    public static class LinkItem implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String href;
        
        public LinkItem() {
            this.href = "";
        }
        
        public LinkItem(String href) {
            this.href = href;
        }
    }
    
    @Data
    public static class PageMetadata implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private long size;
        private long totalElements;
        private long totalPages;
        private long number;
        
        // Default constructor for deserialization
        public PageMetadata() {
            this(0, 0, 0, 0);
        }
        
        public PageMetadata(long size, long number, long totalElements, long totalPages) {
            this.size = size;
            this.number = number;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
        }
    }
}