# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 50/73 (68.5%)
- **Function parity:** 494/795 matched (target 1308) — 62.1%
- **Class/type parity:** 126/204 matched (target 252) — 61.8%
- **Combined symbol parity:** 620/999 matched (target 1560) — 62.1%
- **Average inline-code cosine:** 0.53 (function body across 49 matched files)
- **Average documentation cosine:** 0.65 (doc text across 49 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 36 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

### 1. size_hint
- **Similarity:** 0.75 (needs 10% improvement)
- **Dependencies:** 15
- **Priority Score:** 15000903.0
- **Functions:** 8/8 matched (target 15)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Action:** Review and complete missing sections

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. size_hint

- **Target:** `itertools.SizeHint [PROVENANCE-FALLBACK]`
- **Similarity:** 0.75
- **Dependents:** 15
- **Priority Score:** 15000903.0
- **Functions:** 8/8 matched (target 15)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 1/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `size_hint.rs` vs expected `size_hint.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:size_hint.rs` vs expected `size_hint.rs`
- **Proposed provenance header:** `// port-lint: source size_hint.rs` (current: `// port-lint: source size_hint.rs`)
- **Proposed provenance header:** `// port-lint: tests size_hint.rs` (current: `// port-lint: tests size_hint.rs`)
- **Lint issues:** 2

### 2. either_or_both

- **Target:** `itertools.EitherOrBoth [PROVENANCE-FALLBACK]`
- **Similarity:** 0.57
- **Dependents:** 4
- **Priority Score:** 4003604.2
- **Functions:** 35/35 matched (target 45)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `either_or_both.rs` vs expected `either_or_both.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:either_or_both.rs` vs expected `either_or_both.rs`
- **Proposed provenance header:** `// port-lint: source either_or_both.rs` (current: `// port-lint: source either_or_both.rs`)
- **Proposed provenance header:** `// port-lint: tests either_or_both.rs` (current: `// port-lint: tests either_or_both.rs`)
- **Lint issues:** 2

### 3. peek_nth

- **Target:** `itertools.PeekNth [PROVENANCE-FALLBACK]`
- **Similarity:** 0.61
- **Dependents:** 3
- **Priority Score:** 3011204.0
- **Functions:** 10/10 matched (target 14)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `peek_nth.rs` vs expected `peek_nth.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:peek_nth.rs` vs expected `peek_nth.rs`
- **Proposed provenance header:** `// port-lint: source peek_nth.rs` (current: `// port-lint: source peek_nth.rs`)
- **Proposed provenance header:** `// port-lint: tests peek_nth.rs` (current: `// port-lint: tests peek_nth.rs`)
- **Lint issues:** 2

### 4. repeatn

- **Target:** `itertools.RepeatN [PROVENANCE-FALLBACK]`
- **Similarity:** 0.41
- **Dependents:** 3
- **Priority Score:** 3010805.8
- **Functions:** 6/6 matched (target 8)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `repeatn.rs` vs expected `repeatn.rs`
- **Proposed provenance header:** `// port-lint: source repeatn.rs` (current: `// port-lint: source repeatn.rs`)
- **Lint issues:** 1

### 5. lazy_buffer

- **Target:** `itertools.LazyBuffer [PROVENANCE-FALLBACK]`
- **Similarity:** 0.50
- **Dependents:** 3
- **Priority Score:** 3001105.0
- **Functions:** 9/9 matched (target 19)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `lazy_buffer.rs` vs expected `lazy_buffer.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:lazy_buffer.rs` vs expected `lazy_buffer.rs`
- **Proposed provenance header:** `// port-lint: source lazy_buffer.rs` (current: `// port-lint: source lazy_buffer.rs`)
- **Proposed provenance header:** `// port-lint: tests lazy_buffer.rs` (current: `// port-lint: tests lazy_buffer.rs`)
- **Lint issues:** 2

### 6. intersperse

- **Target:** `itertools.Intersperse [PROVENANCE-FALLBACK]`
- **Similarity:** 0.50
- **Dependents:** 1
- **Priority Score:** 1021105.0
- **Functions:** 6/6 matched (target 24)
- **Missing functions:** _none_
- **Types:** 3/5 matched (target 7)
- **Missing types:** `IntersperseElement`, `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `intersperse.rs` vs expected `intersperse.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:intersperse.rs` vs expected `intersperse.rs`
- **Proposed provenance header:** `// port-lint: source intersperse.rs` (current: `// port-lint: source intersperse.rs`)
- **Proposed provenance header:** `// port-lint: tests intersperse.rs` (current: `// port-lint: tests intersperse.rs`)
- **Lint issues:** 2

### 7. adaptors.multi_product

- **Target:** `adaptors.MultiProduct [PROVENANCE-FALLBACK]`
- **Similarity:** 0.55
- **Dependents:** 1
- **Priority Score:** 1011004.5
- **Functions:** 6/6 matched (target 22)
- **Missing functions:** _none_
- **Types:** 3/4 matched
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `adaptors/multi_product.rs` vs expected `adaptors/multi_product.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:adaptors/multi_product.rs` vs expected `adaptors/multi_product.rs`
- **Proposed provenance header:** `// port-lint: source adaptors/multi_product.rs` (current: `// port-lint: source adaptors/multi_product.rs`)
- **Proposed provenance header:** `// port-lint: tests adaptors/multi_product.rs` (current: `// port-lint: tests adaptors/multi_product.rs`)
- **Lint issues:** 2

### 8. powerset

- **Target:** `itertools.Powerset [PROVENANCE-FALLBACK]`
- **Similarity:** 0.69
- **Dependents:** 1
- **Priority Score:** 1011003.1
- **Functions:** 8/8 matched (target 13)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `powerset.rs` vs expected `powerset.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:powerset.rs` vs expected `powerset.rs`
- **Proposed provenance header:** `// port-lint: source powerset.rs` (current: `// port-lint: source powerset.rs`)
- **Proposed provenance header:** `// port-lint: tests powerset.rs` (current: `// port-lint: tests powerset.rs`)
- **Lint issues:** 2

### 9. combinations_with_replacement

- **Target:** `itertools.CombinationsWithReplacement [PROVENANCE-FALLBACK]`
- **Similarity:** 0.50
- **Dependents:** 1
- **Priority Score:** 1010905.1
- **Functions:** 7/7 matched (target 14)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `combinations_with_replacement.rs` vs expected `combinations_with_replacement.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:combinations_with_replacement.rs` vs expected `combinations_with_replacement.rs`
- **Proposed provenance header:** `// port-lint: source combinations_with_replacement.rs` (current: `// port-lint: source combinations_with_replacement.rs`)
- **Proposed provenance header:** `// port-lint: tests combinations_with_replacement.rs` (current: `// port-lint: tests combinations_with_replacement.rs`)
- **Lint issues:** 2

### 10. permutations

- **Target:** `itertools.Permutations [PROVENANCE-FALLBACK]`
- **Similarity:** 0.54
- **Dependents:** 1
- **Priority Score:** 1010904.6
- **Functions:** 6/6 matched (target 15)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 7)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `permutations.rs` vs expected `permutations.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:permutations.rs` vs expected `permutations.rs`
- **Proposed provenance header:** `// port-lint: source permutations.rs` (current: `// port-lint: source permutations.rs`)
- **Proposed provenance header:** `// port-lint: tests permutations.rs` (current: `// port-lint: tests permutations.rs`)
- **Lint issues:** 2

