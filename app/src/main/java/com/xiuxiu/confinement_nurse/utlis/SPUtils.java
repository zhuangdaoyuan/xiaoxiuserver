package com.xiuxiu.confinement_nurse.utlis;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * SharedPreferences封装类
 * * 所有的commit操作使用了SharedPreferencesCompat.apply进行了替代;尽可能异步操作
 * * 1，看了一下实现，getSharedPreference实际上是一个static ArrayMap保存的，因此没必要封装成static类型了
 *
 */
public class SPUtils {
	private static final String DEFAULT_FILE_NAME = "default"; // 保持的文件名，作为SP的默认文件夹
	
	/**
	 * @param key    关键字
	 * @param object 数据
	 */
	public static void put(Context context,String key, Object object) {
		put(context, key, object, DEFAULT_FILE_NAME);
	}
	
	/**
	 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
	 * if object == null, 默认作为String
	 *
	 * @param key      关键字
	 * @param object   数据
	 * @param fileName 文件名
	 */
	public static void put(Context context, String key, Object object, String fileName) {
		SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		
		// instanceof 用来 指出对象是否是特定类的一个实例
		if (null == object) {
			editor.putString(key, "");
		} else if (object instanceof String) {
			editor.putString(key, (String) object);
		} else if (object instanceof Integer) {
			editor.putInt(key, (Integer) object);
		} else if (object instanceof Boolean) {
			editor.putBoolean(key, (Boolean) object);
		} else if (object instanceof Float) {
			editor.putFloat(key, (Float) object);
		} else if (object instanceof Long) {
			editor.putLong(key, (Long) object);
		} else {
			editor.putString(key, object.toString());
		}
		
		SharedPreferencesCompat.apply(editor);
	}
	
	/**
	 * @param key           关键字
	 * @param defaultObject 默认值
	 * @return 返回获取到的参数
	 */
	public static Object get(Context context,String key, Object defaultObject) {
		return get(context, key, defaultObject, DEFAULT_FILE_NAME);
	}
	
	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 *
	 * @param key           关键字
	 * @param defaultObject 默认值（如果给null，由于不知道数据类型，直接返回null了）
	 * @param fileName      文件名
	 * @return 如果key参数错误，则返回空
	 */
	public static Object get(Context context, String key, Object defaultObject, String fileName) {
		SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		if (defaultObject instanceof String) {
			return sp.getString(key, (String) defaultObject);
		} else if (defaultObject instanceof Integer) {
			return sp.getInt(key, (Integer) defaultObject);
		} else if (defaultObject instanceof Boolean) {
			return sp.getBoolean(key, (Boolean) defaultObject);
		} else if (defaultObject instanceof Float) {
			return sp.getFloat(key, (Float) defaultObject);
		} else if (defaultObject instanceof Long) {
			return sp.getLong(key, (Long) defaultObject);
		}
		return null;
	}
	
	/**
	 * 移除某个key值已经对应的值
	 *
	 * @param key 关键字
	 */
	public static void remove(Context context,String key) {
		remove(context, key, DEFAULT_FILE_NAME);
	}
	
	/**
	 * 移除某个key值已经对应的值
	 *
	 * @param key      关键字
	 * @param fileName 文件名
	 */
	public static void remove(Context context, String key, String fileName) {
		SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
	}
	
	/**
	 * 清除所有数据
	 */
	public static void clear(Context context) {
		clear(context, DEFAULT_FILE_NAME);
	}
	
	/**
	 * 清除所有数据
	 *
	 * @param fileName 文件名
	 */
	public static void clear(Context context, String fileName) {
		SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		SharedPreferencesCompat.apply(editor);
	}
	
	/**
	 * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
	 * <p>
	 * apply相当于commit来说是new API呢，为了更好的兼容，我们做了适配
	 */
	private static class SharedPreferencesCompat {
		private static final Method sApplyMethod = findApplyMethod();
		
		/**
		 * 反射查找apply的方法
		 *
		 * @return 反射方法
		 */
		@SuppressWarnings({"unchecked", "rawtypes"})
		private static Method findApplyMethod() {
			try {
				Class clz = SharedPreferences.Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException e) {
				//
			}
			return null;
		}
		
		/**
		 * 如果找到则使用apply执行，否则使用commit
		 *
		 * @param editor 编辑器
		 */
		private static void apply(SharedPreferences.Editor editor) {
			try {
				if (sApplyMethod != null) {
					sApplyMethod.invoke(editor);
					return;
				}
			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
				//
			}
			editor.commit();
		}
	}

	/**
	 * 添加保存在sp中数据变化的监听
	 * */
	public static void sharePreferenceListener(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener){
		SharedPreferences sp = context.getSharedPreferences(DEFAULT_FILE_NAME, Context.MODE_PRIVATE);
		sp.registerOnSharedPreferenceChangeListener(listener);
	}
	/**
	 * 移除保存在sp中数据变化的监听
	 * */
	public static void removeListener(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener){
		try {
			SharedPreferences sp = context.getSharedPreferences(DEFAULT_FILE_NAME, Context.MODE_PRIVATE);
			sp.unregisterOnSharedPreferenceChangeListener(listener);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
