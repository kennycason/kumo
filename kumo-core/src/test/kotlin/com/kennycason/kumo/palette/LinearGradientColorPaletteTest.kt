package com.kennycason.kumo.palette

import java.awt.Color
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class LinearGradientColorPaletteTest {

    @Test
    fun singleGradient() {
        val color1 = Color(0x00, 0x00, 0x00)
        val color2 = Color(0xFF, 0xFF, 0xFF)
        val gradientSteps = 15
        val colorPalette = LinearGradientColorPalette(color1, color2, gradientSteps)
        val colors = colorPalette.colors
        expectThat(colors.size).isEqualTo(16)
        expectThat(colors[0]).isEqualTo(color1)
        expectThat(colors[gradientSteps]).isEqualTo(color2)

        val stepSize = 0xFF / gradientSteps
        for (i in 0 until gradientSteps) {
            expectThat(colors[i].red).isEqualTo(i * stepSize)
            expectThat(colors[i].green).isEqualTo(i * stepSize)
            expectThat(colors[i].blue).isEqualTo(i * stepSize)
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
        val colors = colorPalette.colors
        expectThat(colors.size).isEqualTo(31)
        expectThat(colors[0]).isEqualTo(color1)
        expectThat(colors[gradientSteps]).isEqualTo(color2)

        val stepSize = 0xFF / gradientSteps
        for (i in 0..gradientSteps) {
            expectThat(colors[i].red).isEqualTo(i * stepSize)
            expectThat(colors[i].green).isEqualTo(i * stepSize)
            expectThat(colors[i].blue).isEqualTo(i * stepSize)
        }
        var expectedColor = 0xFF
        for (i in gradientSteps until colors.size) {
            expectThat(colors[i].red).isEqualTo(expectedColor)
            expectThat(colors[i].green).isEqualTo(expectedColor)
            expectThat(colors[i].blue).isEqualTo(expectedColor)
            expectedColor -= stepSize
        }
    }
}
