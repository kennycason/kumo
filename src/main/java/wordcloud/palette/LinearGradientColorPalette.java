package wordcloud.palette;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * A LinearGradient ColorPalette
 * 
 * @author &#64;wolfposd
 */
public class LinearGradientColorPalette extends ColorPalette {

    /**
     * <p>
     * Creates a ColorPalette using a linear gradient between the two colors
     * specified
     * </p>
     * <p>
     * The following example code will generate a gradient between red and blue,
     * with 18 colors in between red and blue, thus totaling 20 colors:
     * </p>
     * <code>
     * new LinearGradientColorPalette(Color.RED, 18, Color.Blue)
     * </code>
     * 
     * @param color1
     *            start color
     * @param betweenC1andC2
     *            specifies the amount of colors for this gradient between
     *            color1 and color2
     * @param color2
     *            end color
     */
    public LinearGradientColorPalette(Color color1, int betweenC1andC2, Color color2) {
        this.colors = createLinearGradient(color1, color2, betweenC1andC2).toArray(new Color[] {});
    }

    /**
     * Creates a ColorPalette using a linear gradient between the three colors
     * specified
     * 
     * @param color1
     *            start color
     * @param betweenC1andC2
     *            number of colors to be generated between color1 and color2
     * @param color2
     *            middle color
     * @param betweenC2andC3
     *            number of colors to be generated between color2 and color3
     * @param color3
     *            end color
     */
    public LinearGradientColorPalette(Color color1, int betweenC1andC2, Color color2, int betweenC2andC3, Color color3) {
        List<Color> cs = new ArrayList<>();

        // adding colors [c1,c2]
        for (Color c : createLinearGradient(color1, color2, betweenC1andC2)) {
            cs.add(c);
        }
        
        // adding colors ]c2,c3]
        for (Color c : createLinearGradient(color2, color3, betweenC2andC3)) {
            if (c.equals(color2)) { // already being added by first gradient
                continue;
            }
            cs.add(c);
        }

        this.colors = cs.toArray(new Color[] {});
    }

    /**
     * Creates a linear Gradient between two colors
     * 
     * @param color1
     *            start color
     * @param color2
     *            end color
     * @param amountBetweenC1andC2
     *            specifies the amount of colors in this gradient between color1
     *            and color2
     * @return List of colors in this gradient
     */
    public static List<Color> createLinearGradient(Color color1, Color color2, int amountBetweenC1andC2) {
        List<Color> cols = new ArrayList<>();

        for (int i = 0; i <= amountBetweenC1andC2; i++) {
            float ratio = (float) i / (float) amountBetweenC1andC2;

            int red = (int) (color2.getRed() * ratio + color1.getRed() * (1 - ratio));
            int green = (int) (color2.getGreen() * ratio + color1.getGreen() * (1 - ratio));
            int blue = (int) (color2.getBlue() * ratio + color1.getBlue() * (1 - ratio));

            cols.add(new Color(red, green, blue));
        }
        cols.add(color2);
        return cols;
    }
}
