package com.kennycason.kumo.cli

import org.junit.jupiter.api.Test

class KumoCliITest {

    @Test
    fun simple() {
        KumoCli.main(arrayOf(
            "--input", "https://en.wikipedia.org/wiki/Nintendo",
            "--output", "/tmp/nintendo.png"
        ))
    }

    @Test
    fun stopwords() {
        KumoCli.main(arrayOf(
            "--input", "https://en.wikipedia.org/wiki/Nintendo",
            "--output", "/tmp/nintendo_stopwords.png",
            "--stop-words", "nintendo,the"
        ))
    }

    @Test
    fun wordCount() {
        KumoCli.main(arrayOf(
            "--input", "https://en.wikipedia.org/wiki/Nintendo",
            "--output", "/tmp/nintendo_wordcount_10.png",
            "--word-count", "10"
        ))
    }

    @Test
    fun widthAndHeight() {
        KumoCli.main(arrayOf(
            "--input", "https://en.wikipedia.org/wiki/Nintendo",
            "--output", "/tmp/nintendo_width_and_height.png",
            "--width", "100",
            "--height", "100"
        ))
    }

    @Test
    fun randomWordStart() {
        KumoCli.main(arrayOf(
            "--input", "https://en.wikipedia.org/wiki/Nintendo",
            "--output", "/tmp/nintendo_random_wordstart.png",
            "--word-start", "random"
        ))
    }

    @Test
    fun font() {
        KumoCli.main(arrayOf(
            "--input", "https://en.wikipedia.org/wiki/Nintendo",
            "--output", "/tmp/nintendo_font.png",
            "--font-scalar", "sqrt",
            "--font-type", "Impact",
            "--font-weight", "plain",
            "--font-size-min", "4",
            "--font-size-max", "100"
        ))
    }

    @Test
    fun normalizer() {
        KumoCli.main(arrayOf(
            "--input", "https://en.wikipedia.org/wiki/Nintendo",
            "--output", "/tmp/nintendo_normalizers.png",
            "--normalizer", "uppercase,bubble"
        ))
    }

    @Test
    fun backgroundImage() {
        KumoCli.main(arrayOf(
            "--input", "https://en.wikipedia.org/wiki/Nintendo",
            "--output", "/tmp/nintendo_whale_background.png",
            "--width", "990",
            "--height", "618",
            "--background", "https://raw.githubusercontent.com/kennycason/kumo/master/src/test/resources/backgrounds/whale.png"
        ))
    }

    @Test
    fun colorRgb() {
        KumoCli.main(arrayOf(
            "--input", "https://en.wikipedia.org/wiki/Nintendo",
            "--output", "/tmp/nintendo_rgb_color.png",
            "--color", "(255,0,0),(0,255,0),(0,0,255)"
        ))
    }

    @Test
    fun colorRgbHex() {
        KumoCli.main(arrayOf(
            "--input", "https://en.wikipedia.org/wiki/Nintendo",
            "--output", "/tmp/nintendo_color_rgb_hex.png",
            "--color", "(0xff,0,0),(0,0xff,0),(0,0,0xff)"
        ))
    }

    @Test
    fun colorHex() {
        KumoCli.main(arrayOf(
            "--input", "https://en.wikipedia.org/wiki/Nintendo",
            "--output", "/tmp/nintendo_color_hex.png",
            "--color", "(0xffffff),(0xcccccc),(0x999999),(0x666666),(0x333333)"
        ))
    }

    @Test
    fun chinese() {
        KumoCli.main(arrayOf(
            "--input", "https://zh.wikipedia.org/wiki/%E4%BB%BB%E5%A4%A9%E5%A0%82",
            "--output", "/tmp/nintendo_chinese.png",
            "--tokenizer", "chinese"
        ))
    }

    @Test
    fun polar() {
        KumoCli.main(arrayOf(
            "--input", "https://en.wikipedia.org/wiki/Nintendo, https://en.wikipedia.org/wiki/PlayStation",
            "--output", "/tmp/nintendo_vs_playstation.png",
            "--type", "polar",
            "--color", "(0x00ff00),(0x00dd00),(0x007700)|(0xff0000),(0xdd0000),(0x770000)"
        ))
    }

    @Test
    fun layered() {
        KumoCli.main(arrayOf(
            "--input", "https://www.haskell.org/, https://en.wikipedia.org/wiki/Haskell_(programming_language)",
            "--output", "/tmp/haskell_layered.png",
            "--background", "https://raw.githubusercontent.com/kennycason/kumo/master/src/test/resources/backgrounds/haskell_1.bmp,https://raw.githubusercontent.com/kennycason/kumo/master/src/test/resources/backgrounds/haskell_2.bmp",
            "--type", "layered",
            "--color", "(0xFA6C07),(0xFF7614),(0xFF8936)|(0x080706),(0x3B3029),(0x47362A)"
        ))
    }
}
