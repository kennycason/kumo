package com.kennycason.kumo.palette

import org.junit.Assert.*
import org.junit.Test
import java.awt.Color

class LinearGradientColorPaletteTest {

    @Test
    fun singleGradient() {
        val color1 = Color(0x00, 0x00, 0x00)
        val color2 = Color(0xFF, 0xFF, 0xFF)
        val gradientSteps = 15
        val colorPalette = LinearGradientColorPalette(color1, color2, gradientSteps)
        val colors = colorPalette.getColors()
        assertEquals(16, colors.size)
        assertEquals(color1, colors[0])
        assertEquals(color2, colors[gradientSteps])

        val stepSize = 0xFF / gradientSteps
        for (i in 0 until gradientSteps) {
            assertEquals(i * stepSize, colors[i].red)
            assertEquals(i * stepSize, colors[i].green)
            assertEquals(i * stepSize, colors[i].blue)
        }
    }

    @Test
    fun doubleGradient() {
        val color1 = Color(0x00, 0x00, 0x00)
        val color2 = Color(0xFF, 0xFF, 0xFF)
        val color3 = Color(0x00, 0x00, 0x00)

        val gradientSteps = 15
        val colorPalette = LinearGradientColorPalette(color1, color2, color3,
            gradientSteps, gradientSteps)
        val colors = colorPalette.getColors()
        assertEquals(31, colors.size)
        assertEquals(color1, colors[0])
        assertEquals(color2, colors[gradientSteps])

        val stepSize = 0xFF / gradientSteps
        for (i in 0..gradientSteps) {
            assertEquals(i * stepSize, colors[i].red)
            assertEquals(i * stepSize, colors[i].green)
            assertEquals(i * stepSize, colors[i].blue)
        }
        var expectedColor = 0xFF
        for (i in gradientSteps until colors.size) {
            assertEquals(expectedColor, colors[i].red)
            assertEquals(expectedColor, colors[i].green)
            assertEquals(expectedColor, colors[i].blue)
            expectedColor -= stepSize
        }
    }
}
