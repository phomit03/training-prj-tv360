package com.example.tv360.process;

import com.example.tv360.config.CacheConfig;
import com.example.tv360.config.WebConfig;
import com.example.tv360.entity.Country;
import com.example.tv360.service.redis.RedisCacheService;

public class CacheCountry implements Runnable{

    private final RedisCacheService redisCacheService;
    private final CacheConfig cacheConfig;

    public CacheCountry(RedisCacheService redisCacheService, CacheConfig cacheConfig) {
        this.redisCacheService = redisCacheService;
        this.cacheConfig = cacheConfig;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if(redisCacheService.checkExistsKey(cacheConfig.queueNameCountry)) {
                    Country country = (Country) redisCacheService.rPop(cacheConfig.queueNameCountry);
                    if (country != null) {
                        redisCacheService.setValue(cacheConfig.keyCountryPrefix+country.getId(), country);
                    }
                }
                Thread.sleep(1000);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

