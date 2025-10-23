package com.example.lab4_20211602_iot.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public final class JsonUtils {
    private static final Gson GSON = new Gson();
    private JsonUtils(){}

    public static <T> String toJson(List<T> list) {
        return GSON.toJson(list);
    }

    public static <T> T fromJson(String json, Type type) {
        if (json == null || json.isEmpty()) return null;
        return GSON.fromJson(json, type);
    }

    public static <T> Type listTypeOf(Class<T> clazz) {
        return TypeToken.getParameterized(List.class, clazz).getType();
    }
}
