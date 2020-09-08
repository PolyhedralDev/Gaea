package org.polydev.gaea.structures.features;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.polydev.gaea.structures.NMSStructure;
import org.polydev.gaea.util.WorldUtil;

import java.util.List;
import java.util.Random;

public class EntityFeature implements Feature {
    private final int min;
    private final int max;
    private final EntityType type;


    public EntityFeature(int min, int max, EntityType entity) {
        this.max = max;
        this.min = min;
        this.type = entity;
    }

    @Override
    public void populate(NMSStructure s, Random r) {
        int num = r.nextInt(max - min + 1) + min;
        List<Location> all = WorldUtil.getLocationListBetween(s.getBoundingLocations()[0], s.getBoundingLocations()[1]);
        for(int i = 0; i < num; i++) {
            boolean done = false;
            int attempts = 0;
            while(! done && attempts < 8) {
                Location candidate = all.get(r.nextInt(all.size()));
                if(candidate.getBlock().isEmpty()) {
                    candidate.getWorld().spawnEntity(candidate, type);
                    done = true;
                }
                attempts++;
            }
        }
    }
}
