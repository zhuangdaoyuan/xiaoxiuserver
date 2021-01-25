package com.monster.gamma.core;

import androidx.annotation.NonNull;

import com.monster.gamma.GammaUtil;
import com.monster.gamma.callback.GammaCallback;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class Gamma {
    private static volatile Gamma loadSir;
    private Builder builder;

    public static Gamma getDefault() {
        if (loadSir == null) {
            synchronized (Gamma.class) {
                if (loadSir == null) {
                    loadSir = new Gamma();
                }
            }
        }
        return loadSir;
    }

    private Gamma() {
        this.builder = new Builder();
    }

    private void setBuilder(@NonNull Builder builder) {
        this.builder = builder;
    }

    private Gamma(Builder builder) {
        this.builder = builder;
    }

    public LoadService register(@NonNull Object target) {
        return register(target, null, null);
    }

    public LoadService register(Object target, GammaCallback.OnReloadListener onReloadListener) {
        return register(target, onReloadListener, null);
    }

    public <T> LoadService<T> register(Object target, GammaCallback.OnReloadListener onReloadListener, Convertor<T>
            convertor) {
        TargetContext targetContext = GammaUtil.getTargetContext(target);
        return new LoadService<>(convertor, targetContext, onReloadListener, builder);
    }

    public static Builder beginBuilder() {
        return new Builder();
    }

    public static class Builder {
        private List<GammaCallback> callbacks = new ArrayList<>();
        private Class<? extends GammaCallback> defaultCallback;

        public Builder addCallback(@NonNull GammaCallback callback) {
            callbacks.add(callback);
            return this;
        }

        public Builder setDefaultCallback(@NonNull Class<? extends GammaCallback> defaultCallback) {
            this.defaultCallback = defaultCallback;
            return this;
        }

        List<GammaCallback> getCallbacks() {
            return callbacks;
        }

        Class<? extends GammaCallback> getDefaultCallback() {
            return defaultCallback;
        }

        public void commit() {
            getDefault().setBuilder(this);
        }

        public Gamma build() {
            return new Gamma(this);
        }

    }
}
