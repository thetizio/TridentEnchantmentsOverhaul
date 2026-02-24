package com.tizio.tridentenchantmentsoverhaul.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ConfigController {

    ArrayList<ConfigComponent> list;
    Set<String> set;

    public ConfigController(){

        this.list = new ArrayList<>();
        this.set = new HashSet<>();

    }

    public Boolean addBoolean(ConfigComponent c){
        this.list.add(c);
        this.set.add(c.id);
        return Boolean.parseBoolean(c.value);
    }

    public Float addFloat(ConfigComponent c){
        this.list.add(c);
        this.set.add(c.id);
        return Float.parseFloat(c.value);
    }

    public Integer addInteger(ConfigComponent c){
        this.list.add(c);
        this.set.add(c.id);
        return Integer.parseInt(c.value);
    }

}