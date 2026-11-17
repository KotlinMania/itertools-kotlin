# ZipLongest prefetch and reverse traversal

This step starts from clean authored commit 53fa721, preserving its documented
PeekNth nullable-result limitation. A read-only process reconciliation found
no remaining ZipLongest, EitherOrBoth or PeekNth translator attempt. The
original ZipLongest input is the same already-recorded CPU-bound case; it was
not blindly retried. This repair uses documented direct source comparison,
not a generated-output or generator-fix claim.

Kotlin hasNext prefetches one EitherOrBoth item, advancing the underlying
List cursors. Existing nextBack read only the raw List cursors and returned
null when those were exhausted, even if the prefetched item remained pending.
The List size hint likewise omitted that item. This lost the final cached
item during mixed traversal or reverse folds and understated exact hints.

Upstream itertools 0.14.0 zip_longest.rs:75 compares remaining input lengths
for reverse traversal and size_hint takes their maximum. Rust's standard
Peekable models Kotlin's availability prefetch explicitly, including the
pending front item in reverse iteration and exact hints. The actual Rust
oracle composes these unchanged implementations; it does not duplicate the
Kotlin algorithm. The reverse-fold case consumes a mutable borrow via
by_ref, so post-fold exhaustion can be checked legally on the Rust receiver.

The repair preserves the existing tail-selection order. If both raw List
cursors are empty, it consumes the sole pending item through the existing
next operation; otherwise it keeps the original raw-tail cases. The exact
List hint adds the pending item once. Existing EitherOrBoth objects distinguish
cached-item presence from nullable fields without a new Option type or cast.
No claim of general nested-Option return equivalence is introduced.

Five new Kotlin methods cover a prefetched nullable singleton, exact hints
across alternating ends, reverse fold with prefetch, mixed nullable tags and
unprefetched/empty controls. Four failed before repair; the controls already
passed. Five added actual Rust tests cover the same cases, bringing the
shared oracle to fifteen tests. Existing assertions remain unchanged.

Only List-backed reverse iteration and exact List hints are repaired here.
Generic iterator size hints, double-ended capability bounds, cloning and
borrow enforcement remain outside scope. The earlier PeekNth nullable-result
ambiguity and other Option-returning accessor limitations remain documented.
The affected when branches were also formatted, resolving their pre-existing
style findings; no checks were suppressed or disabled.

No pre-existing dirty work was mixed into this step. The separate 1303728
checkpoint remains an 89-file prior-work checkpoint with limited validation.
Dependency pins, authored configuration and all remote holds are unchanged.
Other platforms, downstream builds and publication metadata remain unverified.

Final-source validation: all 424 JVM and 424 macOS ARM64 tests passed, including
five new methods. All fifteen actual Rust oracle tests passed. Global gates
still report 17 existing Detekt findings and 74 existing ktlint findings
(72 commonMain, 2 commonTest). The six old style findings in the affected
when branches were corrected through formatting; none was suppressed. No
finding points at authored source lines or the new test. Receipts are in
build/rust-kotlin-zip-cache/{final-check.log,oracle.log,
static-baseline-comparison.json,final-source.sha256}. No Kotlin or Rust source
changed after validation; only documentation was subsequently updated.
