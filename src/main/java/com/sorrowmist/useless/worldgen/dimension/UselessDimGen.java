// UselessDimGen.java
package com.sorrowmist.useless.worldgen.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import com.sorrowmist.useless.Config;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class UselessDimGen extends ChunkGenerator {

    // 注册Codec用于数据序列化
    public static final Codec<UselessDimGen> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource)
            ).apply(instance, UselessDimGen::new)
    );

    private final Config config;

    public UselessDimGen(BiomeSource biomeSource) {
        super(biomeSource);
        // 加载配置 - 这里需要传入config目录路径
        this.config = Config.load(getConfigPath());
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
        // 超平坦世界不生成原始怪物
    }


    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState randomState) {
        BlockState[] states = new BlockState[level.getHeight()];

        for (int y = level.getMinBuildHeight(); y < level.getMaxBuildHeight(); y++) {
            int index = y - level.getMinBuildHeight();

            if (y == -64) {
                states[index] = Blocks.BEDROCK.defaultBlockState();
            } else if (y >= -63 && y <= 5) {
                states[index] = config.getFillBlock().defaultBlockState();
            } else {
                states[index] = Blocks.AIR.defaultBlockState();
            }
        }

        return new NoiseColumn(level.getMinBuildHeight(), states);
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structureManager, RandomState randomState, ChunkAccess chunk) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        Heightmap oceanFloorMap = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap surfaceMap = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // 设置基岩底层 (Y=-64)
                chunk.setBlockState(pos.set(x, -64, z), Blocks.BEDROCK.defaultBlockState(), false);

                // 生成69层塑料平台 (Y=-63 到 Y=5)
                for (int y = -63; y <= 5; y++) {
                    BlockState blockState;

                    // 判断是否在中心2x2区域内 (坐标7-8)
                    if (x >= 7 && x <= 8 && z >= 7 && z <= 8) {
                        blockState = config.getCenterBlock().defaultBlockState();
                    }
                    // 判断是否在中心14x14区域内
                    else if (x >= 1 && x <= 14 && z >= 1 && z <= 14) {
                        blockState = config.getFillBlock().defaultBlockState();
                    } else {
                        blockState = config.getBorderBlock().defaultBlockState();
                    }

                    chunk.setBlockState(pos.set(x, y, z), blockState, false);
                }

                // Y=6及以上设置为空气
                for (int y = 6; y < 320; y++) {
                    chunk.setBlockState(pos.set(x, y, z), Blocks.AIR.defaultBlockState(), false);
                }

                surfaceMap.update(x, z, 6, Blocks.AIR.defaultBlockState());
                oceanFloorMap.update(x, z, 6, Blocks.AIR.defaultBlockState());
            }
        }
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types heightmap, LevelHeightAccessor level, RandomState randomState) {
        return 70;
    }

    @Override
    public int getMinY() {
        return -64;
    }

    @Override
    public int getGenDepth() {
        return 384;
    }

    @Override
    public void applyCarvers(WorldGenRegion chunkRegion, long seed, RandomState randomState, BiomeManager biomeAccess, StructureManager structureManager, ChunkAccess chunk, GenerationStep.Carving generationStep) {
        // 不需要洞穴雕刻
    }

    @Override
    public void addDebugScreenInfo(java.util.List<String> list, RandomState randomState, BlockPos pos) {
        list.add("Useless Dimension - Plastic Platform");
        list.add("边框方块: " + config.getBorderBlock());
        list.add("填充方块: " + config.getFillBlock());
        list.add("中心方块: " + config.getCenterBlock());
    }

    // 获取配置路径的方法（需要根据你的mod加载器实现）
    private java.nio.file.Path getConfigPath() {
        // 对于Fabric，使用：
        // return net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir();

        // 对于Forge，使用：
         return net.minecraftforge.fml.loading.FMLPaths.CONFIGDIR.get();

        // 这里使用临时实现，你需要根据你的mod加载器调整
    }
}