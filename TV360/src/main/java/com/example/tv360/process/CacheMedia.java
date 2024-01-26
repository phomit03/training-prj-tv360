package com.example.tv360.process;

import com.example.tv360.config.CacheConfig;
import com.example.tv360.entity.Media;
import com.example.tv360.service.redis.RedisCacheService;

public class CacheMedia implements Runnable{

    private final RedisCacheService redisCacheService;
    private final CacheConfig cacheConfig;

    public CacheMedia(RedisCacheService redisCacheService,CacheConfig cacheConfig) {
        this.redisCacheService = redisCacheService;
        this.cacheConfig = cacheConfig;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if(redisCacheService.checkExistsKey(cacheConfig.queueNameMedia)) {
                    Media media = (Media) redisCacheService.rPop(cacheConfig.queueNameMedia);
                    if (media != null) {
                        redisCacheService.setValue(cacheConfig.keyMediaPrefix+media.getId(), media);
                    }
                }
                Thread.sleep(1000);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

