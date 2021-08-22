package com.ppphuang.demo.tools;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;

public class GsonUtils {
    private final static Gson gson = makeBuilder().create();

    public static Gson getGson() {
        return gson;
    }

    public static GsonBuilder makeBuilder() {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .serializeNulls()
                .registerTypeAdapter(Date.class, new MyDateTypeAdapter()).registerTypeAdapter(Double.class, new JsonSerializer<Double>() {

                    @Override
                    public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                        if (src == src.longValue()) {
                            return new JsonPrimitive(src.longValue());
                        }
                        return new JsonPrimitive(src);
                    }
                });
    }

    /**
     * 类似fastjson：处理null值
     *
     * @param jsonObject
     * @param keyName
     * @return
     */
    public static String getString(JsonObject jsonObject, String keyName) {
        try {
            String result = null;
            if (jsonObject == null || keyName == null || !jsonObject.has(keyName)) {
                return result;
            }
            return jsonObject.get(keyName).getAsString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 类似fastjson：处理null值
     *
     * @param jsonObject
     * @param keyName
     * @return
     */
    public static Integer getInteger(JsonObject jsonObject, String keyName) {
        try {
            Integer result = 0;
            if (jsonObject == null || keyName == null || !jsonObject.has(keyName)) {
                return result;
            }
            return jsonObject.get(keyName).getAsInt();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 类似fastjson：处理null值
     *
     * @param jsonObject
     * @param keyName
     * @return
     */
    public static Byte getByte(JsonObject jsonObject, String keyName) {
        try {
            Byte result = 0;
            if (jsonObject == null || keyName == null || !jsonObject.has(keyName)) {
                return result;
            }
            return jsonObject.get(keyName).getAsByte();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 类似fastjson：处理null值
     *
     * @param jsonObject
     * @param keyName
     * @return
     */
    public static Long getLong(JsonObject jsonObject, String keyName) {
        try {
            Long result = null;
            if (jsonObject == null || keyName == null || !jsonObject.has(keyName)) {
                return result;
            }
            return jsonObject.get(keyName).getAsLong();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 类似fastjson：处理null值
     *
     * @param jsonObject
     * @param keyName
     * @return
     */
    public static Double getDouble(JsonObject jsonObject, String keyName) {
        try {
            Double result = null;
            if (jsonObject == null || keyName == null || !jsonObject.has(keyName)) {
                return result;
            }
            return jsonObject.get(keyName).getAsDouble();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 类似fastjson：处理null值
     *
     * @param jsonObject
     * @param keyName
     * @return
     */
    public static JsonObject getJsonObject(JsonObject jsonObject, String keyName) {
        try {
            JsonObject result = null;
            if (jsonObject == null || keyName == null || !jsonObject.has(keyName)) {
                return result;
            }
            return jsonObject.get(keyName).getAsJsonObject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 类似fastjson：处理null值
     *
     * @param jsonObject
     * @param keyName
     * @return
     */
    public static JsonArray getJsonArray(JsonObject jsonObject, String keyName) {
        try {
            JsonArray result = null;
            if (jsonObject == null || keyName == null || !jsonObject.has(keyName)) {
                return result;
            }
            return jsonObject.get(keyName).getAsJsonArray();
        } catch (Exception e) {
            return null;
        }
    }

//    public static class SpringfoxJsonToGsonAdapter implements JsonSerializer<Json> {
//
//        @Override
//        public JsonElement serialize(Json src, Type typeOfSrc, JsonSerializationContext context) {
//            return JsonParser.parseString(src.value());
//        }
//    }
}
