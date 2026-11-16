package com.kennycason.kumo.benchmarks

import java.nio.file.Path
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue

class PerfResultsTest {
    @TempDir lateinit var directory: Path

    private val metadata = compatibilityKeys.associateWith { "fixed" } + mapOf("schema" to "1", "contract" to CONTRACT,
        "git.commit" to "abc123", "git.dirty" to "false", "forks" to "2", "warmups" to "2", "iterations" to "3", "iteration.ms" to "1000")
    private val row = PerfRow("small-rtree", "layout", 42, Quality("a".repeat(64), "b".repeat(64), 100, 90, 10, 900),
        10.0, 0.2, 1024.0, 1.0)
    private fun result(row: PerfRow = this.row, meta: Map<String, String> = metadata) = ResultSet(listOf(row), meta)

    @Test fun csvRoundTripsIncludingUnknownProfilerValues() {
        val rows = listOf(row, row.copy(seed = 123, error = null, allocatedBytes = null, gcCount = null))
        expectThat(ResultCsv.decode(ResultCsv.encode(rows))).isEqualTo(rows)
    }

    @Test fun rejectsMalformedDuplicateAndNonFiniteResults() {
        assertThrows<IllegalArgumentException> { ResultCsv.decode("wrong,header\n") }
        assertThrows<IllegalArgumentException> { ResultCsv.decode(ResultCsv.encode(listOf(row, row))) }
        assertThrows<IllegalArgumentException> { ResultCsv.decode(ResultCsv.encode(listOf(row)).replace("10.0", "NaN")) }
        assertThrows<IllegalArgumentException> { ResultCsv.decode(ResultCsv.encode(listOf(row)).replace("small-rtree", "bad,case")) }
        assertThrows<IllegalArgumentException> { Quality("a".repeat(64), "b".repeat(64), 100, 90, 9, 900) }
    }

    @Test fun metadataAndCsvAreAValidatedPair() {
        val file = directory.resolve("current.csv").toFile()
        result().save(file)
        expectThat(ResultSet.load(file).rows).isEqualTo(listOf(row))
        file.appendText("\n")
        assertThrows<IllegalArgumentException> { ResultSet.load(file) }
    }

    @Test fun flagsSlowdownsOnlyBeyondThresholdAndUncertainty() {
        expectThat(compareResults(result(), result(row.copy(milliseconds = 13.0)))).contains("SLOWER")
        expectThat(compareResults(result(), result(row.copy(milliseconds = 7.0)))).contains("FASTER")
        expectThat(compareResults(result(), result(row.copy(milliseconds = 11.5, error = 3.0))).contains("SLOWER")).isFalse()
    }

    @Test fun qualityRegressionsRemainVisibleEvenWhenFaster() {
        val fewer = row.copy(milliseconds = 5.0, quality = row.quality.copy(placed = 80, skipped = 20, placedWeight = 800))
        expectThat(compareResults(result(), result(fewer))).contains("QUALITY REGRESSION")
        val lessWeight = row.copy(quality = row.quality.copy(placedWeight = 800))
        expectThat(compareResults(result(), result(lessWeight))).contains("QUALITY REGRESSION")
    }

    @Test fun changedLayoutsDoNotHideTimingRegressions() {
        val changed = row.copy(milliseconds = 20.0, quality = row.quality.copy(layoutHash = "c".repeat(64)))
        val report = compareResults(result(), result(changed))
        expectThat(report).contains("OUTPUT CHANGED")
        expectThat(report).contains("SLOWER")
    }

    @Test fun mismatchedEnvironmentsSuppressPercentagesByDefault() {
        val changed = result(row.copy(milliseconds = 100.0), metadata + ("cpu.model" to "different"))
        val report = compareResults(result(), changed)
        expectThat(report).contains("NOT COMPARABLE")
        expectThat(report.contains("+900.0%")).isFalse()
        expectThat(compareResults(result(), changed, allowEnvironmentMismatch = true)).contains("+900.0%")
    }

    @Test fun missingAndNewCasesAreNotSilentlyDropped() {
        expectThat(compareResults(result(), result(row.copy(caseId = "new-case")))).contains("MISSING CASE")
        expectThat(compareResults(result(), result(row.copy(caseId = "new-case")))).contains("NEW CASE")
        expectThat(compareResults(null, result())).contains("No baseline yet")
    }

    @Test fun changedFixturesAreNotCompared() {
        val changed = result(row.copy(quality = row.quality.copy(fixtureHash = "c".repeat(64))))
        expectThat(compareResults(result(), changed)).contains("FIXTURE CHANGED")
    }

    @Test fun shortRunsAndUnknownErrorEstimatesCannotClaimConfidentRegressions() {
        expectThat(compareResults(result(), result(row.copy(milliseconds = 100.0, error = null))))
            .contains("Insufficient error estimate")
    }

    @Test fun promotionRequiresExplicitApprovalForDirtyOrShortRuns() {
        val current = directory.resolve("current.csv").toFile()
        val baseline = directory.resolve("baseline.csv").toFile()
        result(meta = metadata + ("git.dirty" to "true")).save(current)
        assertThrows<IllegalArgumentException> { promote(current, baseline, "initial") }
        expectThat(baseline.exists()).isFalse()
        promote(current, baseline, "initial development reference", allowDirty = true)
        expectThat(ResultSet.load(baseline).metadata.getValue("baseline.reason")).isEqualTo("initial development reference")
        result(meta = metadata + ("forks" to "1")).save(current)
        assertThrows<IllegalArgumentException> { promote(current, baseline, "short") }
        assertThrows<IllegalArgumentException> { promote(current, baseline, "") }
        assertThrows<IllegalArgumentException> { promote(current, current, "same file") }
    }

    @Test fun partialRunsCannotAccidentallyEraseBaselineCoverage() {
        val current = directory.resolve("current.csv").toFile()
        val baseline = directory.resolve("baseline.csv").toFile()
        ResultSet(listOf(row, row.copy(seed = 123)), metadata).save(baseline)
        result().save(current)
        assertThrows<IllegalArgumentException> { promote(current, baseline, "partial") }
        expectThat(ResultSet.load(baseline).rows.size).isEqualTo(2)
        promote(current, baseline, "intentionally reduced coverage", allowCaseRemoval = true)
        expectThat(ResultSet.load(baseline).rows.size).isEqualTo(1)
    }
}
