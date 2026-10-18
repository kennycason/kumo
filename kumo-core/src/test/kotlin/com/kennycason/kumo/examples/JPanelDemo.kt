package com.kennycason.kumo.examples

import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.WordCloud
import com.kennycason.kumo.bg.PixelBoundaryBackground
import com.kennycason.kumo.font.scale.LinearFontScalar
import com.kennycason.kumo.nlp.FrequencyAnalyzer
import com.kennycason.kumo.palette.ColorPalette
import java.awt.Color
import java.awt.Dimension
import java.io.InputStream
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel

class JPanelDemo : JPanel() {

    private val wordCloud: WordCloud

    init {
        wordCloud = buildWordCloud()
        val wordCloudLabel = JLabel(ImageIcon(wordCloud.bufferedImage))
        add(wordCloudLabel)
        repaint()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val frame = JFrame("JPanel WordCloud")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.add(JPanelDemo())
            frame.pack()
            frame.setLocationRelativeTo(null)
            frame.isVisible = true
        }

        private fun buildWordCloud(): WordCloud {
            val frequencyAnalyzer = FrequencyAnalyzer()
            frequencyAnalyzer.wordFrequenciesToReturn = 300
            frequencyAnalyzer.minWordLength = 5

            val wordFrequencies = getInputStream("text/datarank.txt").use { frequencyAnalyzer.load(it) }
            val dimension = Dimension(500, 312)
            val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
            wordCloud.padding = 1
            wordCloud.background = getInputStream("backgrounds/whale_small.png").use { PixelBoundaryBackground(it) }
            wordCloud.colorPalette = ColorPalette(Color(0x4055F1), Color(0x408DF1), Color(0x40AAF1), Color(0x40C5F1), Color(0x40D3F1), Color(0xFFFFFF))
            wordCloud.fontScalar = LinearFontScalar(10, 40)
            wordCloud.build(wordFrequencies)

            return wordCloud
        }

        private fun getInputStream(path: String): InputStream {
            return Thread.currentThread().contextClassLoader.getResourceAsStream(path)
        }
    }
}
