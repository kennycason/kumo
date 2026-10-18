package com.kennycason.kumo

import com.kennycason.kumo.bg.Background
import com.kennycason.kumo.bg.RectangleBackground
import com.kennycason.kumo.collide.RectanglePixelCollidable
import com.kennycason.kumo.collide.checkers.CollisionChecker
import com.kennycason.kumo.collide.checkers.RectangleCollisionChecker
import com.kennycason.kumo.collide.checkers.RectanglePixelCollisionChecker
import com.kennycason.kumo.exception.KumoException
import com.kennycason.kumo.font.FontWeight
import com.kennycason.kumo.font.KumoFont
import com.kennycason.kumo.font.scale.FontScalar
import com.kennycason.kumo.font.scale.LinearFontScalar
import com.kennycason.kumo.image.AngleGenerator
import com.kennycason.kumo.image.CollisionRaster
import com.kennycason.kumo.logging.Logging
import com.kennycason.kumo.padding.Padder
import com.kennycason.kumo.padding.RectanglePadder
import com.kennycason.kumo.padding.WordPixelPadder
import com.kennycason.kumo.palette.ColorPalette
import com.kennycason.kumo.placement.RTreeWordPlacer
import com.kennycason.kumo.placement.RectangleWordPlacer
import com.kennycason.kumo.wordstart.RandomWordStart
import com.kennycason.kumo.wordstart.WordStartStrategy
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.io.OutputStream
import javax.imageio.ImageIO
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