### 11. zip_longest

- **Target:** `itertools.ZipLongest [PROVENANCE-FALLBACK]`
- **Similarity:** 0.40
- **Dependents:** 1
- **Priority Score:** 1010806.0
- **Functions:** 6/6 matched (target 16)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `zip_longest.rs` vs expected `zip_longest.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:zip_longest.rs` vs expected `zip_longest.rs`
- **Proposed provenance header:** `// port-lint: source zip_longest.rs` (current: `// port-lint: source zip_longest.rs`)
- **Proposed provenance header:** `// port-lint: tests zip_longest.rs` (current: `// port-lint: tests zip_longest.rs`)
- **Lint issues:** 2

### 12. flatten_ok

- **Target:** `itertools.FlattenOk [PROVENANCE-FALLBACK]`
- **Similarity:** 0.53
- **Dependents:** 1
- **Priority Score:** 1010804.8
- **Functions:** 6/6 matched (target 18)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 5)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `flatten_ok.rs` vs expected `flatten_ok.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:flatten_ok.rs` vs expected `flatten_ok.rs`
- **Proposed provenance header:** `// port-lint: source flatten_ok.rs` (current: `// port-lint: source flatten_ok.rs`)
- **Proposed provenance header:** `// port-lint: tests flatten_ok.rs` (current: `// port-lint: tests flatten_ok.rs`)
- **Lint issues:** 2

