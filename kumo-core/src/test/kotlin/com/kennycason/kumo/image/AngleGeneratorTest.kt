package com.kennycason.kumo.image

import java.util.Random
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isEqualTo

class AngleGeneratorTest {
    @Test
    fun rangesStartAtTheRequestedAngleAndCycle() {
        val angles = AngleGenerator(-90.0, 90.0, 3)
        expectThat(List(4) { angles.next() })
            .containsExactly(-Math.PI / 2, 0.0, Math.PI / 2, -Math.PI / 2)
    }

    @Test
    fun singleStepRangesProduceAFiniteAngle() {
        expectThat(AngleGenerator(45.0, 90.0, 1).randomNext()).isEqualTo(Math.PI / 4)
    }

    @Test
    fun callerOwnedAngleArraysAreCopied() {
        val radians = doubleArrayOf(Math.PI / 4)
        val angles = AngleGenerator(radians)
        radians[0] = 0.0
        expectThat(angles.randomNext(Random(42))).isEqualTo(Math.PI / 4)
    }

    @Test
    fun emptyOrNonFiniteAnglesAreRejected() {
        assertThrows<IllegalArgumentException> { AngleGenerator(0.0, 90.0, 0) }
        assertThrows<IllegalArgumentException> { AngleGenerator(doubleArrayOf()) }
        assertThrows<IllegalArgumentException> { AngleGenerator(doubleArrayOf(Double.NaN)) }
    }
}
