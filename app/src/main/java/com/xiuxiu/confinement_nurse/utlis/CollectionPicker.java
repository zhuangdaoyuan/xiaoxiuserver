package com.xiuxiu.confinement_nurse.utlis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xiuxiu.confinement_nurse.utlis.xfunc.XPair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class CollectionPicker {
    private CollectionPicker() {
    }




    public interface PickerController<E, T> {
        boolean isPicked(E expect, T iterTarget);
    }

    @Nullable
    public static <E, T> T _pickFirst(@Nullable E expect, @Nullable Collection<T> collection, @NonNull PickerController<E, T> pickerController) {
        if (null == expect || CollectionUtil.isEmpty(collection)) {
            return null;
        }
        T picked = null;
        for (T t : collection) {
            if (pickerController.isPicked(expect, t)) {
                picked = t;
                break;
            }
        }
        return picked;
    }

    @Nullable
    public static <E, T> T pickFirstReverse(@Nullable E expect, @Nullable List<T> collection, @NonNull PickerController<E, T> pickerController) {
        if (null == expect || CollectionUtil.isEmpty(collection)) {
            return null;
        }
        T picked = null;
        for (int i = collection.size() - 1; i > 0; i--) {
            T t = collection.get(i);
            if (pickerController.isPicked(expect, t)) {
                picked = t;
                break;
            }
        }
        return picked;
    }


    @Nullable
    public static <E, T> List<T> _pick(@Nullable E expect, @Nullable Collection<T> collection, @NonNull PickerController<E, T> pickerController) {
        if (null == expect || CollectionUtil.isEmpty(collection)) {
            return null;
        }
        List<T> picked = new ArrayList<>();
        for (T t : collection) {
            if (pickerController.isPicked(expect, t)) {
                picked.add(t);
            }
        }
        return picked;
    }

    public static <E, T, TS> List<TS> _pickCast(E expect, Class<TS> castClass, @Nullable Collection<T> collection, @NonNull PickerController<E, TS> pickerController) {
        if (null == expect || CollectionUtil.isEmpty(collection)) {
            return null;
        }
        List<TS> picked = new ArrayList<>();
        for (T t : collection) {
            if (null != t && castClass.isAssignableFrom(t.getClass())) {
                TS castT = castClass.cast(t);
                if (pickerController.isPicked(expect, castT)) {
                    picked.add(castT);
                }
            }
        }
        return picked;
    }

    // pairs
    @Nullable
    public static <E, T> XPair<Integer, T> _pickFirstPair(@Nullable E expect, @Nullable Collection<T> collection, @NonNull PickerController<E, T> pickerController) {
        if (null == expect || CollectionUtil.isEmpty(collection)) {
            return null;
        }
        T picked = null;
        int i = 0;
        for (T t : collection) {
            if (pickerController.isPicked(expect, t)) {
                picked = t;
                break;
            }
            i++;
        }
        return null == picked ? null : new XPair<>(i, picked);
    }

    @Nullable
    public static <E, T> XPair<Integer, T> _pickFirstReversePair(@Nullable E expect, @Nullable List<T> collection, @NonNull PickerController<E, T> pickerController) {
        if (null == expect || CollectionUtil.isEmpty(collection)) {
            return null;
        }
        T picked = null;
        int pickedIndex = -1;
        for (int i = collection.size() - 1; i > 0; i--) {
            T t = collection.get(i);
            if (pickerController.isPicked(expect, t)) {
                picked = t;
                pickedIndex = i;
                break;
            }
        }
        return null == picked ? null : new XPair<>(pickedIndex, picked);
    }

    @Nullable
    public static <E, T> List<XPair<Integer, T>> _pickPairs(@Nullable E expect, @Nullable Collection<T> collection, @NonNull PickerController<E, T> pickerController) {
        if (null == expect || CollectionUtil.isEmpty(collection)) {
            return null;
        }
        List<XPair<Integer, T>> picked = new ArrayList<>();
        int i = 0;
        for (T t : collection) {
            if (pickerController.isPicked(expect, t)) {
                picked.add(new XPair<>(i, t));
            }
            i++;
        }
        return picked;
    }

    @Nullable
    public static <E, T, TS> List<XPair<Integer, TS>> _pickPairsCast(@Nullable E expect,
                                                                     Class<TS> castClass, @Nullable Collection<T> collection,
                                                                     @NonNull PickerController<E, TS> pickerController) {
        if (null == expect || CollectionUtil.isEmpty(collection)) {
            return null;
        }
        List<XPair<Integer, TS>> picked = new ArrayList<>();
        int i = 0;
        for (T t : collection) {
            if (null != t && castClass.isAssignableFrom(t.getClass())) {
                TS castT = castClass.cast(t);
                if (pickerController.isPicked(expect, castT)) {
                    picked.add(new XPair<>(i, castT));
                }
            }
            i++;
        }
        return picked;
    }


}
