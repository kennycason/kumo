package com.kennycason.kumo

import com.kennycason.kumo.exception.KumoException
import com.kennycason.kumo.logging.Logging
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
) : Logging {

    private val wordClouds = List(layers) {
        WordCloud(dimension, collisionMode).apply {
            backgroundColor = null
        }
    }
    var backgroundColor: Color = Color.BLACK
    val layers: Int
        get() = wordClouds.size

    open fun build(layer: Int, wordFrequencies: List<WordFrequency>) {
        wordClouds[layer].build(wordFrequencies)
    }

    val bufferedImage: BufferedImage
        get() {
            val bufferedImage = BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB)
            val graphics = bufferedImage.graphics
            try {
                graphics.color = backgroundColor
                graphics.fillRect(0, 0, dimension.width, dimension.height)

                for (wordCloud in wordClouds) {
                    graphics.drawImage(wordCloud.bufferedImage, 0, 0, null)
                }
            } finally {
                graphics.dispose()
            }

            return bufferedImage
        }

    operator fun get(layer: Int): WordCloud = wordClouds[layer]

    open fun writeToFile(outputFileName: String) {
        val extension = outputFileName.substringAfterLast('.', "")
        try {
            logger.info { "Saving Layered WordCloud to: $outputFileName" }
            ImageIO.write(bufferedImage, extension, File(outputFileName))
        } catch (e: IOException) {
            throw KumoException("Failed to save Layered WordCloud to: $outputFileName", e)
        }
    }
}
