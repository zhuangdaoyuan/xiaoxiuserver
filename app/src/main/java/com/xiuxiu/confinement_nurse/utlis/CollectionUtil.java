package com.xiuxiu.confinement_nurse.utlis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1R;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc2;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public
class CollectionUtil {

    private CollectionUtil() {
    }

    public static boolean isEmpty(@Nullable Collection collection) {
        return null == collection || 0 == collection.size();
    }

    public static boolean isValidatePosition(@Nullable Collection collection, int position) {
        if (null == collection) {
            return false;
        }
        return position >= 0 && position < collection.size();
    }

    public static <T> T getSafe(@Nullable List<T> list, int position, @Nullable T defaultValue) {
        return isValidatePosition(list, position) ? list.get(position) : defaultValue;
    }

    public static <T> T getSafeOrNull(@Nullable List<T> list, int position) {
        return getSafe(list, position, null);
    }

    public static <T> void _forEach(@Nullable Collection<T> collection, @NonNull XFunc1<T> iterFunc) {
        if (null != collection) {
            for (T t : collection) {
                iterFunc.call(t);
            }
        }
    }

    public static <T> void _forEach(@Nullable Collection<T> collection, @NonNull XFunc1R<T, Boolean> iterFunc) {
        if (null != collection) {
            for (T t : collection) {
                if (iterFunc.call(t)) {
                    return;
                }
            }
        }
    }


    public static <T, TS> void _forEachCast(@Nullable Collection<T> collection, Class<TS> castClass, @NonNull XFunc1<TS> iterFunc) {
        if (null != collection) {
            for (T t : collection) {
                if (null != t && castClass.isAssignableFrom(t.getClass())) {
                    TS castT = castClass.cast(t);
                    iterFunc.call(castT);
                }
            }
        }
    }

    public static <T> void _forEach(@Nullable Collection<T> collection, @NonNull XFunc2<Integer, T> iterFunc) {
        if (null != collection) {
            int i = 0;
            for (T t : collection) {
                iterFunc.call(i, t);
                i++;
            }
        }
    }

    public static <T, TS> void _forEachCast(@Nullable Collection<T> collection, Class<TS> castClass, @NonNull XFunc2<Integer, TS> iterFunc) {
        if (null != collection) {
            int i = 0;
            for (T t : collection) {
                if (null != t && castClass.isAssignableFrom(t.getClass())) {
                    TS castT = castClass.cast(t);
                    iterFunc.call(i, castT);
                }
                i++;
            }
        }
    }

    public static <T> void _removeIf(@Nullable Collection<T> collection, @NonNull XFunc1R<T, Boolean> ifFunc) {
        if (null != collection) {
            Iterator<T> iter = collection.iterator();
            while (iter.hasNext()) {
                if (ifFunc.call(iter.next())) {
                    iter.remove();
                }
            }
        }
    }

    public static <T> void _removeFirstIf(@Nullable Collection<T> collection, @NonNull XFunc1R<T, Boolean> ifFunc) {
        if (null != collection) {
            Iterator<T> iter = collection.iterator();
            while (iter.hasNext()) {
                if (ifFunc.call(iter.next())) {
                    iter.remove();
                    break;
                }
            }
        }
    }



}
