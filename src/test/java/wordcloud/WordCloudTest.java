package wordcloud;

import org.junit.Test;
import wordcloud.bg.RectangleBackground;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    public void testWriteToStreamAsPNG() throws Exception {

        final WordCloud wordCloud = new WordCloud(200, 200, CollisionMode.PIXEL_PERFECT);
        wordCloud.build(WORD_FREQUENCIES);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new RectangleBackground(120, 120));


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