package com.kennycason.kumo.awtimpl;

import com.kennycason.kumo.abst.FontAbst;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class FontImpl extends FontAbst<Font> {

    private Font font;

    public FontImpl(String fontFamily, Face weight, int size) {
        font = new Font(fontFamily, faceToStyle(weight), size);
    }

    public FontImpl(InputStream fromFile) {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, fromFile);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public FontImpl(Font font){
        this.font = font;
    }

    @Override
    public Font getActual() {
        return font;
    }

    @Override
    public void registerIfNecessary() {
        final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        graphicsEnvironment.registerFont(font);
    }

    @Override
    public FontAbst withSize(float size) {
        return new FontImpl(font.deriveFont(size));
    }

    private static int faceToStyle(Face face){
        int style = Font.PLAIN;
        if(face == Face.BOLD){
            style = Font.BOLD;
        }else if(face == Face.ITALIC){
            style = Font.ITALIC;
        }
        return style;
    }
}
