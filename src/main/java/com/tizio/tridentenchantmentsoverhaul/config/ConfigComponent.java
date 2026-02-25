package com.tizio.tridentenchantmentsoverhaul.config;

public class ConfigComponent {

    public String desc;
    public String id;
    public String value;

    public ConfigComponent(String comment, String id, String value){
        this.desc=comment;
        this.id=id;
        this.value=value;
    }

}