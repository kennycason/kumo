package com.kennycason.kumo.font;

import com.kennycason.kumo.exception.KumoException;
import com.kennycason.kumo.interfaces.FontAbst;
import com.kennycason.kumo.interfaces.InstanceCreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by kenny on 7/3/14.
 */
public class KumoFont {
    private static final int DEFAULT_WEIGHT = 10;

    private final FontAbst font;

    public KumoFont(final String type, final FontAbst.Face weight) {
        this.font = InstanceCreator.font(type, weight, DEFAULT_WEIGHT);
    }

    public KumoFont(final FontAbst font) {
        this.font = font;
    }

    public KumoFont(final File file) {
        this(buildAndRegisterFont(file));
    }

    public KumoFont(final InputStream inputStream) {
        this(buildAndRegisterFont(inputStream));
    }

    private static FontAbst buildAndRegisterFont(final File file) {
        try {
            final FontAbst font = InstanceCreator.font(new FileInputStream(file));
            font.registerIfNecessary();
            return font;

        } catch (Exception e) {
            throw new KumoException(e.getMessage(), e);
        }
    }

    private static FontAbst buildAndRegisterFont(final InputStream inputStream) {
        try {
            final FontAbst font = InstanceCreator.font(inputStream);
            font.registerIfNecessary();
            return font;

        } catch (Exception e) {
            throw new KumoException(e.getMessage(), e);
        }
    }

    public FontAbst getFont() {
        return this.font;
    }

}