### 13. peeking_take_while

- **Target:** `itertools.PeekingTakeWhile [PROVENANCE-FALLBACK]`
- **Similarity:** 0.37
- **Dependents:** 1
- **Priority Score:** 1010706.3
- **Functions:** 4/4 matched (target 18)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 4)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `peeking_take_while.rs` vs expected `peeking_take_while.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:peeking_take_while.rs` vs expected `peeking_take_while.rs`
- **Proposed provenance header:** `// port-lint: source peeking_take_while.rs` (current: `// port-lint: source peeking_take_while.rs`)
- **Proposed provenance header:** `// port-lint: tests peeking_take_while.rs` (current: `// port-lint: tests peeking_take_while.rs`)
- **Lint issues:** 2

### 14. with_position

- **Target:** `itertools.WithPosition [PROVENANCE-FALLBACK]`
- **Similarity:** 0.39
- **Dependents:** 1
- **Priority Score:** 1010706.1
- **Functions:** 4/4 matched (target 17)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 5)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `with_position.rs` vs expected `with_position.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:with_position.rs` vs expected `with_position.rs`
- **Proposed provenance header:** `// port-lint: source with_position.rs` (current: `// port-lint: source with_position.rs`)
- **Proposed provenance header:** `// port-lint: tests with_position.rs` (current: `// port-lint: tests with_position.rs`)
- **Lint issues:** 2

### 15. take_while_inclusive

- **Target:** `itertools.TakeWhileInclusive [PROVENANCE-FALLBACK]`
- **Similarity:** 0.53
- **Dependents:** 1
- **Priority Score:** 1010604.7
- **Functions:** 4/4 matched (target 16)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `take_while_inclusive.rs` vs expected `take_while_inclusive.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:take_while_inclusive.rs` vs expected `take_while_inclusive.rs`
- **Proposed provenance header:** `// port-lint: source take_while_inclusive.rs` (current: `// port-lint: source take_while_inclusive.rs`)
- **Proposed provenance header:** `// port-lint: tests take_while_inclusive.rs` (current: `// port-lint: tests take_while_inclusive.rs`)
- **Lint issues:** 2

### 16. tee

- **Target:** `itertools.Tee [PROVENANCE-FALLBACK]`
- **Similarity:** 0.57
- **Dependents:** 1
- **Priority Score:** 1010604.4
- **Functions:** 3/3 matched (target 15)
- **Missing functions:** _none_
- **Types:** 2/3 matched
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tee.rs` vs expected `tee.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:tee.rs` vs expected `tee.rs`
- **Proposed provenance header:** `// port-lint: source tee.rs` (current: `// port-lint: source tee.rs`)
- **Proposed provenance header:** `// port-lint: tests tee.rs` (current: `// port-lint: tests tee.rs`)
- **Lint issues:** 2

### 17. diff

- **Target:** `itertools.Diff [PROVENANCE-FALLBACK]`
- **Similarity:** 0.49
- **Dependents:** 1
- **Priority Score:** 1000405.1
- **Functions:** 3/3 matched (target 15)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 6)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `diff.rs` vs expected `diff.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:diff.rs` vs expected `diff.rs`
- **Proposed provenance header:** `// port-lint: source diff.rs` (current: `// port-lint: source diff.rs`)
- **Proposed provenance header:** `// port-lint: tests diff.rs` (current: `// port-lint: tests diff.rs`)
- **Lint issues:** 2

### 18. groupbylazy

- **Target:** `itertools.Groupbylazy [PROVENANCE-FALLBACK]`
- **Similarity:** 0.35
- **Dependents:** 0
- **Priority Score:** 52706.5
- **Functions:** 12/14 matched (target 24)
- **Missing functions:** `into_iter`, `drop`
- **Types:** 10/13 matched (target 11)
- **Missing types:** `Key`, `Item`, `IntoIter`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `groupbylazy.rs` vs expected `groupbylazy.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:groupbylazy.rs` vs expected `groupbylazy.rs`
- **Proposed provenance header:** `// port-lint: source groupbylazy.rs` (current: `// port-lint: source groupbylazy.rs`)
- **Proposed provenance header:** `// port-lint: tests groupbylazy.rs` (current: `// port-lint: tests groupbylazy.rs`)
- **Lint issues:** 2

### 19. adaptors.coalesce

