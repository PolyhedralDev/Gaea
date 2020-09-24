package org.polydev.gaea.world.palette;

import org.polydev.gaea.math.FastNoise;

import java.util.List;

public class SimplexPalette<E> extends Palette<E> {
    private final FastNoise r;
    public SimplexPalette(FastNoise r) {
        this.r = r;
    }
    @Override
    public E get(int layer, int x, int z) {
        if(layer > this.getSize()) return this.getLayers().get(this.getLayers().size()-1).get(r, x, z);
        List<PaletteLayer<E>> pl = getLayers();
        for(PaletteLayer<E> p : pl) {
            if(layer < p.getLayers()) return p.get(r, x, z);
        }
        return pl.get(pl.size() - 1).get(r, x, z);
    }
}
