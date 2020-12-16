package org.polydev.gaea.population;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public abstract class AsyncGaeaBlockPopulator {
    public abstract CompletableFuture<AsyncPopulationReturn> populate(@NotNull World world, @NotNull Random random, ChunkSnapshot chunk, int id);
}
