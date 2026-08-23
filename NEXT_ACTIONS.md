# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 33/50 (66.0%)
- **Function parity:** 151/378 matched (target 422) — 39.9%
- **Class/type parity:** 44/156 matched (target 110) — 28.2%
- **Combined symbol parity:** 195/534 matched (target 532) — 36.5%
- **Average inline-code cosine:** 0.21 (function body across 33 matched files)
- **Average documentation cosine:** 0.64 (doc text across 33 matched files)
- **Cheat-zeroed Files:** 16
- **Critical Issues:** 30 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

### 1. size_hint
- **Similarity:** 0.55 (needs 30% improvement)
- **Dependencies:** 15
- **Priority Score:** 15000905.0
- **Functions:** 8/8 matched (target 13)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Action:** Deep review - likely missing major functionality

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. size_hint

- **Target:** `itertools.SizeHint [PROVENANCE-FALLBACK]`
- **Similarity:** 0.55
- **Dependents:** 15
- **Priority Score:** 15000905.0
- **Functions:** 8/8 matched (target 13)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 1/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/size_hint.rs` vs expected `size_hint.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/size_hint.rs` vs expected `size_hint.rs`
- **Proposed provenance header:** `// port-lint: source size_hint.rs` (current: `// port-lint: source src/size_hint.rs`)
- **Proposed provenance header:** `// port-lint: source size_hint.rs` (current: `// port-lint: source src/size_hint.rs`)
- **Lint issues:** 2

### 2. either_or_both

- **Target:** `itertools.EitherOrBoth [PROVENANCE-FALLBACK]`
- **Similarity:** 0.39
- **Dependents:** 3
- **Priority Score:** 3133606.2
- **Functions:** 22/35 matched (target 26)
- **Missing functions:** `as_ref`, `as_mut`, `as_deref`, `as_deref_mut`, `or_default`, `left_or_insert`, `right_or_insert`, `left_or_insert_with`, `right_or_insert_with`, `insert_left`, `insert_right`, `insert_both`, `from`
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/either_or_both.rs` vs expected `either_or_both.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/either_or_both.rs` vs expected `either_or_both.rs`
- **Proposed provenance header:** `// port-lint: source either_or_both.rs` (current: `// port-lint: source src/either_or_both.rs`)
- **Proposed provenance header:** `// port-lint: source either_or_both.rs` (current: `// port-lint: source src/either_or_both.rs`)
- **Lint issues:** 2

### 3. lazy_buffer

- **Target:** `itertools.LazyBuffer [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 3
- **Priority Score:** 3051110.0
- **Functions:** 5/9 matched (target 14)
- **Missing functions:** `new`, `len`, `get_array`, `index`
- **Types:** 1/2 matched
- **Missing types:** `Output`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/lazy_buffer.rs` vs expected `lazy_buffer.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/lazy_buffer.rs` vs expected `lazy_buffer.rs`
- **Proposed provenance header:** `// port-lint: source lazy_buffer.rs` (current: `// port-lint: source src/lazy_buffer.rs`)
- **Proposed provenance header:** `// port-lint: source lazy_buffer.rs` (current: `// port-lint: source src/lazy_buffer.rs`)
- **Lint issues:** 2

### 4. peek_nth

- **Target:** `itertools.PeekNth [PROVENANCE-FALLBACK]`
- **Similarity:** 0.52
- **Dependents:** 2
- **Priority Score:** 2041204.8
- **Functions:** 7/10 matched (target 11)
- **Missing functions:** `peek_mut`, `peek_nth_mut`, `fold`
- **Types:** 1/2 matched
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/peek_nth.rs` vs expected `peek_nth.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/peek_nth.rs` vs expected `peek_nth.rs`
- **Proposed provenance header:** `// port-lint: source peek_nth.rs` (current: `// port-lint: source src/peek_nth.rs`)
- **Proposed provenance header:** `// port-lint: source peek_nth.rs` (current: `// port-lint: source src/peek_nth.rs`)
- **Lint issues:** 2

