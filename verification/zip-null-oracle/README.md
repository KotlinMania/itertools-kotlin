# Nullable ZipLongest and presence parity

The dependency graph lists eleven consumers for itertools-kotlin, compared
with ten for Chrono. The repo had no commonMain/commonTest changes. The
44 pre-existing dirty status entries were separately checkpointed in 1303728
(89 actual files); those build/template changes are not authored nullable
iterator repair work. Other candidate libraries were reviewed read-only.

## Source contract

Original itertools 0.14.0 zip_longest.rs next() matches the presence of two
Option values, preserving the payload even when the payload is itself None.
The Kotlin implementation tested payload != null instead, both in its List
path and its general Iterator path. Thus Both(null,null) terminated iteration
and a null on only one side changed the output variant or disappeared.

The repair selects List output tags from index availability and general
Iterator tags from availability/exhaustion state. It consumes left before
right, keeps Fuse-style exhaustion state, and uses no unchecked casts or new
optional-value owner. A present null remains inside Left, Right or Both.
The related EitherOrBoth.hasLeft/hasRight previously tested the nullable
accessor result. They now inspect the variant tag, equivalent to upstream's
Option.is_some check, which is independent of a nested Option payload.

Six new Kotlin test methods failed before repair. They cover List and
Iterator inputs, retained null tails, repeated hasNext, fused exhaustion of
a source that would otherwise revive, presence queries and forward/reverse
folds. Existing tests/assertions are unchanged.

The actual Rust oracle depends directly on unchanged tmp/itertools. Its five
tests use Option<T> as the item, so Some(None) is genuinely distinct from
iterator exhaustion; it does not reproduce the Kotlin implementation.
The repeated-peek test uses Rust Peekable to model Kotlin's prefetching
availability operation explicitly. Both tests of source fusion poll a
reviving input exactly once after exhaustion.

```sh
CARGO_TARGET_DIR=build/rust-kotlin-zip-null/cargo-target cargo test --manifest-path verification/zip-null-oracle/Cargo.toml --offline --locked
```

## Translator limitation

The native rust-kotlin executable was attempted on the original
zip_longest.rs using this repo's gradle.properties, source root and canonical
package. It ran for 3 minutes 7 seconds at roughly full CPU (121.2% at the
last sample), producing no output. Only the verified own process targeting
build/rust-kotlin-zip-null/ZipLongestReview.kt was stopped. The second original
input, either_or_both.rs, exceeded a separate 30-second subprocess timeout.
No generated implementation or successful translator diff is claimed.
These repairs use qualified direct source comparison after bounded tool-first
attempts; no generator change, duplicate job or competing Iris prompt occurred.
Receipts: build/rust-kotlin-zip-null/translator-attempt.txt and either-translator.log.

## Remaining limits

Nullable Option-returning accessors such as left()/right() still cannot
represent every nested-option state with Kotlin nullable returns alone; this
patch fixes tag queries, not that wider representation. Kotlin hasNext
prefetch and mixed front/back iteration/size-hint behavior are separate
existing contracts and not claimed fixed. Generic iterator cloning and Rust
ownership/borrow enforcement also remain outside scope. No custom algorithm,
Unicode approximation, shared type duplicate, pin or settings change is added.
No publication or downstream/other-platform completion is claimed.

Final-source validation: all 412 JVM and 412 macOS ARM64 tests passed, including
the six new regressions. All five actual Rust oracle tests passed. Global
static gates remain blocked by 17 existing Detekt findings and 80 existing
ktlint findings (78 commonMain, 2 commonTest). No finding points at changed
source lines or the new test. The new adversarial fixture originally lacked
a next() exhaustion guard; it was corrected, retaining assertions, before
these final full-suite/static receipts. No check was disabled or suppressed.
See build/rust-kotlin-zip-null/final-check.log, oracle.log,
static-baseline-comparison.json and final-source.sha256. No Kotlin/Rust source
changed after these final checks; subsequent changes only add this note.
Pre-existing checkpoint 1303728 has no claim of other-platform/CI/publication
validation. All dependency pins and authored configuration remain unchanged.
