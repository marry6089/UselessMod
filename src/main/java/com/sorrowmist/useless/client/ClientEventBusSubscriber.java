package com.sorrowmist.useless.client;

import com.sorrowmist.useless.UselessMod;
import com.sorrowmist.useless.items.EndlessBeafItem;
import com.sorrowmist.useless.networking.ModMessages;
import com.sorrowmist.useless.networking.EnchantmentSwitchPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UselessMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (KeyBindings.SWITCH_SILK_TOUCH_KEY.consumeClick()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null) {
                ItemStack mainHandItem = minecraft.player.getMainHandItem();
                if (mainHandItem.getItem() instanceof EndlessBeafItem) {
                    ModMessages.sendToServer(new EnchantmentSwitchPacket(true));
                }
            }
        }

        if (KeyBindings.SWITCH_FORTUNE_KEY.consumeClick()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null) {
                ItemStack mainHandItem = minecraft.player.getMainHandItem();
                if (mainHandItem.getItem() instanceof EndlessBeafItem) {
                    ModMessages.sendToServer(new EnchantmentSwitchPacket(false));
                }
            }
        }
    }
}

@Mod.EventBusSubscriber(modid = UselessMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
class ClientModBusSubscriber {
    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.SWITCH_SILK_TOUCH_KEY);
        event.register(KeyBindings.SWITCH_FORTUNE_KEY);
    }
}