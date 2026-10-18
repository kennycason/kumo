package com.kennycason.kumo

import com.kennycason.kumo.bg.CircleBackground
import com.kennycason.kumo.bg.RectangleBackground
import com.kennycason.kumo.font.FontWeight
import com.kennycason.kumo.font.KumoFont
import com.kennycason.kumo.font.scale.LinearFontScalar
import com.kennycason.kumo.image.AngleGenerator
import com.kennycason.kumo.palette.ColorPalette
import java.awt.Color
import java.awt.Dimension
import java.awt.geom.Path2D
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.file.Path
import javax.imageio.ImageIO
import javax.xml.parsers.DocumentBuilderFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isNotEqualTo
import strikt.assertions.isTrue
import kotlin.math.abs

class CloudLayoutTest {
    @TempDir
    lateinit var directory: Path

    private val words = listOf(
        WordFrequency("Kotlin", 10), WordFrequency("cloud", 8), WordFrequency("layout", 6),
        WordFrequency("seed", 4), WordFrequency("vector", 2), WordFrequency("export", 1)
    )

    @Test
    fun supportsTheLowercaseDslAndExposesPlacedWordMetadata() {
        val wordcloud = wordcloud(words) {
            size(1000, 700)
            seed = 42
            padding = 2
        }
        expectThat(wordcloud.width).isEqualTo(1000)
        expectThat(wordcloud.height).isEqualTo(700)
        expectThat(wordcloud.seed).isEqualTo(42L)
        expectThat(wordcloud.words.map { it.word }).containsExactly(words.map { it.word })
        expectThat(wordcloud.words.map { it.frequency }).containsExactly(words.map { it.frequency })
        expectThat(wordcloud.words.all {
            it.bounds.x >= 0 && it.bounds.y >= 0 &&
                it.bounds.x + it.bounds.width <= wordcloud.width &&
                it.bounds.y + it.bounds.height <= wordcloud.height && it.fontSize > 0
        }).isTrue()
        expectThat(wordcloud.skipped).isEmpty()
    }

    @Test
    fun seedControlsPositionRotationAndRandomColorsWithoutSharedCursors() {
        val palette = ColorPalette(Color.RED, Color.GREEN, Color.BLUE)
        val angles = AngleGenerator(-45.0, 45.0, 5)
        fun build(seedValue: Long) = wordcloud(words) {
            size(400, 300)
            seed = seedValue
            colors = palette
            this.angles = angles
            randomColors = true
        }
        val first = build(42)
        palette.next()
        angles.next()
        build(99)
        val second = build(42)
        expectThat(first.toPng().contentEquals(second.toPng())).isTrue()
        expectThat(first.toSvg()).isEqualTo(second.toSvg())
        expectThat(first.words.map { it.bounds }).containsExactly(second.words.map { it.bounds })
        expectThat(first.toSvg()).isNotEqualTo(build(43).toSvg())
    }

    @Test
    fun rebuildingACloudIsIndependentInBothCollisionModes() {
        for (mode in CollisionMode.entries) {
            val cloud = WordCloud(Dimension(400, 300), mode).apply { seed = 42 }
            cloud.build(words)
            val first = cloud.layout
            val png = first.toPng()
            val svg = first.toSvg()
            cloud.build(words)
            expectThat(cloud.layout.toPng().contentEquals(png)).isTrue()
            cloud.build(emptyList())
            expectThat(cloud.layout.words).isEmpty()
            expectThat(cloud.layout.skipped).isEmpty()
            expectThat(first.toPng().contentEquals(png)).isTrue()
            expectThat(first.toSvg()).isEqualTo(svg)
            cloud.backgroundColor = null
            cloud.build(emptyList())
            val empty = cloud.bufferedImage
            expectThat(empty.getRGB(0, 0, empty.width, empty.height, null, 0, empty.width).all { it == 0 }).isTrue()
        }
    }

