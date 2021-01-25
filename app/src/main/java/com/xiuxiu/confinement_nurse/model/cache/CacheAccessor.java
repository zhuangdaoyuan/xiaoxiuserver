package com.xiuxiu.confinement_nurse.model.cache;

/**
 * 隔离
 * 可以被上层使用
 */
public interface CacheAccessor {

    public void putString(String key, String value);

    public void putInt(String key, int value);

    public void putLong(String key, long value);

    public void putFloat(String key, float value);

    public void putBoolean(String key, boolean value);

    public void remove(String key);

    public void clear();

    public boolean containsKey(String key);


    public String getString(String key);

    public String getString(String key, String defValue);

    public int getInt(String key, int defValue);

    public int getInt(String key);

    public long getLong(String key, long defValue);

    public long getLong(String key);

    public float getFloat(String key, float defValue);

    public float getFloat(String key);

    public boolean getBoolean(String key, boolean defValue);

    public boolean getBoolean(String key);

}
