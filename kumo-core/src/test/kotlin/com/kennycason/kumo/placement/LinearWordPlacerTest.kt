package com.kennycason.kumo.placement

import com.kennycason.kumo.Word
import com.kennycason.kumo.collide.Collidable
import com.kennycason.kumo.collide.checkers.CollisionChecker
import com.kennycason.kumo.collide.checkers.RectangleCollisionChecker
import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isEmpty
import strikt.assertions.isFalse
import strikt.assertions.isTrue

class LinearWordPlacerTest {

    private val checks = RecordingCollisionChecker()
    private val placer = LinearWordPlacer()

    @Test
    fun placesNonOverlappingWordsAndRejectsCollisions() {
        expectThat(placer.place(word("first"))).isTrue()
        expectThat(placer.place(word("overlapping"))).isFalse()
        expectThat(placer.place(word("elsewhere", x = 1000))).isTrue()
    }

    @Test
    fun cachedCollisionAvoidsScanningOtherWords() {
        val obstacles = List(3) { word("obstacle$it", x = it * 1000) }
        obstacles.forEach { expectThat(placer.place(it)).isTrue() }

        // Find a later collision without depending on HashSet iteration order.
        val candidates = obstacles.mapIndexed { index, obstacle ->
            word("candidate$index", x = obstacle.position.x)
        }
        val candidate = candidates.first {
            checks.checkedWords.clear()
            expectThat(placer.place(it)).isFalse()
            checks.checkedWords.size > 1
        }
        val blocker = checks.checkedWords.last()
        checks.checkedWords.clear()

        expectThat(placer.place(candidate)).isFalse()
        expectThat(checks.checkedWords).containsExactly(blocker)
    }

    @Test
    fun movingAwayFromCachedCollisionAllowsPlacement() {
        val blocker = word("blocker")
        val candidate = word("candidate")
        expectThat(placer.place(blocker)).isTrue()
        expectThat(placer.place(candidate)).isFalse()

        candidate.position.setLocation(1000, 0)

        expectThat(placer.place(candidate)).isTrue()
    }

    @Test
    fun updatesCacheWhenAnotherWordBlocksPlacement() {
        val firstBlocker = word("first")
        val secondBlocker = word("second", x = 1000)
        val candidate = word("candidate")
        expectThat(placer.place(firstBlocker)).isTrue()
        expectThat(placer.place(secondBlocker)).isTrue()
        expectThat(placer.place(candidate)).isFalse()

        candidate.position.setLocation(secondBlocker.position)
        expectThat(placer.place(candidate)).isFalse()
        checks.checkedWords.clear()

        expectThat(placer.place(candidate)).isFalse()
        expectThat(checks.checkedWords).containsExactly(secondBlocker)
    }

    @Test
    fun resetClearsPlacedWordsAndCachedCollisions() {
        val candidate = word("candidate")
        expectThat(placer.place(word("blocker"))).isTrue()
        expectThat(placer.place(candidate)).isFalse()

        placer.reset()
        checks.checkedWords.clear()

        expectThat(placer.place(candidate)).isTrue()
        expectThat(checks.checkedWords).isEmpty()
    }

    private fun word(text: String, x: Int = 0): Word {
        val graphics = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics()
        return try {
            val metrics = graphics.getFontMetrics(Font("Dialog", Font.PLAIN, 12))
            Word(text, Color.BLACK, metrics, checks, 0.0).apply {
                position.setLocation(x, 0)
            }
        } finally {
            graphics.dispose()
        }
    }

    private class RecordingCollisionChecker : CollisionChecker {
        val checkedWords = mutableListOf<Collidable>()
        private val delegate = RectangleCollisionChecker()

        override fun collide(collidable: Collidable, collidable2: Collidable): Boolean {
            checkedWords.add(collidable)
            return delegate.collide(collidable, collidable2)
        }
    }
}
