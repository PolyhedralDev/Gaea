import org.polydev.gaea.math.Interpolator3;

public class Interp3Test {
    public static void main(String[] args) {
        Interpolator3 i = new Interpolator3(1, 1, 1, 0, 0, 0, 0, 0);
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                for(int z = 0; z < 8; z++) {
                    System.out.print(i.trilerp((float) x / 6, (float) y / 6, (float) z / 6) + " ");
                }
                System.out.println();
            }
            System.out.println("\n\n");
        }
    }
}
