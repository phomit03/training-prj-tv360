package com.example.tv360.process;

import com.example.tv360.config.CacheConfig;
import com.example.tv360.entity.Media;
import com.example.tv360.repository.MediaRepository;
import com.example.tv360.service.redis.RedisCacheService;

import java.util.List;

public class CacheMediaSearch implements Runnable{

    private final RedisCacheService redisCacheService;
    private final CacheConfig cacheConfig;
    private final MediaRepository mediaRepository;

    public CacheMediaSearch(RedisCacheService redisCacheService, CacheConfig cacheConfig, MediaRepository mediaRepository) {
        this.redisCacheService = redisCacheService;
        this.cacheConfig = cacheConfig;
        this.mediaRepository = mediaRepository;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if(redisCacheService.checkExistsKey(cacheConfig.queueNameMedia)) {
                    Media media = (Media) redisCacheService.rPop(cacheConfig.queueNameMedia);
                    if (media != null) {
                        List<Media> searchResults = mediaRepository.searchUserRepository(media.getTitle());

                        redisCacheService.setValue(cacheConfig.keyMediaEpisodePrefix + media.getId(), searchResults);
                    }
                }
                Thread.sleep(1000);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

