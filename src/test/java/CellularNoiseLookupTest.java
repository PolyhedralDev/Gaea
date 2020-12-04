import org.polydev.gaea.biome.NormalizationUtil;
import org.polydev.gaea.math.FastNoiseLite;

import javax.swing.*;
import java.awt.*;

public class CellularNoiseLookupTest extends JPanel {
    public CellularNoiseLookupTest() {
    }

    public static void main(String[] args) {


        JFrame frame = new JFrame("Noise Test");

        frame.setSize(2048, 2048);

        frame.add(new CellularNoiseLookupTest());
        System.out.println("done");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        FastNoiseLite noiseLite = new FastNoiseLite();

        FastNoiseLite lookup = new FastNoiseLite();
        lookup.setCellularNoiseLookup(lookup);
        lookup.setNoiseType(FastNoiseLite.NoiseType.Cellular);
        lookup.setCellularReturnType(FastNoiseLite.CellularReturnType.NoiseLookup);
        lookup.setFrequency(0.002);

        FastNoiseLite simplexLookup = new FastNoiseLite();
        simplexLookup.setFrequency(0.001);

        lookup.setCellularNoiseLookup(simplexLookup);


        noiseLite.setCellularNoiseLookup(lookup);
        noiseLite.setNoiseType(FastNoiseLite.NoiseType.Cellular);
        noiseLite.setCellularReturnType(FastNoiseLite.CellularReturnType.NoiseLookup);
        noiseLite.setFrequency(0.01);
        for(int x = 0; x < 2048; x++) {
            for(int y = 0; y < 2048; y++) {
                int noiseR = NormalizationUtil.normalize(noiseLite.getNoise(x, y), 126, 1);
                int noiseG = NormalizationUtil.normalize(noiseLite.getCellularNoiseLookup().getNoise(x, y), 127, 1);
                g.setColor(new Color(noiseR + noiseG, noiseR + noiseG, noiseR + noiseG));
                g.drawLine(x, y, x, y);
            }
        }
    }
}