### 5. repeatn

- **Target:** `itertools.RepeatN [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 2
- **Priority Score:** 2020810.0
- **Functions:** 5/6 matched
- **Missing functions:** `rfold`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/repeatn.rs` vs expected `repeatn.rs`
- **Proposed provenance header:** `// port-lint: source repeatn.rs` (current: `// port-lint: source src/repeatn.rs`)
- **Lint issues:** 1

### 6. powerset

- **Target:** `itertools.Powerset [PROVENANCE-FALLBACK]`
- **Similarity:** 0.39
- **Dependents:** 1
- **Priority Score:** 1051006.1
- **Functions:** 4/8 matched (target 9)
- **Missing functions:** `nth`, `count`, `fold`, `remaining_for`
- **Types:** 1/2 matched
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/powerset.rs` vs expected `powerset.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/powerset.rs` vs expected `powerset.rs`
- **Proposed provenance header:** `// port-lint: source powerset.rs` (current: `// port-lint: source src/powerset.rs`)
- **Proposed provenance header:** `// port-lint: source powerset.rs` (current: `// port-lint: source src/powerset.rs`)
- **Lint issues:** 2

### 7. flatten_ok

- **Target:** `itertools.FlattenOk [PROVENANCE-FALLBACK]`
- **Similarity:** 0.13
- **Dependents:** 1
- **Priority Score:** 1050808.8
- **Functions:** 2/6 matched (target 7)
- **Missing functions:** `fold`, `size_hint`, `next_back`, `rfold`
- **Types:** 1/2 matched (target 5)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/flatten_ok.rs` vs expected `flatten_ok.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/flatten_ok.rs` vs expected `flatten_ok.rs`
- **Proposed provenance header:** `// port-lint: source flatten_ok.rs` (current: `// port-lint: source src/flatten_ok.rs`)
- **Proposed provenance header:** `// port-lint: source flatten_ok.rs` (current: `// port-lint: source src/flatten_ok.rs`)
- **Lint issues:** 2

### 8. permutations

- **Target:** `itertools.Permutations [PROVENANCE-FALLBACK]`
- **Similarity:** 0.27
- **Dependents:** 1
- **Priority Score:** 1040907.2
- **Functions:** 3/6 matched (target 10)
- **Missing functions:** `count`, `size_hint`, `size_hint_for`
- **Types:** 2/3 matched (target 7)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/permutations.rs` vs expected `permutations.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/permutations.rs` vs expected `permutations.rs`
- **Proposed provenance header:** `// port-lint: source permutations.rs` (current: `// port-lint: source src/permutations.rs`)
- **Proposed provenance header:** `// port-lint: source permutations.rs` (current: `// port-lint: source src/permutations.rs`)
- **Lint issues:** 2

### 9. zip_longest

- **Target:** `itertools.ZipLongest [PROVENANCE-FALLBACK]`
- **Similarity:** 0.13
- **Dependents:** 1
- **Priority Score:** 1040808.7
- **Functions:** 3/6 matched (target 11)
- **Missing functions:** `fold`, `next_back`, `rfold`
- **Types:** 1/2 matched
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/zip_longest.rs` vs expected `zip_longest.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/zip_longest.rs` vs expected `zip_longest.rs`
- **Proposed provenance header:** `// port-lint: source zip_longest.rs` (current: `// port-lint: source src/zip_longest.rs`)
- **Proposed provenance header:** `// port-lint: source zip_longest.rs` (current: `// port-lint: source src/zip_longest.rs`)
- **Lint issues:** 2

### 10. combinations_with_replacement

