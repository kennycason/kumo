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
import java.util.Random
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
    private val placedWords = mutableListOf<PlacedWord>()
    private val skippedWords = mutableListOf<SkippedWord>()

    var layout: CloudLayout = CloudLayout(dimension.width, dimension.height, Color.BLACK, null, emptyList(), emptyList())
        private set

    /** Built-in random choices start from this seed on every build. Null uses fresh randomness. */
    var seed: Long? = null
    protected var random: Random = Random()
        private set

    var padding: Int = 0

    var background: Background = RectangleBackground(dimension)

    var backgroundColor: Color? = Color.BLACK

    var fontScalar: FontScalar = LinearFontScalar(10, 40)

    var kumoFont: KumoFont = KumoFont("Comic Sans MS", FontWeight.BOLD)

    var angleGenerator: AngleGenerator = AngleGenerator()

    var wordPlacer: RectangleWordPlacer = RTreeWordPlacer()

    var colorPalette: ColorPalette = ColorPalette(0x02B6F2, 0x37C2F0, 0x7CCBE6, 0xC4E7F2, 0xFFFFFF)

    var randomColors: Boolean = false

    var wordStartStrategy: WordStartStrategy = RandomWordStart()

    open fun build(wordFrequencies: List<WordFrequency>) {
        val sorted = wordFrequencies.sorted()

        resetBuildState()

        background.mask(backgroundCollidable)

        var currentWord = 1
        for (word in buildWords(sorted, colorPalette)) {
            val point = wordStartStrategy.getStartingPoint(dimension, word, random)
            val placed = place(word, point)

            if (placed) {
                logger.info { "placed: ${word.word} ($currentWord/${sorted.size})" }
            } else {
                logger.info { "skipped: ${word.word} ($currentWord/${sorted.size})" }
                recordSkipped(word)
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
        layout = CloudLayout(dimension.width, dimension.height, backgroundColor, seed, placedWords, skippedWords)
        layout.renderTo(bufferedImage)
    }

    protected fun resetBuildState() {
        require(padding >= 0) { "Padding must not be negative" }
        random = seed?.let { Random(it) } ?: Random()
        wordPlacer.reset()
        skipped.clear()
        placedWords.clear()
        skippedWords.clear()
        collisionRaster.clear()
        val graphics = bufferedImage.createGraphics()
        try {
            graphics.composite = AlphaComposite.Clear
            graphics.fillRect(0, 0, dimension.width, dimension.height)
        } finally {
            graphics.dispose()
        }
    }

    protected open fun place(word: Word, start: Point): Boolean {
        if (word.dimension.width > dimension.width || word.dimension.height > dimension.height) return false
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
                    placedWords.add(word.snapshot())
                    return true
                }

                position.y = start.y - offset
                if (offset != 0 && position.y >= 0 && position.y < dimension.height && canPlace(word)) {
                    collisionRaster.mask(word.collisionRaster, position)
                    placedWords.add(word.snapshot())
                    return true
                }
                x++
            }
            r += 2
        }

        return false
    }

    protected fun recordSkipped(word: Word) {
        skipped.add(word)
        val reason = if (word.dimension.width > dimension.width || word.dimension.height > dimension.height) {
            SkipReason.TOO_LARGE
        } else {
            SkipReason.NO_SPACE
        }
        skippedWords.add(SkippedWord(word.word, word.frequency, reason))
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
        // Palette cursors belong to this build, not to shared configuration.
        val palette = ColorPalette(colorPalette.colors)
        require(palette.colors.isNotEmpty()) { "A color palette must contain at least one color" }
        for (frequency in wordFrequencies.filter { it.word.isBlank() }) {
            skippedWords.add(SkippedWord(frequency.word, frequency.frequency, SkipReason.BLANK_TEXT))
        }
        val maxFrequency = maxFrequency(wordFrequencies)
        return wordFrequencies
            .filter { it.word.isNotBlank() }
            .map { buildWord(it, maxFrequency, palette) }
    }

    private fun buildWord(wordFrequency: WordFrequency, maxFrequency: Int, colorPalette: ColorPalette): Word {
        val graphics = bufferedImage.createGraphics()
        try {
            graphics.setRenderingHints(Word.getRenderingHints())

            val frequency = wordFrequency.frequency
            val fontHeight = fontScalar.scale(frequency, maxFrequency)
            require(fontHeight.isFinite() && fontHeight > 0) { "Font size must be finite and positive" }
            val wordFont = wordFrequency.font ?: kumoFont
            val font = wordFont.font.deriveFont(fontHeight)
            val fontMetrics = graphics.getFontMetrics(font)

            val theta = angleGenerator.randomNext(random)
            val color = if (randomColors) colorPalette.randomNext(random) else colorPalette.next()
            val word = Word(wordFrequency.word, color, fontMetrics, collisionChecker, theta, frequency)

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
