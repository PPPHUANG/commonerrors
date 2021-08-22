package com.ppphuang.demo.system.config;

import com.google.gson.Gson;
import com.ppphuang.demo.tools.GsonUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class Gson4JsonRedisSerialize<T> implements RedisSerializer<T> {
    private static final byte[] EMPTY_ARRAY = new byte[0];
    Gson gson = GsonUtils.getGson();

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return EMPTY_ARRAY;
        }
        String tmp = String.format("%s_%s", t.getClass().getName(), gson.toJson(t));
        return tmp.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        try {
            if (bytes == null || bytes.length <= 0) {
                return null;
            }
            String tmp = new String(bytes, StandardCharsets.UTF_8);
            String className = tmp.substring(0, tmp.indexOf("_{"));
            String json = tmp.substring(tmp.indexOf("_{") + 1);
            return gson.fromJson(json, (Type) Class.forName(className));
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}