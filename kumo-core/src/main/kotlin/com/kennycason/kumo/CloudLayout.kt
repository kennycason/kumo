package com.kennycason.kumo

import java.awt.AlphaComposite
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.font.FontRenderContext
import java.awt.font.TextLayout
import java.awt.geom.AffineTransform
import java.awt.geom.Area
import java.awt.geom.PathIterator
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.io.StringWriter
import java.util.Collections
import javax.imageio.ImageIO
import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.XMLStreamWriter

/** A completed layout. Rendering it never runs placement again or changes its words. */
class CloudLayout internal constructor(
    val width: Int,
    val height: Int,
    val backgroundColor: Color?,
    val seed: Long?,
    words: List<PlacedWord>,
    skipped: List<SkippedWord>
) {
    val words: List<PlacedWord> = Collections.unmodifiableList(words.toList())
    val skipped: List<SkippedWord> = Collections.unmodifiableList(skipped.toList())

    /** Returns a new image; editing it does not change this layout. */
    fun toBufferedImage(): BufferedImage =
        BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB).also { renderTo(it) }

    fun writePng(path: String) = writePng(File(path))

    fun writePng(file: File) = file.outputStream().use { writePng(it) }

    /** Writes without closing the caller's stream. */
    fun writePng(output: OutputStream) {
        if (!ImageIO.write(toBufferedImage(), "png", output)) {
            throw IOException("No PNG image writer is available")
        }
    }

    fun toPng(): ByteArray = ByteArrayOutputStream().apply { writePng(this) }.toByteArray()

    fun writeSvg(path: String) = writeSvg(File(path))

    fun writeSvg(file: File) = file.outputStream().use { writeSvg(it) }

    /** Writes vector glyph outlines without closing the caller's stream. */
    fun writeSvg(output: OutputStream) {
        writeSvg(XMLOutputFactory.newFactory().createXMLStreamWriter(output, "UTF-8"))
    }

    fun toSvg(): String {
        val output = StringWriter()
        writeSvg(XMLOutputFactory.newFactory().createXMLStreamWriter(output))
        return output.toString()
    }

    internal fun renderTo(image: BufferedImage) {
        val graphics = image.createGraphics()
        try {
            graphics.composite = AlphaComposite.Src
            graphics.color = backgroundColor ?: Color(0, 0, 0, 0)
            graphics.fillRect(0, 0, width, height)
            graphics.composite = AlphaComposite.SrcOver
            words.forEach { it.draw(graphics) }
        } finally {
            graphics.dispose()
        }
    }

    private fun writeSvg(xml: XMLStreamWriter) {
        xml.writeStartDocument("UTF-8", "1.0")
        xml.writeStartElement("svg")
        xml.writeDefaultNamespace("http://www.w3.org/2000/svg")
        xml.writeAttribute("width", width.toString())
        xml.writeAttribute("height", height.toString())
        xml.writeAttribute("viewBox", "0 0 $width $height")
        backgroundColor?.let { color ->
            xml.writeEmptyElement("rect")
            xml.writeAttribute("width", width.toString())
            xml.writeAttribute("height", height.toString())
            xml.writeColor(color)
        }
        for (word in words) {
            xml.writeStartElement("path")
            xml.writeAttribute("d", word.svgPath())
            xml.writeAttribute("fill-rule", "nonzero")
            xml.writeColor(word.color)
            xml.writeStartElement("title")
            xml.writeCharacters(word.word)
            xml.writeEndElement()
            xml.writeEndElement()
        }
        xml.writeEndElement()
        xml.writeEndDocument()
        xml.flush()
    }

    private fun XMLStreamWriter.writeColor(color: Color) {
        writeAttribute("fill", "#" + (color.rgb and 0xFFFFFF).toString(16).padStart(6, '0'))
        if (color.alpha != 255) writeAttribute("fill-opacity", (color.alpha / 255.0).toString())
    }
}

/** Pixel bounds include padding and rotation. The rotation angle is in radians. */
class PlacedWord internal constructor(
    val word: String,
    val frequency: Int,
    val font: Font,
    val color: Color,
    val rotation: Double,
    val bounds: WordBounds,
    image: BufferedImage,
    private val glyph: GlyphMetrics
) {
    private val image = BufferedImage(image.colorModel, image.copyData(null), image.isAlphaPremultiplied, null)
    private val outline by lazy { createOutline() }

    val fontSize: Float get() = font.size2D

    internal fun draw(graphics: Graphics2D) {
        graphics.drawImage(image, bounds.x, bounds.y, null)
    }

    internal fun svgPath(): String = buildString {
        val iterator = outline.getPathIterator(null)
        val coordinates = DoubleArray(6)
        while (!iterator.isDone) {
            val count = when (iterator.currentSegment(coordinates)) {
                PathIterator.SEG_MOVETO -> {
                    append('M')
                    2
                }
                PathIterator.SEG_LINETO -> {
                    append('L')
                    2
                }
                PathIterator.SEG_QUADTO -> {
                    append('Q')
                    4
                }
                PathIterator.SEG_CUBICTO -> {
                    append('C')
                    6
                }
                else -> {
                    append('Z')
                    0
                }
            }
            for (i in 0 until count) {
                if (i > 0) append(' ')
                append(coordinates[i])
            }
            iterator.next()
        }
    }

    private fun createOutline(): Area {
        val outline = Area(TextLayout(word, font, glyph.context).getOutline(
            AffineTransform.getTranslateInstance(0.0, glyph.baseline.toDouble())
        ))
        // Match the clipping, integer translations, rotation, and padding used by the raster renderer.
        outline.intersect(Area(Rectangle(0, 0, glyph.width, glyph.height)))
        val transform = AffineTransform()
        transform.translate(
            ((glyph.rotatedWidth - glyph.width) / 2).toDouble(),
            ((glyph.rotatedHeight - glyph.height) / 2).toDouble()
        )
        transform.rotate(rotation, (glyph.width / 2).toDouble(), (glyph.height / 2).toDouble())
        outline.transform(transform)
        outline.intersect(Area(Rectangle(0, 0, glyph.rotatedWidth, glyph.rotatedHeight)))
        outline.transform(AffineTransform.getTranslateInstance(
            (bounds.x + (bounds.width - glyph.rotatedWidth) / 2).toDouble(),
            (bounds.y + (bounds.height - glyph.rotatedHeight) / 2).toDouble()
        ))
        return outline
    }
}

internal data class GlyphMetrics(
    val width: Int,
    val height: Int,
    val baseline: Int,
    val rotatedWidth: Int,
    val rotatedHeight: Int,
    val context: FontRenderContext
)

data class WordBounds(val x: Int, val y: Int, val width: Int, val height: Int)

data class SkippedWord(val word: String, val frequency: Int, val reason: SkipReason)

enum class SkipReason {
    BLANK_TEXT,
    TOO_LARGE,
    NO_SPACE
}
