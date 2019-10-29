package com.kennycason.kumo.draw.androidimpl;

import android.graphics.*;
import android.graphics.Color;
import com.kennycason.kumo.draw.*;

public class GraphicsImpl implements IGraphics {

    private Canvas canvas;
    private Paint paint;

    public GraphicsImpl(IImage img){
        Bitmap bitmap = (Bitmap) img.getActual();
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setAntiAlias(true);
    }

    @Override
    public void drawImg(IImage img, int x, int y) {
        Bitmap bm = (Bitmap) img.getActual();
        paint.setAlpha(255);
        canvas.drawBitmap(bm, x, y, paint);
    }

    @Override
    public void drawString(String s, int x, int y, IColor color) {
        paint.setColor(color.getInt());
        paint.setAlpha(Color.alpha(color.getInt()));
        canvas.drawText(s, x, y, paint);
    }

    @Override
    public void drawRect(IColor color, int x, int y, int width, int height) {
        paint.setColor(color.getInt());
        paint.setAlpha(Color.alpha(color.getInt()));
        paint.setStyle(Paint.Style.FILL);
        //Mimic the behavior of AWT instead of using the Android one
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }

    @Override
    public void setFont(IFont font) {
        paint.setTypeface((Typeface) font.getActual());
        paint.setTextSize(font.getSize());
    }

    @Override
    public IFontMetrics getFontMetrics() {
        return new FontMetricsImpl(new FontImpl(paint.getTypeface(), paint.getTextSize()));
    }

}
