package org.polydev.gaea.generation;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.biome.BiomeContainer;
import org.polydev.gaea.math.ChunkInterpolator;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.profiler.WorldProfiler;

import java.util.List;
import java.util.Random;

public abstract class GaeaChunkGenerator extends ChunkGenerator {
    private final ChunkInterpolator.InterpolationType interpolationType;
    private FastNoiseLite gen;
    private ChunkInterpolator interp;
    private WorldProfiler profiler;

    public GaeaChunkGenerator(ChunkInterpolator.InterpolationType type) {
        interpolationType = type;
    }

    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biome) {
        try(ProfileFuture ignore = measure("TotalChunkGenTime")) {
            if(gen == null) {
                gen = new FastNoiseLite((int) world.getSeed());
                gen.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
                gen.setFractalType(FastNoiseLite.FractalType.FBm);
                gen.setFractalOctaves(getNoiseOctaves(world));
                gen.setFrequency(getNoiseFrequency(world));
            }
            ChunkData chunk;
            try(ProfileFuture ignored = measure("ChunkBaseGenTime")) {
                interp = interpolationType.getInstance(world, chunkX, chunkZ, this.getContainer(world), gen);
                chunk = generateBase(world, random, chunkX, chunkZ, gen);
            }
            try(ProfileFuture ignored = measure("PaletteApplyTime")) {
                BiomeContainer container = getContainer(world);
                int xOrig = (chunkX << 4);
                int zOrig = (chunkZ << 4);
                for(byte x = 0; x < 4; x++) {
                    for(byte z = 0; z < 4; z++) {
                        int cx = xOrig + x * 4;
                        int cz = zOrig + z * 4;
                        Biome b = container.getBiome(cx, cz, GenerationPhase.PALETTE_APPLY);
                        biome.setBiome(x * 4, z * 4, b.getVanillaBiome());
                    }
                }
            }
            for(GenerationPopulator g : getGenerationPopulators(world)) {
                chunk = g.populate(world, chunk, random, chunkX, chunkZ, interp);
            }
            return chunk;
        }
    }

    public double getInterpolatedNoise(double x, double z) {
        return this.interp.getNoise(x, z);
    }

    public double getInterpolatedNoise(double x, double y, double z) {
        return this.interp.getNoise(x, y, z);
    }

    public void attachProfiler(WorldProfiler p) {
        this.profiler = p;
    }

    private ProfileFuture measure(String id) {
        if(profiler != null) return profiler.measure(id);
        return null;
    }

    public abstract ChunkData generateBase(@NotNull World world, @NotNull Random random, int x, int z, FastNoiseLite noise);

    public abstract int getNoiseOctaves(World w);

    public abstract float getNoiseFrequency(World w);

    public abstract List<GenerationPopulator> getGenerationPopulators(World w);

    public abstract BiomeContainer getContainer(World w);

    public FastNoiseLite getNoiseGenerator() {
        return gen;
    }
}
