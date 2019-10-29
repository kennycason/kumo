package com.kennycason.kumo.draw.androidimpl;

import android.graphics.Typeface;
import com.kennycason.kumo.draw.FontFace;
import com.kennycason.kumo.draw.IFont;

import java.io.File;
import java.io.InputStream;

public class FontImpl implements IFont<Typeface> {

    private Typeface typeface;

    private float size;

    public FontImpl(String fontFamily, FontFace weight, int size){
        typeface = Typeface.create(fontFamily, getStyle(weight));
        this.size = size;
    }

    public FontImpl(InputStream fromInputStream){
        throw new UnsupportedOperationException("Constructing a font from an InputStream is not supported on Android. Use new FontImpl(file) instead.");
    }

    public FontImpl(File file){
        typeface = Typeface.createFromFile(file);
        size = 40;
    }

    public FontImpl(Typeface typeface){
        this.typeface = typeface;
        size = 40;
    }

    public FontImpl(Typeface typeface, float size){
        this.typeface = typeface;
        this.size = size;
    }

    @Override
    public void registerIfNecessary() {
        //Not necessary on Android
    }

    @Override
    public void setSize(float size) {
        this.size = size;
    }

    @Override
    public float getSize() {
        return size;
    }

    @Override
    public Typeface getActual() {
        return typeface;
    }

    private int getStyle(FontFace weight){
        if(weight == FontFace.BOLD){
            return Typeface.BOLD;
        }else if(weight == FontFace.ITALIC){
            return Typeface.ITALIC;
        }else {
            return Typeface.NORMAL;
        }
    }
}
