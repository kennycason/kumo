package com.kennycason.kumo.font.scale

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue

class LinearFontScalarTest {

    @Test
    fun testScale() {
        val fontScalar = LinearFontScalar(-1, 13)
        expectThat(fontScalar.scale(4, 8)).isEqualTo(6.0f)

        val fontScalar2 = LinearFontScalar(0, 0)
        expectThat(fontScalar2.scale(0, 0).isNaN()).isTrue()
    }

    @Test
    fun testNaN() {
        val fontScalar = LinearFontScalar(10, 40)

        for (i in 0 until 6)
            for (j in 1 until 6) {
                expectThat(fontScalar.scale(i, j).isNaN()).isFalse()
            }
    }
}
