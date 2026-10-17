package com.kennycason.kumo.palette

import java.awt.Color
import kotlin.math.roundToInt

/**
 * A LinearGradient ColorPalette
 */
class LinearGradientColorPalette : ColorPalette {

    /**
     * Creates a ColorPalette using a linear gradient between the two colors specified.
     *
     * @param color1 start color
     * @param color2 end color
     * @param gradientSteps specifies the amount of colors for this gradient between color1 and color2
     */
    constructor(color1: Color, color2: Color, gradientSteps: Int) :
            super(createLinearGradient(color1, color2, gradientSteps))

    /**
     * Creates a ColorPalette using a linear gradient between the three colors specified.
     *
     * @param color1 start color
     * @param color2 middle color
     * @param color3 end color
     * @param gradientStepsC1AndC2 number of colors to be generated between color1 and color2
     * @param gradientStepsC2AndC3 number of colors to be generated between color2 and color3
     */
    constructor(
        color1: Color, color2: Color, color3: Color,
        gradientStepsC1AndC2: Int, gradientStepsC2AndC3: Int
    ) : super(createTwoLinearGradients(color1, color2, color3, gradientStepsC1AndC2, gradientStepsC2AndC3))

    companion object {
        private fun createTwoLinearGradients(
            color1: Color, color2: Color, color3: Color,
            gradientStepsC1AndC2: Int, gradientStepsC2AndC3: Int
        ): List<Color> {
            val gradient1 = createLinearGradient(color1, color2, gradientStepsC1AndC2)
            val gradient2 = createLinearGradient(color2, color3, gradientStepsC2AndC3)
            return gradient1 + gradient2.subList(1, gradient2.size)
        }

        private fun createLinearGradient(color1: Color, color2: Color, gradientSteps: Int): List<Color> {
            val colors = mutableListOf<Color>()
            colors.add(color1)

            for (i in 1 until gradientSteps) {
                val ratio = i.toFloat() / gradientSteps.toFloat()
                val red = color2.red * ratio + color1.red * (1 - ratio)
                val green = color2.green * ratio + color1.green * (1 - ratio)
                val blue = color2.blue * ratio + color1.blue * (1 - ratio)
                colors.add(Color(red.roundToInt(), green.roundToInt(), blue.roundToInt()))
            }

            colors.add(color2)
            return colors
        }
    }
}
