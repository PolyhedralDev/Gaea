import org.junit.jupiter.api.Test;
import org.polydev.gaea.util.FastRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RandomTest {
    @Test
    public void test() {
        FastRandom r = new FastRandom(1234);

        FastRandom r2 = new FastRandom(1234);
        for(int i = 0; i < 1000; i++) assertEquals(r.nextInt(1234), r2.nextInt(1234));
    }
}
