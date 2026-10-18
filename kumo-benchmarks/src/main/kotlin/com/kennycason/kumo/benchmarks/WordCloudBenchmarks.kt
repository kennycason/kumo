package com.kennycason.kumo.benchmarks

import com.kennycason.kumo.CloudLayout
import com.kennycason.kumo.WordFrequency
import com.kennycason.kumo.nlp.FrequencyAnalyzer
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
open class LayoutState {
    @Param("typical-pixel")
    @JvmField var caseId: String = "typical-pixel"

    @Param("42")
    @JvmField var seed: Long = 42

    internal lateinit var prepared: PreparedCase

    @Setup(Level.Trial)
    fun prepare() {
        prepared = Cases.find(caseId).prepare()
    }
}

@State(Scope.Thread)
open class ExportState {
    @Param("typical-pixel")
    @JvmField var caseId: String = "typical-pixel"

    @Param("42")
    @JvmField var seed: Long = 42

    lateinit var layout: CloudLayout

    @Setup(Level.Trial)
    fun prepare() {
        layout = Cases.find(caseId).prepare().build(seed)
    }
}

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
open class WordCloudBenchmarks {
    @Benchmark
    fun layout(state: LayoutState): CloudLayout = state.prepared.build(state.seed)

    @Benchmark
    fun png(state: ExportState): ByteArray = state.layout.toPng()

    @Benchmark
    // JMH warm-up initializes the lazy outlines; measurement reuses that same layout.
    fun svgCached(state: ExportState): String = state.layout.toSvg()

    @Benchmark
    fun layoutAndSvg(state: LayoutState): String = state.prepared.build(state.seed).toSvg()
}

@State(Scope.Thread)
open class AnalysisState {
    @Param("text-analysis")
    @JvmField var caseId: String = "text-analysis"

    @Param("42")
    @JvmField var seed: Long = 42

    lateinit var text: String

    @Setup(Level.Trial)
    fun prepare() { text = Fixtures.text }
}

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
open class AnalyzerBenchmarks {
    @Benchmark
    fun analyze(state: AnalysisState): List<WordFrequency> = FrequencyAnalyzer().analyzeText(state.text)
}
