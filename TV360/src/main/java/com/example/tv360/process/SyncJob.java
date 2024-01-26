package com.example.tv360.process;

import com.example.tv360.config.CacheConfig;
import com.example.tv360.dto.MediaDTO;
import com.example.tv360.entity.Media;
import com.example.tv360.service.MediaService;
import com.example.tv360.service.redis.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@EnableScheduling
@Configuration
public class SyncJob {

    @Autowired
    private CacheConfig cacheConfig;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private RedisCacheService redisCacheService;

    @Scheduled(cron = "0 0/1 * * * *")
    public void duplicateMedia() {
        try {
            // query full thông tin media và push vào queue
            int limit = 1000;
            int page = 0;

            while (true) {
                List<MediaDTO> data = mediaService.getAllMedias(limit, page);

                if (!data.isEmpty()) {
                    // push vào các queue khác nhau;
                    for (String queueName : cacheConfig.duplicateMediaQueueName) {
                        redisCacheService.lPushWithListValue(queueName, data);
                    }
                    page++;
                } else {
                    break;
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