- **Target:** `adaptors.Coalesce [PROVENANCE-FALLBACK]`
- **Similarity:** 0.52
- **Dependents:** 0
- **Priority Score:** 52704.8
- **Functions:** 11/11 matched (target 36)
- **Missing functions:** _none_
- **Types:** 11/16 matched (target 15)
- **Missing types:** `CoalescePredicate`, `Item`, `CountItem`, `CItem`, `DedupPredicate`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `adaptors/coalesce.rs` vs expected `adaptors/coalesce.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:adaptors/coalesce.rs` vs expected `adaptors/coalesce.rs`
- **Proposed provenance header:** `// port-lint: source adaptors/coalesce.rs` (current: `// port-lint: source adaptors/coalesce.rs`)
- **Proposed provenance header:** `// port-lint: tests adaptors/coalesce.rs` (current: `// port-lint: tests adaptors/coalesce.rs`)
- **Lint issues:** 2

### 20. next_array

- **Target:** `itertools.NextArray [PROVENANCE-FALLBACK]`
- **Similarity:** 0.41
- **Dependents:** 0
- **Priority Score:** 51305.9
- **Functions:** 7/11 matched (target 7)
- **Missing functions:** `new`, `drop`, `slice_assume_init_mut`, `tracked_drop`
- **Types:** 1/2 matched
- **Missing types:** `TrackedDrop`
- **Tests:** 3/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `next_array.rs` vs expected `next_array.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:next_array.rs` vs expected `next_array.rs`
- **Proposed provenance header:** `// port-lint: source next_array.rs` (current: `// port-lint: source next_array.rs`)
- **Proposed provenance header:** `// port-lint: tests next_array.rs` (current: `// port-lint: tests next_array.rs`)
- **Lint issues:** 2

### 21. lib

- **Target:** `itertools.Itertools [PROVENANCE-FALLBACK]`
- **Similarity:** 0.53
- **Dependents:** 0
- **Priority Score:** 44204.7
- **Functions:** 137/137 matched (target 253)
- **Missing functions:** _none_
- **Types:** 2/5 matched (target 14)
- **Missing types:** `VecDequeIntoIter`, `VecIntoIter`, `Itertools`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source lib.rs`)
- **Lint issues:** 1

### 22. duplicates_impl

- **Target:** `itertools.DuplicatesImpl [PROVENANCE-FALLBACK]`
- **Similarity:** 0.61
- **Dependents:** 0
- **Priority Score:** 42203.9
- **Functions:** 9/11 matched (target 27)
- **Missing functions:** `new`, `next_back`
- **Types:** 9/11 matched (target 10)
- **Missing types:** `Item`, `Container`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `duplicates_impl.rs` vs expected `duplicates_impl.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:duplicates_impl.rs` vs expected `duplicates_impl.rs`
- **Proposed provenance header:** `// port-lint: source duplicates_impl.rs` (current: `// port-lint: source duplicates_impl.rs`)
- **Proposed provenance header:** `// port-lint: tests duplicates_impl.rs` (current: `// port-lint: tests duplicates_impl.rs`)
- **Lint issues:** 2

### 23. merge_join

- **Target:** `itertools.MergeJoin [PROVENANCE-FALLBACK]`
- **Similarity:** 0.23
- **Dependents:** 0
- **Priority Score:** 31907.7
- **Functions:** 9/9 matched (target 33)
- **Missing functions:** _none_
- **Types:** 7/10 matched (target 8)
- **Missing types:** `T`, `MergeResult`, `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `merge_join.rs` vs expected `merge_join.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:merge_join.rs` vs expected `merge_join.rs`
- **Proposed provenance header:** `// port-lint: source merge_join.rs` (current: `// port-lint: source merge_join.rs`)
- **Proposed provenance header:** `// port-lint: tests merge_join.rs` (current: `// port-lint: tests merge_join.rs`)
- **Lint issues:** 2

### 24. adaptors.map

- **Target:** `adaptors.Map [PROVENANCE-FALLBACK]`
- **Similarity:** 0.39
- **Dependents:** 0
- **Priority Score:** 31706.1
- **Functions:** 8/9 matched (target 14)
- **Missing functions:** `clone`
- **Types:** 6/8 matched (target 7)
- **Missing types:** `Item`, `Out`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `adaptors/map.rs` vs expected `adaptors/map.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:adaptors/map.rs` vs expected `adaptors/map.rs`
- **Proposed provenance header:** `// port-lint: source adaptors/map.rs` (current: `// port-lint: source adaptors/map.rs`)
- **Proposed provenance header:** `// port-lint: tests adaptors/map.rs` (current: `// port-lint: tests adaptors/map.rs`)
- **Lint issues:** 2

