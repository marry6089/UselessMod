package com.sorrowmist.useless.registry;

import cofh.core.util.helpers.AugmentDataHelper;
import cofh.thermal.lib.common.item.AugmentItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ThermalParallelItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "useless_mod");

    public static final RegistryObject<AugmentItem> AUGMENT_PARALLEL_1 = ITEMS.register("augment_parallel_1",
            () -> new AugmentItem(new Item.Properties(), AugmentDataHelper.builder()
                    .type("Machine")
                    .mod("MachineParallel", 1.0F)
                    .build()));

    public static final RegistryObject<AugmentItem> AUGMENT_PARALLEL_2 = ITEMS.register("augment_parallel_2",
            () -> new AugmentItem(new Item.Properties(), AugmentDataHelper.builder()
                    .type("Machine")
                    .mod("MachineParallel", 16.0F)
                    .build()));

    public static final RegistryObject<AugmentItem> AUGMENT_PARALLEL_3 = ITEMS.register("augment_parallel_3",
            () -> new AugmentItem(new Item.Properties(), AugmentDataHelper.builder()
                    .type("Machine")
                    .mod("MachineParallel", 256.0F)
                    .build()));
}