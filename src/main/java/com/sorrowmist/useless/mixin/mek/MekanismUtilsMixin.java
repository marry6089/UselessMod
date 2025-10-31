package com.sorrowmist.useless.mixin.mek;

import mekanism.api.math.FloatingLong;
import mekanism.api.math.MathUtils;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.util.MekanismUtils;
import com.sorrowmist.useless.utils.MekUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * Mixin for MekanismUtils to modify upgrade calculations
 */
@Mixin(value = MekanismUtils.class, remap = false)
public class MekanismUtilsMixin {

    /**
     * @author sorrowmist
     * @reason Overwrite to use custom time calculation with configurable multipliers
     */
    @Overwrite
    public static int getTicks(IUpgradeTile tile, int def) {
        if (tile.supportsUpgrades()) {
            double d = (double)def * MekUtils.time(tile);
            return d >= 1.0 ? MathUtils.clampToInt(d) : MathUtils.clampToInt(1.0 / d) * -1;
        } else {
            return def;
        }
    }

    /**
     * @author sorrowmist
     * @reason Overwrite to use custom energy consumption calculation with configurable multipliers
     */
    @Overwrite
    public static FloatingLong getEnergyPerTick(IUpgradeTile tile, FloatingLong def) {
        return tile.supportsUpgrades() ? def.multiply(MekUtils.electricity(tile)) : def;
    }

    /**
     * @author sorrowmist
     * @reason Overwrite to use custom energy capacity calculation with configurable multipliers
     */
    @Overwrite
    public static FloatingLong getMaxEnergy(IUpgradeTile tile, FloatingLong def) {
        return tile.supportsUpgrades() ? def.multiply(MekUtils.capacity(tile)) : def;
    }
}