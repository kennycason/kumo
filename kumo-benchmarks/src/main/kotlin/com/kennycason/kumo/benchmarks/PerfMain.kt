package com.kennycason.kumo.benchmarks

import org.openjdk.jmh.profile.GCProfiler
import org.openjdk.jmh.results.format.ResultFormatType
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.options.OptionsBuilder
import org.openjdk.jmh.runner.options.TimeValue
import java.io.File
import java.nio.file.Files
import java.time.Instant
import kotlin.system.exitProcess

internal val forkArgs = listOf("-Xms512m", "-Xmx2g", "-Djava.awt.headless=true", "-Dfile.encoding=UTF-8")
internal val operations = listOf("layout", "png", "svgCached", "layoutAndSvg", "analyze")

internal data class RunSettings(
    val suite: String,
    val cases: List<String>,
    val seeds: List<Long>,
    val operations: List<String>,
    val forks: Int,
    val warmups: Int,
    val iterations: Int,
    val iterationMs: Long
) {
    init {
        require(forks > 0 && warmups > 0 && iterations > 0 && iterationMs > 0) { "Measurement settings must be positive" }
        require(seeds.isNotEmpty() && seeds.distinct().size == seeds.size) { "Supply distinct seeds" }
        require(operations.isNotEmpty() && operations.distinct().size == operations.size && operations.all { it in com.kennycason.kumo.benchmarks.operations }) { "Unknown or duplicate operation" }
        if (operations.any { it != "analyze" }) {
            require(cases.isNotEmpty() && cases.distinct().size == cases.size) { "Supply distinct cases" }
            cases.forEach { Cases.find(it) }
        }
    }

    fun caseIds(operation: String): List<String> = if (operation == "analyze") listOf("text-analysis") else cases

    fun metadata(): Map<String, String> = mapOf("suite" to suite, "forks" to forks.toString(), "warmups" to warmups.toString(),
        "iterations" to iterations.toString(), "iteration.ms" to iterationMs.toString(), "threads" to "1",
        "mode" to "AverageTime/ms", "profiler" to "gc", "jvm.args" to forkArgs.joinToString(" "))
}

private class Arguments(args: List<String>, allowed: Set<String>, flags: Set<String> = emptySet()) {
    private val values = mutableMapOf<String, String>()
    init {
        var index = 0
        while (index < args.size) {
            val name = args[index].removePrefix("--")
            require(args[index].startsWith("--") && name in allowed && name !in values) { "Unknown or repeated option: ${args[index]}" }
            if (name in flags) values[name] = "true"
            else {
                require(index + 1 < args.size && !args[index + 1].startsWith("--")) { "Missing value for --$name" }
                values[name] = args[++index]
            }
            index++
        }
    }
    operator fun get(name: String): String? = values[name]
    fun flag(name: String): Boolean = values[name] == "true"
    fun file(name: String, default: String): File = File(values[name] ?: default)
}

private fun settings(args: Arguments): RunSettings {
    val suite = args["suite"] ?: "quick"
    require(suite in listOf("smoke", "quick", "full", "stress", "exports", "analysis")) { "Unknown suite: $suite" }
    val defaults = when (suite) {
        "smoke" -> listOf("small-rtree", "typical-pixel")
        "full" -> Cases.all.filterNot { it.id.startsWith("stress") }.map { it.id }
        "stress" -> Cases.all.filter { it.id.startsWith("stress") }.map { it.id }
        "exports" -> listOf("typical-pixel")
        "analysis" -> emptyList()
        else -> Cases.quick
    }
    val selectedOperations = args["operations"]?.split(',') ?: when (suite) {
        "exports" -> listOf("png", "svgCached", "layoutAndSvg")
        "analysis" -> listOf("analyze")
        else -> listOf("layout")
    }
    return RunSettings(suite, args["cases"]?.split(',') ?: defaults,
        args["seeds"]?.split(',')?.map { it.toLong() } ?: if (suite in listOf("quick", "full", "stress")) listOf(42, 123) else listOf(42),
        selectedOperations, args["forks"]?.toInt() ?: if (suite == "smoke") 1 else 2,
        args["warmups"]?.toInt() ?: if (suite == "smoke") 1 else 2,
        args["iterations"]?.toInt() ?: if (suite == "smoke") 1 else 3,
        args["iteration-ms"]?.toLong() ?: if (suite == "smoke") 200 else 1000)
}