- **Target:** `itertools.CombinationsWithReplacement [PROVENANCE-FALLBACK]`
- **Similarity:** 0.30
- **Dependents:** 1
- **Priority Score:** 1030907.1
- **Functions:** 5/7 matched (target 10)
- **Missing functions:** `nth`, `remaining_for`
- **Types:** 1/2 matched
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/combinations_with_replacement.rs` vs expected `combinations_with_replacement.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/combinations_with_replacement.rs` vs expected `combinations_with_replacement.rs`
- **Proposed provenance header:** `// port-lint: source combinations_with_replacement.rs` (current: `// port-lint: source src/combinations_with_replacement.rs`)
- **Proposed provenance header:** `// port-lint: source combinations_with_replacement.rs` (current: `// port-lint: source src/combinations_with_replacement.rs`)
- **Lint issues:** 2

### 11. peeking_take_while

- **Target:** `itertools.PeekingTakeWhile [PROVENANCE-FALLBACK]`
- **Similarity:** 0.28
- **Dependents:** 1
- **Priority Score:** 1020707.2
- **Functions:** 3/4 matched (target 14)
- **Missing functions:** `size_hint`
- **Types:** 2/3 matched (target 4)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/peeking_take_while.rs` vs expected `peeking_take_while.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/peeking_take_while.rs` vs expected `peeking_take_while.rs`
- **Proposed provenance header:** `// port-lint: source peeking_take_while.rs` (current: `// port-lint: source src/peeking_take_while.rs`)
- **Proposed provenance header:** `// port-lint: source peeking_take_while.rs` (current: `// port-lint: source src/peeking_take_while.rs`)
- **Lint issues:** 2

### 12. take_while_inclusive

- **Target:** `itertools.TakeWhileInclusive [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1020610.0
- **Functions:** 3/4 matched (target 14)
- **Missing functions:** `new`
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/take_while_inclusive.rs` vs expected `take_while_inclusive.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/take_while_inclusive.rs` vs expected `take_while_inclusive.rs`
- **Proposed provenance header:** `// port-lint: source take_while_inclusive.rs` (current: `// port-lint: source src/take_while_inclusive.rs`)
- **Proposed provenance header:** `// port-lint: source take_while_inclusive.rs` (current: `// port-lint: source src/take_while_inclusive.rs`)
- **Lint issues:** 2

### 13. tee

- **Target:** `itertools.Tee [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1020610.0
- **Functions:** 2/3 matched (target 12)
- **Missing functions:** `new`
- **Types:** 2/3 matched
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/tee.rs` vs expected `tee.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/tee.rs` vs expected `tee.rs`
- **Proposed provenance header:** `// port-lint: source tee.rs` (current: `// port-lint: source src/tee.rs`)
- **Proposed provenance header:** `// port-lint: source tee.rs` (current: `// port-lint: source src/tee.rs`)
- **Lint issues:** 2

### 14. diff

- **Target:** `itertools.Diff [PROVENANCE-FALLBACK]`
- **Similarity:** 0.29
- **Dependents:** 1
- **Priority Score:** 1020407.1
- **Functions:** 1/3 matched (target 13)
- **Missing functions:** `fmt`, `clone`
- **Types:** 1/1 matched (target 6)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/diff.rs` vs expected `diff.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/diff.rs` vs expected `diff.rs`
- **Proposed provenance header:** `// port-lint: source diff.rs` (current: `// port-lint: source src/diff.rs`)
- **Proposed provenance header:** `// port-lint: source diff.rs` (current: `// port-lint: source src/diff.rs`)
- **Lint issues:** 2

### 15. with_position

- **Target:** `itertools.WithPosition [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1010710.0
- **Functions:** 4/4 matched (target 15)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 5)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/with_position.rs` vs expected `with_position.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/with_position.rs` vs expected `with_position.rs`
- **Proposed provenance header:** `// port-lint: source with_position.rs` (current: `// port-lint: source src/with_position.rs`)
- **Proposed provenance header:** `// port-lint: source with_position.rs` (current: `// port-lint: source src/with_position.rs`)
- **Lint issues:** 2

### 16. combinations

