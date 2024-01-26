package com.example.tv360.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("searchResults");
    }

    // config redis app
    @Value("${duplicate.media.queue.name}")
    public List<String> duplicateMediaQueueName;

    @Value("${queue.media.name}")
    public String queueNameMedia;

    @Value("${cache.media.prefix}")
    public String keyMediaPrefix;

    @Value("${queue.media.episode.name}")
    public String queueNameMediaEpisode;

    @Value("${cache.media.episode.prefix}")
    public String keyMediaEpisodePrefix;

    @Value("${queue.media.relate.name}")
    public String queueNameMediaRelated;

    @Value("${cache.media.related.prefix}")
    public String keyMediaRelatedPrefix;

    @Value("${queue.media.categories.name}")
    public String queueNameMediaCategories;

    @Value("${cache.media.categories.prefix}")
    public String keyMediaCategoriesPrefix;

    @Value("${queue.media.cast.name}")
    public String queueNameMediaCast;

    @Value("${cache.media.cast.prefix}")
    public String keyMediaCastPrefix;

    @Value("${queue.media.search.name}")
    public String queueNameMediaSearch;

    @Value("${cache.media.search.prefix}")
    public String keyMediaSearchPrefix;

    @Value("${duplicate.country.queue.name}")
    public List<String> duplicateCountryQueueName;

    @Value("${queue.country.name}")
    public String queueNameCountry;

    @Value("${cache.country.prefix}")
    public String keyCountryPrefix;
}
