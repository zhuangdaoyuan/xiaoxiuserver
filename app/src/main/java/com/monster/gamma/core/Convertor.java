package com.monster.gamma.core;


import com.monster.gamma.callback.GammaCallback;

/**
 */
public interface Convertor<T> {
   Class<?extends GammaCallback> map(T t);
}
