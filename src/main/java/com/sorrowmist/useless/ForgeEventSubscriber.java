package com.sorrowmist.useless;

import com.sorrowmist.useless.command.UpgradeTestCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// 在 UselessMod.java 中添加
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventSubscriber {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        UpgradeTestCommand.register(event.getDispatcher());
    }
}
