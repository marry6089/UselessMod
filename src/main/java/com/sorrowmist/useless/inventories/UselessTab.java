package com.sorrowmist.useless.inventories;

import com.sorrowmist.useless.UselessMod;
import com.sorrowmist.useless.blocks.GlowPlasticBlock;
import com.sorrowmist.useless.blocks.OreGeneratorBlock;
import com.sorrowmist.useless.blocks.TeleportBlock;
import com.sorrowmist.useless.blocks.TeleportBlock2;
import com.sorrowmist.useless.items.EndlessBeafItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class UselessTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, UselessMod.MOD_ID);


    public static final RegistryObject<CreativeModeTab> USELESS_TAB =
            CREATIVE_TAB.register("useless_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(EndlessBeafItem.ENDLESS_BEAF_ITEM.get()))
                    .title(Component.literal("无用之物"))
                    .displayItems(((pParameters, pOutput) -> {
                        pOutput.accept(EndlessBeafItem.ENDLESS_BEAF_ITEM.get());
                        pOutput.accept(TeleportBlock.TELEPORT_BLOCK_ITEM.get());
                        pOutput.accept(OreGeneratorBlock.ORE_GENERATOR_BLOCK_ITEM.get());
                        pOutput.accept(TeleportBlock2.TELEPORT_BLOCK_ITEM_2.get());
                        for (RegistryObject<Item> item : GlowPlasticBlock.GLOW_PLASTIC_BLOCK_ITEMS.values()) {
                            pOutput.accept(item.get());
                        }
                    }))
                    .build());
}
