package com.sorrowmist.useless.mixin.thermal;

import cofh.lib.common.inventory.ItemStorageCoFH;
import com.sorrowmist.useless.interfaces.IItemStorageCoFHAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStorageCoFH.class, remap = false)
public class ItemStorageCoFHMixin implements IItemStorageCoFHAccessor {
    @Unique private int useless_mod$parallel;

    public void useless_mod$setParallel(int count) {
        this.useless_mod$parallel = count;
    }

    public int useless_mod$getParallel() {
        return this.useless_mod$parallel;
    }

    @Inject(at = @At("RETURN"), method = "getSlotLimit", cancellable = true)
    public void modifyItemStorage(int slot, CallbackInfoReturnable<Integer> cir) {
        if (this.useless_mod$parallel > 0) {
            long newLimit = (long) cir.getReturnValueI() * (this.useless_mod$parallel + 1);
            // 防止整数溢出，同时限制最大堆叠数为 2^31-1
            if (newLimit > Integer.MAX_VALUE) {
                newLimit = Integer.MAX_VALUE;
            }
            cir.setReturnValue((int) newLimit);
        }
    }
}