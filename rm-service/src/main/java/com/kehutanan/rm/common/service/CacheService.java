package com.kehutanan.rm.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final CacheManager cacheManager;

    /**
     * Clears all caches
     */
    public void clearAllCaches() {
        cacheManager.getCacheNames().forEach(cacheName -> 
            Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
    }
    
    /**
     * Clears a specific cache
     *
     * @param cacheName the name of the cache to clear
     */
    public void clearCache(String cacheName) {
        Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
    }
    
    /**
     * Evicts a specific item from cache
     *
     * @param cacheName the name of the cache
     * @param key the key to evict
     */
    public void evictSingle(String cacheName, String key) {
        Objects.requireNonNull(cacheManager.getCache(cacheName)).evict(key);
    }
}