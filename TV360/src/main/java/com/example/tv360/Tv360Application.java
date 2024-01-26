package com.example.tv360;

import com.example.tv360.config.CacheConfig;
import com.example.tv360.process.*;
import com.example.tv360.repository.MediaDetailRepository;
import com.example.tv360.service.redis.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class Tv360Application {
    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private MediaDetailRepository mediaDetailRepository;

    @Autowired
    CacheConfig cacheConfig;

    public static void main(String[] args) {
        SpringApplication.run(Tv360Application.class, args);
    }

    @Bean
    public void initProcess() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        //run process country
        CacheCountry cacheCountry = new CacheCountry(redisCacheService, cacheConfig);
        executorService.execute(cacheCountry);

        //run process media
        CacheMedia cacheMedia = new CacheMedia(redisCacheService, cacheConfig);
        executorService.execute(cacheMedia);

        //run process media-episode
        for (int i =0; i< 2; i++) {
            CacheMediaEpisode cacheMediaEpisode = new CacheMediaEpisode(redisCacheService, mediaDetailRepository, cacheConfig);
            executorService.execute(cacheMediaEpisode);
        }

        //run process media-related
        for (int i =0; i< 2; i++) {
            CacheMediaRelated cacheMediaEpisode = new CacheMediaRelated(redisCacheService, mediaDetailRepository, cacheConfig);
            executorService.execute(cacheMediaEpisode);
        }

        //run process media-categories
        for (int i =0; i< 2; i++) {
            CacheMediaCategories cacheMediaCategories = new CacheMediaCategories(redisCacheService, cacheConfig);
            executorService.execute(cacheMediaCategories);
        }

        //run process media-cast
        for (int i =0; i< 2; i++) {
            CacheMediaCast cacheMediaCast = new CacheMediaCast(redisCacheService, cacheConfig);
            executorService.execute(cacheMediaCast);
        }
    }
}
