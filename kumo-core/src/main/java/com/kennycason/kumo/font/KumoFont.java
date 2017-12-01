package com.kennycason.kumo.font;

import com.kennycason.kumo.exception.KumoException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kenny on 7/3/14.
 */
public class KumoFont {
    private static final int DEFAULT_WEIGHT = 10;

    private final Font font;

    public KumoFont(final String type, final FontWeight weight) {
        this.font = new Font(type, weight.getWeight(), DEFAULT_WEIGHT);
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
            final Font font = Font.createFont(Font.TRUETYPE_FONT, file);
            registerFont(font);
            return font;

        } catch (final FontFormatException | IOException e) {
            throw new KumoException(e.getMessage(), e);
        }
    }

    private static Font buildAndRegisterFont(final InputStream inputStream) {
        try {
            final Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            registerFont(font);
            return font;

        } catch (final FontFormatException | IOException e) {
            throw new KumoException(e.getMessage(), e);
        }
    }

    private static void registerFont(final Font font) {
        final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        graphicsEnvironment.registerFont(font);
    }

    public Font getFont() {
        return this.font;
    }

}
