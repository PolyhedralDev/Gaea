package org.polydev.gaea.population;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.polydev.gaea.Gaea;
import org.polydev.gaea.util.FastRandom;
import org.polydev.gaea.util.GlueList;
import org.polydev.gaea.util.SerializationUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class AsyncPopulationManager {
    private static final List<AsyncGaeaBlockPopulator> attachedAsyncPopulators = new GlueList<>();
    private static final GlueList<ObjectOpenHashSet<ChunkCoordinate>> needsAsyncPop = new GlueList<>();
    private static final GlueList<ObjectOpenHashSet<ChunkCoordinate>> doingAsyncPop = new GlueList<>();
    private static final ObjectOpenHashSet<CompletableFuture<AsyncPopulationReturn>> workingAsyncPopulators = new ObjectOpenHashSet<>();

    public void attach(AsyncGaeaBlockPopulator populator) {
        this.attachedAsyncPopulators.add(populator);
        this.needsAsyncPop.add(new ObjectOpenHashSet<>());
    }

    public synchronized void saveBlocks(World w) throws IOException {
        for (int i = 0; i < doingAsyncPop.size(); i++) {
            for (ChunkCoordinate c: doingAsyncPop.get(i)) {
                needsAsyncPop.get(i).add(c);
            }
        }
        File f = new File(Gaea.getGaeaFolder(w), "chunksasync.bin");
        f.createNewFile();
        SerializationUtil.toFile((GlueList<HashSet<ChunkCoordinate>>) needsAsyncPop.clone(), f);
    }

    @SuppressWarnings("unchecked")
    public synchronized void loadBlocks(World w) throws IOException, ClassNotFoundException {
        File f = new File(Gaea.getGaeaFolder(w), "chunksasync.bin");
        needsAsyncPop.addAll((GlueList<ObjectOpenHashSet<ChunkCoordinate>>) SerializationUtil.fromFile(f));
    }

    public static synchronized void addChunk(ChunkCoordinate c) {
        for (ObjectOpenHashSet<ChunkCoordinate> h: needsAsyncPop) {
            h.add(c);
        }
    }

    public static synchronized void asyncPopulate() {
        for (CompletableFuture<AsyncPopulationReturn> c : workingAsyncPopulators) {
            if (c.isDone()) {
                AsyncPopulationReturn data = c.join();
                int chunkX = data.getChunkX();
                int chunkY = data.getChunkZ();
                ChunkCoordinate chunkCoordinate = new ChunkCoordinate(chunkX, chunkY, data.getWorldID());
                for (int i = 0; i < needsAsyncPop.size(); i++) {
                    if (needsAsyncPop.get(i).contains(chunkCoordinate) && i < data.getPopulatorId()) {
                        continue;
                    }
                }
                HashSet<BlockCoordinate> blockList = data.getChangeList();
                Chunk chunk = null;
                World world = Bukkit.getWorld(data.getWorldID());
                if(world.getUID() != data.getWorldID()) { return; }
                for (BlockCoordinate b: blockList) {
                    if(chunk == null) {
                        if(world.isChunkLoaded(chunkX, chunkY)) {
                            if (workingAsyncPopulators.size() >= 64) {
                                needsAsyncPop.get(data.getPopulatorId()).add(chunkCoordinate);
                                workingAsyncPopulators.remove(c);
                                workingAsyncPopulators.trim();
                            }
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
                World world = Bukkit.getWorld(c.getWorldID());
                if (world.isChunkLoaded(c.getX(), c.getZ()) && workingAsyncPopulators.size() <= 8) {
                    Random random = new FastRandom(world.getSeed());
                    long xRand = (random.nextLong() / 2L << 1L) + 1L;
                    long zRand = (random.nextLong() / 2L << 1L) + 1L;
                    Random chunkRandom = new FastRandom((long) c.getX() * xRand + (long) c.getZ() * zRand ^ world.getSeed());
                    Chunk currentChunk = world.getChunkAt(c.getX(), c.getZ());
                    ChunkSnapshot snapshot = currentChunk.getChunkSnapshot(true, true, false);
                    workingAsyncPopulators.add(attachedAsyncPopulators.get(i).populate(world, chunkRandom, snapshot, i));
                }
            }
        }
    }
}