    @Test
    fun changingMasksDoesNotLeaveOldOccupiedPixelsBehind() {
        val cloud = WordCloud(Dimension(240, 180), CollisionMode.PIXEL_PERFECT).apply {
            seed = 42
            background = CircleBackground(60)
            build(words)
            background = RectangleBackground(Dimension(240, 180))
            build(words)
        }
        val fresh = wordcloud(words) { size(240, 180); seed = 42 }
        expectThat(cloud.layout.toPng().contentEquals(fresh.toPng())).isTrue()
    }

    @Test
    fun polarLayoutsAlsoResetTheirStateAndUseTheBuildSeed() {
        val cloud = PolarWordCloud(Dimension(400, 300), CollisionMode.RECTANGLE, PolarBlendMode.BLUR).apply {
            seed = 12
        }
        cloud.build(words, words)
        val first = cloud.layout.toSvg()
        cloud.build(words, words)
        expectThat(cloud.layout.toSvg()).isEqualTo(first)
        cloud.build(emptyList(), emptyList())
        expectThat(cloud.layout.words).isEmpty()
    }

    @Test
    fun tracksBlankOversizedAndUnplaceableWords() {
        val oversized = wordcloud(listOf(WordFrequency("", 3), WordFrequency(" ", 2), WordFrequency("huge", 1))) {
            size(1, 1)
            seed = 1
        }
        expectThat(oversized.skipped.map { it.reason })
            .containsExactly(SkipReason.BLANK_TEXT, SkipReason.BLANK_TEXT, SkipReason.TOO_LARGE)

        val full = wordcloud(List(3) { WordFrequency("W", 1) }) {
            size(45, 45)
            seed = 1
            font = KumoFont("Dialog", FontWeight.BOLD)
            fontScalar = LinearFontScalar(30, 30)
            angles = AngleGenerator(0)
            collisionMode = CollisionMode.RECTANGLE
        }
        expectThat(full.words.size).isEqualTo(1)
        expectThat(full.skipped.map { it.reason }).containsExactly(SkipReason.NO_SPACE, SkipReason.NO_SPACE)
    }

    @Test
    fun pngAndSvgCanBeWrittenToFilesAndCallerOwnedStreams() {
        val layout = wordcloud(words) { size(400, 300); seed = 42 }
        val png = directory.resolve("cloud.png").toFile()
        val svg = directory.resolve("cloud.svg").toFile()
        layout.writePng(png.path)
        layout.writeSvg(svg)
        expectThat(ImageIO.read(png).width).isEqualTo(400)
        expectThat(svg.readText()).isEqualTo(layout.toSvg())
        for (write in listOf<(OutputStream) -> Unit>(layout::writePng, layout::writeSvg)) {
            val stream = object : ByteArrayOutputStream() {
                var closed = false
                override fun close() { closed = true; super.close() }
            }
            write(stream)
            expectThat(stream.closed).isFalse()
            expectThat(stream.size() > 0).isTrue()
        }
        assertThrows<IOException> { layout.writePng(directory.resolve("missing/cloud.png").toFile()) }
    }

    @Test
    fun svgContainsEscapedVectorPathsAndPreservesAlpha() {
        val text = "Kotlin <&> \"' 日本語"
        val layout = wordcloud(listOf(WordFrequency(text, 10))) {
            size(700, 200)
            seed = 42
            angles = AngleGenerator(0)
            backgroundColor = null
            colors = ColorPalette(Color(10, 20, 30, 128))
        }
        val xml = DocumentBuilderFactory.newInstance().apply { isNamespaceAware = true }
            .newDocumentBuilder().parse(layout.toSvg().byteInputStream())
        expectThat(xml.documentElement.namespaceURI).isEqualTo("http://www.w3.org/2000/svg")
        expectThat(xml.getElementsByTagName("path").length).isEqualTo(1)
        expectThat(xml.getElementsByTagName("image").length).isEqualTo(0)
        expectThat(xml.getElementsByTagName("rect").length).isEqualTo(0)
        expectThat(xml.getElementsByTagName("title").item(0).textContent).isEqualTo(text)
        val path = xml.getElementsByTagName("path").item(0).attributes
        expectThat(path.getNamedItem("fill").nodeValue).isEqualTo("#0a141e")
        expectThat(path.getNamedItem("fill-opacity").nodeValue.toDouble()).isEqualTo(128 / 255.0)
        expectThat(path.getNamedItem("d").nodeValue.isNotBlank()).isTrue()
        expectThat(layout.toBufferedImage().getRGB(0, 0) ushr 24).isEqualTo(0)
    }

