package com.sorrowmist.useless.mixin;

import com.sorrowmist.useless.config.ConfigManager;
import com.sorrowmist.useless.mixin.accessor.BlockEntityBotanyPotAccessor;
import net.darkhax.botanypots.BotanyPotHelper;
import net.darkhax.botanypots.block.BlockEntityBotanyPot;
import net.darkhax.botanypots.block.inv.BotanyPotContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@Mixin(BlockEntityBotanyPot.class)
public abstract class BlockEntityBotanyPotMixin {

    @Inject(
            method = "tickPot(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/darkhax/botanypots/block/BlockEntityBotanyPot;)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void onTickPot(Level level, BlockPos pos, BlockState state, BlockEntityBotanyPot pot, CallbackInfo ci) {
        // 检查输出栏位是否已满
        if (isOutputFull(pot)) {
            // 输出栏位已满，停止生长
            return;
        }

        float multiplier = ConfigManager.getBotanyPotGrowthMultiplier();
        if (multiplier <= 1.0f) return;
        if (pot.getCrop() == null || pot.getSoil() == null || !pot.areGrowthConditionsMet()) return;

        BotanyPotContainer inv = (BotanyPotContainer) pot.getInventory();
        int requiredTime = inv.getRequiredGrowthTime();
        if (requiredTime <= 0) requiredTime = 1;

        float actualTime = requiredTime / multiplier;
        BlockEntityBotanyPotAccessor accessor = (BlockEntityBotanyPotAccessor) pot;
        Random rng = accessor.getRng();

        if (actualTime >= 1.0f) {
            int growthThisTick = Math.max(1, (int) multiplier);
            for (int i = 0; i < growthThisTick; i++) {
                pot.addGrowth(1);
                if (pot.getGrowthTime() >= requiredTime) {
                    accessor.setDoneGrowing(true);
                    break;
                }
            }
            return;
        }

        // 极高速生长 -> 使用植物盆的自动收获机制
        if (!level.isClientSide) {
            int dropsMultiplier = Math.max(1, (int) Math.ceil(multiplier / requiredTime));

            // 使用植物盆的自动收获机制而不是直接生成掉落物
            for (int i = 0; i < dropsMultiplier; i++) {
                boolean harvested = pot.attemptAutoHarvest();
                if (!harvested) {
                    // 如果自动收获失败（输出栏位可能已满），停止继续尝试
                    break;
                }
            }

            // 重置生长状态
            pot.resetGrowth();
        }

        ci.cancel();
    }

    /**
     * 检查植物盆的输出栏位是否已满
     */
    private static boolean isOutputFull(BlockEntityBotanyPot pot) {
        BotanyPotContainer inv = (BotanyPotContainer) pot.getInventory();

        // 检查所有存储槽位是否都已满
        for (int slot : BotanyPotContainer.STORAGE_SLOT) {
            ItemStack stack = inv.getItem(slot);
            if (stack.isEmpty() || stack.getCount() < stack.getMaxStackSize()) {
                // 至少有一个槽位未满
                return false;
            }
        }

        // 所有槽位都已满
        return true;
    }
}