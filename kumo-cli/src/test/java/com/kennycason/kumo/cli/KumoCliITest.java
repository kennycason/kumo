package com.kennycason.kumo.cli;

import com.kennycason.kumo.IntegrationTest;
import org.junit.Ignore;
import org.junit.experimental.categories.Category;

/**
 * Created by kenny on 6/12/16.
 */
@Category(IntegrationTest.class)
@Ignore
public class KumoCliITest {

    public void simple() {
        KumoCli.main(new String[] {
                "--input", "https://en.wikipedia.org/wiki/Nintendo",
                "--output", "/tmp/nintendo.png"
        });
    }

    public void stopwords() {
        KumoCli.main(new String[] {
                "--input", "https://en.wikipedia.org/wiki/Nintendo",
                "--output", "/tmp/nintendo_stopwords.png",
                "--stop-words", "nintendo,the"
        });
    }

    public void wordCount() {
        KumoCli.main(new String[] {
                "--input", "https://en.wikipedia.org/wiki/Nintendo",
                "--output", "/tmp/nintendo_wordcount_10.png",
                "--word-count", "10"
        });
    }

    public void widthAndHeight() {
        KumoCli.main(new String[] {
                "--input", "https://en.wikipedia.org/wiki/Nintendo",
                "--output", "/tmp/nintendo_width_and_height.png",
                "--width", "100",
                "--height", "100"
        });
    }

    public void randomWordStart() {
        KumoCli.main(new String[] {
                "--input", "https://en.wikipedia.org/wiki/Nintendo",
                "--output", "/tmp/nintendo_random_wordstart.png",
                "--word-start", "random"
        });
    }

    public void font() {
        KumoCli.main(new String[] {
                "--input", "https://en.wikipedia.org/wiki/Nintendo",
                "--output", "/tmp/nintendo_font.png",
                "--font-scalar", "sqrt",
                "--font-type", "Impact",
                "--font-weight", "plain",
                "--font-size-min", "4",
                "--font-size-max", "100"
        });
    }

    
    public void normalizer() {
        KumoCli.main(new String[] {
                "--input", "https://en.wikipedia.org/wiki/Nintendo",
                "--output", "/tmp/nintendo_normalizers.png",
                "--normalizer", "uppercase,bubble"
        });
    }

    
    public void backgroundImage() {
        KumoCli.main(new String[] {
                "--input", "https://en.wikipedia.org/wiki/Nintendo",
                "--output", "/tmp/nintendo_whale_background.png",
                "--width", "990",
                "--height", "618",
                "--background", "https://raw.githubusercontent.com/kennycason/kumo/master/src/test/resources/backgrounds/whale.png"
        });
    }

    
    public void colorRgb() {
        KumoCli.main(new String[] {
                "--input", "https://en.wikipedia.org/wiki/Nintendo",
                "--output", "/tmp/nintendo_rgb_color.png",
                "--color", "(255,0,0),(0,255,0),(0,0,255)"
        });
    }

    
    public void colorRgbHex() {
        KumoCli.main(new String[] {
                "--input", "https://en.wikipedia.org/wiki/Nintendo",
                "--output", "/tmp/nintendo_color_rgb_hex.png",
                "--color", "(0xff,0,0),(0,0xff,0),(0,0,0xff)"
        });
    }

    
    public void colorHex() {
        KumoCli.main(new String[] {
                "--input", "https://en.wikipedia.org/wiki/Nintendo",
                "--output", "/tmp/nintendo_color_hex.png",
                "--color", "(0xffffff),(0xcccccc),(0x999999),(0x666666),(0x333333)"
        });
    }

    
    public void chinese() {
        KumoCli.main(new String[] {
                "--input", "https://zh.wikipedia.org/wiki/%E4%BB%BB%E5%A4%A9%E5%A0%82",
                "--output", "/tmp/nintendo_chinese.png",
                "--tokenizer", "chinese"
        });
    }

    
    public void polar() {
        KumoCli.main(new String[] {
                "--input", "https://en.wikipedia.org/wiki/Nintendo, https://en.wikipedia.org/wiki/PlayStation",
                "--output", "/tmp/nintendo_vs_playstation.png",
                "--type", "polar",
                "--color", "(0x00ff00),(0x00dd00),(0x007700)|(0xff0000),(0xdd0000),(0x770000)"
        });
    }

    
    public void layered() {
        KumoCli.main(new String[] {
                "--input", "https://www.haskell.org/, https://en.wikipedia.org/wiki/Haskell_(programming_language)",
                "--output", "/tmp/haskell_layered.png",
                "--background", "https://raw.githubusercontent.com/kennycason/kumo/master/src/test/resources/backgrounds/haskell_1.bmp,https://raw.githubusercontent.com/kennycason/kumo/master/src/test/resources/backgrounds/haskell_2.bmp",
                "--type", "layered",
                "--color", "(0xFA6C07),(0xFF7614),(0xFF8936)|(0x080706),(0x3B3029),(0x47362A)"
        });
    }
}
