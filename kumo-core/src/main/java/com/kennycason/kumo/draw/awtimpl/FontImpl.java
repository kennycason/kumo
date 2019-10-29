package com.kennycason.kumo.draw.awtimpl;

import com.kennycason.kumo.draw.FontFace;
import com.kennycason.kumo.draw.IFont;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FontImpl implements IFont<Font> {

    private Font font;

    public FontImpl(String fontFamily, FontFace weight, int size) {
        font = new Font(fontFamily, faceToStyle(weight), size);
    }

    public FontImpl(InputStream fromFile) {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, fromFile);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public FontImpl(File file){
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, file);
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
    public void setSize(float size) {
        font = font.deriveFont(size);
    }

    @Override
    public float getSize() {
        return font.getSize();
    }

    private static int faceToStyle(FontFace face){
        int style = Font.PLAIN;
        if(face == FontFace.BOLD){
            style = Font.BOLD;
        }else if(face == FontFace.ITALIC){
            style = Font.ITALIC;
        }
        return style;
    }
}
