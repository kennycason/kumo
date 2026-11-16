package com.kennycason.kumo.benchmarks

import java.io.File
import java.lang.management.ManagementFactory
import java.nio.file.Files
import java.nio.file.StandardCopyOption.REPLACE_EXISTING
import java.time.Instant
import java.util.Locale
import java.util.Properties
import java.util.concurrent.TimeUnit

internal data class PerfRow(
    val caseId: String,
    val operation: String,
    val seed: Long,
    val quality: Quality,
    val milliseconds: Double,
    val error: Double?,
    val allocatedBytes: Double?,
    val gcCount: Double?
) {
    val key: String get() = "$caseId/$operation/$seed"

    init {
        require(milliseconds.isFinite() && milliseconds > 0) { "Invalid time for $key" }
        require(listOfNotNull(error, allocatedBytes, gcCount).all { it.isFinite() && it >= 0 }) { "Invalid metric for $key" }
    }
}

internal object ResultCsv {
    private val header = listOf("case", "operation", "seed", "fixture_hash", "layout_hash", "input_words",
        "placed", "skipped", "placed_weight", "ms_op", "error_ms", "allocated_bytes_op", "gc_count")

    // The schema deliberately only contains safe IDs, hashes, and numbers, not free-form metadata.
    fun encode(rows: List<PerfRow>): String = (listOf(header) + rows.map { row ->
        listOf(row.caseId, row.operation, row.seed, row.quality.fixtureHash, row.quality.layoutHash,
            row.quality.inputWords, row.quality.placed, row.quality.skipped, row.quality.placedWeight,
            row.milliseconds, row.error, row.allocatedBytes, row.gcCount).map { it?.toString().orEmpty() }
    }).joinToString("\n", postfix = "\n") { fields ->
        require(fields.none { value -> value.any { it in ",\r\n\"" } }) { "Unsafe CSV field" }
        fields.joinToString(",")
    }

    fun decode(text: String): List<PerfRow> {
        val lines = text.lineSequence().filter { it.isNotBlank() }.toList()
        require(lines.isNotEmpty() && lines.first().split(',') == header) { "Unrecognized performance CSV schema" }
        val rows = lines.drop(1).map { line ->
            val fields = line.split(',')
            require(fields.size == header.size) { "Malformed performance CSV row" }
            require(fields.take(2).all { it.matches(Regex("[A-Za-z0-9_-]+")) }) { "Invalid case or operation" }
            require(fields[3].matches(Regex("[a-f0-9]{64}")) && fields[4].matches(Regex("[a-f0-9]{64}"))) { "Invalid fingerprint" }
            PerfRow(fields[0], fields[1], fields[2].toLong(),
                Quality(fields[3], fields[4], fields[5].toInt(), fields[6].takeIf { it.isNotEmpty() }?.toInt(),
                    fields[7].takeIf { it.isNotEmpty() }?.toInt(), fields[8].takeIf { it.isNotEmpty() }?.toLong()),
                fields[9].toDouble(), fields[10].takeIf { it.isNotEmpty() }?.toDouble(),
                fields[11].takeIf { it.isNotEmpty() }?.toDouble(), fields[12].takeIf { it.isNotEmpty() }?.toDouble())
        }
        require(rows.map { it.key }.distinct().size == rows.size) { "Duplicate benchmark keys" }
        return rows
    }
}

internal fun metadataFile(csv: File): File = File(csv.parentFile, csv.nameWithoutExtension + ".properties")

internal fun writeMetadata(file: File, values: Map<String, String>) {
    file.parentFile?.mkdirs()
    file.writer(Charsets.UTF_8).use { writer ->
        Properties().apply { putAll(values) }.store(writer, "Kumo benchmark metadata; do not edit result/checksum pairs")
    }
}

internal data class ResultSet(val rows: List<PerfRow>, val metadata: Map<String, String>) {
    fun save(csv: File) {
        csv.parentFile?.mkdirs()
        val text = ResultCsv.encode(rows)
        csv.writeText(text, Charsets.UTF_8)
        writeMetadata(metadataFile(csv), metadata + mapOf("csv.sha256" to sha256(text.toByteArray(Charsets.UTF_8)), "status" to "complete"))
    }

    companion object {
        fun load(csv: File): ResultSet {
            require(csv.isFile && metadataFile(csv).isFile) { "Missing CSV or metadata: ${csv.path}" }
            val metadata = metadataFile(csv).reader(Charsets.UTF_8).use { reader ->
                Properties().apply { load(reader) }.entries.associate { it.key.toString() to it.value.toString() }
            }
            val bytes = csv.readBytes()
            require(metadata["status"] == "complete" && metadata["schema"] == "1") { "Incomplete or unsupported run: $csv" }
            require(metadata["csv.sha256"] == sha256(bytes)) { "CSV/metadata checksum mismatch: $csv" }
            return ResultSet(ResultCsv.decode(bytes.toString(Charsets.UTF_8)), metadata)
        }
    }
}

