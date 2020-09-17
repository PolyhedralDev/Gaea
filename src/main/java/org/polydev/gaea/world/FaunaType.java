package org.polydev.gaea.world;

import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public enum FaunaType implements Fauna {
    TALL_GRASS(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:tall_grass[half=lower]"), Bukkit.createBlockData("minecraft:tall_grass[half=upper]")),
    TALL_FERN(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:large_fern[half=lower]"), Bukkit.createBlockData("minecraft:large_fern[half=upper]")),
    SUNFLOWER(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:sunflower[half=lower]"), Bukkit.createBlockData("minecraft:sunflower[half=upper]")),
    ROSE_BUSH(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:rose_bush[half=lower]"), Bukkit.createBlockData("minecraft:rose_bush[half=upper]")),
    LILAC(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:lilac[half=lower]"), Bukkit.createBlockData("minecraft:lilac[half=upper]")),
    PEONY(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:peony[half=lower]"), Bukkit.createBlockData("minecraft:peony[half=upper]")),
    GRASS(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:grass")),
    FERN(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:fern")),
    AZURE_BLUET(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:azure_bluet")),
    LILY_OF_THE_VALLEY(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:lily_of_the_valley")),
    BLUE_ORCHID(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:blue_orchid")),
    POPPY(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:poppy")),
    DANDELION(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:dandelion")),
    WITHER_ROSE(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:wither_rose")),
    DEAD_BUSH(Sets.newHashSet(Material.GRASS_BLOCK, Material.SAND, Material.RED_SAND), Bukkit.createBlockData("minecraft:dead_bush")),
    RED_TULIP(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:red_tulip")),
    ORANGE_TULIP(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:orange_tulip")),
    WHITE_TULIP(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:white_tulip")),
    PINK_TULIP(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:pink_tulip")),
    OXEYE_DAISY(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:oxeye_daisy")),
    ALLIUM(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:allium")),
    CORNFLOWER(Sets.newHashSet(Material.GRASS_BLOCK), Bukkit.createBlockData("minecraft:cornflower")),
    LILY_PAD(Sets.newHashSet(Material.WATER), Bukkit.createBlockData("minecraft:lily_pad")),
    RED_MUSHROOM(Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.STONE, Material.NETHERRACK), Bukkit.createBlockData("minecraft:red_mushroom")),
    BROWN_MUSHROOM(Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.STONE, Material.NETHERRACK), Bukkit.createBlockData("minecraft:brown_mushroom")),;

    private final List<BlockData> data = new ArrayList<>();

    private final Set<Material> spawns;

    FaunaType(Set<Material> validSpawns, BlockData... type) {
        data.addAll(Arrays.asList(type));
        this.spawns = validSpawns;
    }

    @Override
    public Block getHighestValidSpawnAt(Chunk chunk, int x, int z) {
        int y;
        for(y = chunk.getWorld().getMaxHeight() - 1; (!spawns.contains(chunk.getBlock(x, y, z).getType())) && y > 0; y--);
        if(y <= 0) return null;
        return chunk.getBlock(x, y, z);
    }

    @Override
    public boolean plant(Location l) {
        for(int i = 1; i < data.size() + 1; i++) {
            if(! l.clone().add(0, i, 0).getBlock().isEmpty()) return false;
        }
        for(int i = 1; i < data.size() + 1; i++) {
            l.clone().add(0, i, 0).getBlock().setBlockData(data.get(i - 1), false);
        }
        return true;
    }
}
