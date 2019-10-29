package com.kennycason.kumo.draw.androidimpl;

import android.graphics.Paint;
import com.kennycason.kumo.draw.IFont;
import com.kennycason.kumo.draw.IFontMetrics;

public class FontMetricsImpl implements IFontMetrics {

    private Paint paint;

    private FontImpl font;

    public FontMetricsImpl(IFont f){
        this.font = (FontImpl) f;
        this.paint = new Paint();
        this.paint.setTypeface(font.getActual());
        this.paint.setTextSize(font.getSize());
    }


    @Override
    public int getBottom() {
        return Math.round(paint.getFontMetrics().bottom);
    }

    //We need to negate this because kumo expects both numbers to be positive
    // - however, the top is negative and bottom positive in Android. For more info, see https://stackoverflow.com/a/27631737/5489898
    @Override
    public int getTop() {
        return Math.round(-paint.getFontMetrics().top);
    }

    @Override
    public int getLeading(){
        return Math.round(paint.getFontMetrics().leading);
    }

    @Override
    public int measure(String s) {
        return (int)Math.ceil(paint.measureText(s));
    }

    @Override
    public IFont getFont() {
        return font;
    }
}