internal val compatibilityKeys = listOf("contract", "jmh.version", "os.name", "os.version", "os.arch", "cpu.model",
    "cpu.cores", "memory.bytes", "java.runtime", "java.vendor", "java.vm", "jvm.args", "java.options.hash",
    "forks", "warmups", "iterations", "iteration.ms", "threads", "mode", "profiler")

internal fun environment(root: File): Map<String, String> {
    fun command(vararg args: String): String = try {
        val process = ProcessBuilder(*args).directory(root).redirectError(ProcessBuilder.Redirect.DISCARD).start()
        if (process.waitFor(5, TimeUnit.SECONDS)) {
            if (process.exitValue() == 0) process.inputStream.bufferedReader().readText().trim() else "unknown"
        }
        else { process.destroyForcibly(); "unknown" }
    } catch (_: Exception) { "unknown" }
    val cpu = when (System.getProperty("os.name")) {
        "Mac OS X" -> command("sysctl", "-n", "machdep.cpu.brand_string")
        "Linux" -> File("/proc/cpuinfo").takeIf { it.isFile }?.useLines { lines ->
            lines.firstOrNull { it.startsWith("model name") || it.startsWith("Hardware") }?.substringAfter(':')?.trim()
        }.orEmpty().ifBlank { "unknown" }
        else -> "unknown"
    }
    val source = File(PerfRow::class.java.protectionDomain.codeSource.location.toURI())
    val bean = ManagementFactory.getOperatingSystemMXBean() as? com.sun.management.OperatingSystemMXBean
    val gitStatus = command("git", "status", "--porcelain", "--untracked-files=normal")
    return mapOf(
        "schema" to "1", "contract" to CONTRACT, "jmh.version" to "1.37", "timestamp" to Instant.now().toString(),
        "os.name" to System.getProperty("os.name"), "os.version" to System.getProperty("os.version"),
        "os.arch" to System.getProperty("os.arch"), "cpu.model" to cpu, "cpu.cores" to Runtime.getRuntime().availableProcessors().toString(),
        "memory.bytes" to (bean?.totalPhysicalMemorySize?.toString() ?: "unknown"),
        "java.runtime" to System.getProperty("java.runtime.version"), "java.vendor" to System.getProperty("java.vendor"),
        "java.vm" to System.getProperty("java.vm.name"), "git.commit" to command("git", "rev-parse", "HEAD"),
        "git.dirty" to if (gitStatus == "unknown") "unknown" else gitStatus.isNotBlank().toString(),
        "artifact.sha256" to if (source.isFile) sha256(source.readBytes()) else "unpackaged-classes",
        // Hash injected JVM options instead of persisting potentially sensitive option values.
        "java.options.hash" to sha256(listOf("JAVA_TOOL_OPTIONS", "_JAVA_OPTIONS", "JDK_JAVA_OPTIONS")
            .joinToString("\n") { System.getenv(it).orEmpty() }.toByteArray())
    )
}

