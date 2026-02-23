package com.tizio.tridentenchantmentsoverhaul;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TridentEnchantmentsOverhaul.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue IMPALING_UNIVERSAL = BUILDER
            .comment("Should impaling deal increased damage against every entity?")
            .define("impalingUniversal", true);

    public static final ForgeConfigSpec.DoubleValue IMPALING_DAMAGE = BUILDER
            .comment("How much should impaling increase damage each level?")
            .defineInRange("impalingDamage",1.0,0.1,100);

    public static final ForgeConfigSpec.BooleanValue IMPALING_PIERCE = BUILDER
            .comment("Should impaling make the trident pierce trough mobs?")
            .define("impalingPierce", true);

    public static final ForgeConfigSpec.BooleanValue LOYALTY_LOYAL = BUILDER
            .comment("Should loyalty make tridents remain in inventory after throwing?")
            .define("loyaltyInventory", true);

    public static final ForgeConfigSpec.DoubleValue LOYALTY_THROW = BUILDER
            .comment("How much should loyalty boost trident throw strength each level? (0=vanilla)")
            .defineInRange("loyaltyThrow", 0.0, -1.0, 1.0);

    public static final ForgeConfigSpec.DoubleValue LOYALTY_INACCURACY_DECREASE = BUILDER
            .comment("How much should loyalty boost trident throw accuracy each level? (0=vanilla)")
            .comment("Default vanilla inaccuracy is 1, values <= 0 mean perfect accuracy")
            .comment("Example with 0.3 and loyalty 3 -> 1 - 0.3 * 3 = 0.1 total inaccuracy left")
            .comment("Example with -0.2 and loyalty 3 -> 1 - (-0.2 * 3) = 1.6 total inaccuracy left (more than vanilla)")
            .defineInRange("loyaltyInaccuracyDecrease", 0.0, -1.0, 1.0);

    public static final ForgeConfigSpec.BooleanValue RIPTIDE_SLOW_FALL = BUILDER
            .comment("Should riptide give you slow falling after using it?")
            .define("riptideSlowFalling", true);

    public static final ForgeConfigSpec.IntValue RIPTIDE_DURATION = BUILDER
            .comment("How long should slow falling from riptide last in seconds? (irrelevant if riptideSlowFalling=false)")
            .defineInRange("riptideDuration",5,1,60);

    public static final ForgeConfigSpec.BooleanValue CHANNELING_UNIVERSAL = BUILDER
            .comment("Should channeling always summon lightnings when hitting mobs?")
            .define("channelingUniversal", true);

    static final ForgeConfigSpec SPEC = BUILDER.build();

}