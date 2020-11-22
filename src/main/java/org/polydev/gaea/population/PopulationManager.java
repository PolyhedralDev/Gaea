package org.polydev.gaea.population;

import org.polydev.gaea.util.FastRandom;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.Gaea;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.profiler.WorldProfiler;
import org.polydev.gaea.util.GlueList;
import org.polydev.gaea.util.SerializationUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static io.papermc.lib.PaperLib.getChunkAtAsync;

public class PopulationManager extends BlockPopulator {
    private final List<GaeaBlockPopulator> attachedPopulators = new GlueList<>();
    private final HashSet<ChunkCoordinate> needsPop = new HashSet<>();
    private final JavaPlugin main;
    private final Object popLock = new Object();
    private WorldProfiler profiler;

    public PopulationManager(JavaPlugin main) {
        this.main = main;
    }

    public void attach(GaeaBlockPopulator populator) {
        this.attachedPopulators.add(populator);
    }

    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        try(ProfileFuture ignored = measure()) {
            needsPop.add(new ChunkCoordinate(chunk));
            int x = chunk.getX();
            int z = chunk.getZ();
            GlueList<CompletableFuture<Chunk>> chunks = new GlueList<>();
            if(main.isEnabled()) {
                for(int xi = - 1; xi <= 1; xi++) {
                    for(int zi = - 1; zi <= 1; zi++) {
                        if(xi == 0 && zi == 0) continue;
                        if(world.isChunkGenerated(xi + x, zi + z)) checkNeighbors(xi + x, zi + z, world);
                    }
                }
            }
        }
    }

    private ProfileFuture measure() {
        if(profiler != null) return profiler.measure("PopulationManagerTime");
        return null;
    }

    public void attachProfiler(WorldProfiler p) {
        this.profiler = p;
    }

    @SuppressWarnings("unchecked")
    public synchronized void saveBlocks(World w) throws IOException {
        File f = new File(Gaea.getGaeaFolder(w), "chunks.bin");
        f.createNewFile();
        SerializationUtil.toFile((HashSet<ChunkCoordinate>) needsPop.clone(), f);
    }

    @SuppressWarnings("unchecked")
    public synchronized void loadBlocks(World w) throws IOException, ClassNotFoundException {
        File f = new File(Gaea.getGaeaFolder(w), "chunks.bin");
        needsPop.addAll((HashSet<ChunkCoordinate>) SerializationUtil.fromFile(f));
    }


    // Synchronize to prevent chunks from being queued for population multiple times.
    public synchronized void checkNeighbors(int x, int z, World w) {
        ChunkCoordinate c = new ChunkCoordinate(x, z, w.getUID());
        if(w.isChunkGenerated(x + 1, z)
                && w.isChunkGenerated(x - 1, z)
                && w.isChunkGenerated(x, z + 1)
                && w.isChunkGenerated(x, z - 1) && needsPop.contains(c)) {
            CompletableFuture<Chunk> chunk = getChunkAtAsync(w, z, z, false);
            Random random = new FastRandom(w.getSeed());
            long xRand = (random.nextLong() / 2L << 1L) + 1L;
            long zRand = (random.nextLong() / 2L << 1L) + 1L;
            random.setSeed((long) x * xRand + (long) z * zRand ^ w.getSeed());
            for(GaeaBlockPopulator r : attachedPopulators) {
                r.populate(w, random, chunk, x, z);
            }
            needsPop.remove(c);
        }
    }
}