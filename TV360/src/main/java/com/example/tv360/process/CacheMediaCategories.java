package com.example.tv360.process;

import com.example.tv360.config.CacheConfig;
import com.example.tv360.entity.Category;
import com.example.tv360.entity.Media;
import com.example.tv360.service.redis.RedisCacheService;

import java.util.ArrayList;
import java.util.Set;

public class CacheMediaCategories implements Runnable{

    private final RedisCacheService redisCacheService;
    private final CacheConfig cacheConfig;

    public CacheMediaCategories(RedisCacheService redisCacheService, CacheConfig cacheConfig) {
        this.redisCacheService = redisCacheService;
        this.cacheConfig = cacheConfig;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if(redisCacheService.checkExistsKey(cacheConfig.queueNameMediaCategories)) {
                    Media media = (Media) redisCacheService.rPop(cacheConfig.queueNameMediaCategories);
                    if (media != null) {
                        Set<Category> categories = media.getCategories();


                        // Cache the list of categories related to the media
                        redisCacheService.setValue(cacheConfig.keyMediaCategoriesPrefix + media.getId(), new ArrayList<>(categories));                    }
                }
                Thread.sleep(1000);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

