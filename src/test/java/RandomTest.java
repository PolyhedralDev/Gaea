import org.bukkit.Material;
import org.junit.jupiter.api.Test;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.util.FastRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RandomTest {
    @Test
    public void test() {
        FastRandom r = new FastRandom(1234);

        FastRandom r2 = new FastRandom(1234);
        for(int i = 0; i < 1000; i++) assertEquals(r.nextInt(1234), r2.nextInt(1234));
    }

    @Test
    public void prob() {



        /*
        ProbabilityCollection<Material> old = new ProbabilityCollection<>();

        old.add(Material.DIRT, 500);
        old.add(Material.GRASS_BLOCK, 1000);
        old.add(Material.ACACIA_FENCE, 1000);
        old.add(Material.ACACIA_BOAT, 1000);
        old.add(Material.ACACIA_BUTTON, 1000);
        old.add(Material.ACACIA_DOOR, 1000);
        old.add(Material.ACACIA_FENCE_GATE, 1000);
        old.add(Material.ACACIA_LEAVES, 1000);

        FastRandom random = new FastRandom(12);
        System.out.println("Old: ");
        performance(index -> old.get(random));
        */


        ProbabilityCollection<Material> fast = new ProbabilityCollection<>();

        fast.add(Material.DIRT, 500);
        fast.add(Material.GRASS_BLOCK, 1000);
        fast.add(Material.ACACIA_FENCE, 1000);
        fast.add(Material.ACACIA_BOAT, 1000);
        fast.add(Material.ACACIA_BUTTON, 1000);
        fast.add(Material.ACACIA_DOOR, 1000);
        fast.add(Material.ACACIA_FENCE_GATE, 1000);
        fast.add(Material.ACACIA_LEAVES, 1000);


        FastRandom random2 = new FastRandom(12);
        System.out.println("New: ");
        performance(index -> fast.get(random2));

        System.out.println(fast.size() + " / " + fast.getTotalProbability());


    }

    private void performance(Consumer<Integer> consumer) {
        List<Long> times = new ArrayList<>();

        int num = 100000;
        for(int i = 0; i < num; i++) {
            long l = System.nanoTime();
            consumer.accept(i);
            long r = System.nanoTime() - l;
            times.add(r);
        }

        long total = times.stream().mapToLong(lon -> lon).sum();

        double max = ((double) times.stream().mapToLong(lon -> lon).max().getAsLong());
        double min = ((double) times.stream().mapToLong(lon -> lon).min().getAsLong());

        double avg = ((double) total / times.size());

        System.out.println(format(min) + " / " + format(avg) + " / " + format(max));
        System.out.println("Total: " + format(total));
    }

    private String format(double ns) {
        String[] units = new String[] {"ns", "us", "ms", "s"};

        int i = 0;

        while(ns > 1000 && i < 4) {
            ns /= 1000;
            i++;
        }

        return ns + units[i];
    }
}