### 25. kmerge_impl

- **Target:** `itertools.KMergeImpl [PROVENANCE-FALLBACK]`
- **Similarity:** 0.54
- **Dependents:** 0
- **Priority Score:** 21404.6
- **Functions:** 8/8 matched (target 18)
- **Missing functions:** _none_
- **Types:** 4/6 matched (target 5)
- **Missing types:** `KMergePredicate`, `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `kmerge_impl.rs` vs expected `kmerge_impl.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:kmerge_impl.rs` vs expected `kmerge_impl.rs`
- **Proposed provenance header:** `// port-lint: source kmerge_impl.rs` (current: `// port-lint: source kmerge_impl.rs`)
- **Proposed provenance header:** `// port-lint: tests kmerge_impl.rs` (current: `// port-lint: tests kmerge_impl.rs`)
- **Lint issues:** 2

### 26. exactly_one_err

- **Target:** `itertools.ExactlyOneErr [PROVENANCE-FALLBACK]`
- **Similarity:** 0.33
- **Dependents:** 0
- **Priority Score:** 20806.7
- **Functions:** 5/6 matched (target 18)
- **Missing functions:** `fmt`
- **Types:** 1/2 matched (target 5)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `exactly_one_err.rs` vs expected `exactly_one_err.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:exactly_one_err.rs` vs expected `exactly_one_err.rs`
- **Proposed provenance header:** `// port-lint: source exactly_one_err.rs` (current: `// port-lint: source exactly_one_err.rs`)
- **Proposed provenance header:** `// port-lint: tests exactly_one_err.rs` (current: `// port-lint: tests exactly_one_err.rs`)
- **Lint issues:** 2

### 27. rciter_impl

- **Target:** `itertools.RcIterImpl [PROVENANCE-FALLBACK]`
- **Similarity:** 0.55
- **Dependents:** 0
- **Priority Score:** 20804.5
- **Functions:** 5/5 matched (target 13)
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Item`, `IntoIter`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `rciter_impl.rs` vs expected `rciter_impl.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:rciter_impl.rs` vs expected `rciter_impl.rs`
- **Proposed provenance header:** `// port-lint: source rciter_impl.rs` (current: `// port-lint: source rciter_impl.rs`)
- **Proposed provenance header:** `// port-lint: tests rciter_impl.rs` (current: `// port-lint: tests rciter_impl.rs`)
- **Lint issues:** 2

### 28. iter_index

- **Target:** `itertools.IterIndex [PROVENANCE-FALLBACK]`
- **Similarity:** 0.48
- **Dependents:** 0
- **Priority Score:** 20505.2
- **Functions:** 2/2 matched (target 22)
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 8)
- **Missing types:** `Sealed`, `Output`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `iter_index.rs` vs expected `iter_index.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:iter_index.rs` vs expected `iter_index.rs`
- **Proposed provenance header:** `// port-lint: source iter_index.rs` (current: `// port-lint: source iter_index.rs`)
- **Proposed provenance header:** `// port-lint: tests iter_index.rs` (current: `// port-lint: tests iter_index.rs`)
- **Lint issues:** 2

### 29. grouping_map

- **Target:** `itertools.GroupingMap [PROVENANCE-FALLBACK]`
- **Similarity:** 0.64
- **Dependents:** 0
- **Priority Score:** 12503.6
- **Functions:** 20/20 matched (target 30)
- **Missing functions:** _none_
- **Types:** 4/5 matched
- **Missing types:** `Out`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `grouping_map.rs` vs expected `grouping_map.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:grouping_map.rs` vs expected `grouping_map.rs`
- **Proposed provenance header:** `// port-lint: source grouping_map.rs` (current: `// port-lint: source grouping_map.rs`)
- **Proposed provenance header:** `// port-lint: tests grouping_map.rs` (current: `// port-lint: tests grouping_map.rs`)
- **Lint issues:** 2

### 30. combinations