private fun report(current: File, baseline: File, output: File, allowMismatch: Boolean = false, warnPercent: Double = 10.0) {
    require(warnPercent.isFinite() && warnPercent >= 0) { "--warn-percent must be finite and nonnegative" }
    require(output.canonicalFile !in listOf(current, baseline, metadataFile(current), metadataFile(baseline)).map { it.canonicalFile }) {
        "A report must not overwrite result CSV or metadata files"
    }
    val base = if (baseline.exists()) ResultSet.load(baseline) else null
    output.parentFile?.mkdirs()
    output.writeText(compareResults(base, ResultSet.load(current), allowMismatch, warnPercent), Charsets.UTF_8)
    println("Comparison: ${output.path}")
}

private fun runBenchmarks(settings: RunSettings, output: File, baseline: File) {
    output.mkdirs()
    val run = Files.createTempDirectory(output.toPath(), "run-").toFile()
    val caseMetadata = settings.operations.flatMap { settings.caseIds(it) }.distinct().associate { id ->
        "case.$id" to if (id == "text-analysis") "Preloaded DataRank text; default FrequencyAnalyzer settings" else Cases.find(id).toString()
    }
    val metadata = environment(File(".").canonicalFile) + settings.metadata() + caseMetadata
    writeMetadata(File(run, "run.properties"), metadata + ("status" to "incomplete"))
    val rows = mutableListOf<PerfRow>()
    val qualitySamples = mutableMapOf<Pair<String, Long>, Quality>()
    for (operation in settings.operations) {
        val ids = settings.caseIds(operation)
        for (id in ids) for (seed in settings.seeds) qualitySamples.getOrPut(id to seed) { quality(id, seed) }
        println("Benchmarking $operation: ${ids.size} cases × ${settings.seeds.size} seeds × ${settings.forks} forks")
        val log = File(run, "jmh-$operation.log")
        println("Progress log: ${log.path}")
        val className = if (operation == "analyze") AnalyzerBenchmarks::class.java.name else WordCloudBenchmarks::class.java.name
        val options = OptionsBuilder().include("^${Regex.escape(className)}\\.$operation$")
            .param("caseId", *ids.toTypedArray()).param("seed", *settings.seeds.map { it.toString() }.toTypedArray())
            .threads(1).forks(settings.forks).warmupIterations(settings.warmups)
            .warmupTime(TimeValue.milliseconds(settings.iterationMs)).measurementIterations(settings.iterations)
            .measurementTime(TimeValue.milliseconds(settings.iterationMs)).timeout(TimeValue.minutes(5))
            .jvmArgs(*forkArgs.toTypedArray()).addProfiler(GCProfiler::class.java).shouldFailOnError(true)
            .resultFormat(ResultFormatType.JSON).result(File(run, "jmh-$operation.json").path).output(log.path).build()
        val results = try { Runner(options).run() } catch (error: Exception) {
            throw IllegalStateException("Benchmark failed; see ${log.path}", error)
        }
        check(results.size == ids.size * settings.seeds.size) { "Missing JMH results; see ${log.path}" }
        for (result in results) {
            val params = result.params
            val id = params.getParam("caseId")
            val seed = params.getParam("seed").toLong()
            val primary = result.primaryResult
            check(primary.scoreUnit == "ms/op") { "Unexpected JMH units: ${primary.scoreUnit}" }
            fun metric(name: String): Double? = result.secondaryResults[name]?.score?.takeIf { it.isFinite() && it >= 0 }
            rows.add(PerfRow(id, operation, seed, qualitySamples.getValue(id to seed), primary.score,
                primary.scoreError.takeIf { it.isFinite() && it >= 0 }, metric("gc.alloc.rate.norm"), metric("gc.count")))
        }
    }
    val completed = metadata + mapOf("completed" to Instant.now().toString(), "status" to "complete")
    ResultSet(rows.sortedBy { it.key }, completed).save(File(run, "current.csv"))
    writeMetadata(File(run, "run.properties"), completed)
    publishRun(run, output)
    report(File(output, "current.csv"), baseline, File(output, "comparison.md"))
    File(run, "comparison.md").writeText(File(output, "comparison.md").readText(Charsets.UTF_8), Charsets.UTF_8)
    println("Results: ${File(output, "current.csv").path}")
    println("Raw JMH data, logs, and metadata retained in ${run.path}")
}

