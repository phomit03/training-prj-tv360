package com.example.tv360.process;

import com.example.tv360.config.CacheConfig;
import com.example.tv360.entity.Media;
import com.example.tv360.entity.MediaDetail;
import com.example.tv360.repository.MediaDetailRepository;
import com.example.tv360.service.redis.RedisCacheService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CacheMediaRelated implements Runnable{

    private final RedisCacheService redisCacheService;
    private final MediaDetailRepository mediaDetailRepository;

    private final CacheConfig cacheConfig;

    public CacheMediaRelated(RedisCacheService redisCacheService, MediaDetailRepository mediaDetailRepository, CacheConfig cacheConfig) {
        this.redisCacheService = redisCacheService;
        this.mediaDetailRepository = mediaDetailRepository;
        this.cacheConfig = cacheConfig;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (redisCacheService.checkExistsKey(cacheConfig.queueNameMediaRelated)) {
                    Media media = (Media) redisCacheService.rPop(cacheConfig.queueNameMediaRelated);
                    if (media != null) {
                        Set<MediaDetail> mediaDetails = media.getMediaDetails();

                        // Tìm ra media liên quan từ danh sách media-details
                        List<Media> relatedMedia = mediaDetails.stream()
                                .flatMap(mediaDetail -> mediaDetail.getMedia().getCategories().stream())
                                .flatMap(category -> category.getMedia().stream())
                                .filter(relatedMediaItem -> !relatedMediaItem.equals(media))
                                .filter(relatedMediaItem -> relatedMediaItem.getStatus() == 1)
                                .distinct()
                                .collect(Collectors.toList());

                        redisCacheService.setValue(cacheConfig.keyMediaRelatedPrefix+media.getId(), relatedMedia);
                    }
                }
                Thread.sleep(1000);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}

