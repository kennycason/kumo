package com.kennycason.kumo.androidimpl;

import android.graphics.Paint;
import com.kennycason.kumo.abst.FontAbst;
import com.kennycason.kumo.abst.FontMetricsAbst;

public class FontMetricsImpl extends FontMetricsAbst {

    private Paint paint;

    private FontImpl font;

    public FontMetricsImpl(FontAbst f){
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
    public int measure(String s) {
        return (int)Math.ceil(paint.measureText(s));
    }

    @Override
    public FontAbst getFont() {
        return font;
    }
}
