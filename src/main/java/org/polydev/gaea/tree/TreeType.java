package org.polydev.gaea.tree;

import co.aikar.taskchain.BukkitTaskChainFactory;
import com.google.common.collect.Sets;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.tree.fractal.FractalTree;

import java.util.Collections;
import java.util.Random;
import java.util.Set;

public enum TreeType implements Tree {
    SHATTERED_SMALL(null, Collections.singleton(Material.END_STONE)),
    SHATTERED_LARGE(null, Collections.singleton(Material.END_STONE)),
    GIANT_OAK(null, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL)),
    GIANT_SPRUCE(null, Sets.newHashSet(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL)),
    SMALL_SHATTERED_PILLAR(null, Collections.singleton(Material.END_STONE)),
    LARGE_SHATTERED_PILLAR(null, Collections.singleton(Material.END_STONE)),
    CACTUS(null, Sets.newHashSet(Material.SAND, Material.RED_SAND)),
    ICE_SPIKE(null, Sets.newHashSet(Material.SNOW_BLOCK, Material.SNOW, Material.STONE, Material.GRASS_BLOCK)),
    OAK(org.bukkit.TreeType.TREE, null),
    LARGE_OAK(org.bukkit.TreeType.BIG_TREE, null),
    SPRUCE(org.bukkit.TreeType.REDWOOD, null),
    LARGE_SPRUCE(org.bukkit.TreeType.TALL_REDWOOD, null),
    MEGA_SPRUCE(org.bukkit.TreeType.MEGA_REDWOOD, null),
    BIRCH(org.bukkit.TreeType.BIRCH, null),
    CHORUS_PLANT(org.bukkit.TreeType.CHORUS_PLANT, null),
    ACACIA(org.bukkit.TreeType.ACACIA, null),
    TALL_BIRCH(org.bukkit.TreeType.TALL_BIRCH, null),
    JUNGLE(org.bukkit.TreeType.JUNGLE, null),
    SMALL_JUNGLE(org.bukkit.TreeType.SMALL_JUNGLE, null),
    JUNGLE_COCOA(org.bukkit.TreeType.COCOA_TREE, null),
    JUNGLE_BUSH(org.bukkit.TreeType.JUNGLE_BUSH, null),
    DARK_OAK(org.bukkit.TreeType.DARK_OAK, null),
    BROWN_MUSHROOM(org.bukkit.TreeType.BROWN_MUSHROOM, null),
    RED_MUSHROOM(org.bukkit.TreeType.RED_MUSHROOM, null),
    SWAMP_OAK(org.bukkit.TreeType.SWAMP, null),
    WARPED_FUNGUS(org.bukkit.TreeType.WARPED_FUNGUS, null),
    CRIMSON_FUNGUS(org.bukkit.TreeType.CRIMSON_FUNGUS, null);

    private final org.bukkit.TreeType vanillaType;
    private final Set<Material> spawnable;

    TreeType(org.bukkit.TreeType vanillaType, Set<Material> spawnable) {
        this.vanillaType = vanillaType;
        this.spawnable = spawnable;
    }

    public boolean isCustom() {
        return this.vanillaType == null;
    }

    public org.bukkit.TreeType getVanillaTreeType() {
        return vanillaType;
    }

    public CustomTreeType getCustomTreeType() {
        if(getVanillaTreeType() != null) return null;
        return CustomTreeType.valueOf(this.toString());
    }

    public boolean plant(Location l, Random r, boolean doSpawnCheck, JavaPlugin main) {
        if(this.getVanillaTreeType() == null) {
            if(! spawnable.contains(l.clone().getBlock().getType())) return false;
            FractalTree tree = getCustomTreeType().getTree(l.add(0, 1, 0), r);
            if(main.isEnabled()) BukkitTaskChainFactory.create(main).newChain()
                    .async(tree::grow)
                    .sync(() -> tree.plant(doSpawnCheck))
                    .execute();
            return true;
        }
        return l.getWorld().generateTree(l, this.getVanillaTreeType());
    }
}
