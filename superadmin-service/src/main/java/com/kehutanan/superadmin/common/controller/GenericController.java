package com.kehutanan.superadmin.common.controller;

import java.util.UUID;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Generic interface for RESTful CRUD controllers
 * 
 * @param <T> Entity type
 * @param <D> DTO type for request/response
 */
public interface GenericController<T, D> {
    
    /**
     * Get all entities with pagination
     * 
     * @param pageable Pagination information
     * @return Paged model of entities
     */
    ResponseEntity<PagedModel<EntityModel<T>>> getAll(Pageable pageable);
    
    /**
     * Get entity by ID
     * 
     * @param id Entity ID
     * @return Entity
     */
    ResponseEntity<T> getById(@PathVariable UUID id);
    
    /**
     * Create new entity
     * 
     * @param dto DTO with entity data
     * @return Created entity
     */
    ResponseEntity<T> create(@Valid @RequestBody D dto);
    
    /**
     * Update entity
     * 
     * @param id Entity ID
     * @param dto DTO with updated entity data
     * @return Updated entity
     */
    ResponseEntity<T> update(@PathVariable UUID id, @Valid @RequestBody D dto);
    
    /**
     * Delete entity
     * 
     * @param id Entity ID
     * @return Empty response
     */
    ResponseEntity<Void> delete(@PathVariable UUID id);
}
