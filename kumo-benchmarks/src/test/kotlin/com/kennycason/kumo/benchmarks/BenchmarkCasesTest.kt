package com.kennycason.kumo.benchmarks

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

class BenchmarkCasesTest {
    @Test fun stressFixturesContainExactlyFiveThousandDistinctPositiveWords() {
        val words = Fixtures.words(5000)
        expectThat(words.size).isEqualTo(5000)
        expectThat(words.map { it.word }.distinct().size).isEqualTo(5000)
        expectThat(words.all { it.frequency > 0 && Fixtures.font.font.canDisplayUpTo(it.word) == -1 }).isTrue()
    }

    @Test fun seededLayoutsAndTheirFingerprintsAreRepeatable() {
        val first = quality("small-rtree", 42)
        expectThat(quality("small-rtree", 42)).isEqualTo(first)
        expectThat(first.placed!! + first.skipped!!).isEqualTo(100)
        expectThat(first.placed!! > 0).isTrue()
    }

    @Test fun fixturesDistinguishChangedCasesAndSeedsDistinguishLayouts() {
        val first = quality("small-rtree", 42)
        expectThat(quality("small-linear", 42).fixtureHash != first.fixtureHash).isTrue()
        expectThat(quality("small-rtree", 123).layoutHash != first.layoutHash).isTrue()
    }

    @Test fun rejectsUnknownCasesAndInvalidSettings() {
        assertThrows<IllegalStateException> { Cases.find("missing") }
        assertThrows<IllegalArgumentException> { RunSettings("quick", listOf("small-rtree"), listOf(42), listOf("layout"), 0, 2, 3, 1000) }
        assertThrows<IllegalArgumentException> { RunSettings("quick", listOf("small-rtree"), listOf(42, 42), listOf("layout"), 2, 2, 3, 1000) }
    }

    @Test fun exportStateHasASingleSelfContainedSetup() {
        val state = ExportState().apply { caseId = "small-rtree"; prepare() }
        expectThat(state.layout.words.isNotEmpty()).isTrue()
        expectThat(WordCloudBenchmarks().png(state).isNotEmpty()).isTrue()
        expectThat(WordCloudBenchmarks().svgCached(state)).isEqualTo(WordCloudBenchmarks().svgCached(state))
    }
}
