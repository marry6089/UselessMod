package com.sorrowmist.useless.items;

import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.lib.common.item.AugmentItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UpgradeAugmentItem extends AugmentItem {
    public UpgradeAugmentItem(Item.Properties builder, CompoundTag augmentData) {
        super(builder, augmentData);
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(1, StringHelper.getTextComponent("useless_mod.augment.upgrade").withStyle(ChatFormatting.GOLD));
    }
}