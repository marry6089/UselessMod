package com.sorrowmist.useless.blocks;

import com.sorrowmist.useless.UselessMod;
import com.sorrowmist.useless.api.EnumColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class GlowPlasticBlock extends Block implements IColoredBlock {

    // 在方块类内部直接定义注册器
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, UselessMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UselessMod.MOD_ID);

    // 存储所有颜色的方块和物品
    public static final Map<EnumColor, RegistryObject<GlowPlasticBlock>> GLOW_PLASTIC_BLOCKS = new LinkedHashMap<>();
    public static final Map<EnumColor, RegistryObject<Item>> GLOW_PLASTIC_BLOCK_ITEMS = new LinkedHashMap<>();

    private final EnumColor color;

    public GlowPlasticBlock(EnumColor color, UnaryOperator<Properties> propertyModifier) {
        super(applyLightLevelAdjustments(propertyModifier.apply(Properties.of()
                .mapColor(color.getMapColor())
                .strength(5F, 6F)
                .requiresCorrectToolForDrops())));
        this.color = color;
    }

    @Override
    public EnumColor getColor() {
        return color;
    }

    private static Properties applyLightLevelAdjustments(Properties properties) {
        return properties.lightLevel(state -> 15);
    }

    // 静态初始化块 - 注册所有颜色的方块和物品
    static {
        UnaryOperator<Properties> glowPlasticProperties = properties -> properties;

        // 为每种颜色注册方块和物品
        for (EnumColor color : EnumColor.valuesInOrder()) {
            String registryName = color.getRegistryPrefix() + "_glow_plastic";

            // 注册方块
            RegistryObject<GlowPlasticBlock> block = BLOCKS.register(registryName,
                    () -> new GlowPlasticBlock(color, glowPlasticProperties));
            GLOW_PLASTIC_BLOCKS.put(color, block);

            // 注册对应的物品
            RegistryObject<Item> item = ITEMS.register(registryName,
                    () -> new BlockItem(block.get(), new Item.Properties()));
            GLOW_PLASTIC_BLOCK_ITEMS.put(color, item);
        }
    }

    // 初始化方法
    public static void init(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
}