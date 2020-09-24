package org.polydev.gaea.world.palette;

import java.util.List;
import java.util.Random;

public class RandomPalette<E> extends Palette<E> {
    private final Random r;
    public RandomPalette(Random r) {
        this.r = r;
    }
    @Override
    public E get(int layer, int x, int z) {
        if(layer > this.getSize()) return this.getLayers().get(this.getLayers().size()-1).get(r);
        List<PaletteLayer<E>> pl = getLayers();

        for(PaletteLayer<E> p : pl) {
            if(layer < p.getLayers()) return p.get(r);
        }
        return pl.get(pl.size() - 1).get(r);
    }
}
