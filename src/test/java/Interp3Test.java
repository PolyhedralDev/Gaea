import org.polydev.gaea.math.Interpolator;
import org.polydev.gaea.math.Interpolator3;

public class Interp3Test {
    public static void main(String[] args) {
        Interpolator3 i = new Interpolator3(1, 1, 1, 0, 0, 0, 0, 0);
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                for(int z = 0; z < 4; z++) {
                    System.out.print(i.trilerp((float)x/3, (float)y/3, (float)z/3) + " ");
                }
                System.out.println();
            }
            System.out.println("\n\n");
        }
    }
}
