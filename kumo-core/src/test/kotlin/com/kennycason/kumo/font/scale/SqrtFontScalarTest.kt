package com.kennycason.kumo.font.scale

import org.junit.Assert.*
import org.junit.Test

class SqrtFontScalarTest {

    @Test
    fun testScale() {
        val fontScalar = SqrtFontScalar(-1, 13)
        assertEquals(8.899495f, fontScalar.scale(4, 8), 0.0f)

        val fontScalar2 = SqrtFontScalar(0, 0)
        assertEquals(Float.NaN, fontScalar2.scale(0, 0), 0.0f)
    }

    @Test
    fun testNaN() {
        val fontScalar = SqrtFontScalar(10, 40)

        for (i in 0 until 6)
            for (j in 1 until 6) {
                assertNotEquals(Float.NaN, fontScalar.scale(i, j))
            }
    }
}