- **Target:** `itertools.Combinations [PROVENANCE-FALLBACK]`
- **Similarity:** 0.67
- **Dependents:** 0
- **Priority Score:** 12303.3
- **Functions:** 18/18 matched (target 40)
- **Missing functions:** _none_
- **Types:** 4/5 matched (target 7)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `combinations.rs` vs expected `combinations.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:combinations.rs` vs expected `combinations.rs`
- **Proposed provenance header:** `// port-lint: source combinations.rs` (current: `// port-lint: source combinations.rs`)
- **Proposed provenance header:** `// port-lint: tests combinations.rs` (current: `// port-lint: tests combinations.rs`)
- **Lint issues:** 2

### 31. tuple_impl

- **Target:** `itertools.TupleImpl [PROVENANCE-FALLBACK]`
- **Similarity:** 0.53
- **Dependents:** 0
- **Priority Score:** 11604.7
- **Functions:** 9/9 matched (target 70)
- **Missing functions:** _none_
- **Types:** 6/7 matched (target 15)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tuple_impl.rs` vs expected `tuple_impl.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:tuple_impl.rs` vs expected `tuple_impl.rs`
- **Proposed provenance header:** `// port-lint: source tuple_impl.rs` (current: `// port-lint: source tuple_impl.rs`)
- **Proposed provenance header:** `// port-lint: tests tuple_impl.rs` (current: `// port-lint: tests tuple_impl.rs`)
- **Lint issues:** 2

### 32. free

- **Target:** `itertools.Free [PROVENANCE-FALLBACK]`
- **Similarity:** 0.75
- **Dependents:** 0
- **Priority Score:** 11602.5
- **Functions:** 15/15 matched (target 47)
- **Missing functions:** _none_
- **Types:** 0/1 matched
- **Missing types:** `VecIntoIter`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `free.rs` vs expected `free.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:free.rs` vs expected `free.rs`
- **Proposed provenance header:** `// port-lint: source free.rs` (current: `// port-lint: source free.rs`)
- **Proposed provenance header:** `// port-lint: tests free.rs` (current: `// port-lint: tests free.rs`)
- **Lint issues:** 2

### 33. unique_impl

- **Target:** `itertools.UniqueImpl [PROVENANCE-FALLBACK]`
- **Similarity:** 0.30
- **Dependents:** 0
- **Priority Score:** 11007.0
- **Functions:** 7/7 matched (target 19)
- **Missing functions:** _none_
- **Types:** 2/3 matched
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `unique_impl.rs` vs expected `unique_impl.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:unique_impl.rs` vs expected `unique_impl.rs`
- **Proposed provenance header:** `// port-lint: source unique_impl.rs` (current: `// port-lint: source unique_impl.rs`)
- **Proposed provenance header:** `// port-lint: tests unique_impl.rs` (current: `// port-lint: tests unique_impl.rs`)
- **Lint issues:** 2

### 34. process_results_impl

- **Target:** `itertools.ProcessResultsImpl [PROVENANCE-FALLBACK]`
- **Similarity:** 0.47
- **Dependents:** 0
- **Priority Score:** 10905.3
- **Functions:** 7/7 matched (target 12)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `process_results_impl.rs` vs expected `process_results_impl.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:process_results_impl.rs` vs expected `process_results_impl.rs`
- **Proposed provenance header:** `// port-lint: source process_results_impl.rs` (current: `// port-lint: source process_results_impl.rs`)
- **Proposed provenance header:** `// port-lint: tests process_results_impl.rs` (current: `// port-lint: tests process_results_impl.rs`)
- **Lint issues:** 2

### 35. multipeek_impl

- **Target:** `itertools.MultiPeekImpl [PROVENANCE-FALLBACK]`
- **Similarity:** 0.63
- **Dependents:** 0
- **Priority Score:** 10903.7
- **Functions:** 7/7 matched (target 12)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `multipeek_impl.rs` vs expected `multipeek_impl.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:multipeek_impl.rs` vs expected `multipeek_impl.rs`
- **Proposed provenance header:** `// port-lint: source multipeek_impl.rs` (current: `// port-lint: source multipeek_impl.rs`)
- **Proposed provenance header:** `// port-lint: tests multipeek_impl.rs` (current: `// port-lint: tests multipeek_impl.rs`)
- **Lint issues:** 2

### 36. pad_tail

- **Target:** `itertools.PadTail [PROVENANCE-FALLBACK]`
- **Similarity:** 0.59
- **Dependents:** 0
- **Priority Score:** 10804.1
- **Functions:** 6/6 matched (target 17)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `pad_tail.rs` vs expected `pad_tail.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:pad_tail.rs` vs expected `pad_tail.rs`
- **Proposed provenance header:** `// port-lint: source pad_tail.rs` (current: `// port-lint: source pad_tail.rs`)
- **Proposed provenance header:** `// port-lint: tests pad_tail.rs` (current: `// port-lint: tests pad_tail.rs`)
- **Lint issues:** 2

