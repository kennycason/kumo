package com.kennycason.kumo

import com.kennycason.kumo.bg.Background
import com.kennycason.kumo.exception.KumoException
import com.kennycason.kumo.font.KumoFont
import com.kennycason.kumo.font.scale.FontScalar
import com.kennycason.kumo.image.AngleGenerator
import com.kennycason.kumo.palette.ColorPalette
import com.kennycason.kumo.placement.RectangleWordPlacer
import com.kennycason.kumo.wordstart.WordStartStrategy
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

open class LayeredWordCloud(
    layers: Int,
    private val dimension: Dimension,
    collisionMode: CollisionMode
) {
    private val wordClouds: MutableList<WordCloud> = ArrayList()
    private var backgroundColor: Color = Color.BLACK

    init {
        for (i in 0 until layers) {
            val wordCloud = WordCloud(dimension, collisionMode)
            wordCloud.backgroundColor = null
            wordClouds.add(wordCloud)
        }
    }

    open fun build(layer: Int, wordFrequencies: List<WordFrequency>) {
        wordClouds[layer].build(wordFrequencies)
    }

    fun setPadding(layer: Int, padding: Int) {
        wordClouds[layer].padding = padding
    }

    fun setColorPalette(layer: Int, colorPalette: ColorPalette) {
        wordClouds[layer].colorPalette = colorPalette
    }

    fun setBackground(layer: Int, background: Background) {
        wordClouds[layer].background = background
    }

    fun setFontScalar(layer: Int, fontScalar: FontScalar) {
        wordClouds[layer].fontScalar = fontScalar
    }

    fun setKumoFont(layer: Int, kumoFont: KumoFont) {
        wordClouds[layer].kumoFont = kumoFont
    }

    fun setAngleGenerator(layer: Int, angleGenerator: AngleGenerator) {
        wordClouds[layer].angleGenerator = angleGenerator
    }

    fun setWordPlacer(layer: Int, wordPlacer: RectangleWordPlacer) {
        wordClouds[layer].wordPlacer = wordPlacer
    }

    fun setBackgroundColor(backgroundColor: Color) {
        this.backgroundColor = backgroundColor
    }

    fun getBufferedImage(): BufferedImage {
        val bufferedImage = BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB)
        val graphics = bufferedImage.graphics
        graphics.color = backgroundColor
        graphics.fillRect(0, 0, dimension.width, dimension.height)

        for (wordCloud in wordClouds) {
            graphics.drawImage(wordCloud.bufferedImage, 0, 0, null)
        }

        return bufferedImage
    }

    fun getLayer(layer: Int): WordCloud = wordClouds[layer]

    fun getAt(layer: Int): WordCloud = getLayer(layer)

    fun getSkipped(layer: Int): Set<Word> = wordClouds[layer].skipped

    open fun writeToFile(outputFileName: String) {
        var extension = ""
        val i = outputFileName.lastIndexOf('.')
        if (i > 0) {
            extension = outputFileName.substring(i + 1)
        }
        try {
            LOGGER.info("Saving Layered WordCloud to: {}", outputFileName)
            ImageIO.write(getBufferedImage(), extension, File(outputFileName))
        } catch (e: IOException) {
            throw KumoException(e)
        }
    }

    fun setWordStartStrategy(layer: Int, scheme: WordStartStrategy) {
        wordClouds[layer].wordStartStrategy = scheme
    }

    fun getLayers(): Int = wordClouds.size

    companion object {
        private val LOGGER = LoggerFactory.getLogger(LayeredWordCloud::class.java)
    }
}
