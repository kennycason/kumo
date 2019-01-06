package com.kennycason.kumo.awtimpl;

import com.kennycason.kumo.abst.ImageAbst;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageImpl extends ImageAbst<BufferedImage> {

    private BufferedImage bufferedImage;

    public ImageImpl(int width, int height) {
        super(width, height);
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public ImageImpl(InputStream inputStream){
        super(0, 0);
        try {
            bufferedImage = ImageIO.read(inputStream);
            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getColor(int x, int y) {
        return bufferedImage.getRGB(x, y);
    }

    @Override
    public BufferedImage getActual() {
        return bufferedImage;
    }
}