### 37. put_back_n_impl

- **Target:** `itertools.PutBackNImpl [PROVENANCE-FALLBACK]`
- **Similarity:** 0.51
- **Dependents:** 0
- **Priority Score:** 10704.9
- **Functions:** 5/5 matched (target 15)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `put_back_n_impl.rs` vs expected `put_back_n_impl.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:put_back_n_impl.rs` vs expected `put_back_n_impl.rs`
- **Proposed provenance header:** `// port-lint: source put_back_n_impl.rs` (current: `// port-lint: source put_back_n_impl.rs`)
- **Proposed provenance header:** `// port-lint: tests put_back_n_impl.rs` (current: `// port-lint: tests put_back_n_impl.rs`)
- **Lint issues:** 2

### 38. sources

- **Target:** `itertools.Sources [PROVENANCE-FALLBACK]`
- **Similarity:** 0.57
- **Dependents:** 0
- **Priority Score:** 10704.3
- **Functions:** 4/4 matched (target 12)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 5)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `sources.rs` vs expected `sources.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:sources.rs` vs expected `sources.rs`
- **Proposed provenance header:** `// port-lint: source sources.rs` (current: `// port-lint: source sources.rs`)
- **Proposed provenance header:** `// port-lint: tests sources.rs` (current: `// port-lint: tests sources.rs`)
- **Lint issues:** 2

### 39. zip_eq_impl

- **Target:** `itertools.ZipEqImpl [PROVENANCE-FALLBACK]`
- **Similarity:** 0.29
- **Dependents:** 0
- **Priority Score:** 10507.1
- **Functions:** 3/3 matched (target 14)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `zip_eq_impl.rs` vs expected `zip_eq_impl.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:zip_eq_impl.rs` vs expected `zip_eq_impl.rs`
- **Proposed provenance header:** `// port-lint: source zip_eq_impl.rs` (current: `// port-lint: source zip_eq_impl.rs`)
- **Proposed provenance header:** `// port-lint: tests zip_eq_impl.rs` (current: `// port-lint: tests zip_eq_impl.rs`)
- **Lint issues:** 2

### 40. adaptors.mod

- **Target:** `adaptors.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 4410.0
- **Functions:** 28/28 matched (target 150)
- **Missing functions:** _none_
- **Types:** 16/16 matched (target 30)
- **Missing types:** _none_
- **Tests:** 1/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `adaptors/mod.rs` vs expected `adaptors/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:adaptors/mod.rs` vs expected `adaptors/mod.rs`
- **Proposed provenance header:** `// port-lint: source adaptors/mod.rs` (current: `// port-lint: source adaptors/mod.rs`)
- **Proposed provenance header:** `// port-lint: tests adaptors/mod.rs` (current: `// port-lint: tests adaptors/mod.rs`)
- **Lint issues:** 2

### 41. format

- **Target:** `itertools.Format [PROVENANCE-FALLBACK]`
- **Similarity:** 0.53
- **Dependents:** 0
- **Priority Score:** 904.7
- **Functions:** 6/6 matched (target 20)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 5)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `format.rs` vs expected `format.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:format.rs` vs expected `format.rs`
- **Proposed provenance header:** `// port-lint: source format.rs` (current: `// port-lint: source format.rs`)
- **Proposed provenance header:** `// port-lint: tests format.rs` (current: `// port-lint: tests format.rs`)
- **Lint issues:** 2

### 42. k_smallest

- **Target:** `itertools.KSmallest [PROVENANCE-FALLBACK]`
- **Similarity:** 0.78
- **Dependents:** 0
- **Priority Score:** 502.2
- **Functions:** 5/5 matched (target 20)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `k_smallest.rs` vs expected `k_smallest.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:k_smallest.rs` vs expected `k_smallest.rs`
- **Proposed provenance header:** `// port-lint: source k_smallest.rs` (current: `// port-lint: source k_smallest.rs`)
- **Proposed provenance header:** `// port-lint: tests k_smallest.rs` (current: `// port-lint: tests k_smallest.rs`)
- **Lint issues:** 2

### 43. minmax

