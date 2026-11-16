# Performance measurements

Kumo's performance suite is opt-in and uses [JMH](https://github.com/openjdk/jmh). Normal `mvn verify` runs correctness tests, not benchmarks. The existing `WordPlacerPerfTest` is now a seeded correctness smoke test rather than a one-shot stopwatch.

## Run and compare

Use a valid `JAVA_HOME` for JDK 11, 17, or 21. JDK 21 is the reference runtime for the initial local baseline and the manual GitHub workflow. Stop other heavy workloads and keep power/thermal conditions consistent when collecting reference measurements.

```sh
./perf.sh run --suite smoke       # Short harness check, not a reference measurement
./perf.sh run --suite quick       # Default: 4 cases, 2 seeds, 2 JVM forks per case/seed
./perf.sh compare                 # Compare latest successful run against the saved baseline
```

`run` rebuilds and tests the benchmark module and its reactor dependencies before measuring. The benchmark module is activated only by the `benchmarks` Maven profile. `./perf.sh build` or `./perf.sh test` builds the JMH harness and runs its correctness/tooling tests without timing anything. Generated Java harness files are confined to `target`; benchmark sources are Kotlin, processed with kapt.

The default measured settings are two warm-up iterations and three measurement iterations of one second each, across two independent JVM forks. Each fork uses one worker thread, a 512 MiB initial / 2 GiB maximum heap, UTF-8, headless AWT, and the JMH GC profiler. These are starting settings, not a guarantee that every workload has reached steady state. Increase them when results remain noisy.

The quick suite typically takes a few minutes including building and JVM startup. Larger suites take longer; stress cases are intentionally manual, not part of every PR.

## Suites and cases

| Suite | Cases | Default operations |
|---|---|---|
| `smoke` | Small R-tree and typical pixel-perfect, seed 42 | Layout; 1 fork, 1 warm-up, 1 measurement, 200 ms iterations |
| `quick` | Small linear/R-tree, typical pixel-perfect, dense pixel-perfect; seeds 42 and 123 | Layout |
| `full` | All non-stress cases; seeds 42 and 123 | Layout |
| `stress` | 4000×4000, 5000 words, R-tree and pixel-perfect; seeds 42 and 123 | Layout |
| `exports` | Typical pixel-perfect, seed 42 | PNG, cached SVG, layout + first SVG |
| `analysis` | Bundled DataRank text fixture | Text analysis |

Run `./perf.sh list` for the complete case catalog. It includes 100-word 500×500, 500-word 1000×1000, 2000-word 2000×2000, circle, whale-image mask, rotated, and overfilled scenarios. Linear and R-tree comparisons use rectangle collision mode; the rectangle placer is not used in pixel-perfect mode.

Examples for focused investigation:

```sh
./perf.sh run --cases typical-linear,typical-rtree --seeds 42,123
./perf.sh run --suite full --cases mask-pixel,circle-pixel,large-pixel
./perf.sh run --suite exports
./perf.sh run --suite analysis
./perf.sh run --cases typical-pixel --forks 3 --warmups 5 --iterations 8 --iteration-ms 2000
```

`--operations` accepts `layout,png,svgCached,layoutAndSvg,analyze`. Analysis always uses its own `text-analysis` case. The analysis seed is recorded for consistent result keys but does not affect tokenization. Use a different output directory or baseline file for experiments with different measurement settings.

### What is timed

- `layout`: construct a fresh `WordCloud`, prepare its word images, place words, and render its completed raster/layout snapshot. This is an end-to-end fresh build, not an isolated R-tree query benchmark. It does not time text analysis, fixture/font/mask decoding, encoding, or filesystem output.
- `png`: encode a prepared layout to a fresh in-memory PNG byte array. It does not include disk writes or repacking.
- `svgCached`: export a prepared layout whose lazy glyph geometry has been initialized during warm-up. It measures repeated SVG exports, not first-export cost.
- `layoutAndSvg`: generate a fresh layout and export its SVG for the first time. This includes lazy glyph geometry creation and prevents cached results from hiding first-use cost.
- `analyze`: construct a fresh `FrequencyAnalyzer` and analyze preloaded raw text. It excludes disk/network reads and layout generation.

Benchmark logging is disabled. Layout methods return their results to JMH so the generated harness consumes them. There are no explicit `System.gc()` calls between operations.

### Fixed inputs and quality

Layout fixtures use a deterministic generator of distinct ASCII words and positive Zipf-like integer frequencies. They are independent of the analyzer, so a tokenizer change cannot silently alter a placement workload. Every case fingerprints its exact word/frequency sequence, settings, and the bundled `OpenSansEmoji.ttf`; masked cases also fingerprint the mask image. Text analysis fingerprints the input text bytes.

Each selected case/seed is also run once outside timed iterations to capture placed/skipped counts, placed frequency weight, and a layout fingerprint containing word order, bounds, rotation, size, and color. These quality samples are not JMH auxiliary counters and do not add work to the timed operations. They are guardrails, not a substitute for spatial correctness or visual regression tests.

