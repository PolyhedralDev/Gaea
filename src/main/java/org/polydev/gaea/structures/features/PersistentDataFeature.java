package org.polydev.gaea.structures.features;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.persistence.PersistentDataType;
import org.polydev.gaea.structures.NMSStructure;
import org.polydev.gaea.structures.StructureUtil;

import java.util.Random;

public class PersistentDataFeature implements Feature {
    private final NamespacedKey key;

    public PersistentDataFeature(NamespacedKey key) {
        this.key = key;
    }

    @Override
    public void populate(NMSStructure s, Random r) {
        for(Location chestLoc : StructureUtil.getChestsIn(s.getBoundingLocations()[0], s.getBoundingLocations()[1])) {
            BlockState blockState = chestLoc.getBlock().getState();
            if(blockState instanceof Chest && (chestLoc.getBlock().getType().equals(Material.CHEST) || chestLoc.getBlock().getType().equals(Material.TRAPPED_CHEST))) {
                Chest chest = (Chest) chestLoc.getBlock().getState();
                chest.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, (s.getRotation() / 90));
                chest.update();
            }
        }
    }
}