- **Target:** `itertools.Combinations [PROVENANCE-FALLBACK]`
- **Similarity:** 0.41
- **Dependents:** 0
- **Priority Score:** 102305.9
- **Functions:** 12/18 matched
- **Missing functions:** `array_combinations`, `len`, `extract_item`, `new`, `nth`, `count`
- **Types:** 1/5 matched (target 2)
- **Missing types:** `ArrayCombinations`, `CombinationsGeneric`, `PoolIndex`, `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/combinations.rs` vs expected `combinations.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/combinations.rs` vs expected `combinations.rs`
- **Proposed provenance header:** `// port-lint: source combinations.rs` (current: `// port-lint: source src/combinations.rs`)
- **Proposed provenance header:** `// port-lint: source combinations.rs` (current: `// port-lint: source src/combinations.rs`)
- **Lint issues:** 2

### 17. next_array

- **Target:** `itertools.NextArray [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 51310.0
- **Functions:** 7/11 matched (target 7)
- **Missing functions:** `new`, `drop`, `slice_assume_init_mut`, `tracked_drop`
- **Types:** 1/2 matched
- **Missing types:** `TrackedDrop`
- **Tests:** 3/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/next_array.rs` vs expected `next_array.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/next_array.rs` vs expected `next_array.rs`
- **Proposed provenance header:** `// port-lint: source next_array.rs` (current: `// port-lint: source src/next_array.rs`)
- **Proposed provenance header:** `// port-lint: source next_array.rs` (current: `// port-lint: source src/next_array.rs`)
- **Lint issues:** 2

### 18. format

- **Target:** `itertools.Format [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 50910.0
- **Functions:** 2/6 matched (target 14)
- **Missing functions:** `fmt`, `format`, `clone`, `drop`
- **Types:** 2/3 matched (target 4)
- **Missing types:** `PutBackOnDrop`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/format.rs` vs expected `format.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/format.rs` vs expected `format.rs`
- **Proposed provenance header:** `// port-lint: source format.rs` (current: `// port-lint: source src/format.rs`)
- **Proposed provenance header:** `// port-lint: source format.rs` (current: `// port-lint: source src/format.rs`)
- **Lint issues:** 2

### 19. duplicates_impl

- **Target:** `itertools.DuplicatesImpl [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 42210.0
- **Functions:** 9/11 matched (target 27)
- **Missing functions:** `new`, `next_back`
- **Types:** 9/11 matched (target 10)
- **Missing types:** `Item`, `Container`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/duplicates_impl.rs` vs expected `duplicates_impl.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/duplicates_impl.rs` vs expected `duplicates_impl.rs`
- **Proposed provenance header:** `// port-lint: source duplicates_impl.rs` (current: `// port-lint: source src/duplicates_impl.rs`)
- **Proposed provenance header:** `// port-lint: source duplicates_impl.rs` (current: `// port-lint: source src/duplicates_impl.rs`)
- **Lint issues:** 2

### 20. unique_impl

- **Target:** `itertools.UniqueImpl [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 41010.0
- **Functions:** 4/7 matched (target 15)
- **Missing functions:** `count_new_keys`, `count`, `next_back`
- **Types:** 2/3 matched
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/unique_impl.rs` vs expected `unique_impl.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:src/unique_impl.rs` vs expected `unique_impl.rs`
- **Proposed provenance header:** `// port-lint: source unique_impl.rs` (current: `// port-lint: source src/unique_impl.rs`)
- **Proposed provenance header:** `// port-lint: tests unique_impl.rs` (current: `// port-lint: tests src/unique_impl.rs`)
- **Lint issues:** 2

### 21. intersperse

