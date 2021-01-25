package com.xiuxiu.confinement_nurse.utlis.xfunc;

import java.io.Serializable;

public class XPair<K, V> implements Serializable {
    public K key;
    public V value;

    public XPair() {

    }

    public XPair(K key, V value) {
        this.key = key;
        this.value = value;
    }
}