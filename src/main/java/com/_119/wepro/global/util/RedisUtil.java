package com._119.wepro.global.util;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedisUtil {

  private final RedisTemplate<String, String> redisTemplate;

  public String getData(String key) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    return valueOperations.get(key);
  }

  public void setData(String key, String value) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    valueOperations.set(key, value);
  }

  public void setDataExpire(String key, String value, long duration) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    valueOperations.set(key, value, Duration.ofMillis(duration));
  }

  public void deleteData(String key) {
    redisTemplate.delete(key);
  }

  public void expireValues(String key, int timeout) {
    redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
  }

  public boolean existsData(String key) {
    return redisTemplate.hasKey(key);
  }
}