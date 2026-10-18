package com.kennycason.kumo.placement

import com.kennycason.kumo.Word
import com.kennycason.kumo.collide.checkers.RectangleCollisionChecker
import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isFalse
import strikt.assertions.isTrue

class RTreeWordPlacerTest {
    @Test
    fun rejectsIntersectionsAndAcceptsDisjointWords() {
        val placer = RTreeWordPlacer()
        expectThat(placer.place(word(0))).isTrue()
        expectThat(placer.place(word(0))).isFalse()
        expectThat(placer.place(word(500))).isTrue()
        expectThat(placer.place(word(500))).isFalse()
    }

    @Test
    fun resetRemovesPreviouslyIndexedWords() {
        val placer = RTreeWordPlacer()
        expectThat(placer.place(word(0))).isTrue()
        placer.reset()
        expectThat(placer.place(word(0))).isTrue()
    }

    private fun word(x: Int): Word {
        val graphics = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics()
        return try {
            Word("word", Color.BLACK, graphics.getFontMetrics(Font("Dialog", Font.PLAIN, 12)), RectangleCollisionChecker(), 0.0)
                .apply { position.setLocation(x, 0) }
        } finally {
            graphics.dispose()
        }
    }
}
