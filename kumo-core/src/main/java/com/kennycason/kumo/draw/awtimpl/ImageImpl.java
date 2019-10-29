package com.kennycason.kumo.draw.awtimpl;

import com.kennycason.kumo.draw.IImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageImpl implements IImage<BufferedImage> {

    private BufferedImage bufferedImage;

    public ImageImpl(int width, int height) {
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public ImageImpl(InputStream inputStream){
        try {
            bufferedImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImageImpl(BufferedImage image){
        this.bufferedImage = image;
    }

    @Override
    public int getColor(int x, int y) {
        return bufferedImage.getRGB(x, y);
    }

    @Override
    public int getWidth() {
        return bufferedImage.getWidth();
    }

    @Override
    public int getHeight() {
        return bufferedImage.getHeight();
    }

    @Override
    public BufferedImage getActual() {
        return bufferedImage;
    }
}
