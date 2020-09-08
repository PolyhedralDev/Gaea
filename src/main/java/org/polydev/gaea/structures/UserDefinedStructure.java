package org.polydev.gaea.structures;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.polydev.gaea.structures.features.Feature;
import org.polydev.gaea.structures.spawn.StructureSpawnInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class UserDefinedStructure implements Structure {
    private static final Map<UserDefinedStructure, Object> nmsContainer = new HashMap<>();
    private final String id;
    private final List<Feature> features;
    private final StructureSpawnInfo spawnInfo;

    public UserDefinedStructure(String id, File location, List<Feature> features, StructureSpawnInfo spawnInfo) {
        this.id = id;
        this.features = features;
        this.spawnInfo = spawnInfo;
        try {
            nmsContainer.put(this, NMSStructure.getAsTag(new FileInputStream(location)));
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("[Gaea] Unable to load User Defined Structure " + id + "!");
        }
    }

    public boolean isLoaded() {
        return nmsContainer.containsKey(this);
    }

    @Override
    public NMSStructure getInstance(Location origin, Random r) {
        return new NMSStructure(origin, nmsContainer.get(this));
    }

    @Override
    public List<Feature> getFeatures() {
        return this.features;
    }

    @Override
    public StructureSpawnInfo getSpawnInfo() {
        return spawnInfo;
    }

    @Override
    public String toString() {
        return "USER_DEF:" + id;
    }
}
