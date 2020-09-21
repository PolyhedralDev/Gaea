package org.polydev.gaea.world.palette;

import org.bukkit.block.data.BlockData;

import java.util.List;
import java.util.Random;

public class RandomPalette extends BlockPalette {
    private final Random r;
    public RandomPalette(Random r) {
        this.r = r;
    }
    @Override
    public BlockData getBlockData(int layer, int x, int z) {
        List<PaletteLayer> pl = getLayers();
        for(PaletteLayer p : pl) {
            if(layer < p.getLayers()) return p.get(r);
        }
        return pl.get(pl.size() - 1).get(r);
    }
}
