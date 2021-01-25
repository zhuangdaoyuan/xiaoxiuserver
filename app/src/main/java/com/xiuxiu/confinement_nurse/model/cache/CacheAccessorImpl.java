package com.xiuxiu.confinement_nurse.model.cache;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tencent.mmkv.MMKV;


public final class CacheAccessorImpl implements CacheAccessor {


    @NonNull
    private MMKV kv;

    public CacheAccessorImpl(Context context) {
        String rootDir = MMKV.initialize(context.getApplicationContext());
        kv = MMKV.defaultMMKV();
    }

    @Override
    public void putString(String key, String value) {
        kv.encode(key, value);
    }

    @Override
    public void putInt(String key, int value) {
        kv.encode(key, value);
    }

    @Override
    public void putLong(String key, long value) {
        kv.encode(key, value);
    }

    @Override
    public void putFloat(String key, float value) {
        kv.encode(key, value);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        kv.encode(key, value);
    }

    @Override
    public void remove(String key) {
        kv.remove(key);
    }

    @Override
    public void clear() {
        kv.clear();
    }

    @Override
    public boolean containsKey(String key) {
        return kv.containsKey(key);
    }

    @Override
    public String getString(String key) {
        return kv.decodeString(key);
    }

    @Override
    public String getString(String key, String defValue) {
        return kv.decodeString(key, defValue);
    }

    @Override
    public int getInt(String key, int defValue) {
        return kv.decodeInt(key, defValue);
    }

    @Override
    public int getInt(String key) {
        return getInt(key);
    }

    @Override
    public long getLong(String key, long defValue) {
        return kv.decodeLong(key, defValue);
    }

    @Override
    public long getLong(String key) {
        return kv.decodeLong(key);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return kv.getFloat(key, defValue);
    }

    @Override
    public float getFloat(String key) {
        return kv.decodeFloat(key);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return kv.decodeBool(key, defValue);
    }

    @Override
    public boolean getBoolean(String key) {
        return kv.decodeBool(key);
    }


}
