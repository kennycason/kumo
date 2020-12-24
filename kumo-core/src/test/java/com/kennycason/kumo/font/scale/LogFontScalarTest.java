package com.kennycason.kumo.font.scale;

import org.junit.Assert;
import org.junit.Test;

public class LogFontScalarTest {

    @Test
    public void testScale() {
        final LogFontScalar fontScalar = new LogFontScalar(-1, 13);
        Assert.assertEquals(8.333333f, fontScalar.scale(4, 8), 0.0f);

        final LogFontScalar fontScalar2 = new LogFontScalar(0, 0);
        Assert.assertEquals(Float.NaN, fontScalar2.scale(0, 0), 0.0f);
    }

    @Test
    public void testNaN() {
        final LogFontScalar fontScalar = new LogFontScalar(10, 40);

        for (int i=0; i<6; i++)
            for (int j=1; j<6; j++) {
                Assert.assertNotEquals(Float.NaN, fontScalar.scale(i, j));
            }
    }
}
