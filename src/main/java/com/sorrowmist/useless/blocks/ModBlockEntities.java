package com.sorrowmist.useless.blocks;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import com.sorrowmist.useless.UselessMod;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, UselessMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<OreGeneratorBlockEntity>> ORE_GENERATOR =
            BLOCK_ENTITIES.register("ore_generator",
                    () -> BlockEntityType.Builder.of(OreGeneratorBlockEntity::new,
                            OreGeneratorBlock.ORE_GENERATOR_BLOCK.get()).build(null));
}