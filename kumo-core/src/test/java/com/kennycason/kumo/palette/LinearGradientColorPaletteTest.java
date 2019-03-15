package com.kennycason.kumo.palette;

import com.kennycason.kumo.draw.Color;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by kenny on 2/20/16.
 */
public class LinearGradientColorPaletteTest {

    @Test
    public void singleGradient() {
        final Color color1 = new Color(0x00, 0x00, 0x00);
        final Color color2 = new Color(0xFF, 0xFF, 0xFF);
        final int gradientSteps = 15;
        final LinearGradientColorPalette colorPalette = new LinearGradientColorPalette(color1, color2, gradientSteps);
        assertEquals(16, colorPalette.getColors().size()); // add two because inclusive beginning and end color
        assertEquals(color1, colorPalette.getColors().get(0));
        assertEquals(color2, colorPalette.getColors().get(gradientSteps));

        final int stepSize = 0xFF / gradientSteps;
        for (int i = 0; i < gradientSteps; i++) {
            assertEquals(i * stepSize, colorPalette.getColors().get(i).getRed());
            assertEquals(i * stepSize, colorPalette.getColors().get(i).getGreen());
            assertEquals(i * stepSize, colorPalette.getColors().get(i).getBlue());
        }
    }

    @Test
    public void doubleGradient() {
        final Color color1 = new Color(0x00, 0x00, 0x00);
        final Color color2 = new Color(0xFF, 0xFF, 0xFF);
        final Color color3 = new Color(0x00, 0x00, 0x00);

        final int gradientSteps = 15;
        final LinearGradientColorPalette colorPalette = new LinearGradientColorPalette(color1, color2, color3,
                                                                                gradientSteps, gradientSteps);
        assertEquals(31, colorPalette.getColors().size()); // add two because inclusive beginning and end color
        assertEquals(color1, colorPalette.getColors().get(0));
        assertEquals(color2, colorPalette.getColors().get(gradientSteps));

        final int stepSize = 0xFF / gradientSteps;
        // test first half of gradient
        for (int i = 0; i <= gradientSteps; i++) {
            assertEquals(i * stepSize, colorPalette.getColors().get(i).getRed());
            assertEquals(i * stepSize, colorPalette.getColors().get(i).getGreen());
            assertEquals(i * stepSize, colorPalette.getColors().get(i).getBlue());
        }
        // test second half of gradient
        int expectedColor = 0xFF; // gradient is decending
        for (int i = gradientSteps; i < colorPalette.getColors().size(); i++) {
            assertEquals(expectedColor, colorPalette.getColors().get(i).getRed());
            assertEquals(expectedColor, colorPalette.getColors().get(i).getGreen());
            assertEquals(expectedColor, colorPalette.getColors().get(i).getBlue());
            expectedColor -= stepSize;
        }
    }
}
