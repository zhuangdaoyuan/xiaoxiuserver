package com.lxj.xpopupext.popup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class CollectionUtil {

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




}
