package com.sorrowmist.useless.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.sorrowmist.useless.config.ConfigManager;
import com.sorrowmist.useless.UselessMod;
import mekanism.api.Upgrade;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class UpgradeTestCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("useless_upgrade_test")
                .requires(source -> source.hasPermission(2))
                .executes(UpgradeTestCommand::execute)
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        // 测试配置值
        int maxUpgrade = ConfigManager.getMaxUpgrade();
        source.sendSuccess(() -> Component.literal("Config Max Upgrade: " + maxUpgrade), false);

        // 测试实际升级限制
        int speedMax = Upgrade.SPEED.getMax();
        int energyMax = Upgrade.ENERGY.getMax();

        source.sendSuccess(() -> Component.literal("Speed Upgrade Max: " + speedMax), false);
        source.sendSuccess(() -> Component.literal("Energy Upgrade Max: " + energyMax), false);

        UselessMod.LOGGER.info("Upgrade Test - Config: {}, Speed: {}, Energy: {}",
                maxUpgrade, speedMax, energyMax);

        return 1;
    }
}