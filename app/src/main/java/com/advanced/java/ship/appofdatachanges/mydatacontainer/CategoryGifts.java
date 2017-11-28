package com.advanced.java.ship.appofdatachanges.mydatacontainer;

import java.util.List;

/**
 * Created by Anton on 22.11.2017.
 */

public class CategoryGifts extends TypeData{
    private final String name;
    private final List<MyData> list;

    public CategoryGifts(String name, List<MyData> list){
        super(TypeData.CATEGORY);
        this.name = name;
        this.list = list;
    }

    public String getName(){
        return name;
    }

    public void addAll(List<MyData> list){
        this.list.addAll(list);
    }

    public List<MyData> getAll(){
        return list;
    }

    public MyData get(int index){
        return list.get(index);
    }
}
