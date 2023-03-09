package io.innocentdream.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonHelper {

    public static JsonObject streamToObject(InputStream stream) {
        return Streams.parse(new JsonReader(new InputStreamReader(stream))).getAsJsonObject();
    }

    public static JsonArray streamToArray(InputStream stream) {
        return Streams.parse(new JsonReader(new InputStreamReader(stream))).getAsJsonArray();
    }

    public static JsonObject getObject(JsonObject data, String key) {
        return data.get(key).getAsJsonObject();
    }

    public static JsonArray getArray(JsonObject data, String key) {
        return data.get(key).getAsJsonArray();
    }

    public static JsonPrimitive getPrimitive(JsonObject data, String key) {
        return data.get(key).getAsJsonPrimitive();
    }

    public static long getLong(JsonObject data, String key) {
        return getPrimitive(data, key).getAsLong();
    }

    public static int getInt(JsonObject data, String key) {
        return getPrimitive(data, key).getAsInt();
    }

    public static short getShort(JsonObject data, String key) {
        return getPrimitive(data, key).getAsShort();
    }

    public static byte getByte(JsonObject data, String key) {
        return getPrimitive(data, key).getAsByte();
    }

    public static float getFloat(JsonObject data, String key) {
        return getPrimitive(data, key).getAsFloat();
    }

    public static double getDouble(JsonObject data, String key) {
        return getPrimitive(data, key).getAsDouble();
    }

    public static boolean getBoolean(JsonObject data, String key) {
        return getPrimitive(data, key).getAsBoolean();
    }

    public static String getString(JsonObject data, String key) {
        try {
            return data.get(key).getAsString();
        } catch (UnsupportedOperationException | IllegalStateException e) {
            return data.get(key).toString();
        }
    }

}
