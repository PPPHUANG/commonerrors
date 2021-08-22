package com.ppphuang.demo.system.config;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ppphuang.demo.tools.GsonUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class Gson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    Gson gson = GsonUtils.getGson();

    private static final byte[] EMPTY_ARRAY = new byte[0];


    @Override
    public byte[] serialize(T input) throws SerializationException {
        if (input == null) {
            return EMPTY_ARRAY;
        }

        return gson.toJson(input).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        return deserialize(bytes, new TypeToken<T>() {
        }.getType());
    }

    public T deserialize(byte[] bytes, Type type) throws SerializationException {
        return null == bytes ? null : gson.fromJson(new String(bytes, StandardCharsets.UTF_8), type);
    }
}
