package com.sto.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: egor
 * Date: 4/24/13
 */
public enum STOCategory {
    AUTOSERVICES(7, "Автосервисы"),
    SPARES(8, "Автозапчасти"),
    DISASSEMBLE(9, "Разборки"),
    WASHES(10, "Автомойки"),
    TIRE_SERVICE(11, "Шиномонтажи"),
    FUELING(12, "Заправки (АЗС)"),
    CAR_DEALERS(13, "Автосалоны");


    private static Map<Integer, STOCategory> cachedValues;


    private int id;
    private String name;

    private STOCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static STOCategory getById(int id) {
        initiate();
        return cachedValues.get(id);
    }

    private static void initiate() {
        if (cachedValues == null) {
            cachedValues = new HashMap<>(STOCategory.values().length, 1);
            for (STOCategory category : STOCategory.values()) {
                cachedValues.put(category.id, category);
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
