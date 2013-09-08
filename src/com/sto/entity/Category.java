package com.sto.entity;

import com.sto.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: egor
 * Date: 4/24/13
 */

//todo maybe check it out from database
public enum Category {
    AUTOSERVICES(7, "Автосервисы", R.drawable.autoservices),
    SPARES(8, "Автозапчасти", R.drawable.spares),
    DISASSEMBLE(9, "Разборки", R.drawable.disassemble),
    WASHES(10, "Автомойки", R.drawable.washes),
    FUELING(12, "Заправки (АЗС)", R.drawable.fueling);


    private int id;
    private String name;
    private int resource;

    private Category(int id, String name, int resource) {
        this.id = id;
        this.name = name;
        this.resource = resource;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getResource() {
        return resource;
    }

    public static Category getCategoryByName(String name){
        for (Category category: Category.values() ){
            if (category.getName().equals(name)){
                return category;
            }
        }
        return null;
    }

    public static Category getCategoryById(int id){
        for (Category category: Category.values() ){
            if (category.getId() == id){
                return category;
            }
        }
        return null;
    }

    public static List<String> getCategoriesName(){
        List<String> result = new ArrayList<>(Category.values().length);
        for( Category category: Category.values()){
            result.add(category.getName());
        }
        return result;
    }

}
