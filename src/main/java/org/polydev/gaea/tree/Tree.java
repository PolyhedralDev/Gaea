package org.polydev.gaea.tree;

import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.plugin.java.JavaPlugin;
import co.aikar.taskchain.BukkitTaskChainFactory;
import org.polydev.gaea.tree.fractal.FractalTree;

import java.util.Random;

public enum Tree {
    SHATTERED_SMALL(null),
    SHATTERED_LARGE(null),
    GIANT_OAK(null),
    GIANT_SPRUCE(null),
    SMALL_SHATTERED_PILLAR(null),
    LARGE_SHATTERED_PILLAR(null),
    CACTUS(null),
    OAK(TreeType.TREE),
    LARGE_OAK(TreeType.BIG_TREE),
    SPRUCE(TreeType.REDWOOD),
    LARGE_SPRUCE(TreeType.TALL_REDWOOD),
    MEGA_SPRUCE(TreeType.MEGA_REDWOOD),
    BIRCH(TreeType.BIRCH),
    CHORUS_PLANT(TreeType.CHORUS_PLANT),
    ACACIA(TreeType.ACACIA),
    TALL_BIRCH(TreeType.TALL_BIRCH),
    JUNGLE(TreeType.JUNGLE),
    SMALL_JUNGLE(TreeType.SMALL_JUNGLE),
    JUNGLE_COCOA(TreeType.COCOA_TREE),
    JUNGLE_BUSH(TreeType.JUNGLE_BUSH),
    DARK_OAK(TreeType.DARK_OAK),
    BROWN_MUSHROOM(TreeType.BROWN_MUSHROOM),
    RED_MUSHROOM(TreeType.RED_MUSHROOM),
    SWAMP_OAK(TreeType.SWAMP),
    CRIMSON_FUNGUS(TreeType.CRIMSON_FUNGUS),
    WARPED_FUNGUS(TreeType.WARPED_FUNGUS);

    private final TreeType vanillaType;

    Tree(TreeType vanillaType) {
        this.vanillaType = vanillaType;
    }

    public boolean isCustom() {
        return this.vanillaType == null;
    }

    public TreeType getVanillaTreeType() {
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
