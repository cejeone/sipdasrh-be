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
import com.kehutanan.tktrh.ppkh.kegiatan.model.KegiatanBastReboRehab;
import com.kehutanan.tktrh.ppkh.kegiatan.model.dto.KegiatanBastReboRehabDTO;

import lombok.Data;

@Data
public class KegiatanBastReboRehabPageDTO implements Serializable {
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
    public KegiatanBastReboRehabPageDTO() {
        this.embedded = new Embedded(new ArrayList<>());
        this.links = new HashMap<>();
        this.page = new PageMetadata(0, 0, 0, 0);
    }
    
    public KegiatanBastReboRehabPageDTO(Page<KegiatanBastReboRehab> page, PagedModel.PageMetadata pageMetadata, List<Link> links) {
        // Set embedded content
        List<KegiatanBastReboRehabDTO> kegiatanBastReboRehabList = page.getContent().stream()
                .map(KegiatanBastReboRehabDTO::new)
                .collect(Collectors.toList());
        this.embedded = new Embedded(kegiatanBastReboRehabList);
        
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
        
        @JsonProperty("kegiatanBastReboRehabList")
        private List<KegiatanBastReboRehabDTO> kegiatanBastReboRehabList;
        
        public Embedded() {
            this.kegiatanBastReboRehabList = new ArrayList<>();
        }
        
        public Embedded(List<KegiatanBastReboRehabDTO> kegiatanBastReboRehabList) {
            this.kegiatanBastReboRehabList = kegiatanBastReboRehabList;
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
