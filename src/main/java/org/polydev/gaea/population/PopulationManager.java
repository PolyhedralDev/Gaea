package org.polydev.gaea.population;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PopulationManager extends BlockPopulator {
    private final List<GaeaBlockPopulator> attachedPopulators = new ArrayList<>();
    public void attach(GaeaBlockPopulator populator) {
        this.attachedPopulators.add(populator);
    }

    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        long l = System.nanoTime();
        World w = chunk.getWorld();
        int x = chunk.getX();
        int z = chunk.getZ();
        if(w.isChunkGenerated(x+1, z)) checkNeighbors(w.getChunkAt(x+1, z));
        if(w.isChunkGenerated(x-1, z)) checkNeighbors(w.getChunkAt(x-1, z));
        if(w.isChunkGenerated(x, z+1)) checkNeighbors(w.getChunkAt(x, z+1));
        if(w.isChunkGenerated(x, z-1)) checkNeighbors(w.getChunkAt(x, z-1));
        checkNeighbors(chunk);
        //System.out.println("Chunk populated in " + (System.nanoTime()-l)/1000000D + "ms");
    }

    private void checkNeighbors(Chunk c) {
        int x = c.getX();
        int z = c.getZ();
        World w = c.getWorld();
        if(!w.isChunkGenerated(x+1, z)
                || !w.isChunkGenerated(x-1, z)
                || !w.isChunkGenerated(x, z+1)
                || !w.isChunkGenerated(x, z-1)) return;

        Random random = new Random(w.getSeed());
        long xRand = random.nextLong() / 2L * 2L + 1L;
        long zRand = random.nextLong() / 2L * 2L + 1L;
        random.setSeed((long) x * xRand + (long) z * zRand ^ w.getSeed());

        for(GaeaBlockPopulator r : attachedPopulators) {
            r.populate(w, random, c);
        }
    }
}