- **Target:** `itertools.Intersperse [PROVENANCE-FALLBACK]`
- **Similarity:** 0.50
- **Dependents:** 0
- **Priority Score:** 31105.0
- **Functions:** 6/6 matched (target 25)
- **Missing functions:** _none_
- **Types:** 2/5 matched (target 6)
- **Missing types:** `IntersperseElement`, `Intersperse`, `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/intersperse.rs` vs expected `intersperse.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/intersperse.rs` vs expected `intersperse.rs`
- **Proposed provenance header:** `// port-lint: source intersperse.rs` (current: `// port-lint: source src/intersperse.rs`)
- **Proposed provenance header:** `// port-lint: source intersperse.rs` (current: `// port-lint: source src/intersperse.rs`)
- **Lint issues:** 2

### 22. pad_tail

- **Target:** `itertools.PadTail [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 30810.0
- **Functions:** 4/6 matched (target 13)
- **Missing functions:** `next_back`, `rfold`
- **Types:** 1/2 matched
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/pad_tail.rs` vs expected `pad_tail.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/pad_tail.rs` vs expected `pad_tail.rs`
- **Proposed provenance header:** `// port-lint: source pad_tail.rs` (current: `// port-lint: source src/pad_tail.rs`)
- **Proposed provenance header:** `// port-lint: source pad_tail.rs` (current: `// port-lint: source src/pad_tail.rs`)
- **Lint issues:** 2

### 23. exactly_one_err

- **Target:** `itertools.ExactlyOneErr [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 30810.0
- **Functions:** 4/6 matched (target 13)
- **Missing functions:** `new`, `fmt`
- **Types:** 1/2 matched (target 5)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/exactly_one_err.rs` vs expected `exactly_one_err.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/exactly_one_err.rs` vs expected `exactly_one_err.rs`
- **Proposed provenance header:** `// port-lint: source exactly_one_err.rs` (current: `// port-lint: source src/exactly_one_err.rs`)
- **Proposed provenance header:** `// port-lint: source exactly_one_err.rs` (current: `// port-lint: source src/exactly_one_err.rs`)
- **Lint issues:** 2

### 24. iter_index

- **Target:** `itertools.IterIndex [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 20510.0
- **Functions:** 2/2 matched (target 22)
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 8)
- **Missing types:** `Sealed`, `Output`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/iter_index.rs` vs expected `iter_index.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/iter_index.rs` vs expected `iter_index.rs`
- **Proposed provenance header:** `// port-lint: source iter_index.rs` (current: `// port-lint: source src/iter_index.rs`)
- **Proposed provenance header:** `// port-lint: source iter_index.rs` (current: `// port-lint: source src/iter_index.rs`)
- **Lint issues:** 2

### 25. sources

- **Target:** `itertools.Sources [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10710.0
- **Functions:** 4/4 matched (target 12)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 5)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/sources.rs` vs expected `sources.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/sources.rs` vs expected `sources.rs`
- **Proposed provenance header:** `// port-lint: source sources.rs` (current: `// port-lint: source src/sources.rs`)
- **Proposed provenance header:** `// port-lint: source sources.rs` (current: `// port-lint: source src/sources.rs`)
- **Lint issues:** 2

### 26. put_back_n_impl

- **Target:** `itertools.PutBackNImpl [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10710.0
- **Functions:** 5/5 matched (target 14)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/put_back_n_impl.rs` vs expected `put_back_n_impl.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/put_back_n_impl.rs` vs expected `put_back_n_impl.rs`
- **Proposed provenance header:** `// port-lint: source put_back_n_impl.rs` (current: `// port-lint: source src/put_back_n_impl.rs`)
- **Proposed provenance header:** `// port-lint: source put_back_n_impl.rs` (current: `// port-lint: source src/put_back_n_impl.rs`)
- **Lint issues:** 2

### 27. zip_eq_impl

- **Target:** `itertools.ZipEqImpl [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10510.0
- **Functions:** 3/3 matched (target 13)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/zip_eq_impl.rs` vs expected `zip_eq_impl.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/zip_eq_impl.rs` vs expected `zip_eq_impl.rs`
- **Proposed provenance header:** `// port-lint: source zip_eq_impl.rs` (current: `// port-lint: source src/zip_eq_impl.rs`)
- **Proposed provenance header:** `// port-lint: source zip_eq_impl.rs` (current: `// port-lint: source src/zip_eq_impl.rs`)
- **Lint issues:** 2

