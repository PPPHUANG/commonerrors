package com.ppphuang.demo.system.config;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisOperator<T> {

    void set(String key, T value);

    void set(String key, T value, long timeoutInSeconds);

    Boolean setnx(String key, T value);

    Boolean setnx(String key, T value, int ttlSeconds);

    T get(String key);

    Long incr(String key);

    Long incrBy(String key, Integer step);

    Long decr(String key);

    Long decrBy(String key, Integer step);

    Boolean delete(String key);

    Long deleteAll(List<String> key);

    Boolean expire(String key, long timeoutInSeconds);

    void hset(String hashKey, String key, T value);

    T hget(String hashKey, String key);

    Long hdelete(String hashKey, String key);

    Boolean hexist(String hashKey, String key);

    Cursor<Map.Entry<String, T>> hscan(String key, ScanOptions scanOptions);

    long hlen(String key);

    void mset(Map<String, T> data, long timeoutInSeconds);

    Map<String, T> mget(List<String> ids);

    Long zsetBatchAdd(String key, Set<ZSetOperations.TypedTuple<T>> tuples);

    Long zsetCount(String key, double min, double max);

    Long zsetDelete(String key, T value);

    Long lpush(String key, T value);

}
