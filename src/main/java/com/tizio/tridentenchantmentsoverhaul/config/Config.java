package com.tizio.tridentenchantmentsoverhaul.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Config {

    public static final Logger LOGGER = LoggerFactory.getLogger("tridentenchantmentsoverhaul");

    public static Map<String,String> values = new HashMap<String,String>();

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
        if (s.contains(".")) try{Double.parseDouble(s); return "Double";}catch(Exception ignore){}
        try{Integer.parseInt(s); return "Integer";}catch(Exception ignore){}
        return "Error";
    }

    public static void initialize(){

        ArrayList<ConfigComponent> components = new ArrayList<ConfigComponent>();

        components.add(new ConfigComponent(
                "Should impaling deal increased damage against every entity?",
                "impalingUniversal",
                "true"
        ));
        components.add(new ConfigComponent(
                "How much should impaling increase damage each level?\n" +
                "Should likely be [0.0,100.0]",
                "impalingDamage",
                "1.0"
        ));
        components.add(new ConfigComponent(
                "Should impaling make the trident pierce trough mobs?",
                "impalingPierce",
                "true"
        ));
        components.add(new ConfigComponent(
                "Should loyalty make tridents remain in inventory after throwing?",
                "loyaltyInventory",
                "true"
        ));
        components.add(new ConfigComponent(
                "How much should loyalty boost trident throw strength each level? (0=vanilla)\n" +
                "Should likely be [-1.0,1.0]",
                "loyaltyThrow",
                "0.0"
        ));
        components.add(new ConfigComponent(
                "How much should loyalty boost trident throw accuracy each level? (0=vanilla)\n" +
                "Default vanilla inaccuracy is 1, values <= 0 mean perfect accuracy\n" +
                "Example with 0.3 and loyalty 3 -> 1 - 0.3 * 3 = 0.1 total inaccuracy left\n" +
                "Example with -0.2 and loyalty 3 -> 1 - (-0.2 * 3) = 1.6 total inaccuracy left (more than vanilla)\n" +
                "Should likely be [-1.0,1.0]",
                "loyaltyInaccuracyDecrease",
                "0.0"
        ));
        components.add(new ConfigComponent(
                "Should riptide give you slow falling after using it?",
                "riptideSlowFalling",
                "true"
        ));
        components.add(new ConfigComponent(
                "How long should slow falling from riptide last in seconds? (irrelevant if riptideSlowFalling=false)\n" +
                "Should likely be [1,60]",
                "riptideDuration",
                "5"
        ));
        components.add(new ConfigComponent(
                "Should channeling always summon lightnings when hitting mobs?",
                "channelingUniversal",
                "true"
        ));

        String line; String[] parts;
        for(ConfigComponent c: components){
            values.put(c.id,c.value);
        }

        File test = new File("config/tridentenchantmentsoverhaul.toml");
        if (!test.exists()){
            try(BufferedWriter writer = new BufferedWriter(new FileWriter("config/tridentenchantmentsoverhaul.toml"));){
                for(ConfigComponent c: components){
                    write(writer,c);
                }
            }catch(Exception e){
                LOGGER.info("something went wrong while writing file "+e.getMessage());
            }
        }

        try(BufferedReader reader = new BufferedReader(new FileReader("config/tridentenchantmentsoverhaul.toml"));){
            while((line=reader.readLine())!=null){
                parts = line.split("=");
                if(line.charAt(0)!='#'){
                    LOGGER.info("read config "+parts[0].trim()+"="+parts[1].trim());
                    if (typeFromString(parts[1].trim()).equals(typeFromString(values.get(parts[0].trim())))){
                        values.put(parts[0].trim(),parts[1].trim());
                    } else{
                        LOGGER.info("WARNING: config value of "+parts[0].trim()+" is not valid, keeping default");
                    }
                }
            }
        }catch(Exception e){
            LOGGER.info("something went wrong while reading config "+e.getMessage());
        }

        components.clear();
        LOGGER.info("FINAL CONFIGS");
        for(String s: values.keySet()){
            LOGGER.info(s+"="+values.get(s));
        }

    }

}