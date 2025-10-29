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

    // 矩阵样板数量配置
    public static final ForgeConfigSpec.IntValue MATRIX_PATTERN_COUNT;

    static {
        // 初始化配置
        BUILDER.push("维度生成设置");

        BORDER_BLOCK = BUILDER
                .comment("边框方块，若不存在则使用蓝色羊毛")
                .define("边框方块", "useless_mod:aqua_glow_plastic");

        FILL_BLOCK = BUILDER
                .comment("填充方块，若不存在则使用白色羊毛")
                .define("填充方块", "useless_mod:white_glow_plastic");

        CENTER_BLOCK = BUILDER
                .comment("中心方块，若不存在则使用灰色羊毛")
                .define("中心方块", "useless_mod:light_gray_glow_plastic");

        BUILDER.pop();

        BUILDER.push("游戏机制设置");

        BOTANY_POT_GROWTH_MULTIPLIER = BUILDER
                .comment("1.0为原版速度，2.0为2倍速度，0.5为半速")
                .defineInRange("植物盆生长倍率", 1.0, 0.1, 2147483647);

        // 新增矩阵样板数量配置
        MATRIX_PATTERN_COUNT = BUILDER
                .comment("减少数量时请保持槽位空！否则可能会造成样板丢失")
                .defineInRange("矩阵样板核心槽位倍率", 1, 1, 100); // 默认10，最小1，最大100

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

    // 获取矩阵样板数量
    public static int getMatrixPatternCount() {
        return MATRIX_PATTERN_COUNT.get();
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
