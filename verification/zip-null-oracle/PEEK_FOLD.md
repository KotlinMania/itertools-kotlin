# Consuming buffered PeekNth folds

This step starts from clean authored commit 8053950. The existing
peek_nth.rs 30-second translator timeout is reused as evidence; the same
stalled input was not retried and no duplicate job was launched. The repair
uses direct upstream comparison, not a generated-output or generator claim.

In itertools 0.14.0 peek_nth.rs, Iterator.next pops the front buffered item,
and the owning fold consumes buf.into_iter() before folding the underlying
iterator. Kotlin's fold loop iterated over buf without removing anything.
The aggregate result could be correct while prefetched items remained in the
receiver and were replayed by subsequent iteration. If a callback threw,
both successfully processed and failing buffered items remained in buf.

The Kotlin retained receiver is compared with Rust's legal mutable-borrow
form: iter.by_ref().fold(...). This drains buffered items before callbacks,
retains only unprocessed items on unwind, and permits checking receiver state
afterward. The repair changes the buffered loop to removeFirst before calling
f, preserving the source's order and the existing underlying-iterator loop.
MultiPeek already uses that same buffer-draining Kotlin representation.
No replacement aggregation algorithm or new optional-value type is added.

Four new Kotlin methods test prefetched-plus-underlying consumption, buffered
null consumption once, callback failure without replay and empty-fold control.
Three failed before repair; the empty control already passed. Four actual
Rust tests exercise the same borrowed forms on unchanged PeekNth, including
Option<i32> items and catch_unwind. The shared oracle now has nineteen tests.
No existing test assertion, public signature or dependency pin is changed.

This does not claim Rust's owning move/drop behavior is enforced by Kotlin,
or that generic deterministic destructor timing matches. The callback tests
check cursor state and unchanged exception propagation. Existing PeekNth
nullable-result ambiguity and size-hint accounting remain outside scope, as
do other adapters' consuming/borrowing contracts. No broad API migration is
performed.

Pre-existing checkpoint 1303728 (44 dirty entries / 89 files) remains separate
from authored repairs. It has no new other-platform, CI or publication
validation claim. Configuration and remote holds remain unchanged; downstream
builds and unbuilt platforms are not validated by this bounded repair.

Final-source validation: all 428 JVM and 428 macOS ARM64 tests passed, including
four new methods. All nineteen actual Rust oracle tests passed. Global gates
retain 17 existing Detekt and 74 existing ktlint findings (72 commonMain,
2 commonTest), none on authored lines or the new test. No assertion or check
was weakened, disabled or suppressed. Receipts are in
build/rust-kotlin-peek-fold/{final-check.log,oracle.log,
static-baseline-comparison.json,final-source.sha256}. No Kotlin or Rust source
changed after final validation; only this documentation note was appended.
