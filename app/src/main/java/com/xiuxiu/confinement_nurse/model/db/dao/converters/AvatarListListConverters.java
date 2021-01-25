package com.xiuxiu.confinement_nurse.model.db.dao.converters;

import android.util.Log;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiuxiu.confinement_nurse.model.cache.GonAccessorImpl;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class AvatarListListConverters {
    @TypeConverter
    public List<String> stringToObject(String msg) {
        Type type = new TypeToken<String>() {
        }.getType();
        try {
            String[] o = GonAccessorImpl.getOriginalGson().fromJson(msg, (Type) String[].class);
            List<String> stringB = Arrays.asList(o);
            return stringB;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @TypeConverter
    public String objectToString(List<String> msg) {
        return GonAccessorImpl.getOriginalGson().toJson(msg);
    }
}
