package org.polydev.gaea.population;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public abstract class GaeaBlockPopulator {
    public abstract void populate(@NotNull World world, @NotNull Random random, @NotNull CompletableFuture<Chunk> chunk, int x, int z);
}
