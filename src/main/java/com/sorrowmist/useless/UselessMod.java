package com.sorrowmist.useless;

import com.mojang.logging.LogUtils;
import com.sorrowmist.useless.blocks.*;
import com.sorrowmist.useless.config.ConfigManager;
import com.sorrowmist.useless.inventories.UselessTab;
import com.sorrowmist.useless.items.EndlessBeafItem;
import com.sorrowmist.useless.networking.ModMessages;
import com.sorrowmist.useless.worldgen.dimension.UselessDimension;
import com.sorrowmist.useless.worldgen.dimension.UselessDimension2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixins;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(UselessMod.MOD_ID)
public class UselessMod {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "useless_mod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public UselessMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 初始化所有内容
        initAll(modEventBus);

        // 注册配置
        registerConfig();

        // 注册网络消息
        ModMessages.register();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // 注册 Mixin 配置
        Mixins.addConfiguration("useless_mod.mixins.json");
    }

    /**
     * 注册配置
     */
    private void registerConfig() {
        // 注册通用配置
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigManager.SPEC, "useless_mod-common.toml");
        LOGGER.info("已注册 TOML 配置文件");
    }

    /**
     * 初始化所有注册项
     */
    public void initAll(IEventBus iEventBus) {
        EndlessBeafItem.init(iEventBus);
        UselessTab.init(iEventBus);
        TeleportBlock.init(iEventBus);
        UselessDimension.init(iEventBus);
        OreGeneratorBlock.init(iEventBus);
        UselessDimension2.init(iEventBus);
        TeleportBlock2.init(iEventBus);
        GlowPlasticBlock.init(iEventBus);
    }

    /**
     * 通用设置
     */
    private void commonSetup(final FMLCommonSetupEvent event) {
        // 在通用设置中记录配置信息
        LOGGER.info("植物盆生长倍率: {}", ConfigManager.getBotanyPotGrowthMultiplier());
        LOGGER.info("边框方块: {}", ConfigManager.getBorderBlock());
        LOGGER.info("填充方块: {}", ConfigManager.getFillBlock());
        LOGGER.info("中心方块: {}", ConfigManager.getCenterBlock());
    }

    /**
     * 添加创造模式标签内容
     */
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // 这里可以添加物品到创造模式标签
    }

    /**
     * 服务器启动事件
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // 服务器启动时执行的操作
        LOGGER.info("HELLO from server starting");
        LOGGER.info("服务器配置 - 植物盆生长倍率: {}", ConfigManager.getBotanyPotGrowthMultiplier());
    }

    /**
     * 客户端事件订阅器
     */
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // 客户端设置
            LOGGER.info("客户端配置 - 植物盆生长倍率: {}", ConfigManager.getBotanyPotGrowthMultiplier());
        }
    }
}