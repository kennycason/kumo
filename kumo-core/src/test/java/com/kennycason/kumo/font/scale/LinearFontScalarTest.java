package com.kennycason.kumo.font.scale;

import org.junit.Assert;
import org.junit.Test;

public class LinearFontScalarTest {

    @Test
    public void testScale() {
        final LinearFontScalar fontScalar = new LinearFontScalar(-1, 13);
        Assert.assertEquals(5.0f, fontScalar.scale(4, 1, 8), 0.0f);

        final LinearFontScalar fontScalar2 = new LinearFontScalar(0, 0);
        Assert.assertEquals(Float.NaN, fontScalar2.scale(0, 0, 0), 0.0f);
    }
}
