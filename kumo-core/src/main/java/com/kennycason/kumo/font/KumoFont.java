package com.kennycason.kumo.font;

import com.kennycason.kumo.draw.Font;
import com.kennycason.kumo.draw.FontFace;
import com.kennycason.kumo.exception.KumoException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by kenny on 7/3/14.
 */
public class KumoFont {
    private static final int DEFAULT_WEIGHT = 10;

    private final Font font;

    public KumoFont(final String type, final FontFace weight) {
        this.font = new Font(type, weight, DEFAULT_WEIGHT);
    }

    public KumoFont(final Font font) {
        this.font = font;
    }

    public KumoFont(final File file) {
        this(buildAndRegisterFont(file));
    }

    public KumoFont(final InputStream inputStream) {
        this(buildAndRegisterFont(inputStream));
    }

    private static Font buildAndRegisterFont(final File file) {
        try {
            final Font font = new Font(new FileInputStream(file));
            font.registerIfNecessary();
            return font;

        } catch (Exception e) {
            throw new KumoException(e.getMessage(), e);
        }
    }

    private static Font buildAndRegisterFont(final InputStream inputStream) {
        try {
            final Font font = new Font(inputStream);
            font.registerIfNecessary();
            return font;

        } catch (Exception e) {
            throw new KumoException(e.getMessage(), e);
        }
    }

    public Font getFont() {
        return this.font;
    }

}
