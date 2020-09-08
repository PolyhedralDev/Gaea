package org.polydev.gaea.structures.features;

import org.bukkit.Location;
import org.bukkit.Material;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.structures.NMSStructure;
import org.polydev.gaea.util.WorldUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BlockReplaceFeature implements Feature {
    private final ProbabilityCollection<Material> materials;
    private final double percent;
    private final List<Material> replaceable = Arrays.asList(Material.COBBLESTONE, Material.COBBLESTONE_STAIRS, Material.COBBLESTONE_SLAB, Material.COBBLESTONE_WALL,
            Material.STONE_BRICK_SLAB, Material.STONE_BRICK_STAIRS, Material.STONE_BRICK_WALL, Material.STONE_BRICKS, Material.OAK_WOOD, Material.OAK_LOG, Material.OAK_PLANKS,
            Material.OAK_FENCE, Material.OAK_FENCE_GATE, Material.OAK_SLAB, Material.OAK_STAIRS, Material.SPRUCE_FENCE, Material.SPRUCE_FENCE_GATE, Material.SPRUCE_LOG, Material.SPRUCE_PLANKS,
            Material.SPRUCE_SLAB, Material.SPRUCE_STAIRS);

    public BlockReplaceFeature(double percent, ProbabilityCollection<Material> materials) {
        this.materials = materials;
        this.percent = percent;
    }

    @Override
    public void populate(NMSStructure s, Random r) {
        List<Location> all = WorldUtil.getLocationListBetween(s.getBoundingLocations()[0], s.getBoundingLocations()[1]);
        for(int i = 0; i < all.size() * (percent / 100); i++) {
            boolean done = false;
            int attempts = 0;
            while(! done && attempts < 5) {
                Location candidate = all.get(r.nextInt(all.size()));
                if(replaceable.contains(candidate.getBlock().getType())) {
                    candidate.getBlock().setType(materials.get(r));
                    done = true;
                }
                attempts++;
            }
        }
    }
}