open class WordCloud(
    protected val dimension: Dimension,
    protected val collisionMode: CollisionMode
) : Logging {

    protected val collisionChecker: CollisionChecker = deriveCollisionChecker(collisionMode)
    protected val collisionRaster: CollisionRaster = CollisionRaster(dimension)
    val bufferedImage: BufferedImage = BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB)
    protected val backgroundCollidable: RectanglePixelCollidable = RectanglePixelCollidable(collisionRaster, Point(0, 0))
    protected val padder: Padder = derivePadder(collisionMode)
    val skipped: MutableSet<Word> = HashSet()

    var padding: Int = 0

    var background: Background = RectangleBackground(dimension)

    var backgroundColor: Color? = Color.BLACK

    var fontScalar: FontScalar = LinearFontScalar(10, 40)

    var kumoFont: KumoFont = KumoFont("Comic Sans MS", FontWeight.BOLD)

    var angleGenerator: AngleGenerator = AngleGenerator()

    var wordPlacer: RectangleWordPlacer = RTreeWordPlacer()

    var colorPalette: ColorPalette = ColorPalette(0x02B6F2, 0x37C2F0, 0x7CCBE6, 0xC4E7F2, 0xFFFFFF)

    var wordStartStrategy: WordStartStrategy = RandomWordStart()

    open fun build(wordFrequencies: List<WordFrequency>) {
        val sorted = wordFrequencies.sorted()

        wordPlacer.reset()
        skipped.clear()

        background.mask(backgroundCollidable)

        var currentWord = 1
        for (word in buildWords(sorted, colorPalette)) {
            val point = wordStartStrategy.getStartingPoint(dimension, word)
            val placed = place(word, point)

            if (placed) {
                logger.info { "placed: ${word.word} ($currentWord/${sorted.size})" }
            } else {
                logger.info { "skipped: ${word.word} ($currentWord/${sorted.size})" }
                skipped.add(word)
            }
            currentWord++
        }

        drawForegroundToBackground()
    }

    fun writeToFile(outputFileName: String) {
        val extension = outputFileName.substringAfterLast('.', "")
        try {
            logger.info { "Saving WordCloud to: $outputFileName" }
            ImageIO.write(bufferedImage, extension, File(outputFileName))
        } catch (e: IOException) {
            logger.error(e) { "Failed to save WordCloud to: $outputFileName" }
        }
    }

    fun writeToStreamAsPNG(outputStream: OutputStream) {
        writeToStream("png", outputStream)
    }

    fun writeToStream(format: String, outputStream: OutputStream) {
        try {
            logger.debug { "Writing WordCloud image data to output stream" }
            ImageIO.write(bufferedImage, format, outputStream)
            logger.debug { "Done writing WordCloud image data to output stream" }
        } catch (e: IOException) {
            logger.error(e) { "Could not write WordCloud to output stream" }
            throw KumoException("Could not write WordCloud to output stream", e)
        }
    }

    protected open fun drawForegroundToBackground() {
        if (backgroundColor == null) return

        val backgroundBufferedImage = BufferedImage(dimension.width, dimension.height, bufferedImage.type)
        val graphics = backgroundBufferedImage.graphics
        try {
            graphics.color = backgroundColor
            graphics.fillRect(0, 0, dimension.width, dimension.height)
            graphics.drawImage(bufferedImage, 0, 0, null)
        } finally {
            graphics.dispose()
        }

        val graphics2 = bufferedImage.graphics
        try {
            graphics2.drawImage(backgroundBufferedImage, 0, 0, null)
        } finally {
            graphics2.dispose()
        }
    }

    protected open fun place(word: Word, start: Point): Boolean {
        val graphics = bufferedImage.graphics
        try {
            val maxRadius = computeRadius(dimension, start)
            val position = word.position

            var r = 0
            while (r < maxRadius) {
                var x = max(-start.x, -r)
                while (x <= min(r, dimension.width - start.x - 1)) {
                    position.x = start.x + x

                    val offset = sqrt((r * r - x * x).toDouble()).toInt()

                    position.y = start.y + offset
                    if (position.y >= 0 && position.y < dimension.height && canPlace(word)) {
                        collisionRaster.mask(word.collisionRaster, position)
                        graphics.drawImage(word.bufferedImage, position.x, position.y, null)
                        return true
                    }

                    position.y = start.y - offset
                    if (offset != 0 && position.y >= 0 && position.y < dimension.height && canPlace(word)) {
                        collisionRaster.mask(word.collisionRaster, position)
                        graphics.drawImage(word.bufferedImage, position.x, position.y, null)
                        return true
                    }
                    x++
                }
                r += 2
            }

            return false
        } finally {
            graphics.dispose()
        }
    }

    private fun canPlace(word: Word): Boolean {
        val position = word.position
        val dimensionOfWord = word.dimension

        if (position.y < 0 || position.y + dimensionOfWord.height > dimension.height) {
            return false
        } else if (position.x < 0 || position.x + dimensionOfWord.width > dimension.width) {
            return false
        }

        return when (collisionMode) {
            CollisionMode.RECTANGLE ->
                !backgroundCollidable.collide(word) && wordPlacer.place(word)
            CollisionMode.PIXEL_PERFECT ->
                !backgroundCollidable.collide(word)
        }
    }

    protected open fun buildWords(wordFrequencies: List<WordFrequency>, colorPalette: ColorPalette): List<Word> {
        val maxFrequency = maxFrequency(wordFrequencies)
        return wordFrequencies
            .filter { it.word.isNotEmpty() }
            .map { buildWord(it, maxFrequency, colorPalette) }
    }

    private fun buildWord(wordFrequency: WordFrequency, maxFrequency: Int, colorPalette: ColorPalette): Word {
        val graphics = bufferedImage.createGraphics()
        try {
            graphics.setRenderingHints(Word.getRenderingHints())

            val frequency = wordFrequency.frequency
            val fontHeight = fontScalar.scale(frequency, maxFrequency)
            val wordFont = wordFrequency.font ?: kumoFont
            val font = wordFont.font.deriveFont(fontHeight)
            val fontMetrics = graphics.getFontMetrics(font)

            val theta = angleGenerator.randomNext()
            val word = Word(wordFrequency.word, colorPalette.next(), fontMetrics, collisionChecker, theta)

            if (padding > 0) {
                padder.pad(word, padding)
            }
            return word
        } finally {
            graphics.dispose()
        }
    }

    companion object {
        fun computeRadius(dimension: Dimension, start: Point): Int {
            val maxDistanceX = max(start.x, dimension.width - start.x) + 1
            val maxDistanceY = max(start.y, dimension.height - start.y) + 1
            return ceil(sqrt((maxDistanceX * maxDistanceX + maxDistanceY * maxDistanceY).toDouble())).toInt()
        }

        private fun maxFrequency(wordFrequencies: List<WordFrequency>): Int =
            if (wordFrequencies.isEmpty()) 1 else wordFrequencies[0].frequency

        private fun derivePadder(collisionMode: CollisionMode): Padder = when (collisionMode) {
            CollisionMode.PIXEL_PERFECT -> WordPixelPadder()
            CollisionMode.RECTANGLE -> RectanglePadder()
        }

        private fun deriveCollisionChecker(collisionMode: CollisionMode): CollisionChecker = when (collisionMode) {
            CollisionMode.PIXEL_PERFECT -> RectanglePixelCollisionChecker()
            CollisionMode.RECTANGLE -> RectangleCollisionChecker()
        }
    }
}
