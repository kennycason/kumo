package com.kennycason.kumo.draw.awtimpl;

import com.kennycason.kumo.draw.*;

import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

public class GraphicsImpl implements IGraphics {

    private Font font;
    private Graphics2D graphics2D;

    public GraphicsImpl(IImage img) {
        BufferedImage bufferedImage = (BufferedImage) img.getActual();
        graphics2D = bufferedImage.createGraphics();
    }

    @Override
    public void drawImg(IImage img, int x, int y) {
        BufferedImage imageToDraw = (BufferedImage) img.getActual();
        graphics2D.drawImage(imageToDraw, x, y, null);
    }

    @Override
    public void drawString(String s, int x, int y, IColor color) {
        graphics2D.setColor((Color) ((VaryingImpl)color).getActual());
        graphics2D.drawString(s, x, y);
    }

    @Override
    public void drawRect(IColor color, int x, int y, int width, int height) {
        graphics2D.setColor((Color) ((VaryingImpl)color).getActual());
        graphics2D.fillRect(x, y, width, height);
    }

    @Override
    public void enableAntiAliasing() {
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
    }

    @Override
    public void setFont(IFont font) {
        this.font = (Font) font.getActual();
        graphics2D.setFont(this.font);
    }

    @Override
    public IFontMetrics getFontMetrics() {
        return new FontMetricsImpl(graphics2D.getFontMetrics(font));
    }
}
