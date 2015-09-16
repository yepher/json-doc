
package com.yepher.jsondoc.helpers;

import java.io.Reader;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * helper class for GSON that:
 * <ul>
 * <li>includes support for enumerated subclasses;
 * <li>is a singleton that eliminates the need for repetitive constructions of the GSON instance
 * </ul>
 * 
 * @author jrenkel
 */
public class GsonHelper {

    private final static Logger logger = LoggerFactory.getLogger(GsonHelper.class);

    /**
     * the GsonHelper factory class that:
     * <ul>
     * <li>creates the only GsonHelper instance that is needed
     * <li>has an implicit semaphore to make initialization threadsafe
     * </ul>
     * basically this class:
     * <ul>
     * <li>creates an appropriate Gson instance</li>
     * <li>has delegate methods for all public methods in that instance</li>
     * </ul>
     * 
     * @author jrenkel
     */
    private static class Factory {

        private static GsonHelper instance = new GsonHelper();
    }

    private Gson gson;

    /**
     * get the GsonHelper singleton instance
     * 
     * @return the GsonHelper singleton instance
     */
    public static GsonHelper getInstance() {
        return Factory.instance;
    }

    private GsonHelper() {
        super();

        this.gson = getGson(false);
    }

    public void setPrettyPrinting(boolean usePrettyPrinting) {
        this.gson = getGson(usePrettyPrinting);
    }

    private Gson getGson(boolean usePrettyPrinting) {
        EnumeratedSubclassAdapterFactory factory = new EnumeratedSubclassAdapterFactory();

        GsonBuilder builder = new GsonBuilder();
        if (usePrettyPrinting) {
            builder.setPrettyPrinting();
        }
        builder.registerTypeAdapterFactory(factory);
        return builder.create();
    }

    public <T>T fromJson(String json, TypeToken<T> typeOfT) throws JsonSyntaxException {
        try {
            return this.gson.fromJson(json, typeOfT.getType());
        } catch (JsonSyntaxException e) {
            logger.error("JsonSyntaxException on string: {}", json);
            throw e;
        }
    }

    public <T>TypeAdapter<T> getAdapter(TypeToken<T> type) {
        return this.gson.getAdapter(type);
    }

    public <T>TypeAdapter<T> getDelegateAdapter(TypeAdapterFactory skipPast, TypeToken<T> type) {
        return this.gson.getDelegateAdapter(skipPast, type);
    }

    public <T>TypeAdapter<T> getAdapter(Class<T> type) {
        return this.gson.getAdapter(type);
    }

    public JsonElement toJsonTree(Object src) {
        return this.gson.toJsonTree(src);
    }

    public JsonElement toJsonTree(Object src, Type typeOfSrc) {
        return this.gson.toJsonTree(src, typeOfSrc);
    }

    public String toJson(Object src) {
        return this.gson.toJson(src);
    }

    public String toJson(Object src, Type typeOfSrc) {
        return this.gson.toJson(src, typeOfSrc);
    }

    public void toJson(Object src, Appendable writer) throws JsonIOException {
        this.gson.toJson(src, writer);
    }

    public void toJson(Object src, Type typeOfSrc, Appendable writer) throws JsonIOException {
        this.gson.toJson(src, typeOfSrc, writer);
    }

    public void toJson(Object src, Type typeOfSrc, JsonWriter writer) throws JsonIOException {
        this.gson.toJson(src, typeOfSrc, writer);
    }

    public String toJson(JsonElement jsonElement) {
        return this.gson.toJson(jsonElement);
    }

    public void toJson(JsonElement jsonElement, Appendable writer) throws JsonIOException {
        this.gson.toJson(jsonElement, writer);
    }

    public void toJson(JsonElement jsonElement, JsonWriter writer) throws JsonIOException {
        this.gson.toJson(jsonElement, writer);
    }

    public <T>T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        try {
            return this.gson.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            logger.error("JsonSyntaxException on string: {}", json);
            throw e;
        }
    }

    public <T>T fromJson(String json, Type typeOfT) throws JsonSyntaxException {
        try {
            return this.gson.fromJson(json, typeOfT);
        } catch (JsonSyntaxException e) {
            logger.error("JsonSyntaxException on string: {}", json);
            throw e;
        }
    }

    public <T>T fromJson(Reader json, Class<T> classOfT) throws JsonSyntaxException, JsonIOException {
        return this.gson.fromJson(json, classOfT);
    }

    public <T>T fromJson(Reader json, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return this.gson.fromJson(json, typeOfT);
    }

    public <T>T fromJson(JsonReader reader, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return this.gson.fromJson(reader, typeOfT);
    }

    public <T>T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
        return this.gson.fromJson(json, classOfT);
    }

    public <T>T fromJson(JsonElement json, Type typeOfT) throws JsonSyntaxException {
        return this.gson.fromJson(json, typeOfT);
    }

    @Override
    public String toString() {
        return this.gson.toString();
    }

}
