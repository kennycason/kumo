package com.kennycason.kumo.font.scale;

import org.junit.Assert;
import org.junit.Test;

public class SqrtFontScalarTest {

    @Test
    public void testScale() {
        final SqrtFontScalar fontScalar = new SqrtFontScalar(-1, 13);
        Assert.assertEquals(6.656854f, fontScalar.scale(4, 1, 8), 0.0f);

        final SqrtFontScalar fontScalar2 = new SqrtFontScalar(0, 0);
        Assert.assertEquals(Float.NaN, fontScalar2.scale(0, 0, 0), 0.0f);
    }
}
