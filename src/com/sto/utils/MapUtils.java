package com.sto.utils;

import java.util.*;

public class MapUtils {

    public static <K, V> void addToMap(Map<K, List<V>> map, K key, V value) {
        List<V> list = map.get(key);
        if (list == null) {
            list = new ArrayList<V>();
            map.put(key, list);
        }
        list.add(value);
    }
}