internal fun compareResults(base: ResultSet?, current: ResultSet, allowEnvironmentMismatch: Boolean = false, warnPercent: Double = 10.0): String {
    val mismatch = if (base == null) emptyList() else compatibilityKeys.filter {
        base.metadata[it].isNullOrBlank() || current.metadata[it].isNullOrBlank() || base.metadata[it] != current.metadata[it] ||
            base.metadata[it] == "unknown" || current.metadata[it] == "unknown"
    }
    val before = base?.rows?.associateBy { it.key }.orEmpty()
    val after = current.rows.associateBy { it.key }
    fun number(value: Double?): String = value?.let { String.format(Locale.ROOT, "%.3f", it) } ?: "—"
    fun delta(old: Double?, new: Double?): Double? = if (old != null && new != null && old > 0) (new / old - 1) * 100 else null
    fun percent(value: Double?): String = value?.let { String.format(Locale.ROOT, "%+.1f%%", it) } ?: "—"
    return buildString {
        appendLine("# Kumo performance comparison")
        appendLine()
        appendLine("Report-only: timing changes never fail a build or update the baseline.")
        appendLine("Times are mean milliseconds per operation; error is JMH's reported confidence-interval half-width.")
        appendLine("Lower time/allocation is better, provided placement quality is preserved.")
        appendLine()
        appendLine("Current commit: `${current.metadata["git.commit"]}` (dirty=${current.metadata["git.dirty"]}).")
        appendLine("Baseline commit: `${base?.metadata?.get("git.commit") ?: "not set"}`.")
        if (base == null) appendLine("No baseline yet. Review this run and explicitly promote it when ready.")
        if (mismatch.isNotEmpty()) {
            appendLine()
            appendLine("**Incompatible environment/settings:** ${mismatch.joinToString { "`$it`" }}.")
            if (allowEnvironmentMismatch) appendLine("Comparison explicitly forced; percentages are not evidence of a code regression.")
            else appendLine("Timing percentages are suppressed. Compare on the same environment and with the same measurement settings.")
        }
        appendLine()
        appendLine("| Case / operation / seed | Base ms | Current ms ± error | Time Δ | Current bytes/op | Bytes/op Δ | Placed base → current | Observation |")
        appendLine("|---|---:|---:|---:|---:|---:|---:|---|")
        for (key in (before.keys + after.keys).sorted()) {
            val old = before[key]
            val new = after[key]
            val comparable = old != null && new != null && old.quality.fixtureHash == new.quality.fixtureHash &&
                (mismatch.isEmpty() || allowEnvironmentMismatch)
            val change = if (comparable) delta(old?.milliseconds, new?.milliseconds) else null
            val observation = when {
                new == null -> "MISSING CASE"
                old == null -> "NEW CASE"
                old.quality.fixtureHash != new.quality.fixtureHash -> "FIXTURE CHANGED"
                !comparable -> "NOT COMPARABLE"
                else -> buildList {
                    if (new.quality.placed != null && old.quality.placed != null &&
                        (new.quality.placed < old.quality.placed || new.quality.placedWeight!! < old.quality.placedWeight!!)) {
                        add("QUALITY REGRESSION")
                    } else if (old.quality.layoutHash != new.quality.layoutHash) add("OUTPUT CHANGED; review quality")
                    add(when {
                        old.error == null || new.error == null -> "Insufficient error estimate"
                        change!! > warnPercent && new.milliseconds - new.error > old.milliseconds + old.error -> "SLOWER; investigate"
                        change < -warnPercent && new.milliseconds + new.error < old.milliseconds - old.error -> "FASTER"
                        else -> "No clear timing regression"
                    })
                }.joinToString("; ")
            }
            appendLine("| $key | ${number(old?.milliseconds)} | ${number(new?.milliseconds)} ± ${number(new?.error)} | " +
                "${percent(change)} | ${number(new?.allocatedBytes)} | ${percent(if (comparable) delta(old?.allocatedBytes, new?.allocatedBytes) else null)} | " +
                "${old?.quality?.placed ?: "—"} → ${new?.quality?.placed ?: "—"} | $observation |")
        }
        appendLine()
        appendLine("Quality is sampled outside timed iterations. A changed layout is not automatically a regression; fewer placed words or less placed frequency weight is flagged separately.")
    }
}

internal fun promote(current: File, baseline: File, reason: String, allowDirty: Boolean = false, allowSmoke: Boolean = false, allowCaseRemoval: Boolean = false) {
    require(current.canonicalFile != baseline.canonicalFile) { "Current and baseline must be different files" }
    require(reason.isNotBlank()) { "Baseline promotion requires a reason" }
    val results = ResultSet.load(current)
    require(results.rows.isNotEmpty()) { "Cannot promote empty results" }
    if (baseline.exists() && !allowCaseRemoval) {
        val previousKeys = ResultSet.load(baseline).rows.map { it.key }.toSet()
        require(results.rows.map { it.key }.containsAll(previousKeys)) { "Promotion would remove baseline cases; run the full selection or explicitly --allow-case-removal" }
    }
    require(allowDirty || results.metadata["git.dirty"] == "false") { "Run was dirty/unknown; commit first or explicitly use --allow-dirty" }
    val sufficient = (results.metadata["forks"]?.toIntOrNull() ?: 0) >= 2 && (results.metadata["warmups"]?.toIntOrNull() ?: 0) >= 2 &&
        (results.metadata["iterations"]?.toIntOrNull() ?: 0) >= 3 && (results.metadata["iteration.ms"]?.toLongOrNull() ?: 0) >= 1000
    require(allowSmoke || sufficient) { "Smoke/short runs are not suitable baselines; use a measured suite or explicitly --allow-smoke" }
    ResultSet(results.rows, results.metadata + mapOf("baseline.reason" to reason, "baseline.promoted" to Instant.now().toString(),
        "baseline.allowDirty" to allowDirty.toString(), "baseline.allowSmoke" to allowSmoke.toString(),
        "baseline.allowCaseRemoval" to allowCaseRemoval.toString())).save(baseline)
}

internal fun publishRun(run: File, output: File) {
    // Metadata includes a CSV checksum, so interrupted copies cannot masquerade as a valid pair.
    Files.copy(File(run, "current.csv").toPath(), File(output, "current.csv").toPath(), REPLACE_EXISTING)
    Files.copy(File(run, "current.properties").toPath(), File(output, "current.properties").toPath(), REPLACE_EXISTING)
}