    @Test
    fun renderingImagesAndMutatingLegacyBuffersCannotChangeASnapshot() {
        val cloud = WordCloud(Dimension(400, 300), CollisionMode.PIXEL_PERFECT).apply { seed = 42; build(words) }
        val layout = cloud.layout
        val before = layout.toPng()
        val image = layout.toBufferedImage()
        image.setRGB(0, 0, Color.MAGENTA.rgb)
        cloud.bufferedImage.setRGB(0, 0, Color.MAGENTA.rgb)
        expectThat(layout.toPng().contentEquals(before)).isTrue()
        assertThrows<UnsupportedOperationException> { (layout.words as MutableList<PlacedWord>).clear() }
    }

    @Test
    fun validatesCanvasPaddingAndFrequencies() {
        assertThrows<IllegalArgumentException> { wordcloud(words) { size(0, 100) } }
        assertThrows<IllegalArgumentException> { wordcloud(words) { padding = -1 } }
        assertThrows<IllegalArgumentException> { wordcloud(listOf(WordFrequency("invalid", 0))) }
        assertThrows<IllegalArgumentException> { wordcloud(words) { colors = ColorPalette(emptyList()) } }
    }

    @Test
    fun vectorPathsFollowRasterPositionPaddingAndRotation() {
        for (degrees in listOf(0, 45, 90, -90)) {
            for (wordPadding in listOf(0, 4)) {
                val layout = wordcloud(listOf(WordFrequency("Kotlin", 1))) {
                    size(300, 300)
                    seed = 42
                    padding = wordPadding
                    backgroundColor = null
                    angles = AngleGenerator(degrees)
                    font = KumoFont("Dialog", FontWeight.BOLD)
                }
                val image = layout.toBufferedImage()
                val pixels = (0 until image.height).flatMap { y ->
                    (0 until image.width).filter { x -> image.getRGB(x, y) ushr 24 > 0 }.map { x -> x to y }
                }
                val vector = parsePath(layout.words.single().svgPath()).bounds2D
                expectThat(abs(vector.minX - pixels.minOf { it.first }) < 3).isTrue()
                expectThat(abs(vector.minY - pixels.minOf { it.second }) < 3).isTrue()
                expectThat(abs(vector.maxX - pixels.maxOf { it.first } - 1) < 3).isTrue()
                expectThat(abs(vector.maxY - pixels.maxOf { it.second } - 1) < 3).isTrue()
            }
        }
    }

    private fun parsePath(data: String): Path2D.Double {
        val tokens = Regex("[MLQCZ]|-?[0-9]+(?:\\.[0-9]+)?(?:E[+-]?[0-9]+)?")
            .findAll(data).map { it.value }.iterator()
        fun coordinate() = tokens.next().toDouble()
        return Path2D.Double().apply {
            while (tokens.hasNext()) {
                when (tokens.next()) {
                    "M" -> moveTo(coordinate(), coordinate())
                    "L" -> lineTo(coordinate(), coordinate())
                    "Q" -> quadTo(coordinate(), coordinate(), coordinate(), coordinate())
                    "C" -> curveTo(coordinate(), coordinate(), coordinate(), coordinate(), coordinate(), coordinate())
                    "Z" -> closePath()
                }
            }
        }
    }
}
