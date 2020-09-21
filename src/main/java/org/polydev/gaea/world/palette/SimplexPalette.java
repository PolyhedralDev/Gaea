package org.polydev.gaea.world.palette;

import org.bukkit.block.data.BlockData;
import org.polydev.gaea.math.FastNoise;

import java.util.List;
import java.util.Random;

public class SimplexPalette extends BlockPalette {
    private final FastNoise r;
    public SimplexPalette(FastNoise r) {
        this.r = r;
    }
    @Override
    public BlockData getBlockData(int layer, int x, int z) {
        List<PaletteLayer> pl = getLayers();
        for(PaletteLayer p : pl) {
            if(layer < p.getLayers()) return p.get(r, x, z);
        }
        return pl.get(pl.size() - 1).get(r, x, z);
    }
}
