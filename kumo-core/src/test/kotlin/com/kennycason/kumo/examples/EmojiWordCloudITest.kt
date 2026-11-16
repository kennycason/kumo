package com.kennycason.kumo.examples

import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.WordCloud
import com.kennycason.kumo.WordFrequency
import com.kennycason.kumo.font.KumoFont
import java.awt.Dimension
import java.util.Random
import org.junit.jupiter.api.Test

class EmojiWordCloudITest {

    companion object {
        private val RANDOM = Random()

        private val EMOJIS = arrayOf(
            "emoji",
            "kumo",
            "\uD83D\uDE02",
            "\uD83D\uDC4D",
            "\uD83D\uDE0A",
            "\uD83D\uDE01",
            "\uD83D\uDE12",
            "\uD83D\uDE11",
            "\uD83D\uDE0D",
            "\uD83D\uDC4C",
            "\uD83D\uDC55",
            "\uD83D\uDCAA",
            "\uD83D\uDE4C",
            "\uD83D\uDE2D",
            "\uD83D\uDE29",
            "\uD83D\uDE21",
            "\uD83D\uDD25",
            "\uD83D\uDE33",
            "\uD83D\uDD25",
            "\uD83D\uDE21",
            "\uD83D\uDCA9",
            "\uD83D\uDC7B",
            "\uD83D\uDC7D",
            "\uD83D\uDC57",
            "\uD83D\uDD25",
            "\uD83C\uDF53",
            "\uD83C\uDFB1",
            "\uD83C\uDFA7",
            "\uD83D\uDE91",
            "\uD83C\uDFEB",
            "\uD83D\uDCBE",
            "\uD83D\uDCAF",
            "\uD83D\uDE32",
            "\uD83D\uDE31",
            "\uD83D\uDE0F",
            "\uD83D\uDC44",
            "\uD83D\uDC40 "
        )

        private fun buildWordFrequencies(): List<WordFrequency> {
            val wordFrequencies = mutableListOf<WordFrequency>()
            for (emoji in EMOJIS) {
                wordFrequencies.add(WordFrequency(emoji, RANDOM.nextInt(250)))
            }
            return wordFrequencies
        }
    }

    @Test
    fun emojiCloud() {
        val dimension = Dimension(250, 250)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        // Load font from resource
        wordCloud.kumoFont = KumoFont(EmojiWordCloudITest::class.java.getResourceAsStream("/font/OpenSansEmoji.ttf"))
        wordCloud.padding = 2
        wordCloud.build(buildWordFrequencies())
        wordCloud.writeToFile("output/wordcloud_emoji.png")
    }
}
