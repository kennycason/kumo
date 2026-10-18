# Kumo Kotlin roadmap

The Kotlin migration branch now incorporates the upstream collision-cache optimization. Future development should prioritize a layout result API, reproducible builds, and measured improvements to placement quality and performance.

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

## Return a layout result and separate rendering

Have layout generation return a `CloudLayout` containing placed words, positions, sizes, rotations, colors, bounds, and reasons for skipped words. Keep build state separate from configuration and rendering.

This would support SVG export, hit testing, recoloring without repacking, and interactive previews. A Kotlin DSL could provide a convenient configuration layer over that API.

## Make layouts reproducible and builds independent

- Supply one seed that controls random placement, rotation, and color selection.
- Define repeat-build behavior. Currently, `WordCloud.build()` resets the word placer and skipped set but does not clear its existing image or collision raster.
- Keep occupancy, palette cursors, and other working state local to a build. Reproducible pixel output also requires consistent fonts and rendering settings.
- Use fixed inputs and seeds for visual regression tests and performance comparisons.

## Measure and improve performance

- Replace the R-tree collision query's full match count with an existence query that stops at the first match.
- Prepare and place word images incrementally instead of constructing all word images before placement begins.
- Compare the current `BitSet` traversal with packed row operations using bitwise AND and OR for collision checks and mask updates.
- Evaluate caching font metrics and reusable word masks where repeated inputs or placement retries justify it.
- Establish [JMH benchmarks](https://github.com/openjdk/jmh) across word counts, canvas sizes, rotations, and sparse and dense shapes. Measure runtime, allocations, peak memory, and placement success. Speed improvements should preserve layout quality.

[d3-cloud](https://github.com/jasondavies/d3-cloud) is a useful reference for sprite-mask-based word placement.

## Improve placement quality with bounded retries

- Allow alternate angles and starting points before skipping a word.
- Optionally retry at smaller font sizes, with an explicit minimum size.
- Offer placement budgets for fast previews and denser final output, including time or attempt limits.
- Explore shape-aware starting points to avoid spending attempts in unusable regions of a mask.
- Compare strategies using placed-word count, coverage, readability, and runtime.

## Clarify text analysis and support larger inputs

- Distinguish raw text, file paths, and readers with APIs such as `analyzeText`, `analyzeFile`, and `analyze(reader)`. Currently, `load(String)` means a file path.
- Count tokens while reading instead of collecting all input lines first. The frequency dictionary still needs space for distinct tokens.
- Evaluate selecting the top words with a bounded heap instead of sorting the full vocabulary.
- Improve handling of tabs, Unicode whitespace, word boundaries, and language-specific tokenization. The default tokenizer currently splits ordinary spaces only.

## Make asynchronous work explicit

Provide an asynchronous layout operation with progress, cancellation, and bounded concurrency. Its completion should mean that the layout result is ready; currently, parallel layer builds return early and writing the output performs the wait.

Independent layers and word-image preparation are candidates for parallel execution. Word placement requires coordination because each accepted word changes the remaining space.

## Explore new features

- SVG export and interactive previews built from the layout result.
- Word colors sampled from a background image.
- Pinned words and preserving existing positions when a cloud is updated.
- Fractional weights for relevance scores and TF-IDF, alongside frequency counts.
- Shared size scales and normalized weights for more meaningful polar-cloud comparisons between datasets.
