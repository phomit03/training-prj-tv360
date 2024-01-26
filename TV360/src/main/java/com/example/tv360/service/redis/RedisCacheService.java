package com.example.tv360.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheService {
    @Autowired
    private RedisTemplate redisTemplate;

    public void setValue(String key, Object value) {
         redisTemplate.opsForValue().set(key,value);
    }

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setTTL(String key, Long timeout, TimeUnit timeUnit) {
        redisTemplate.expire(key,timeout, timeUnit);
    }

    public void setValueWithTTL(String key, Object value, Long timeout) {
        try {
            redisTemplate.opsForValue().set(key, value);
            this.setTTL(key, timeout,TimeUnit.SECONDS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void lPush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    public  void lPushWithListValue(String key, List value) {
        redisTemplate.opsForList().leftPushAll(key, value);
    }

    public Object rPop(String key) {
        return  redisTemplate.opsForList().rightPop(key);
    }

    public boolean checkExistsKey(String key) {
        boolean check = false;
        try {
            check = redisTemplate.hasKey(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return check;
    }
}
