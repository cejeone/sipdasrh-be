package com.kehutanan.tktrh.ppkh.program.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;

import com.kehutanan.tktrh.ppkh.program.model.ProgramPaguAnggaran;
import com.kehutanan.tktrh.ppkh.program.model.dto.ProgramPaguAnggaranDTO;

import lombok.Data;

@Data
public class ProgramPaguAnggaranPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<ProgramPaguAnggaranDTO> content;
    private PageMetadata metadata;
    private Links links;
    
    /**
     * Default constructor required for JSON deserialization
     */
    public ProgramPaguAnggaranPageDTO() {
        this.content = new ArrayList<>();
        this.metadata = new PageMetadata(0, 0, 0, 0);
        this.links = new Links();
    }
    
    public ProgramPaguAnggaranPageDTO(Page<ProgramPaguAnggaran> page, PagedModel.PageMetadata pageMetadata, List<Link> links) {
        this.content = page.getContent().stream()
                .map(ProgramPaguAnggaranDTO::new)
                .collect(Collectors.toList());
        
        this.metadata = new PageMetadata(
                pageMetadata.getSize(),
                pageMetadata.getNumber(),
                pageMetadata.getTotalElements(),
                pageMetadata.getTotalPages()
        );
        
        this.links = new Links();
        for (Link link : links) {
            switch(link.getRel().value()) {
                case "self":
                    this.links.setSelf(link.getHref());
                    break;
                case "first":
                    this.links.setFirst(link.getHref());
                    break;
                case "prev":
                    this.links.setPrev(link.getHref());
                    break;
                case "next":
                    this.links.setNext(link.getHref());
                    break;
                case "last":
                    this.links.setLast(link.getHref());
                    break;
            }
        }
    }
    
    @Data
    public static class PageMetadata implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private long size;
        private long number;
        private long totalElements;
        private long totalPages;
        
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
    
    @Data
    public static class Links implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String self;
        private String first;
        private String prev;
        private String next;
        private String last;
        
        // Default constructor for deserialization
        public Links() {
            this.self = "";
            this.first = "";
            this.prev = "";
            this.next = "";
            this.last = "";
        }
    }
}