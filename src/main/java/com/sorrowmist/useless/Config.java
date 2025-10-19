package com.sorrowmist.useless;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.registries.BuiltInRegistries;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class Config {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // 在字段名中包含提示信息
    public String 边框方块_若不存在则使用蓝色羊毛 = "useless_mod:aqua_glow_plastic";
    public String 填充方块_若不存在则使用白色羊毛 = "useless_mod:white_glow_plastic";
    public String 中心方块_若不存在则使用灰色羊毛 = "useless_mod:light_gray_glow_plastic";

    public static Config load(Path configPath) {
        File configFile = configPath.resolve("uselessdim_config.json").toFile();

        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                return GSON.fromJson(reader, Config.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 如果配置文件不存在，创建默认配置
        Config config = new Config();
        config.save(configPath);
        return config;
    }

    public void save(Path configPath) {
        File configFile = configPath.resolve("uselessdim_config.json").toFile();

        try {
            if (!configFile.getParentFile().exists()) {
                configFile.getParentFile().mkdirs();
            }

            try (FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获取边框方块
    public Block getBorderBlock() {
        return getBlockFromString(边框方块_若不存在则使用蓝色羊毛, Blocks.BLUE_WOOL);
    }

    // 获取填充方块
    public Block getFillBlock() {
        return getBlockFromString(填充方块_若不存在则使用白色羊毛, Blocks.WHITE_WOOL);
    }

    // 获取中心方块
    public Block getCenterBlock() {
        return getBlockFromString(中心方块_若不存在则使用灰色羊毛, Blocks.GRAY_WOOL);
    }

    private Block getBlockFromString(String blockId, Block fallback) {
        try {
            ResourceLocation location = ResourceLocation.parse(blockId);
            Block block = BuiltInRegistries.BLOCK.get(location);
            return block != Blocks.AIR ? block : fallback;
        } catch (Exception e) {
            return fallback;
        }
    }
}