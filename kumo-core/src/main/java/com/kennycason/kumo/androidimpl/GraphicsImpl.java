package com.kennycason.kumo.androidimpl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.kennycason.kumo.abst.*;

public class GraphicsImpl extends GraphicsAbst {

    private Canvas canvas;
    private Paint paint;

    public GraphicsImpl(ImageAbst img){
        Bitmap bitmap = ((ImageImpl) img).getActual();
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    public void drawImg(ImageAbst img, int x, int y) {
        Bitmap bm = ((ImageImpl)img).getActual();
        canvas.drawBitmap(bm, x, y, paint);
    }

    @Override
    public void drawString(String s, int x, int y, ColorAbst color) {
        paint.setColor(color.getInt());
        canvas.drawText(s, x, y, paint);
    }

    @Override
    public void drawRect(ColorAbst color, int x, int y, int width, int height) {
        paint.setColor(color.getInt());
        paint.setStyle(Paint.Style.FILL);
        //Mimic the behavior of AWT instead of using the Android one
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }

    @Override
    public void translate(int x, int y) {
        canvas.translate(x, y);
    }

    @Override
    public void rotate(double angle, int xPivot, int yPivot) {
        canvas.rotate((float) angle, xPivot, yPivot);
    }

    @Override
    public void enableAntiAliasing() {
        paint.setAntiAlias(true);
    }

    @Override
    public void setFont(FontAbst font) {
        paint.setTypeface(((FontImpl)font).getActual());
        paint.setTextSize(((FontImpl)font).getSize());
    }

    @Override
    public FontMetricsAbst getFontMetrics() {
        return new FontMetricsImpl(new FontImpl(paint.getTypeface(), paint.getTextSize()));
    }

}
