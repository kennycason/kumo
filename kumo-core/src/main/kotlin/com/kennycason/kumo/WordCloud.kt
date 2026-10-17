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
import com.kennycason.kumo.padding.Padder
import com.kennycason.kumo.padding.RectanglePadder
import com.kennycason.kumo.padding.WordPixelPadder
import com.kennycason.kumo.palette.ColorPalette
import com.kennycason.kumo.placement.RTreeWordPlacer
import com.kennycason.kumo.placement.RectangleWordPlacer
import com.kennycason.kumo.wordstart.RandomWordStart
import com.kennycason.kumo.wordstart.WordStartStrategy
import org.slf4j.LoggerFactory
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
) {
    protected val collisionChecker: CollisionChecker = deriveCollisionChecker(collisionMode)
    protected val collisionRaster: CollisionRaster = CollisionRaster(dimension)
    val bufferedImage: BufferedImage = BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB)
    protected val backgroundCollidable: RectanglePixelCollidable = RectanglePixelCollidable(collisionRaster, Point(0, 0))
    protected val padder: Padder = derivePadder(collisionMode)
    val skipped: MutableSet<Word> = HashSet()

    @set:JvmName("setPadding")
    @get:JvmName("getPadding")
    var padding: Int = 0

    @set:JvmName("setBackground")
    var background: Background = RectangleBackground(dimension)

    @set:JvmName("setBackgroundColor")
    var backgroundColor: Color? = Color.BLACK

    @set:JvmName("setFontScalar")
    var fontScalar: FontScalar = LinearFontScalar(10, 40)

    @set:JvmName("setKumoFont")
    var kumoFont: KumoFont = KumoFont("Comic Sans MS", FontWeight.BOLD)

    @set:JvmName("setAngleGenerator")
    var angleGenerator: AngleGenerator = AngleGenerator()

    @set:JvmName("setWordPlacer")
    var wordPlacer: RectangleWordPlacer = RTreeWordPlacer()

    @set:JvmName("setColorPalette")
    var colorPalette: ColorPalette = ColorPalette(0x02B6F2, 0x37C2F0, 0x7CCBE6, 0xC4E7F2, 0xFFFFFF)

    @set:JvmName("setWordStartStrategy")
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
                if (LOGGER.isInfoEnabled) {
                    LOGGER.info("placed: {} ({}/{})", word.word, currentWord, sorted.size)
                }
            } else {
                if (LOGGER.isInfoEnabled) {
                    LOGGER.info("skipped: {} ({}/{})", word.word, currentWord, sorted.size)
                }
                skipped.add(word)
            }
            currentWord++
        }

        drawForegroundToBackground()
    }

    fun writeToFile(outputFileName: String) {
        var extension = ""
        val i = outputFileName.lastIndexOf('.')
        if (i > 0) {
            extension = outputFileName.substring(i + 1)
        }
        try {
            LOGGER.info("Saving WordCloud to: {}", outputFileName)
            ImageIO.write(bufferedImage, extension, File(outputFileName))
        } catch (e: IOException) {
            LOGGER.error(e.message, e)
        }
    }

    fun writeToStreamAsPNG(outputStream: OutputStream) {
        writeToStream("png", outputStream)
    }

    fun writeToStream(format: String, outputStream: OutputStream) {
        try {
            LOGGER.debug("Writing WordCloud image data to output stream")
            ImageIO.write(bufferedImage, format, outputStream)
            LOGGER.debug("Done writing WordCloud image data to output stream")
        } catch (e: IOException) {
            LOGGER.error(e.message, e)
            throw KumoException("Could not write wordcloud to outputstream due to an IOException", e)
        }
    }

    protected open fun drawForegroundToBackground() {
        if (backgroundColor == null) return

        val backgroundBufferedImage = BufferedImage(dimension.width, dimension.height, bufferedImage.type)
        val graphics = backgroundBufferedImage.graphics

        graphics.color = backgroundColor
        graphics.fillRect(0, 0, dimension.width, dimension.height)
        graphics.drawImage(bufferedImage, 0, 0, null)

        val graphics2 = bufferedImage.graphics
        graphics2.drawImage(backgroundBufferedImage, 0, 0, null)
    }

    protected open fun place(word: Word, start: Point): Boolean {
        val graphics = bufferedImage.graphics

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
        graphics.setRenderingHints(Word.getRenderingHints())

        val frequency = wordFrequency.frequency
        val fontHeight = fontScalar.scale(frequency, maxFrequency)
        val font = (if (wordFrequency.hasFont()) wordFrequency.font else kumoFont)!!.font.deriveFont(fontHeight)
        val fontMetrics = graphics.getFontMetrics(font)

        val theta = angleGenerator.randomNext()
        val word = Word(wordFrequency.word, colorPalette.next(), fontMetrics, collisionChecker, theta)

        if (padding > 0) {
            padder.pad(word, padding)
        }
        return word
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(WordCloud::class.java)

        @JvmStatic
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