fun main(args: Array<String>) {
    if (args.isEmpty() || args.any { it == "--help" || it == "help" }) {
        println("""
            Kumo performance tools (run from the repository root using ./perf.sh)
              run      [--suite smoke|quick|full|stress|exports|analysis] [--cases id,id] [--seeds 42,123]
                       [--operations layout,png,svgCached,layoutAndSvg,analyze] [--forks 2] [--warmups 2]
                       [--iterations 3] [--iteration-ms 1000] [--output target/perf] [--baseline perf/baseline.csv]
              compare  [--current target/perf/current.csv] [--baseline perf/baseline.csv] [--report target/perf/comparison.md]
                       [--allow-environment-mismatch] [--warn-percent 10]
              promote  --reason 'Why this baseline is accepted' [--current target/perf/current.csv]
                       [--baseline perf/baseline.csv] [--allow-dirty] [--allow-smoke] [--allow-case-removal]
              list
            Comparisons are report-only. Baselines change only through explicit promotion.
        """.trimIndent())
        return
    }
    try {
        when (args.first()) {
            "list" -> {
                require(args.size == 1) { "list does not accept options" }
                Cases.all.forEach { println("${it.id}: ${it.width}×${it.height}, ${it.count} words, ${it.mode}, ${it.shape}") }
            }
            "run" -> {
                val options = Arguments(args.drop(1), setOf("suite", "cases", "seeds", "operations", "forks", "warmups", "iterations", "iteration-ms", "output", "baseline"))
                runBenchmarks(settings(options), options.file("output", "target/perf"), options.file("baseline", "perf/baseline.csv"))
            }
            "compare" -> {
                val options = Arguments(args.drop(1), setOf("current", "baseline", "report", "allow-environment-mismatch", "warn-percent"), setOf("allow-environment-mismatch"))
                report(options.file("current", "target/perf/current.csv"), options.file("baseline", "perf/baseline.csv"),
                    options.file("report", "target/perf/comparison.md"), options.flag("allow-environment-mismatch"), options["warn-percent"]?.toDouble() ?: 10.0)
            }
            "promote" -> {
                val flags = setOf("allow-dirty", "allow-smoke", "allow-case-removal")
                val options = Arguments(args.drop(1), flags + setOf("current", "baseline", "reason"), flags)
                val current = options.file("current", "target/perf/current.csv")
                val baseline = options.file("baseline", "perf/baseline.csv")
                promote(current, baseline, options["reason"].orEmpty(), options.flag("allow-dirty"), options.flag("allow-smoke"), options.flag("allow-case-removal"))
                println("Baseline updated: ${baseline.path}. Review and commit its CSV and .properties file together.")
            }
            else -> error("Unknown command: ${args.first()}. Use --help.")
        }
    } catch (error: Exception) {
        System.err.println("Performance command failed: ${error.message}")
        exitProcess(1)
    }
}
