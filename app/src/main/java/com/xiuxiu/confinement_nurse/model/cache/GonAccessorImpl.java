package com.xiuxiu.confinement_nurse.model.cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GonAccessorImpl {
    private static Gson GSON;


    static final Gson ORIGINAL_GSON;

    static {
        ORIGINAL_GSON = new GsonBuilder()
                .create();

        GSON = generateOriginalGsonBuilder().create();
    }

    private static GsonBuilder generateOriginalGsonBuilder() {
        return new GsonBuilder();
//                .addSerializationExclusionStrategy(new AnnotationSerializationExclusionStrategy())
//                .addDeserializationExclusionStrategy(new AnnotationDeserializationExclusionStrategy());
    }


    public static Gson getGson() {
        return GSON;
    }

    public static Gson getOriginalGson() {
        return ORIGINAL_GSON;
    }

}
