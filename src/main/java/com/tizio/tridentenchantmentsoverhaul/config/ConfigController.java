package com.tizio.tridentenchantmentsoverhaul.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ConfigController {

    ArrayList<String> list;
    Set<String> set;

    public ConfigController(){

        this.list = new ArrayList<>();
        this.set = new HashSet<>();

    }

    private void addListSet(String desc, String id, Object value){
        this.list.add(desc);
        this.list.add(id);
        this.list.add(value.toString());
        this.set.add(id);
    }

    public Boolean addBoolean(String desc, String id, Boolean value){
        addListSet(desc, id, value);
        return value;
    }

    public Float addFloat(String desc, String id, Float value){
        addListSet(desc, id, value);
        return value;
    }

    public Integer addInteger(String desc, String id, Integer value){
        addListSet(desc, id, value);
        return value;
    }

}