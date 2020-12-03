package com.kennycason.kumo.font.scale;

import org.junit.Assert;
import org.junit.Test;

public class SqrtFontScalarTest {

    @Test
    public void testScale() {
        final SqrtFontScalar fontScalar = new SqrtFontScalar(-1, 13);
        Assert.assertEquals(8.899495f, fontScalar.scale(4, 8), 0.0f);

        final SqrtFontScalar fontScalar2 = new SqrtFontScalar(0, 0);
        Assert.assertEquals(Float.NaN, fontScalar2.scale(0, 0), 0.0f);
    }

    @Test
    public void testNaN() {
        final SqrtFontScalar fontScalar = new SqrtFontScalar(10, 40);

        for (int i=0; i<6; i++)
            for (int j=1; j<6; j++) {
                Assert.assertNotEquals(Float.NaN, fontScalar.scale(i, j));
            }
    }
}
