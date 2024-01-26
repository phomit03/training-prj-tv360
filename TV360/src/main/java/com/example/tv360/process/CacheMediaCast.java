package com.example.tv360.process;

import com.example.tv360.config.CacheConfig;
import com.example.tv360.entity.Cast;
import com.example.tv360.entity.Media;
import com.example.tv360.service.redis.RedisCacheService;

import java.util.ArrayList;
import java.util.Set;

public class CacheMediaCast implements Runnable{

    private final RedisCacheService redisCacheService;
    private final CacheConfig cacheConfig;

    public CacheMediaCast(RedisCacheService redisCacheService, CacheConfig cacheConfig) {
        this.redisCacheService = redisCacheService;
        this.cacheConfig = cacheConfig;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if(redisCacheService.checkExistsKey(cacheConfig.queueNameMediaCast)) {
                    Media media = (Media) redisCacheService.rPop(cacheConfig.queueNameMediaCast);
                    if (media != null) {
                        Set<Cast> cast = media.getCast();

                        // Cache the list of Cast related to the media
                        redisCacheService.setValue(cacheConfig.keyMediaCastPrefix + media.getId(), new ArrayList<>(cast));                    }
                }
                Thread.sleep(1000);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

