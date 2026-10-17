package com.kennycason.kumo.font.scale

import org.junit.Assert.*
import org.junit.Test

class LogFontScalarTest {

    @Test
    fun testScale() {
        val fontScalar = LogFontScalar(-1, 13)
        assertEquals(8.333333f, fontScalar.scale(4, 8), 0.0f)

        val fontScalar2 = LogFontScalar(0, 0)
        assertEquals(Float.NaN, fontScalar2.scale(0, 0), 0.0f)
    }

    @Test
    fun testNaN() {
        val fontScalar = LogFontScalar(10, 40)

        for (i in 0 until 6)
            for (j in 1 until 6) {
                assertNotEquals(Float.NaN, fontScalar.scale(i, j))
            }
    }
}
