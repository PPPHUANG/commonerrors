package com.ppphuang.demo.system.config;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 不支持redis里纯纯的 String反序列化，比如php那边存"90,80,20",这边用此方法get出来的结果无法进行json反序列化，所以会报错
 * 只支持，json的序列化和反序列化
 *
 * @param <T>
 */
@Service("redisOperatorImpl")
public class RedisOperatorImpl<T> implements RedisOperator<T> {

    private final RedisTemplate<String, T> redisTemplateJson;

    public RedisOperatorImpl(RedisConnectionFactory redisConnectionFactory) {
        this.redisTemplateJson = new RedisTemplate<>();
        redisTemplateJson.setConnectionFactory(redisConnectionFactory);
        redisTemplateJson.setKeySerializer(new StringRedisSerializer());
        redisTemplateJson.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplateJson.setHashKeySerializer(new GenericToStringSerializer<>(Object.class));
        redisTemplateJson.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplateJson.afterPropertiesSet();
    }

    @Override
    public void set(String key, T value) {
        redisTemplateJson.opsForValue().set(key, value);
    }


    @Override
    public void set(String key, T value, long timeoutInSeconds) {
        redisTemplateJson.opsForValue().set(key, value, timeoutInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public Boolean setnx(String key, T value) {
        return redisTemplateJson.opsForValue().setIfAbsent(key, value);
    }

    @Override
    public Boolean setnx(String key, T value, int ttlSeconds) {
        return redisTemplateJson.opsForValue().setIfAbsent(key, value, ttlSeconds, TimeUnit.SECONDS);
    }

    @Override
    public T get(String key) {
        return redisTemplateJson.opsForValue().get(key);
    }

    @Override
    public Long incr(String key) {
        return redisTemplateJson.opsForValue().increment(key, 1);
    }

    @Override
    public Long incrBy(String key, Integer step) {
        return redisTemplateJson.opsForValue().increment(key, step);
    }

    @Override
    public Long decr(String key) {
        return redisTemplateJson.opsForValue().increment(key, -1);
    }

    @Override
    public Long decrBy(String key, Integer step) {
        return redisTemplateJson.opsForValue().increment(key, -step);
    }

    @Override
    public Boolean delete(String key) {
        return redisTemplateJson.delete(key);
    }

    @Override
    public Long deleteAll(List<String> keyList) {
        return redisTemplateJson.delete(keyList);
    }

    @Override
    public Boolean expire(String key, long timeoutInSeconds) {
        return redisTemplateJson.expire(key, timeoutInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void hset(String hashKey, String key, T value) {
        redisTemplateJson.<String, T>opsForHash().put(key, hashKey, value);
    }

    @Override
    public T hget(String hashKey, String key) {
        return redisTemplateJson.<String, T>opsForHash().get(key, hashKey);
    }

    @Override
    public Long hdelete(String hashKey, String key) {
        return redisTemplateJson.<String, T>opsForHash().delete(key, hashKey);
    }

    @Override
    public Boolean hexist(String hashKey, String key) {
        return redisTemplateJson.<String, T>opsForHash().hasKey(key, hashKey);
    }

    @Override
    public Cursor<Map.Entry<String, T>> hscan(String key, ScanOptions scanOptions) {
        return redisTemplateJson.<String, T>opsForHash().scan(key, scanOptions);
    }

    @Override
    public long hlen(String key) {
        return redisTemplateJson.<String, T>opsForHash().size(key);
    }

    @Override
    public void mset(Map<String, T> data, long timeoutInSeconds) {
        RedisCallback<List<Object>> pipelineCallback = connection -> {
            Expiration expiration = Expiration.from(timeoutInSeconds, TimeUnit.SECONDS);
            RedisStringCommands.SetOption setOption = RedisStringCommands.SetOption.UPSERT;
            connection.openPipeline();
            for (Map.Entry<String, T> entry : data.entrySet()) {

                StringRedisSerializer keySerializer = (StringRedisSerializer) redisTemplateJson.getKeySerializer();
                GenericJackson2JsonRedisSerializer valueSerializer =
                        (GenericJackson2JsonRedisSerializer) redisTemplateJson.getValueSerializer();

                byte[] rawKey = keySerializer.serialize(entry.getKey());
                byte[] rawValue = valueSerializer.serialize(entry.getValue());

                if (rawKey == null || rawValue == null) {
                    continue;
                }

                connection.set(rawKey, rawValue, expiration, setOption);
            }
            return connection.closePipeline();
        };

        redisTemplateJson.execute(pipelineCallback);

    }

    @Override
    public Map<String, T> mget(List<String> ids) {
        List<T> values = redisTemplateJson.opsForValue().multiGet(ids);
        Map<String, T> map = new HashMap<>();
        if (values == null) {
            return map;
        }

        for (int idx = 0; idx < values.size(); idx++) {
            T value = values.get(idx);
            if (value == null) {
                continue;
            }
            map.put(ids.get(idx), value);
        }


        return map;
    }

    @Override
    public Long zsetBatchAdd(String key, Set<ZSetOperations.TypedTuple<T>> tuples) {
        return redisTemplateJson.opsForZSet().add(key, tuples);
    }

    @Override
    public Long zsetCount(String key, double min, double max) {
        return redisTemplateJson.opsForZSet().count(key, min, max);
    }

    @Override
    public Long zsetDelete(String key, T value) {
        return redisTemplateJson.opsForZSet().remove(key, value);
    }

    @Override
    public Long lpush(String key, T value) {
        return redisTemplateJson.opsForList().leftPush(key, value);
    }


}
