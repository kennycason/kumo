# Kumo Kotlin roadmap

The Kotlin migration branch incorporates the upstream collision-cache optimization. The layout-first Kotlin API, seeded generation, repeat-build resets, explicit analyzer methods, and short-circuit R-tree queries are now implemented. Further work should focus on measured placement quality and performance improvements.

## Migration merge conflict resolved

On October 18, 2026, `kenny/kotlin-migration` was rebased onto `master` at `1c95daf` to resolve the Java-to-Kotlin migration conflict in [PR #117](https://github.com/kennycason/kumo/pull/117). The conflicting path was:

`kumo-core/src/main/java/com/kennycason/kumo/placement/LinearWordPlacer.java`

The upstream changes were ported into [LinearWordPlacer.kt](kumo-core/src/main/kotlin/com/kennycason/kumo/placement/LinearWordPlacer.kt), and the superseded Java file remains deleted. The upstream README update and the roadmap notes were both preserved.

The Kotlin port preserves the `lastHitCache` behavior from the [upstream Java implementation](https://github.com/kennycason/kumo/blob/1c95daf7a1901854f55f39a01000cbe5c4edbe86/kumo-core/src/main/java/com/kennycason/kumo/placement/LinearWordPlacer.java):

- Remember the previously colliding placed word for each word being positioned.
- Check that cached collision first on subsequent placement attempts, avoiding a full scan when it still collides.
- Update the cache when another collision is found and remove the entry after successful placement.
- Clear both placed words and cached collisions in `reset()`, fixing the upstream cache cleanup omission.

[Regression tests](kumo-core/src/test/kotlin/com/kennycason/kumo/placement/LinearWordPlacerTest.kt) cover ordinary placement, repeated collisions without a full scan, moving away from a cached collision, switching blockers, and reset behavior.

Java 11 verification passed with `mvn -B clean verify -Djava.awt.headless=true`: 51 tests passed, no failures or errors, and one previously disabled rotation test remained skipped. All modules packaged successfully.

## Implemented: layout results and separate rendering

`wordcloud(words) { size(1000, 700); seed = 42; padding = 2 }` returns a `CloudLayout` snapshot containing placed words, positions, fonts, rotations, colors, bounds, and reasons for skipped words. Its `writePng` and `writeSvg` methods render without repeating placement. SVG exports actual vector glyph outlines rather than embedded raster images or font-dependent text.

The existing `WordCloud` API exposes the latest result as `layout`; older snapshots and their rendering remain independent of subsequent builds. Hit testing, recoloring without repacking, and interactive previews can build on this result API later.

## Implemented: reproducible and independent builds

- A build seed controls built-in random placement, rotation, and optional random colors. Palette cycling starts fresh for every build.
- Standard and polar builds reset their occupancy, image, placed words, skipped words, and random source. Building again replaces rather than accumulates a layout.
- Reproducible pixel output still requires consistent fonts and rendering settings. Custom random strategies must use the supplied build random source.
- Regression tests cover seeded output, repeat builds, changed background masks, independent snapshots, and PNG/SVG export.

The layout API passes full `clean verify` builds on Java 11, 17, and 21: 76 tests passed and the one pre-existing disabled rotation test remained skipped on each version.

## Measure and improve performance

- Implemented: R-tree collision queries stop at the first match rather than counting all intersections.
- Deferred by request: prepare and place word images incrementally instead of constructing all word images before placement begins (`buildWords`).
- Follow-up after the API updates: compare the current `BitSet` traversal with packed row operations using bitwise AND and OR for collision checks and mask updates.
- Evaluate caching font metrics and reusable word masks where repeated inputs or placement retries justify it.
- Implemented: opt-in [JMH benchmarks and baseline/report tooling](PERF.md) across word counts, canvas sizes, rotations, and sparse/dense shapes. Layout, encoding, SVG first/repeat export, and analyzer measurements are separate. Reports record time, allocation, and placement quality; peak-memory profiling remains future work.

[d3-cloud](https://github.com/jasondavies/d3-cloud) is a useful reference for sprite-mask-based word placement.

## Improve placement quality with bounded retries

- Allow alternate angles and starting points before skipping a word.
- Optionally retry at smaller font sizes, with an explicit minimum size.
- Offer placement budgets for fast previews and denser final output, including time or attempt limits.
- Explore shape-aware starting points to avoid spending attempts in unusable regions of a mask.
- Compare strategies using placed-word count, coverage, readability, and runtime.

## Clarify text analysis and support larger inputs

- Implemented: `analyzeText`, `analyzeFile`, `analyze(reader)`, `analyze(inputStream)`, `analyzeTexts`, `analyzeUrl`, and `topFrequencies`. The old `load` methods are deprecated aliases.
- Implemented: count tokens while reading instead of collecting all input lines first. The frequency dictionary still needs space for distinct tokens.
- Evaluate selecting the top words with a bounded heap instead of sorting the full vocabulary.
- Improve handling of tabs, Unicode whitespace, word boundaries, and language-specific tokenization. The default tokenizer currently splits ordinary spaces only.

## Make asynchronous work explicit

Provide an asynchronous layout operation with progress, cancellation, and bounded concurrency. Its completion should mean that the layout result is ready; currently, parallel layer builds return early and writing the output performs the wait.

Independent layers and word-image preparation are candidates for parallel execution. Word placement requires coordination because each accepted word changes the remaining space.

## Explore new features

- SVG export is implemented; interactive previews can build on the layout result.
- Word colors sampled from a background image.
- Pinned words and preserving existing positions when a cloud is updated.
- Fractional weights for relevance scores and TF-IDF, alongside frequency counts.
- Shared size scales and normalized weights for more meaningful polar-cloud comparisons between datasets.
