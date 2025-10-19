package com.sorrowmist.useless;

import com.mojang.logging.LogUtils;
import com.sorrowmist.useless.blocks.GlowPlasticBlock;
import com.sorrowmist.useless.blocks.OreGeneratorBlock;
import com.sorrowmist.useless.blocks.TeleportBlock;
import com.sorrowmist.useless.blocks.TeleportBlock2;
import com.sorrowmist.useless.inventories.UselessTab;
import com.sorrowmist.useless.items.EndlessBeafItem;
import com.sorrowmist.useless.worldgen.dimension.UselessDimension;
import com.sorrowmist.useless.worldgen.dimension.UselessDimension2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixins;

import java.nio.file.Path;
import java.nio.file.Paths;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(UselessMod.MOD_ID)
public class UselessMod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "useless_mod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static Config CONFIG;


    public UselessMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        //IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        initAll(modEventBus);
        initializeConfig();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        Mixins.addConfiguration("useless_mod.mixins.json");

    }

    private void initializeConfig() {
        try {
            // 获取配置目录路径
            Path configDir = Paths.get("config");
            // 加载或创建配置文件
            CONFIG = Config.load(configDir);
            LOGGER.info("配置文件已加载: {}", configDir.resolve("uselessdim_config.json"));
        } catch (Exception e) {
            LOGGER.error("加载配置文件时出错", e);
        }
    }
    public void initAll(IEventBus iEventBus){
        EndlessBeafItem.init(iEventBus);
        UselessTab.init(iEventBus);
        TeleportBlock.init(iEventBus);
        UselessDimension.init(iEventBus);
        OreGeneratorBlock.init(iEventBus);
        UselessDimension2.init(iEventBus);
        TeleportBlock2.init(iEventBus);
        GlowPlasticBlock.init(iEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {}

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }



    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {}
    }
}
