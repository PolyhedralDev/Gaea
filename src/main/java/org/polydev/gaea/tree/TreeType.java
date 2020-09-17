package org.polydev.gaea.tree;

import co.aikar.taskchain.BukkitTaskChainFactory;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.tree.fractal.FractalTree;

import java.util.Random;

public enum TreeType implements Tree {
    SHATTERED_SMALL(null),
    SHATTERED_LARGE(null),
    GIANT_OAK(null),
    GIANT_SPRUCE(null),
    SMALL_SHATTERED_PILLAR(null),
    LARGE_SHATTERED_PILLAR(null),
    CACTUS(null),
    OAK(org.bukkit.TreeType.TREE),
    LARGE_OAK(org.bukkit.TreeType.BIG_TREE),
    SPRUCE(org.bukkit.TreeType.REDWOOD),
    LARGE_SPRUCE(org.bukkit.TreeType.TALL_REDWOOD),
    MEGA_SPRUCE(org.bukkit.TreeType.MEGA_REDWOOD),
    BIRCH(org.bukkit.TreeType.BIRCH),
    CHORUS_PLANT(org.bukkit.TreeType.CHORUS_PLANT),
    ACACIA(org.bukkit.TreeType.ACACIA),
    TALL_BIRCH(org.bukkit.TreeType.TALL_BIRCH),
    JUNGLE(org.bukkit.TreeType.JUNGLE),
    SMALL_JUNGLE(org.bukkit.TreeType.SMALL_JUNGLE),
    JUNGLE_COCOA(org.bukkit.TreeType.COCOA_TREE),
    JUNGLE_BUSH(org.bukkit.TreeType.JUNGLE_BUSH),
    DARK_OAK(org.bukkit.TreeType.DARK_OAK),
    BROWN_MUSHROOM(org.bukkit.TreeType.BROWN_MUSHROOM),
    RED_MUSHROOM(org.bukkit.TreeType.RED_MUSHROOM),
    SWAMP_OAK(org.bukkit.TreeType.SWAMP);

    private final org.bukkit.TreeType vanillaType;

    TreeType(org.bukkit.TreeType vanillaType) {
        this.vanillaType = vanillaType;
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

    public void plant(Location l, Random r, boolean doSpawnCheck, JavaPlugin main) {
        if(this.getVanillaTreeType() == null) {
            FractalTree tree = getCustomTreeType().getTree(l, r);
            BukkitTaskChainFactory.create(main).newChain()
                    .async(tree::grow)
                    .sync(() -> tree.plant(doSpawnCheck))
                    .execute();
            return;
        }
        l.getWorld().generateTree(l, this.getVanillaTreeType());
    }
}
