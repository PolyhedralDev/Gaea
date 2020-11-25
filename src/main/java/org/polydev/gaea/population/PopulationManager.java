package org.polydev.gaea.population;

import net.jafama.FastMath;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.Gaea;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.profiler.WorldProfiler;
import org.polydev.gaea.util.FastRandom;
import org.polydev.gaea.util.GlueList;
import org.polydev.gaea.util.SerializationUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PopulationManager extends BlockPopulator {
    private final List<GaeaBlockPopulator> attachedPopulators = new GlueList<>();
    private final List<AsyncGaeaBlockPopulator> attachedAsyncPopulators = new GlueList<>();
    private final HashSet<ChunkCoordinate> needsPop = new HashSet<>();
    private final GlueList<HashSet<ChunkCoordinate>> needsAsyncPop = new GlueList<>();
    private final GlueList<HashSet<ChunkCoordinate>> doingAsyncPop = new GlueList<>();
    private final HashSet<CompletableFuture<AsyncPopulationReturn>> workingAsyncPopulators = new HashSet<>();
    private final JavaPlugin main;
    private final Object popLock = new Object();
    private WorldProfiler profiler;

    public PopulationManager(JavaPlugin main) {
        this.main = main;
    }

    public void attach(GaeaBlockPopulator populator) {
        this.attachedPopulators.add(populator);
    }

    public void attach(AsyncGaeaBlockPopulator populator) {
        this.attachedAsyncPopulators.add(populator);
        this.needsAsyncPop.add(new HashSet<>());
    }

    public void asyncPopulate(World world) {
        for (CompletableFuture<AsyncPopulationReturn> c : workingAsyncPopulators) {
            if (c.isDone()) {
                AsyncPopulationReturn data = c.join();
                Integer chunkX = data.getChunkX();
                Integer chunkY = data.getChunkZ();
                ChunkCoordinate chunkCoordinate = new ChunkCoordinate(chunkX, chunkY, data.getWorldID());
                for (int i = 0; i < needsAsyncPop.size(); i++) {
                    if (needsAsyncPop.get(i).contains(chunkCoordinate) && i < data.getPopulatorId()) {
                        continue;
                    }
                }
                HashSet<BlockCoordinate> blockList = data.getChangeList();
                Chunk chunk = null;
                if(world.getUID() != data.getWorldID()) { return; }
                for (BlockCoordinate b: blockList) {
                    if (chunkX == null) {
                        chunkX = FastMath.floorMod(b.getX(), 16);
                        chunkY = FastMath.floorMod(b.getY(), 16);
                    }
                    if(chunk == null) {
                        if(world.isChunkLoaded(chunkX, chunkY)) {
                            continue;
                        }
                        chunk = world.getChunkAt(chunkX, chunkY);
                    }
                    Block block = chunk.getBlock(chunkX, b.getY(), chunkY);
                    block.setBlockData(b.getBlockData(), false);
                }
                if(chunk == null) {
                    needsAsyncPop.get(data.getPopulatorId()).add(chunkCoordinate);
                }
                doingAsyncPop.get(data.getPopulatorId()).remove(chunkCoordinate);
                workingAsyncPopulators.remove(c);
            }
        }
        for (int i = 0; i < needsAsyncPop.size(); i++) {
            for (ChunkCoordinate c: needsAsyncPop.get(i)) {
               if (world.isChunkLoaded(c.getX(), c.getZ())) {
                   Random random = new FastRandom(world.getSeed());
                   long xRand = (random.nextLong() / 2L << 1L) + 1L;
                   long zRand = (random.nextLong() / 2L << 1L) + 1L;
                   random.setSeed((long) c.getX() * xRand + (long) c.getZ() * zRand ^ world.getSeed());
                   Chunk currentChunk = world.getChunkAt(c.getX(), c.getZ());
                   workingAsyncPopulators.add(attachedAsyncPopulators.get(i).populate(world, random, currentChunk, i));
               }
            }
        }
    }

    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        try(ProfileFuture ignored = measure()) {
            needsPop.add(new ChunkCoordinate(chunk));
            for(HashSet<ChunkCoordinate> h : needsAsyncPop) {
                h.add(new ChunkCoordinate(chunk));
            }
            int x = chunk.getX();
            int z = chunk.getZ();
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
        for (int i = 0; i < doingAsyncPop.size(); i++) {
            for (ChunkCoordinate c: doingAsyncPop.get(i)) {
                needsAsyncPop.get(i).add(c);
            }
        }
        f = new File(Gaea.getGaeaFolder(w), "chunksasync.bin");
        f.createNewFile();
        SerializationUtil.toFile((GlueList<HashSet<ChunkCoordinate>>) needsAsyncPop.clone(), f);
    }

    @SuppressWarnings("unchecked")
    public synchronized void loadBlocks(World w) throws IOException, ClassNotFoundException {
        File f = new File(Gaea.getGaeaFolder(w), "chunks.bin");
        needsPop.addAll((HashSet<ChunkCoordinate>) SerializationUtil.fromFile(f));
        f = new File(Gaea.getGaeaFolder(w), "chunksasync.bin");
        needsAsyncPop.addAll((GlueList<HashSet<ChunkCoordinate>>) SerializationUtil.fromFile(f));
    }


    // Synchronize to prevent chunks from being queued for population multiple times.
    public synchronized void checkNeighbors(int x, int z, World w) {
        ChunkCoordinate c = new ChunkCoordinate(x, z, w.getUID());
        if(w.isChunkGenerated(x + 1, z)
                && w.isChunkGenerated(x - 1, z)
                && w.isChunkGenerated(x, z + 1)
                && w.isChunkGenerated(x, z - 1) && needsPop.contains(c)) {
            Random random = new FastRandom(w.getSeed());
            long xRand = (random.nextLong() / 2L << 1L) + 1L;
            long zRand = (random.nextLong() / 2L << 1L) + 1L;
            random.setSeed((long) x * xRand + (long) z * zRand ^ w.getSeed());
            Chunk currentChunk = w.getChunkAt(x, z);
            for (int i = 0; i < attachedPopulators.size(); i++) {
                attachedPopulators.get(i).populate(w, random, currentChunk);
            }
            needsPop.remove(c);
            for (int i = 0; i < needsAsyncPop.size(); i++) {
                needsAsyncPop.get(i).add(c);
            }
        }
    }
}