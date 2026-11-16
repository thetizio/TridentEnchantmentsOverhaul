package com.tizio.tridentenchantmentsoverhaul.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

public class Config {

    public static final Logger LOGGER = LoggerFactory.getLogger("tridentenchantmentsoverhaul");

    public static ConfigController control = new ConfigController();

    public static Boolean impalingPierce = control.addBoolean(
            "Should impaling make the trident pierce trough mobs?",
            "impalingPierce",
            true
    );

    public static Boolean loyaltyInventory = control.addBoolean(
            "Should loyalty make tridents remain in inventory after throwing?",
            "loyaltyInventory",
            true
    );

    public static Float loyaltyThrow = control.addFloat(
            "How much should loyalty boost trident throw strength each level? (0=vanilla)\n" +
                    "Float, should likely be [-1.0,1.0]",
            "loyaltyThrow",
            0.0f
    );

    public static Float loyaltyInaccuracyDecrease = control.addFloat(
            "How much should loyalty boost trident throw accuracy each level? (0=vanilla)\n" +
                    "Default vanilla inaccuracy is 1, values <= 0 mean perfect accuracy\n" +
                    "Example with 0.3 and loyalty 3 -> 1 - 0.3 * 3 = 0.1 total inaccuracy left\n" +
                    "Example with -0.2 and loyalty 3 -> 1 - (-0.2 * 3) = 1.6 total inaccuracy left (more than vanilla)\n" +
                    "Float, should likely be [-1.0,1.0]",
            "loyaltyInaccuracyDecrease",
            0.0f
    );

    public static Boolean riptideSlowFalling = control.addBoolean(
            "Should riptide give you slow falling after using it?",
            "riptideSlowFalling",
            true
    );

    public static Integer riptideDuration = control.addInteger(
            "How long should slow falling from riptide last in seconds? (irrelevant if riptideSlowFalling=false)\n" +
                    "Integer, should likely be [1,60]",
            "riptideDuration",
            5
    );

    public static Boolean riptideUniversal = control.addBoolean(
            "Should players be able to use riptide outside water?",
            "riptideUniversal",
            true
    );

    public static void modifyConfig(String name, String value) {

        Object x;
        try {
            switch(Config.class.getDeclaredField(name).getType().toString().replace("class java.lang.","")){
                case "Boolean": x = Boolean.parseBoolean(value); break;
                case "Float": x = Float.parseFloat(value); break;
                case "Integer": x = Integer.parseInt(value); break;
                default: throw new Exception();
            }
        } catch (Exception e){
            LOGGER.info("WARNING: config value of "+name+" is not valid, keeping default");
            return;
        }

        try {
            Field field = Config.class.getDeclaredField(name);
            field.set(null, x);
        }
        catch (Exception e) {
            LOGGER.info("Failed to modify config value of "+name);
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
                Iterator<String> iterator = control.list.iterator();
                while(iterator.hasNext()){
                    writer.write("#"+iterator.next().replace("\n","\n#")+"\n"+
                            iterator.next()+" = "+iterator.next()+"\n");
                }
            }catch(Exception e){
                LOGGER.info("something went wrong while writing file "+e.getMessage());
            }
        }

        try(BufferedReader reader = new BufferedReader(new FileReader("config/tridentenchantmentsoverhaul.toml"));){
            String line, part1, part2; String[] parts;
            while((line=reader.readLine())!=null){
                parts = line.split("=");
                if(line.charAt(0)!='#'){
                    part1 = parts[0].trim();
                    part2 = parts[1].trim();
                    LOGGER.info("read config "+part1+"="+part2);
                    if (control.set.contains(part1)){
                        modifyConfig(part1,part2);
                    }
                }
            }
        }catch(Exception e){
            LOGGER.info("something went wrong while reading config "+e.getMessage());
        }

        LOGGER.info("FINAL CONFIG");
        int current = -2;
        int size = control.list.size();
        while((current+=3)<size){
            try{
                LOGGER.info(control.list.get(current)+" = "+Config.class.getDeclaredField(control.list.get(current)).get(null));
            }catch(Exception e){
                LOGGER.info("something went wrong while reading final configs");
            }
        }

        control.set.clear();
        control.list.clear();

    }

}