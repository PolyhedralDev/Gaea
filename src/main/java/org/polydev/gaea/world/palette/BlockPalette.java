package org.polydev.gaea.world.palette;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.math.ProbabilityCollection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * A class representation of a "slice" of the world.
 * Used to get a section of blocks, based on the depth at which they are found.
 */
public abstract class BlockPalette {
    private final List<PaletteLayer> pallet = new ArrayList<>();

    /**
     * Constructs a blank palette.
     */
    public BlockPalette() {

    }

    /**
     * Adds a material to the palette, for a number of layers.
     *
     * @param m      - The material to add to the palette.
     * @param layers - The number of layers the material occupies.
     * @return - BlockPalette instance for chaining.
     */
    public BlockPalette add(Material m, int layers) {
        pallet.add(new PaletteLayer(m.createBlockData(), layers + (pallet.size() == 0 ? 0 : pallet.get(pallet.size() - 1).getLayers())));
        return this;
    }

    /**
     * Adds a ProbabilityCollection to the palette, for a number of layers.
     *
     * @param m      - The ProbabilityCollection to add to the palette.
     * @param layers - The number of layers the material occupies.
     * @return - BlockPalette instance for chaining.
     */
    public BlockPalette add(ProbabilityCollection<Material> m, int layers) {
        ProbabilityCollection<BlockData> d = new ProbabilityCollection<>();
        Iterator<ProbabilityCollection.ProbabilitySetElement<Material>> i = m.iterator();
        while(i.hasNext()) {
            ProbabilityCollection.ProbabilitySetElement<Material> e = i.next();
            d.add(e.getObject().createBlockData(), e.getProbability());
        }
        pallet.add(new PaletteLayer(d, layers + (pallet.size() == 0 ? 0 : pallet.get(pallet.size() - 1).getLayers())));
        return this;
    }

    /**
     * Adds a ProbabilityCollection to the palette, for a number of layers.
     *
     * @param m      - The ProbabilityCollection to add to the palette.
     * @param layers - The number of layers the material occupies.
     * @return - BlockPalette instance for chaining.
     */
    public BlockPalette addBlockData(ProbabilityCollection<BlockData> m, int layers) {
        pallet.add(new PaletteLayer(m, layers + (pallet.size() == 0 ? 0 : pallet.get(pallet.size() - 1).getLayers())));
        return this;
    }

    public Material get(int layer, int x, int z) {
        return getBlockData(layer, x, z).getMaterial();
    }

    /**
     * Fetches a material from the palette, at a given layer.
     *
     * @param layer - The layer at which to fetch the material.
     * @return BlockData - The material fetched.
     */
    public abstract BlockData getBlockData(int layer, int x, int z);


    /**
     * Gets the BlockData at a layer using the provided Random instance.
     * @param layer The layer to fetch
     * @return BlockData at layer.
     */
    public BlockData getBlockData(int layer, Random r) {
        List<PaletteLayer> pl = getLayers();
        for(PaletteLayer p : pl) {
            if(layer < p.getLayers()) return p.get(r);
        }
        return pl.get(pl.size() - 1).get(r);
    }


    public int getSize() {
        return pallet.get(pallet.size()-1).getLayers();
    }

    enum NoiseType {
        WHITE_NOISE, SIMPLEX_FRACTAL
    }

    public List<PaletteLayer> getLayers() {
        return pallet;
    }

    /**
     * Class representation of a layer of a BlockPalette.
     */
    public static class PaletteLayer {
        private final boolean col;
        private final int layers;
        private ProbabilityCollection<BlockData> collection;
        private BlockData m;

        /**
         * Constructs a PaletteLayer with a ProbabilityCollection of materials and a number of layers.
         *
         * @param type   - The collection of materials to choose from.
         * @param layers - The number of layers.
         */
        public PaletteLayer(ProbabilityCollection<BlockData> type, int layers) {
            this.col = true;
            this.collection = type;
            this.layers = layers;
        }

        /**
         * Constructs a PaletteLayer with a single Material and a number of layers.
         *
         * @param type   - The material to use.
         * @param layers - The number of layers.
         */
        public PaletteLayer(BlockData type, int layers) {
            this.col = false;
            this.m = type;
            this.layers = layers;
        }

        /**
         * Gets the number of layers.
         *
         * @return int - the number of layers.
         */
        public int getLayers() {
            return layers;
        }

        /**
         * Gets a material from the layer.
         *
         * @return Material - the material..
         */
        public BlockData get(Random random) {
            if(col) return this.collection.get(random);
            return m;
        }

        public BlockData get(FastNoise random, int x, int z) {
            if(col) return this.collection.get(random, x, z);
            return m;
        }
    }
}