- **Target:** `itertools.MinMax [PROVENANCE-FALLBACK]`
- **Similarity:** 0.54
- **Dependents:** 0
- **Priority Score:** 304.6
- **Functions:** 2/2 matched (target 14)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `minmax.rs` vs expected `minmax.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:minmax.rs` vs expected `minmax.rs`
- **Proposed provenance header:** `// port-lint: source minmax.rs` (current: `// port-lint: source minmax.rs`)
- **Proposed provenance header:** `// port-lint: tests minmax.rs` (current: `// port-lint: tests minmax.rs`)
- **Lint issues:** 2

### 44. cons_tuples_impl

- **Target:** `itertools.ConsTuplesImpl [PROVENANCE-FALLBACK]`
- **Similarity:** 0.71
- **Dependents:** 0
- **Priority Score:** 302.9
- **Functions:** 1/1 matched (target 8)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `cons_tuples_impl.rs` vs expected `cons_tuples_impl.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:cons_tuples_impl.rs` vs expected `cons_tuples_impl.rs`
- **Proposed provenance header:** `// port-lint: source cons_tuples_impl.rs` (current: `// port-lint: source cons_tuples_impl.rs`)
- **Proposed provenance header:** `// port-lint: tests cons_tuples_impl.rs` (current: `// port-lint: tests cons_tuples_impl.rs`)
- **Lint issues:** 2

### 45. unziptuple

- **Target:** `itertools.UnzipTuple [PROVENANCE-FALLBACK]`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 208.1
- **Functions:** 1/1 matched (target 8)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `unziptuple.rs` vs expected `unziptuple.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:unziptuple.rs` vs expected `unziptuple.rs`
- **Proposed provenance header:** `// port-lint: source unziptuple.rs` (current: `// port-lint: source unziptuple.rs`)
- **Proposed provenance header:** `// port-lint: tests unziptuple.rs` (current: `// port-lint: tests unziptuple.rs`)
- **Lint issues:** 2

### 46. ziptuple

- **Target:** `itertools.Ziptuple [PROVENANCE-FALLBACK]`
- **Similarity:** 0.20
- **Dependents:** 0
- **Priority Score:** 208.0
- **Functions:** 1/1 matched (target 21)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `ziptuple.rs` vs expected `ziptuple.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:ziptuple.rs` vs expected `ziptuple.rs`
- **Proposed provenance header:** `// port-lint: source ziptuple.rs` (current: `// port-lint: source ziptuple.rs`)
- **Proposed provenance header:** `// port-lint: tests ziptuple.rs` (current: `// port-lint: tests ziptuple.rs`)
- **Lint issues:** 2

### 47. group_map

- **Target:** `itertools.GroupMap [PROVENANCE-FALLBACK]`
- **Similarity:** 0.71
- **Dependents:** 0
- **Priority Score:** 202.9
- **Functions:** 2/2 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `group_map.rs` vs expected `group_map.rs`
- **Proposed provenance header:** `// port-lint: source group_map.rs` (current: `// port-lint: source group_map.rs`)
- **Lint issues:** 1

### 48. extrema_set

- **Target:** `itertools.ExtremaSet [PROVENANCE-FALLBACK]`
- **Similarity:** 0.88
- **Dependents:** 0
- **Priority Score:** 201.2
- **Functions:** 2/2 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `extrema_set.rs` vs expected `extrema_set.rs`
- **Proposed provenance header:** `// port-lint: source extrema_set.rs` (current: `// port-lint: source extrema_set.rs`)
- **Lint issues:** 1

### 49. concat_impl

- **Target:** `itertools.ConcatImpl [PROVENANCE-FALLBACK]`
- **Similarity:** 0.67
- **Dependents:** 0
- **Priority Score:** 103.3
- **Functions:** 1/1 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `concat_impl.rs` vs expected `concat_impl.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:concat_impl.rs` vs expected `concat_impl.rs`
- **Proposed provenance header:** `// port-lint: source concat_impl.rs` (current: `// port-lint: source concat_impl.rs`)
- **Proposed provenance header:** `// port-lint: tests concat_impl.rs` (current: `// port-lint: tests concat_impl.rs`)
- **Lint issues:** 2

### 50. impl_macros

- **Target:** `itertools.ImplMacros [PROVENANCE-FALLBACK]`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `impl_macros.rs` vs expected `impl_macros.rs`
- **Proposed provenance header:** `// port-lint: source impl_macros.rs` (current: `// port-lint: source impl_macros.rs`)
- **Lint issues:** 1

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

