# PeekNth predicate consumption and nullable items

This step starts from clean authored commit be5e037. A read-only process
check confirmed neither prior ZipLongest/EitherOrBoth translation attempt
remained running; no duplicate or unrelated job was stopped or restarted.
The separate new original peek_nth.rs input was attempted once with a
30-second subprocess limit. It produced no output and was stopped by that
limit. The receipt is build/rust-kotlin-peek-predicate/translator.log.
No generated implementation, successful translator diff or generator fix
is claimed; the repair follows direct unchanged-upstream comparison.

In itertools 0.14.0 peek_nth.rs:121, next_if first calls self.next(). If the
predicate accepts the item it returns it; on rejection it pushes it back;
on panic the removed item stays consumed. The previous Kotlin code peeked
before calling the predicate, so an exception retained the item incorrectly.
It also used null as absence and skipped the predicate for present null items.
The repair mirrors the source order and restores the item only on rejection.
nextIfEq delegates to this same operation.

The separate PeekingNext implementation at peek_nth.rs:171 calls peek/filter
before next. Thus rejection and panic retain the buffered item. That order
is intentionally preserved. Kotlin now checks buffer presence instead of
using an Elvis return on the nullable payload, so it calls the predicate for
present null items without changing the exception-consumption contract.

Seven new Kotlin methods cover accepted/rejected nulls, equality acceptance,
predicate invocation count, prefetched nextIf exception consumption,
peekingNext exception retention and empty-input behavior. Five failed before
repair; retention-on-peeking-panic and empty-input controls already passed.
No existing assertions or production iterator APIs were changed. Five added
actual Rust tests use Option<i32> payloads and catch_unwind on the original
PeekNth implementation; the shared oracle now contains ten actual tests.

The public Kotlin nullable result still cannot, by itself, distinguish an
accepted null from no returned item. Tests assert callback invocation and
subsequent iterator state rather than claiming a new nested-Option result
representation. The source oracle directly asserts Some(None). Generic
borrow/mutable-reference enforcement, deterministic Drop timing, existing
size-hint accounting and consuming-fold ownership remain outside this patch.
The exception tests verify cursor state and unchanged exception propagation,
not Kotlin/Rust destructor equivalence.

The older 1303728 checkpoint contains 44 pre-existing dirty status entries
(89 files) and remains separate from this authored repair. No other platform,
downstream, CI or publication validation is claimed for that checkpoint.
All Kotlin dependency pins and authored configuration remain unchanged.

Final-source validation: all 419 JVM and 419 macOS ARM64 tests passed, including
seven new methods. All ten actual Rust oracle tests passed. Global static
gates remain blocked by the same 17 existing Detekt and 80 existing ktlint
findings (78 commonMain, 2 commonTest), with none in authored source lines or
the new test. No check or assertion was weakened or suppressed. Receipts:
build/rust-kotlin-peek-predicate/{final-check.log,oracle.log,
static-baseline-comparison.json,final-source.sha256}. Final process reconciliation
confirms none of the three bounded own translator jobs remains running.
No Kotlin or Rust source changed after final validation, only this note.
