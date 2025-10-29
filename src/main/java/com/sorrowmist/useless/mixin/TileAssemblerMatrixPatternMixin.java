package com.sorrowmist.useless.mixin;

import com.glodblock.github.extendedae.common.tileentities.matrix.TileAssemblerMatrixPattern;
import com.sorrowmist.useless.config.ConfigManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(TileAssemblerMatrixPattern.class)
public class TileAssemblerMatrixPatternMixin {

    // 允许修改静态字段
    @Final
    @Mutable
    @Shadow(remap = false)
    public static int INV_SIZE;

    // 使用 @ModifyConstant 来修改 INV_SIZE
    @ModifyConstant(
        method = "<init>(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", // 构造函数签名
        constant = @Constant(intValue = 36) // 查找 INV_SIZE 被初始化为 36 的地方
    )
    private static int modifyINV_SIZE(int originalValue) {
        // 将 INV_SIZE 修改为你需要的值
        int a = ConfigManager.getMatrixPatternCount();
        return INV_SIZE*a; // 返回修改后的值
    }
}
