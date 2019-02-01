package com.kennycason.kumo.awtimpl;

import com.kennycason.kumo.abst.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphicsImpl extends GraphicsAbst {

    private Font font;
    private Graphics2D graphics2D;

    public GraphicsImpl(ImageAbst img) {
        BufferedImage bufferedImage = ((ImageImpl) img).getActual();
        graphics2D = bufferedImage.createGraphics();
    }

    @Override
    public void drawImg(ImageAbst img, int x, int y) {
        BufferedImage imageToDraw = ((ImageImpl)img).getActual();
        graphics2D.drawImage(imageToDraw, x, y, null);
    }

    @Override
    public void drawString(String s, int x, int y, ColorAbst color) {
        graphics2D.setColor(((ColorImpl)color).getActual());
        graphics2D.drawString(s, x, y);
    }

    @Override
    public void drawRect(ColorAbst color, int x, int y, int width, int height) {
        graphics2D.setColor(((ColorImpl)color).getActual());
        graphics2D.fillRect(x, y, width, height);
    }

    @Override
    public void enableAntiAliasing() {
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
    }

    @Override
    public void setFont(FontAbst font) {
        this.font = ((FontImpl)font).getActual();
        graphics2D.setFont(this.font);
    }

    @Override
    public FontMetricsAbst getFontMetrics() {
        return new FontMetricsImpl(graphics2D.getFontMetrics(font));
    }
}
