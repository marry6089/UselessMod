package com.sorrowmist.useless.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ConfigManager {
    // 配置构建器和规范
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // 维度生成配置
    public static final ForgeConfigSpec.ConfigValue<String> BORDER_BLOCK;
    public static final ForgeConfigSpec.ConfigValue<String> FILL_BLOCK;
    public static final ForgeConfigSpec.ConfigValue<String> CENTER_BLOCK;

    // 植物盆生长速度配置
    public static final ForgeConfigSpec.DoubleValue BOTANY_POT_GROWTH_MULTIPLIER;

    static {
        // 初始化配置
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

        BUILDER.pop();

        // 构建配置规范
        SPEC = BUILDER.build();
    }

    // 获取边框方块
    public static Block getBorderBlock() {
        return getBlockFromString(BORDER_BLOCK.get(), Blocks.BLUE_WOOL);
    }

    // 获取填充方块
    public static Block getFillBlock() {
        return getBlockFromString(FILL_BLOCK.get(), Blocks.WHITE_WOOL);
    }

    // 获取中心方块
    public static Block getCenterBlock() {
        return getBlockFromString(CENTER_BLOCK.get(), Blocks.GRAY_WOOL);
    }

    // 获取植物盆生长倍率
    public static float getBotanyPotGrowthMultiplier() {
        return BOTANY_POT_GROWTH_MULTIPLIER.get().floatValue();
    }

    private static Block getBlockFromString(String blockId, Block fallback) {
        try {
            ResourceLocation location = ResourceLocation.parse(blockId);
            Block block = BuiltInRegistries.BLOCK.get(location);
            return block != Blocks.AIR ? block : fallback;
        } catch (Exception e) {
            return fallback;
        }
    }
}