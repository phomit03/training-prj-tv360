package com.example.tv360.process;

import com.example.tv360.config.CacheConfig;
import com.example.tv360.entity.Media;
import com.example.tv360.entity.MediaDetail;
import com.example.tv360.repository.MediaDetailRepository;
import com.example.tv360.service.redis.RedisCacheService;

import java.util.List;
import java.util.stream.Collectors;

public class CacheMediaEpisode implements Runnable{

    private final RedisCacheService redisCacheService;
    private final MediaDetailRepository mediaDetailRepository;

    private final CacheConfig cacheConfig;

    public CacheMediaEpisode(RedisCacheService redisCacheService, MediaDetailRepository mediaDetailRepository, CacheConfig cacheConfig) {
        this.redisCacheService = redisCacheService;
        this.mediaDetailRepository = mediaDetailRepository;
        this.cacheConfig = cacheConfig;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (redisCacheService.checkExistsKey(cacheConfig.queueNameMediaEpisode)) {
                    Media media = (Media) redisCacheService.rPop(cacheConfig.queueNameMediaEpisode);
                    if (media != null) {
                        //query media-detail by media-id
                        List<MediaDetail> mediaDetails = mediaDetailRepository.findByMediaId(media.getId());

                        //get episode from episodeList
                        String episodeList = mediaDetails.stream()
                                .map(MediaDetail::getEpisode)
                                .map(String::valueOf)
                                .collect(Collectors.joining(","));

                        redisCacheService.setValue(cacheConfig.keyMediaEpisodePrefix + media.getId(), episodeList);
                    }
                }
                Thread.sleep(1000);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}

