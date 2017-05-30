package com.kennycason.kumo.palette;

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
     * @param color2
     *            end color
     * @param gradientSteps
     *            specifies the amount of colors for this gradient between
     *            color1 and color2
     */
    public LinearGradientColorPalette(final Color color1,
                                      final Color color2,
                                      final int gradientSteps) {
        super(createLinearGradient(color1, color2, gradientSteps));
    }

    /**
     * Creates a ColorPalette using a linear gradient between the three colors
     * specified
     * 
     * @param color1
     *            start color
     * @param color2
     *            middle color
     * @param color3
     *            end color
     * @param gradientStepsC1AndC2
     *            number of colors to be generated between color1 and color2
     *            this includes both color1 and color2
     * @param gradientStepsC2AndC3
     *            number of colors to be generated between color2 and color3
     *            this includes both color2 and color3
     */
    public LinearGradientColorPalette(final Color color1,
                                      final Color color2,
                                      final Color color3,
                                      final int gradientStepsC1AndC2,
                                      final int gradientStepsC2AndC3) {
        super(createTwoLinearGradients(color1, color2, color3, gradientStepsC1AndC2, gradientStepsC2AndC3));
    }

    private static List<Color> createTwoLinearGradients(final Color color1,
                                                        final Color color2,
                                                        final Color color3,
                                                        final int gradientStepsC1AndC2,
                                                        final int gradientStepsC2AndC3) {
        final List<Color> colors = new ArrayList<>();

        final List<Color> gradient1 = createLinearGradient(color1, color2, gradientStepsC1AndC2);
        final List<Color> gradient2 = createLinearGradient(color2, color3, gradientStepsC2AndC3);

        colors.addAll(gradient1);
        // the first item will overlap with the color2, so ignore it
        colors.addAll(gradient2.subList(1, gradient2.size()));

        return colors;
    }

    /**
     * Creates a linear Gradient between two colors
     * 
     * @param color1
     *            start color
     * @param color2
     *            end color
     * @param gradientSteps
     *            specifies the amount of colors in this gradient between color1
     *            and color2, this includes both color1 and color2
     * @return List of colors in this gradient
     */
    private static List<Color> createLinearGradient(final Color color1, final Color color2, final int gradientSteps) {
        final List<Color> colors = new ArrayList<>(gradientSteps + 1);

        // add beginning color to the gradient
        colors.add(color1);

        for (int i = 1; i < gradientSteps; i++) {
            float ratio = (float) i / (float) gradientSteps;

            final float red =color2.getRed() * ratio + color1.getRed() * (1 - ratio);
            final float green = color2.getGreen() * ratio + color1.getGreen() * (1 - ratio);
            final float blue = color2.getBlue() * ratio + color1.getBlue() * (1 - ratio);

            colors.add(new Color(Math.round(red), Math.round(green), Math.round(blue)));
        }
        // add end color to the gradient
        colors.add(color2);

        return colors;
    }
}
