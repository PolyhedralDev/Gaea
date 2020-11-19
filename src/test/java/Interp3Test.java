import org.junit.jupiter.api.Test;
import org.polydev.gaea.math.Interpolator;
import org.polydev.gaea.math.Interpolator3;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Interp3Test {
    @Test
    public void interp3() {
        Interpolator3 i = new Interpolator3(1, 1, 1, 0, 0, 0, 0, 0, Interpolator.Type.NEAREST_NEIGHBOR);
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                for(int z = 0; z < 8; z++) {
                    System.out.print(i.trilerp(x / 6D, y / 6D, z / 6D) + " ");
                }
                System.out.println();
            }
            System.out.println("\n\n");
        }
    }
    @Test
    public void interpNN() {
        assertEquals(0.8, Interpolator.lerp(1, 0.8, 1.4, Interpolator.Type.NEAREST_NEIGHBOR));
        assertEquals(1.4, Interpolator.lerp(1.3, 0.8, 1.4, Interpolator.Type.NEAREST_NEIGHBOR));
    }
    @Test
    public void interpLN() {
        assertEquals(1, Interpolator.lerp(0.5, 0.8, 1.2, Interpolator.Type.LINEAR));
    }
}
