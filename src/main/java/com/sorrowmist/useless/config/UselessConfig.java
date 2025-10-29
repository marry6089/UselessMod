package com.sorrowmist.useless.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class UselessConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // 维度生成配置
    public static final ForgeConfigSpec.ConfigValue<String> BORDER_BLOCK;
    public static final ForgeConfigSpec.ConfigValue<String> FILL_BLOCK;
    public static final ForgeConfigSpec.ConfigValue<String> CENTER_BLOCK;

    // 植物盆生长速度配置
    public static final ForgeConfigSpec.DoubleValue BOTANY_POT_GROWTH_MULTIPLIER;

    // 矩阵数量配置
    public static final ForgeConfigSpec.IntValue MATRIX_PATTERN_COUNT;

    static {
        BUILDER.push("维度生成设置");

        BORDER_BLOCK = BUILDER
                .comment("边框方块，若不存在则使用蓝色羊毛")
                .define("border_block", "useless_mod:aqua_glow_plastic");

        FILL_BLOCK = BUILDER
                .comment("填充方块，若不存在则使用白色羊毛")
                .define("fill_block", "useless_mod:white_glow_plastic");

        CENTER_BLOCK = BUILDER
                .comment("中心方块，若不存在则使用灰色羊毛")
                .define("center_block", "useless_mod:light_gray_glow_plastic");

        BUILDER.pop();

        BUILDER.push("游戏机制设置");

        BOTANY_POT_GROWTH_MULTIPLIER = BUILDER
                .comment("植物盆生长倍率 - 1.0为原版速度，2.0为2倍速度，0.5为半速")
                .defineInRange("botany_pot_growth_multiplier", 1.0, 0.1, 2147483647);

        // 矩阵样板数量配置
        MATRIX_PATTERN_COUNT = BUILDER
                .comment("矩阵样板槽位-倍数")
                .defineInRange("matrix_pattern_count", 1, 1, 100); // 默认10，最小1，最大100


        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
