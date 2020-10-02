package org.polydev.gaea.population;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.Gaea;
import org.polydev.gaea.util.SerializationUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class PopulationManager extends BlockPopulator {
    private final List<GaeaBlockPopulator> attachedPopulators = new ArrayList<>();
    private final HashSet<ChunkCoordinate> needsPop = new HashSet<>();
    private final JavaPlugin main;
    private final Object popLock = new Object();


    public PopulationManager(JavaPlugin main) {
        this.main = main;
    }

    public void attach(GaeaBlockPopulator populator) {
        this.attachedPopulators.add(populator);
    }

    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        needsPop.add(new ChunkCoordinate(chunk));
        int x = chunk.getX();
        int z = chunk.getZ();
        if(main.isEnabled()) {
            if(world.isChunkGenerated(x + 1, z)) checkNeighbors(x + 1, z, world);
            if(world.isChunkGenerated(x - 1, z)) checkNeighbors(x - 1, z, world);
            if(world.isChunkGenerated(x, z + 1)) checkNeighbors(x, z + 1, world);
            if(world.isChunkGenerated(x, z - 1)) checkNeighbors(x, z - 1, world);
        }
    }

    public void saveBlocks(World w) throws IOException {
        File f = new File(Gaea.getGaeaFolder(w), "chunks.bin");
        f.createNewFile();
        SerializationUtil.toFile(needsPop, f);
    }

    @SuppressWarnings("unchecked")
    public void loadBlocks(World w) throws IOException, ClassNotFoundException {
        File f = new File(Gaea.getGaeaFolder(w), "chunks.bin");
        needsPop.addAll((HashSet<ChunkCoordinate>) SerializationUtil.fromFile(f));
    }


    // Synchronize to prevent chunks from being queued for population multiple times.
    private synchronized void checkNeighbors(int x, int z, World w) {
        Bukkit.getScheduler().runTask(main, () -> {
            ChunkCoordinate c = new ChunkCoordinate(x, z, w.getUID());
            if(w.isChunkGenerated(x + 1, z)
                    && w.isChunkGenerated(x - 1, z)
                    && w.isChunkGenerated(x, z + 1)
                    && w.isChunkGenerated(x, z - 1) && needsPop.contains(c)) {
                needsPop.remove(c);
                Random random = new Random(w.getSeed());
                long xRand = random.nextLong() / 2L * 2L + 1L;
                long zRand = random.nextLong() / 2L * 2L + 1L;
                random.setSeed((long) x * xRand + (long) z * zRand ^ w.getSeed());
                for(GaeaBlockPopulator r : attachedPopulators) {
                    r.populate(w, random, w.getChunkAt(x, z));
                }
            }
        });
    }
}