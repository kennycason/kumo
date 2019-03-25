package com.kennycason.kumo.font.scale;

import org.junit.Assert;
import org.junit.Test;

public class LogFontScalarTest {

    @Test
    public void testScale() {
        final LogFontScalar fontScalar = new LogFontScalar(-1, 13);
        Assert.assertEquals(9.614654f, fontScalar.scale(4, 1, 8), 0.0f);

        final LogFontScalar fontScalar2 = new LogFontScalar(0, 0);
        Assert.assertEquals(Float.NaN, fontScalar2.scale(0, 0, 0), 0.0f);
    }
}
