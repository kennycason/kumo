package com.kennycason.kumo;

import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.abst.DimensionAbst;
import com.kennycason.kumo.abst.InstanceCreator;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class WordCloudTest {

    public static final List<WordFrequency> WORD_FREQUENCIES = Arrays.asList(new WordFrequency("apple", 22),
                                                                             new WordFrequency("baby", 3),
                                                                             new WordFrequency("back", 15),
                                                                             new WordFrequency("bear", 9),
                                                                             new WordFrequency("boy", 26));

    @Test
    public void testWriteToStreamAsPNG() throws IOException {
        final DimensionAbst dimension = InstanceCreator.dimension(200, 200);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.build(WORD_FREQUENCIES);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new RectangleBackground(dimension));

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        wordCloud.writeToStreamAsPNG(byteArrayOutputStream);

        final byte[] bytes = byteArrayOutputStream.toByteArray();
        assertNotEquals(0, bytes.length);

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        final ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream);
        final Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageInputStream);
        assertTrue(imageReaders.hasNext());
        final ImageReader imageReader = imageReaders.next();
        assertEquals("png", imageReader.getFormatName());
    }

}