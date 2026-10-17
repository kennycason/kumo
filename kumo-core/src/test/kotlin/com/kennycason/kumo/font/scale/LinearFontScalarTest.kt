package com.kennycason.kumo.font.scale

import org.junit.Assert.*
import org.junit.Test

class LinearFontScalarTest {

    @Test
    fun testScale() {
        val fontScalar = LinearFontScalar(-1, 13)
        assertEquals(6.0f, fontScalar.scale(4, 8), 0.0f)

        val fontScalar2 = LinearFontScalar(0, 0)
        assertEquals(Float.NaN, fontScalar2.scale(0, 0), 0.0f)
    }

    @Test
    fun testNaN() {
        val fontScalar = LinearFontScalar(10, 40)

        for (i in 0 until 6)
            for (j in 1 until 6) {
                assertNotEquals(Float.NaN, fontScalar.scale(i, j))
            }
    }
}