### 28. minmax

- **Target:** `itertools.MinMax [PROVENANCE-FALLBACK]`
- **Similarity:** 0.44
- **Dependents:** 0
- **Priority Score:** 10305.6
- **Functions:** 1/2 matched (target 13)
- **Missing functions:** `into_option`
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/minmax.rs` vs expected `minmax.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/minmax.rs` vs expected `minmax.rs`
- **Proposed provenance header:** `// port-lint: source minmax.rs` (current: `// port-lint: source src/minmax.rs`)
- **Proposed provenance header:** `// port-lint: source minmax.rs` (current: `// port-lint: source src/minmax.rs`)
- **Lint issues:** 2

### 29. unziptuple

- **Target:** `itertools.UnzipTuple [PROVENANCE-FALLBACK]`
- **Similarity:** 0.18
- **Dependents:** 0
- **Priority Score:** 10208.2
- **Functions:** 1/1 matched (target 2)
- **Missing functions:** _none_
- **Types:** 0/1 matched (target 0)
- **Missing types:** `MultiUnzip`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/unziptuple.rs` vs expected `unziptuple.rs`
- **Proposed provenance header:** `// port-lint: source unziptuple.rs` (current: `// port-lint: source src/unziptuple.rs`)
- **Lint issues:** 1

### 30. k_smallest

- **Target:** `itertools.KSmallest [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 510.0
- **Functions:** 5/5 matched (target 20)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/k_smallest.rs` vs expected `k_smallest.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/k_smallest.rs` vs expected `k_smallest.rs`
- **Proposed provenance header:** `// port-lint: source k_smallest.rs` (current: `// port-lint: source src/k_smallest.rs`)
- **Proposed provenance header:** `// port-lint: source k_smallest.rs` (current: `// port-lint: source src/k_smallest.rs`)
- **Lint issues:** 2

### 31. group_map

- **Target:** `itertools.GroupMap [PROVENANCE-FALLBACK]`
- **Similarity:** 0.71
- **Dependents:** 0
- **Priority Score:** 202.9
- **Functions:** 2/2 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/group_map.rs` vs expected `group_map.rs`
- **Proposed provenance header:** `// port-lint: source group_map.rs` (current: `// port-lint: source src/group_map.rs`)
- **Lint issues:** 1

### 32. extrema_set

- **Target:** `itertools.ExtremaSet [PROVENANCE-FALLBACK]`
- **Similarity:** 0.88
- **Dependents:** 0
- **Priority Score:** 201.2
- **Functions:** 2/2 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/extrema_set.rs` vs expected `extrema_set.rs`
- **Proposed provenance header:** `// port-lint: source extrema_set.rs` (current: `// port-lint: source src/extrema_set.rs`)
- **Lint issues:** 1

### 33. concat_impl

- **Target:** `itertools.ConcatImpl [PROVENANCE-FALLBACK]`
- **Similarity:** 0.64
- **Dependents:** 0
- **Priority Score:** 103.6
- **Functions:** 1/1 matched (target 5)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/concat_impl.rs` vs expected `concat_impl.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/concat_impl.rs` vs expected `concat_impl.rs`
- **Proposed provenance header:** `// port-lint: source concat_impl.rs` (current: `// port-lint: source src/concat_impl.rs`)
- **Proposed provenance header:** `// port-lint: source concat_impl.rs` (current: `// port-lint: source src/concat_impl.rs`)
- **Lint issues:** 2

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `adaptors.mod` | `adaptors.Mod` | 0 | `adaptors/mod.rs` | `adaptors/Mod.kt` |
| `lib` | `Lib` | 0 | `lib.rs` | `Lib.kt` |