## Files

| Path | Purpose |
|---|---|
| `PERF.md` | Methodology, commands, and baseline policy |
| `perf/baseline.csv` | Reviewed reference results, tracked in Git |
| `perf/baseline.properties` | Reference environment, settings, artifact fingerprint, promotion reason, CSV checksum |
| `target/perf/current.csv` | Latest successful results |
| `target/perf/current.properties` | Metadata matching the latest CSV |
| `target/perf/comparison.md` | Human-readable comparison report |
| `target/perf/run-*/` | Per-run CSV, metadata, raw JMH JSON samples, and progress logs |

Normal runs never modify `perf/baseline.*`. Every invocation gets an archive directory. The top-level current pair is published only after all selected measurements succeed; an interrupted/failed run leaves the previous successful current pair intact. Its archive remains marked incomplete. CSV checksums prevent interrupted copies or mixed CSV/metadata pairs from being accepted. `mvn clean` removes these generated `target` reports and archives; preserve CI artifacts or copy them elsewhere for long-term raw-sample history. The repository baseline is outside `target` and survives cleaning.

Metadata includes the measured JAR's SHA-256, Git commit and dirty flag, CPU/core count, memory, OS/architecture, JDK vendor/runtime, JMH version, fork JVM arguments, and measurement settings. Injected JVM-option environment variables are hashed, not written out, because they can contain credentials. The artifact fingerprint identifies the actual timed binary even when the working tree is dirty; it is not an environment-compatibility key.

## Read a comparison

Reports match rows by case, operation, and seed—not by row order. Missing/new cases remain visible. Changed fixture hashes are not compared. Different hardware, OS, JDK, JVM settings, or measurement settings suppress timing percentages by default, even if both files are valid. Unidentified environments are also treated as incompatible.

```sh
./perf.sh compare --current target/perf/current.csv --baseline perf/baseline.csv
./perf.sh compare --allow-environment-mismatch   # Exploratory only; explicitly marked unsafe
```

Times are mean milliseconds per operation. Errors are JMH's reported confidence-interval half-widths, not standard deviations. The report flags a potential slowdown only when the mean increases by more than the warning threshold (default 10%, configurable with `--warn-percent`) and the reported intervals do not overlap. This is a conservative review heuristic, not a formal regression test. Short smoke runs may not have an error estimate and cannot claim a confident slowdown or improvement.

Fewer placed words or lower placed frequency weight is flagged independently of speed. Changed output is reported even when placement counts are unchanged. Allocation is JMH's normalized bytes/op, not peak heap or RSS; GC counts are also saved but are not normalized per operation.

All comparisons are report-only. A slower result does not fail CI. Invalid inputs, failed forks, corrupted results, and failed correctness tests still fail normally.

## Deliberately promote a baseline

First review the report and raw samples. Prefer a clean, committed working tree and repeated stable runs on the same machine.

```sh
./perf.sh promote --reason 'Accepted reference after reviewing layout quality and repeated runs'
```

Commit both `perf/baseline.csv` and `perf/baseline.properties`. Promotion requires a reason and refuses dirty/unknown source state, short/smoke settings, or accidentally dropping existing reference cases. Explicit overrides exist for deliberate exceptions:

```sh
./perf.sh promote --reason 'Provisional local reference for uncommitted development' --allow-dirty
./perf.sh promote --reason 'Intentionally reset the scenario catalog' --allow-case-removal
```

`--allow-smoke` is available for testing the promotion workflow, not for establishing a useful performance reference. Partial export/analysis suites should generally use a separate `--baseline perf/exports-baseline.csv` or `perf/analysis-baseline.csv` rather than replacing layout coverage.

The initial saved reference was collected locally from the in-progress Kotlin/layout work. Its metadata deliberately records a dirty source state and an explicit promotion exception. Treat it as a provisional reference for that environment; do not interpret Linux CI versus that Mac reference as a regression. Replace it with a clean-commit reference when the branch is committed and measurements are stable.

## CI and future changes

The ordinary build has a separate benchmark-tooling job that compiles the harness and runs its unit tests, but does not perform measurements. The manually dispatched **Performance report** workflow runs the selected suite on Java 21, publishes a job summary, and retains all raw results/logs as artifacts for 30 days. It never promotes a baseline, pushes files, or gates on timing.

Shared GitHub runners are noisy. For trustworthy PR comparisons, the next step is paired base/candidate measurements on the same controlled runner. Dedicated hardware and an observed noise floor should come before hard performance gates. No nightly schedule is enabled yet.

When changing fixture definitions or what a benchmark includes, bump `CONTRACT` and review the new reference instead of presenting the workloads as directly comparable. When changing project versions manually, activate `-Pbenchmarks` during `versions:set` so this optional module's parent version stays synchronized; the release workflow already does this.

Follow-up microbenchmarks can isolate R-tree queries or collision-mask row operations. They should complement these end-to-end measurements rather than replace them.
