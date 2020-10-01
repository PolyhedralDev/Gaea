import org.bukkit.Material;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PaletteTest {
    @Test
    public void getBlocks() {
        Palette<Material> palette = new RandomPalette<>(new Random(2403));
        for(int i = 0; i < 100; i++) {
            palette.add(i % 2 == 0 ? Material.DIRT : Material.STONE, 1);
        }
        for(int i = 0; i < 100; i++) {
            assertEquals(palette.get(i, 0, 0), i % 2 == 0 ? Material.DIRT : Material.STONE);
        }
        assertEquals(palette.get(10000, 0, 0), Material.STONE);
    }

    @Test
    public void getProbabilityBlocks() {

    }

    @Test
    public void main() {
        long l = System.nanoTime();
        Random r = new Random();
        //testing time taken to instantiate/fill palette. Realistic test.
        Palette<Material> p = new RandomPalette<>(r);
        System.out.println((System.nanoTime() - l) / 1000 + "us elapsed (Instantiation)");
        l = System.nanoTime();
        p.add(Material.GRASS_BLOCK, 1);
        System.out.println((System.nanoTime() - l) / 1000000 + "ms elapsed (Fill 1)");
        l = System.nanoTime();
        p.add(Material.DIRT, 12);
        System.out.println((System.nanoTime() - l) / 1000000 + "ms elapsed (Fill 2)");
        l = System.nanoTime();
        p.add(new ProbabilityCollection<Material>().add(Material.STONE, 1).add(Material.DIRT, 1), 20);
        System.out.println((System.nanoTime() - l) / 1000000 + "ms elapsed (Fill 3)");
        l = System.nanoTime();
        p.add(Material.STONE, 30);
        System.out.println((System.nanoTime() - l) / 1000000 + "ms elapsed (Fill 4)");
        l = System.nanoTime();

        //testing time taken to get the top layer of materials Realistic test, however, much time is taken by System.out.
        List<Material> m = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            long l2 = System.nanoTime();
            m.add(p.get(i, 0, 0));
            System.out.println(p.get(i, 0, 0) + " retrieved in " + (System.nanoTime() - l2) / 1000 + "us");
        }
        System.out.println((double) (System.nanoTime() - l) / 1000000 + "ms elapsed (Getters, raw x10), got " + m.size() + " values");

        //testing time taken to get 100k materials. Unrealistic stress test.
        for(int i = 0; i < 1000000; i++) {
            p.get(i, 0, 0);
        }
        System.out.println((double) (System.nanoTime() - l) / 1000000 + "ms elapsed (Getters, raw x100000), got " + 100000 + " values");

        //testing time taken to instantiate and fill 500k alternating layers of dirt/stone. Unrealistic stress test.
        System.out.println();
        System.out.println("Beginning fill for stress-test");
        l = System.nanoTime();
        Palette<Material> p2 = new RandomPalette<>(new Random(2403));
        for(int i = 0; i < 1000; i++) {
            p2.add(Material.DIRT, 1);
            p2.add(Material.STONE, 1);
        }

        //testing time taken to retrieve all 1m layers created in previous test. Unrealistic stress test.
        System.out.println((System.nanoTime() - l) / 1000000 + "ms elapsed (Instantiation/Fill x500000)");
        l = System.nanoTime();
        for(int i = 0; i < 1000000; i++) {
            long l2 = System.nanoTime();
            if(i % 100001 == 0)
                System.out.println(p2.get(i, 0, 0) + " retrieved in " + (System.nanoTime() - l2) / 1000 + "us at layer " + i);
        }
        System.out.println((double) (System.nanoTime() - l) / 1000000 + "ms elapsed (Getters, raw x1000000), got " + 1000000 + " values");
    }
}
