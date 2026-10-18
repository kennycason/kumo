package com.kennycason.kumo

import com.kennycason.kumo.bg.Background
import com.kennycason.kumo.font.FontWeight
import com.kennycason.kumo.font.KumoFont
import com.kennycason.kumo.font.scale.FontScalar
import com.kennycason.kumo.font.scale.LinearFontScalar
import com.kennycason.kumo.image.AngleGenerator
import com.kennycason.kumo.palette.ColorPalette
import com.kennycason.kumo.wordstart.RandomWordStart
import com.kennycason.kumo.wordstart.WordStartStrategy
import java.awt.Color
import java.awt.Dimension

/** Builds an independent layout. A seed reproduces built-in randomness on the same font/rendering environment. */
fun wordcloud(words: List<WordFrequency>, configure: WordCloudConfig.() -> Unit = {}): CloudLayout =
    WordCloudConfig().apply(configure).build(words)

class WordCloudConfig {
    private var width = 600
    private var height = 600

    var seed: Long? = null
    var padding = 0
    var collisionMode = CollisionMode.PIXEL_PERFECT
    var background: Background? = null
    var backgroundColor: Color? = Color.BLACK
    var fontScalar: FontScalar = LinearFontScalar(10, 40)
    var font: KumoFont = KumoFont("Comic Sans MS", FontWeight.BOLD)
    var angles = AngleGenerator()
    var colors = ColorPalette(0x02B6F2, 0x37C2F0, 0x7CCBE6, 0xC4E7F2, 0xFFFFFF)
    var randomColors = false
    var wordStart: WordStartStrategy = RandomWordStart()

    fun size(width: Int, height: Int) {
        require(width > 0 && height > 0) { "Canvas dimensions must be positive" }
        this.width = width
        this.height = height
    }

    internal fun build(words: List<WordFrequency>): CloudLayout {
        require(padding >= 0) { "Padding must not be negative" }
        require(words.all { it.frequency > 0 }) { "Word frequencies must be positive" }
        val cloud = WordCloud(Dimension(width, height), collisionMode)
        cloud.seed = seed
        cloud.padding = padding
        background?.let { cloud.background = it }
        cloud.backgroundColor = backgroundColor
        cloud.fontScalar = fontScalar
        cloud.kumoFont = font
        cloud.angleGenerator = angles
        cloud.colorPalette = colors
        cloud.randomColors = randomColors
        cloud.wordStartStrategy = wordStart
        cloud.build(words)
        return cloud.layout
    }
}
