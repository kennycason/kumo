package com.kennycason.kumo.benchmarks

import com.kennycason.kumo.CloudLayout
import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.WordCloud
import com.kennycason.kumo.WordFrequency
import com.kennycason.kumo.bg.Background
import com.kennycason.kumo.bg.CircleBackground
import com.kennycason.kumo.bg.PixelBoundaryBackground
import com.kennycason.kumo.font.KumoFont
import com.kennycason.kumo.font.scale.LinearFontScalar
import com.kennycason.kumo.image.AngleGenerator
import com.kennycason.kumo.nlp.FrequencyAnalyzer
import com.kennycason.kumo.palette.ColorPalette
import com.kennycason.kumo.placement.LinearWordPlacer
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.security.MessageDigest

internal const val CONTRACT = "kumo-perf-v1"

internal fun sha256(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256")
    .digest(bytes).joinToString("") { "%02x".format(it) }

internal fun resource(path: String): ByteArray = checkNotNull(
    BenchmarkCase::class.java.classLoader.getResourceAsStream(path)
) { "Missing benchmark fixture: $path" }.use { it.readBytes() }

internal object Fixtures {
    val fontBytes by lazy { resource("font/OpenSansEmoji.ttf") }
    val font by lazy { KumoFont(Font.createFont(Font.TRUETYPE_FONT, fontBytes.inputStream())) }
    val mask by lazy { resource("backgrounds/whale_small.png") }
    val textBytes by lazy { resource("text/datarank.txt") }
    val text by lazy { textBytes.toString(Charsets.UTF_8) }

    // Independent of FrequencyAnalyzer: changes to tokenization must not change layout inputs.
    private val stems = ("cloud kotlin layout vector performance collision canvas render frequency " +
        "algorithm language library palette typography image rectangle circle analysis rotation " +
        "geometry placement benchmark memory padding deterministic graphics words sunflower").split(' ')

    fun words(count: Int): List<WordFrequency> = List(count) { index ->
        WordFrequency(stems[index % stems.size] + suffix(index / stems.size), 1_000_000 / (index + 1))
    }

    private fun suffix(value: Int): String {
        var remaining = value
        return buildString {
            do {
                append('a' + remaining % 26)
                remaining /= 26
            } while (remaining > 0)
        }
    }
}

data class BenchmarkCase(
    val id: String,
    val width: Int,
    val height: Int,
    val count: Int,
    val mode: CollisionMode = CollisionMode.PIXEL_PERFECT,
    val linear: Boolean = false,
    val shape: String = "rectangle",
    val rotated: Boolean = false,
    val maxFont: Int = 48
) {
    internal fun prepare(): PreparedCase = PreparedCase(this)
}

internal object Cases {
    val all = listOf(
        BenchmarkCase("small-linear", 500, 500, 100, CollisionMode.RECTANGLE, linear = true),
        BenchmarkCase("small-rtree", 500, 500, 100, CollisionMode.RECTANGLE),
        BenchmarkCase("typical-pixel", 1000, 1000, 500, rotated = true),
        BenchmarkCase("dense-pixel", 500, 500, 500, rotated = true, maxFont = 72),
        BenchmarkCase("typical-linear", 1000, 1000, 500, CollisionMode.RECTANGLE, linear = true),
        BenchmarkCase("typical-rtree", 1000, 1000, 500, CollisionMode.RECTANGLE),
        BenchmarkCase("circle-pixel", 1000, 1000, 500, shape = "circle", rotated = true),
        BenchmarkCase("mask-pixel", 500, 312, 300, shape = "mask", rotated = true),
        BenchmarkCase("large-pixel", 2000, 2000, 2000, rotated = true),
        BenchmarkCase("large-linear", 2000, 2000, 2000, CollisionMode.RECTANGLE, linear = true),
        BenchmarkCase("large-rtree", 2000, 2000, 2000, CollisionMode.RECTANGLE),
        BenchmarkCase("stress-pixel", 4000, 4000, 5000, rotated = true),
        BenchmarkCase("stress-rtree", 4000, 4000, 5000, CollisionMode.RECTANGLE)
    )
    val quick = all.take(4).map { it.id }

    fun find(id: String): BenchmarkCase = all.singleOrNull { it.id == id }
        ?: error("Unknown case '$id'. Available: ${all.joinToString { it.id }}")
}

internal class PreparedCase(val case: BenchmarkCase) {
    val words = Fixtures.words(case.count)
    private val background: Background? = when (case.shape) {
        "circle" -> CircleBackground(case.width / 2)
        "mask" -> PixelBoundaryBackground(Fixtures.mask.inputStream())
        else -> null
    }
    val fingerprint: String = sha256(buildString {
        append(CONTRACT).append(case)
        append(sha256(Fixtures.fontBytes))
        if (case.shape == "mask") append(sha256(Fixtures.mask))
        words.forEach { append('\n').append(it.word).append('\t').append(it.frequency) }
    }.toByteArray(Charsets.UTF_8))

    fun build(seed: Long): CloudLayout {
        val cloud = WordCloud(Dimension(case.width, case.height), case.mode)
        cloud.seed = seed
        cloud.padding = 2
        cloud.kumoFont = Fixtures.font
        cloud.fontScalar = LinearFontScalar(12, case.maxFont)
        cloud.angleGenerator = if (case.rotated) AngleGenerator(-90.0, 90.0, 3) else AngleGenerator(0)
        cloud.colorPalette = ColorPalette(Color(0x02B6F2), Color(0xFFFFFF))
        cloud.backgroundColor = null
        if (case.linear) cloud.wordPlacer = LinearWordPlacer()
        background?.let { cloud.background = it }
        cloud.build(words)
        return cloud.layout
    }
}

internal data class Quality(
    val fixtureHash: String,
    val layoutHash: String,
    val inputWords: Int,
    val placed: Int?,
    val skipped: Int?,
    val placedWeight: Long?
) {
    init {
        require(inputWords >= 0) { "Invalid input count" }
        require((placed == null && skipped == null && placedWeight == null) ||
            (placed != null && skipped != null && placedWeight != null && placed >= 0 && skipped >= 0 &&
                placed + skipped == inputWords && placedWeight >= 0)) { "Inconsistent placement quality" }
    }
}

internal fun quality(caseId: String, seed: Long): Quality {
    if (caseId == "text-analysis") {
        val counts = FrequencyAnalyzer().analyzeText(Fixtures.text)
        return Quality(sha256(Fixtures.textBytes), sha256(counts.joinToString("\n") {
            "${it.word}\t${it.frequency}"
        }.toByteArray()), 0, null, null, null)
    }
    val prepared = Cases.find(caseId).prepare()
    val layout = prepared.build(seed)
    check(layout.words.size + layout.skipped.size == prepared.case.count) { "Layout lost input words" }
    val hash = sha256(layout.words.joinToString("\n") {
        "${it.word}\t${it.frequency}\t${it.bounds}\t${it.rotation}\t${it.fontSize}\t${it.color.rgb}"
    }.toByteArray(Charsets.UTF_8))
    return Quality(prepared.fingerprint, hash, prepared.case.count, layout.words.size, layout.skipped.size,
        layout.words.sumOf { it.frequency.toLong() })
}
