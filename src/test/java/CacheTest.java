import org.bukkit.Material;
import org.junit.jupiter.api.Test;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.util.FastRandom;
import org.polydev.gaea.util.GlueList;

import java.util.List;

public class CacheTest {

    @Test
    public void regular() {
        ProbabilityCollection<Material> collection = new ProbabilityCollection<>();
        collection.add(Material.DIRT, 100);
        collection.add(Material.GRASS_BLOCK, 127);
        collection.add(Material.GRASS_PATH, 101);

        FastRandom random = new FastRandom(123);

        long s = System.nanoTime();
        for(int i = 0; i < 10000000; i++) {
            collection.get(random);
        }
        double t = (double) (System.nanoTime() - s);
        System.out.println(t / 1000000 + "ms, " + t / 10000000 + "ns per");

        List<Long> longList = new GlueList<>();
        for(int i = 0; i < 10000; i++) {
            long tm = System.nanoTime();
            collection.get(random);
            long diff = System.nanoTime() - tm;
            longList.add(diff);
        }
        long sum = 0;
        for(long l : longList) sum += l;
        System.out.println(sum / longList.size() + "ns per get");
    }

    @Test
    public void fast() {
        ProbabilityCollection<Material> collection = new ProbabilityCollection<>();
        collection.add(Material.DIRT, 100);
        collection.add(Material.GRASS_BLOCK, 127);
        collection.add(Material.GRASS_PATH, 101);

        FastRandom random = new FastRandom(123);

        long s = System.nanoTime();
        for(int i = 0; i < 10000000; i++) {
            collection.get(random);
        }
        double t = (double) (System.nanoTime() - s);
        System.out.println(t / 1000000 + "ms, " + t / 10000000 + "ns per");

        List<Long> longList = new GlueList<>();
        for(int i = 0; i < 10000; i++) {
            long tm = System.nanoTime();
            collection.get(random);
            long diff = System.nanoTime() - tm;
            longList.add(diff);
        }
        long sum = 0;
        for(long l : longList) sum += l;
        System.out.println(sum / longList.size() + "ns per get");
    }
}
