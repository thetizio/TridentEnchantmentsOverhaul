package com.tizio.tridentenchantmentsoverhaul.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {

    public static final Logger LOGGER = LoggerFactory.getLogger("tridentenchantmentsoverhaul");

    public static ConfigController control = new ConfigController();

    public static Boolean impalingUniversal = control.addBoolean(new ConfigComponent(
            "Should impaling deal increased damage against every entity?",
            "impalingUniversal",
            "true"
    ));

    public static Float impalingDamage = control.addFloat(new ConfigComponent(
            "How much should impaling increase damage each level?\n" +
            "Float, should likely be [0.0,100.0]",
            "impalingDamage",
            "1.0"
    ));

    public static Boolean impalingPierce = control.addBoolean(new ConfigComponent(
            "Should impaling make the trident pierce trough mobs?",
            "impalingPierce",
            "true"
    ));

    public static Boolean loyaltyInventory = control.addBoolean(new ConfigComponent(
            "Should loyalty make tridents remain in inventory after throwing?",
            "loyaltyInventory",
            "true"
    ));

    public static Float loyaltyThrow = control.addFloat(new ConfigComponent(
            "How much should loyalty boost trident throw strength each level? (0=vanilla)\n" +
            "Float, should likely be [-1.0,1.0]",
            "loyaltyThrow",
            "0.0"
    ));

    public static Float loyaltyInaccuracyDecrease = control.addFloat(new ConfigComponent(
            "How much should loyalty boost trident throw accuracy each level? (0=vanilla)\n" +
            "Default vanilla inaccuracy is 1, values <= 0 mean perfect accuracy\n" +
            "Example with 0.3 and loyalty 3 -> 1 - 0.3 * 3 = 0.1 total inaccuracy left\n" +
            "Example with -0.2 and loyalty 3 -> 1 - (-0.2 * 3) = 1.6 total inaccuracy left (more than vanilla)\n" +
            "Float, should likely be [-1.0,1.0]",
            "loyaltyInaccuracyDecrease",
            "0.0"
    ));

    public static Boolean riptideSlowFalling = control.addBoolean(new ConfigComponent(
            "Should riptide give you slow falling after using it?",
            "riptideSlowFalling",
            "true"
    ));

    public static Integer riptideDuration = control.addInteger(new ConfigComponent(
            "How long should slow falling from riptide last in seconds? (irrelevant if riptideSlowFalling=false)\n" +
            "Integer, should likely be [1,60]",
            "riptideDuration",
            "5"
    ));

    public static Boolean channelingUniversal = control.addBoolean(new ConfigComponent(
            "Should channeling always summon lightnings when hitting mobs?",
            "channelingUniversal",
            "true"
    ));

    public static void write(BufferedWriter b, ConfigComponent c){
        try{
            b.write("#"+c.desc.replace("\n","\n#")+"\n"+
                    c.id+" = "+c.value+"\n");
        }catch(Exception e){
            LOGGER.info("something went wrong while writing configs "+e.getMessage());
        }
    }

    public static String typeFromString(String s){
        if (s.equals("true") || s.equals("false")) return "Boolean";
        if (s.contains(".")) try{Float.parseFloat(s); return "Float";}catch(Exception ignore){}
        try{Integer.parseInt(s); return "Integer";}catch(Exception ignore){}
        return "Error";
    }

    public static void modifyConfig(String name, Object value) {
        try {
            Field field = Config.class.getDeclaredField(name);
            field.set(null, value);
        }
        catch (Exception e) {
            LOGGER.info("failed to modify config");
        }
    }

    public static void initialize(){

        if (!Files.exists(Path.of("config/tridentenchantmentsoverhaul.toml"))){
            if (!Files.exists(Path.of("config"))){
                try{
                    Files.createDirectory(Path.of("config"));
                }catch(Exception e){
                    LOGGER.info("something went wrong while creating config folder");
                }
            }
            try(BufferedWriter writer = new BufferedWriter(new FileWriter("config/tridentenchantmentsoverhaul.toml"));){
                for(ConfigComponent c: control.list){
                    write(writer,c);
                }
            }catch(Exception e){
                LOGGER.info("something went wrong while writing file "+e.getMessage());
            }
        }

        try(BufferedReader reader = new BufferedReader(new FileReader("config/tridentenchantmentsoverhaul.toml"));){
            String line, part1, part2, t1, t2; String[] parts;
            while((line=reader.readLine())!=null){
                parts = line.split("=");
                if(line.charAt(0)!='#'){
                    part1 = parts[0].trim();
                    part2 = parts[1].trim();
                    LOGGER.info("read config "+part1+"="+part2);
                    t1 = typeFromString(part2);
                    t2 = Config.class.getDeclaredField(part1).getType().toString().replace("class java.lang.","");
                    if (control.set.contains(part1) && t1.equals(t2)){
                        switch(t2){
                            case "Boolean": modifyConfig(part1,Boolean.parseBoolean(part2)); break;
                            case "Float": modifyConfig(part1,Float.parseFloat(part2)); break;
                            case "Integer": modifyConfig(part1,Integer.parseInt(part2)); break;
                        }
                    } else{
                        LOGGER.info("WARNING: config value of "+part1+" is not valid, keeping default");
                    }
                }
            }
        }catch(Exception e){
            LOGGER.info("something went wrong while reading config "+e.getMessage());
        }

        LOGGER.info("FINAL CONFIG");
        for(ConfigComponent c: control.list){
            try{
                LOGGER.info(c.id+" = "+Config.class.getDeclaredField(c.id).get(null));
            }catch(Exception e){
                LOGGER.info("something went wrong while reading final configs");
            }
        }

        control.set.clear();
        control.list.clear();

    }

}