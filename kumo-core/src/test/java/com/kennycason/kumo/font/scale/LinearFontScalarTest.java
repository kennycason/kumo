package com.kennycason.kumo.font.scale;

import org.junit.Assert;
import org.junit.Test;

public class LinearFontScalarTest {

    @Test
    public void testScale() {
        final LinearFontScalar fontScalar = new LinearFontScalar(-1, 13);
        Assert.assertEquals(6.0f, fontScalar.scale(4, 8), 0.0f);

        final LinearFontScalar fontScalar2 = new LinearFontScalar(0, 0);
        Assert.assertEquals(Float.NaN, fontScalar2.scale(0, 0), 0.0f);

    }

    @Test
    public void testNaN() {
        final LinearFontScalar fontScalar = new LinearFontScalar(10, 40);

        for (int i=0; i<6; i++)
            for (int j=1; j<6; j++) {
                Assert.assertNotEquals(Float.NaN, fontScalar.scale(i, j));
            }
    }
}
