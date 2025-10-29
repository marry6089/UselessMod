// 文件: RegistryHandler.java
package com.sorrowmist.useless.registry;

import com.sorrowmist.useless.UselessMod;
import com.sorrowmist.useless.blocks.*;
import com.sorrowmist.useless.inventories.UselessTab;
import com.sorrowmist.useless.items.EndlessBeafItem;
import com.sorrowmist.useless.worldgen.dimension.UselessDimension;
import com.sorrowmist.useless.worldgen.dimension.UselessDimension2;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class RegistryHandler {
    // 存储所有需要注册的DeferredRegister
    private static final List<DeferredRegister<?>> REGISTRIES = new ArrayList<>();

    /**
     * 初始化所有注册
     */
    public static void initAll(IEventBus modEventBus) {
        // 收集所有需要注册的DeferredRegister
        collectAllRegistries();

        // 注册所有DeferredRegister
        for (DeferredRegister<?> registry : REGISTRIES) {
            registry.register(modEventBus);
        }

        UselessMod.LOGGER.info("已注册 {} 个注册器", REGISTRIES.size());
    }

    /**
     * 收集所有需要注册的DeferredRegister
     */
    private static void collectAllRegistries() {
        // 物品注册
        addRegistry(EndlessBeafItem.ITEMS);

        // 创造模式标签注册
        addRegistry(UselessTab.CREATIVE_TAB);

        // 方块和物品注册
        addRegistry(TeleportBlock.BLOCKS);
        addRegistry(TeleportBlock.ITEMS);
        addRegistry(TeleportBlock2.BLOCKS);
        addRegistry(TeleportBlock2.ITEMS);
        addRegistry(OreGeneratorBlock.BLOCKS);
        addRegistry(OreGeneratorBlock.ITEMS);
        addRegistry(GlowPlasticBlock.BLOCKS);
        addRegistry(GlowPlasticBlock.ITEMS);

        // 方块实体注册
        addRegistry(ModBlockEntities.BLOCK_ENTITIES);

        // 维度注册
        addRegistry(UselessDimension.CHUNK_GENERATORS);
        addRegistry(UselessDimension2.CHUNK_GENERATORS);
    }

    /**
     * 安全地添加注册器到列表
     */
    private static <T> void addRegistry(DeferredRegister<T> registry) {
        if (registry != null) {
            REGISTRIES.add(registry);
        }
    }

    /**
     * 获取注册器数量（用于调试）
     */
    public static int getRegistryCount() {
        return REGISTRIES.size();
    }
}