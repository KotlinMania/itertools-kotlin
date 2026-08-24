# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 50/50 (100.0%)
- **Function parity:** 258/504 matched (target 791) — 51.2%
- **Class/type parity:** 70/177 matched (target 171) — 39.5%
- **Combined symbol parity:** 328/681 matched (target 962) — 48.2%
- **Average inline-code cosine:** 0.33 (function body across 49 matched files)
- **Average documentation cosine:** 0.61 (doc text across 49 matched files)
- **Cheat-zeroed Files:** 8
- **Critical Issues:** 42 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

### 1. size_hint
- **Similarity:** 0.55 (needs 30% improvement)
- **Dependencies:** 15
- **Priority Score:** 15000905.0
- **Functions:** 8/8 matched (target 15)
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

- **Target:** `itertools.SizeHint`
- **Similarity:** 0.55
- **Dependents:** 15
- **Priority Score:** 15000905.0
- **Functions:** 8/8 matched (target 15)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 2. either_or_both

- **Target:** `itertools.EitherOrBoth`
- **Similarity:** 0.52
- **Dependents:** 3
- **Priority Score:** 3013604.8
- **Functions:** 34/35 matched (target 41)
- **Missing functions:** `from`
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_

### 3. lazy_buffer

- **Target:** `itertools.LazyBuffer`
- **Similarity:** 0.47
- **Dependents:** 3
- **Priority Score:** 3011105.2
- **Functions:** 9/9 matched (target 20)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Output`

### 4. peek_nth

- **Target:** `itertools.PeekNth`
- **Similarity:** 0.61
- **Dependents:** 2
- **Priority Score:** 2011203.9
- **Functions:** 10/10 matched (target 14)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 5. repeatn

- **Target:** `itertools.RepeatN`
- **Similarity:** 0.41
- **Dependents:** 2
- **Priority Score:** 2010805.9
- **Functions:** 6/6 matched (target 8)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Item`

### 6. lib

- **Target:** `itertools.AllEqual`
- **Similarity:** 0.02
- **Dependents:** 0
- **Priority Score:** 1364209.8
- **Functions:** 6/137 matched (target 10)
- **Missing functions:** `interleave`, `interleave_shortest`, `intersperse`, `intersperse_with`, `get`, `zip_longest`, `zip_eq`, `batching`, `chunk_by`, `group_by`, `chunks`, `tuple_windows`, `circular_tuple_windows`, `tuples`, `tee`, `map_into`, `map_ok`, `filter_ok`, `filter_map_ok`, `flatten_ok`, `process_results`, `merge`, `merge_by`, `merge_join_by`, `kmerge`, `kmerge_by`, `cartesian_product`, `multi_cartesian_product`, `coalesce`, `dedup`, `dedup_by`, `dedup_with_count`, `dedup_by_with_count`, `duplicates`, `duplicates_by`, `unique`, `unique_by`, `peeking_take_while`, `take_while_ref`, `take_while_inclusive`, `while_some`, `tuple_combinations`, `array_combinations`, `combinations`, `combinations_with_replacement`, `permutations`, `powerset`, `pad_using`, `with_position`, `positions`, `update`, `next_array`, `collect_array`, `next_tuple`, `collect_tuple`, `find_position`, `find_or_last`, `find_or_first`, `contains`, `dropping`, `dropping_back`, `concat`, `collect_vec`, `try_collect`, `set_from`, `join`, `format`, `format_with`, `fold_ok`, `fold_options`, `fold1`, `tree_reduce`, `inner0`, `inner`, `tree_fold1`, `sum1`, `product1`, `sorted_unstable`, `sorted_unstable_by`, `sorted_unstable_by_key`, `sorted`, `sorted_by`, `sorted_by_key`, `sorted_by_cached_key`, `k_smallest`, `k_smallest_by`, `k_smallest_by_key`, `k_smallest_relaxed`, `k_smallest_relaxed_by`, `k_smallest_relaxed_by_key`, `k_largest`, `k_largest_by`, `k_largest_by_key`, `k_largest_relaxed`, `k_largest_relaxed_by`, `k_largest_relaxed_by_key`, `tail`, `partition_map`, `partition_result`, `into_group_map`, `into_group_map_by`, `into_grouping_map`, `into_grouping_map_by`, `min_set`, `min_set_by`, `min_set_by_key`, `max_set`, `max_set_by`, `max_set_by_key`, `minmax`, `minmax_by_key`, `minmax_by`, `position_max`, `position_max_by_key`, `position_max_by`, `position_min`, `position_min_by_key`, `position_min_by`, `position_minmax`, `position_minmax_by_key`, `position_minmax_by`, `exactly_one`, `at_most_one`, `multipeek`, `counts`, `counts_by`, `multiunzip`, `try_len`, `equal`, `assert_equal`, `partition`
- **Types:** 1/5 matched (target 7)
- **Missing types:** `VecDequeIntoIter`, `VecIntoIter`, `Itertools`, `State`

### 7. adaptors.multi_product

- **Target:** `adaptors.MultiProduct`
- **Similarity:** 0.13
- **Dependents:** 1
- **Priority Score:** 1071008.8
- **Functions:** 2/6 matched
- **Missing functions:** `new`, `count`, `size_hint`, `last`
- **Types:** 1/4 matched (target 2)
- **Missing types:** `MultiProductInner`, `MultiProductIter`, `Item`

### 8. flatten_ok

- **Target:** `itertools.FlattenOk [PROVENANCE-FALLBACK]`
- **Similarity:** 0.13
- **Dependents:** 1
- **Priority Score:** 1050808.8
- **Functions:** 2/6 matched (target 9)
- **Missing functions:** `fold`, `size_hint`, `next_back`, `rfold`
- **Types:** 1/2 matched (target 5)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only by basename: `tests:tests/flatten_ok.rs` vs expected `flatten_ok.rs`
- **Proposed provenance header:** `// port-lint: tests flatten_ok.rs` (current: `// port-lint: tests tests/flatten_ok.rs`)
- **Lint issues:** 1

### 9. permutations

- **Target:** `itertools.Permutations`
- **Similarity:** 0.27
- **Dependents:** 1
- **Priority Score:** 1040907.2
- **Functions:** 3/6 matched (target 10)
- **Missing functions:** `count`, `size_hint`, `size_hint_for`
- **Types:** 2/3 matched (target 7)
- **Missing types:** `Item`

### 10. zip_longest

- **Target:** `itertools.ZipLongest`
- **Similarity:** 0.13
- **Dependents:** 1
- **Priority Score:** 1040808.7
- **Functions:** 3/6 matched (target 11)
- **Missing functions:** `fold`, `next_back`, `rfold`
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 11. combinations_with_replacement

- **Target:** `itertools.CombinationsWithReplacement`
- **Similarity:** 0.30
- **Dependents:** 1
- **Priority Score:** 1030907.1
- **Functions:** 5/7 matched (target 10)
- **Missing functions:** `nth`, `remaining_for`
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 12. powerset

- **Target:** `itertools.Powerset`
- **Similarity:** 0.60
- **Dependents:** 1
- **Priority Score:** 1021004.0
- **Functions:** 7/8 matched (target 13)
- **Missing functions:** `remaining_for`
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 13. peeking_take_while

- **Target:** `itertools.PeekingTakeWhile [PROVENANCE-FALLBACK]`
- **Similarity:** 0.28
- **Dependents:** 1
- **Priority Score:** 1020707.2
- **Functions:** 3/4 matched (target 16)
- **Missing functions:** `size_hint`
- **Types:** 2/3 matched (target 4)
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only by basename: `tests:tests/peeking_take_while.rs` vs expected `peeking_take_while.rs`
- **Proposed provenance header:** `// port-lint: tests peeking_take_while.rs` (current: `// port-lint: tests tests/peeking_take_while.rs`)
- **Lint issues:** 1

### 14. tee

- **Target:** `itertools.Tee [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1020610.0
- **Functions:** 2/3 matched (target 12)
- **Missing functions:** `new`
- **Types:** 2/3 matched
- **Missing types:** `Item`

### 15. take_while_inclusive

- **Target:** `itertools.TakeWhileInclusive [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1020610.0
- **Functions:** 3/4 matched (target 15)
- **Missing functions:** `new`
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`

### 16. diff

- **Target:** `itertools.Diff`
- **Similarity:** 0.29
- **Dependents:** 1
- **Priority Score:** 1020407.1
- **Functions:** 1/3 matched (target 13)
- **Missing functions:** `fmt`, `clone`
- **Types:** 1/1 matched (target 6)
- **Missing types:** _none_

### 17. with_position

- **Target:** `itertools.WithPosition [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1010710.0
- **Functions:** 4/4 matched (target 16)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 5)
- **Missing types:** `Item`

### 18. adaptors.mod

- **Target:** `adaptors.Update [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 274410.0
- **Functions:** 9/28 matched (target 32)
- **Missing functions:** `size_hint`, `fold`, `put_back`, `with_value`, `into_parts`, `count`, `last`, `nth`, `all`, `cartesian_product`, `take_while_ref`, `tuple_combinations`, `from`, `checked_binomial`, `test_checked_binomial`, `collect`, `next_back`, `rfold`, `transpose_result`
- **Types:** 8/16 matched (target 8)
- **Missing types:** `Item`, `PutBack`, `Product`, `TakeWhileRef`, `TupleCombinations`, `HasCombination`, `Tuple1Combination`, `Combination`
- **Tests:** 0/1 matched

### 19. groupbylazy

- **Target:** `itertools.Groupbylazy`
- **Similarity:** 0.03
- **Dependents:** 0
- **Priority Score:** 242709.8
- **Functions:** 1/14 matched (target 12)
- **Missing functions:** `call_mut`, `new`, `step`, `lookup_buffer`, `next_element`, `step_buffering`, `push_next_group`, `step_current`, `group_key`, `drop_group`, `into_iter`, `drop`, `new_chunks`
- **Types:** 2/13 matched (target 3)
- **Missing types:** `KeyFunction`, `Key`, `ChunkIndex`, `GroupInner`, `GroupBy`, `Item`, `IntoIter`, `Groups`, `Group`, `Chunks`, `Chunk`

### 20. adaptors.coalesce

- **Target:** `adaptors.Coalesce`
- **Similarity:** 0.24
- **Dependents:** 0
- **Priority Score:** 182707.6
- **Functions:** 6/11 matched (target 19)
- **Missing functions:** `size_hint`, `fold`, `new`, `coalesce_pair`, `dedup_pair`
- **Types:** 3/16 matched (target 7)
- **Missing types:** `CoalescePredicate`, `Item`, `NoCount`, `WithCount`, `CountItem`, `CItem`, `Coalesce`, `DedupPred2CoalescePred`, `DedupPredicate`, `DedupEq`, `Dedup`, `DedupPredWithCount2CoalescePred`, `DedupWithCount`

### 21. tuple_impl

- **Target:** `itertools.TupleImpl`
- **Similarity:** 0.11
- **Dependents:** 0
- **Priority Score:** 141608.9
- **Functions:** 2/9 matched (target 49)
- **Missing functions:** `new`, `size_hint`, `tuples`, `add_then_div`, `tuple_windows`, `circular_tuple_windows`, `buffer_len`
- **Types:** 0/7 matched (target 9)
- **Missing types:** `HomogeneousTuple`, `TupleBuffer`, `Item`, `Tuples`, `TupleWindows`, `CircularTupleWindows`, `TupleCollect`

### 22. merge_join

- **Target:** `itertools.MergeJoin [PROVENANCE-FALLBACK]`
- **Similarity:** 0.09
- **Dependents:** 0
- **Priority Score:** 131909.1
- **Functions:** 4/9 matched (target 25)
- **Missing functions:** `merge_by_new`, `left`, `right`, `fold`, `nth`
- **Types:** 2/10 matched (target 3)
- **Missing types:** `MergeLte`, `Merge`, `MergeFuncLR`, `FuncLR`, `T`, `OrderingOrBool`, `MergeResult`, `Item`
- **Provenance warning:** port-lint provenance header matched only by basename: `tests:tests/merge_join.rs` vs expected `merge_join.rs`
- **Proposed provenance header:** `// port-lint: tests merge_join.rs` (current: `// port-lint: tests tests/merge_join.rs`)
- **Lint issues:** 1

### 23. adaptors.map

- **Target:** `adaptors.Map`
- **Similarity:** 0.17
- **Dependents:** 0
- **Priority Score:** 121708.3
- **Functions:** 3/9 matched (target 10)
- **Missing functions:** `size_hint`, `fold`, `collect`, `next_back`, `call`, `clone`
- **Types:** 2/8 matched (target 3)
- **Missing types:** `MapSpecialCase`, `MapSpecialCaseFn`, `Item`, `Out`, `MapSpecialCaseFnOk`, `MapSpecialCaseFnInto`

### 24. combinations

- **Target:** `itertools.Combinations`
- **Similarity:** 0.46
- **Dependents:** 0
- **Priority Score:** 82305.4
- **Functions:** 14/18 matched (target 23)
- **Missing functions:** `array_combinations`, `len`, `extract_item`, `new`
- **Types:** 1/5 matched (target 2)
- **Missing types:** `ArrayCombinations`, `CombinationsGeneric`, `PoolIndex`, `Item`

### 25. grouping_map

- **Target:** `itertools.GroupingMap`
- **Similarity:** 0.55
- **Dependents:** 0
- **Priority Score:** 72504.5
- **Functions:** 17/20 matched (target 27)
- **Missing functions:** `call`, `new_map_for_grouping`, `new`
- **Types:** 1/5 matched (target 2)
- **Missing types:** `MapForGrouping`, `GroupingMapFn`, `Out`, `GroupingMapBy`

### 26. kmerge_impl

- **Target:** `itertools.KMergeImpl`
- **Similarity:** 0.40
- **Dependents:** 0
- **Priority Score:** 71406.0
- **Functions:** 5/8 matched (target 12)
- **Missing functions:** `new`, `size_hint`, `kmerge_pred`
- **Types:** 2/6 matched (target 3)
- **Missing types:** `KMerge`, `KMergePredicate`, `KMergeByLt`, `Item`

### 27. process_results_impl

- **Target:** `itertools.ProcessResultsImpl`
- **Similarity:** 0.10
- **Dependents:** 0
- **Priority Score:** 60909.0
- **Functions:** 2/7 matched
- **Missing functions:** `next_body`, `size_hint`, `fold`, `next_back`, `rfold`
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`

### 28. next_array

- **Target:** `itertools.NextArray [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 51310.0
- **Functions:** 7/11 matched (target 7)
- **Missing functions:** `new`, `drop`, `slice_assume_init_mut`, `tracked_drop`
- **Types:** 1/2 matched
- **Missing types:** `TrackedDrop`
- **Tests:** 3/4 matched

### 29. format

- **Target:** `itertools.Format`
- **Similarity:** 0.15
- **Dependents:** 0
- **Priority Score:** 50908.5
- **Functions:** 2/6 matched (target 14)
- **Missing functions:** `fmt`, `format`, `clone`, `drop`
- **Types:** 2/3 matched (target 4)
- **Missing types:** `PutBackOnDrop`

### 30. rciter_impl

- **Target:** `itertools.RcIterImpl`
- **Similarity:** 0.18
- **Dependents:** 0
- **Priority Score:** 50808.2
- **Functions:** 2/5 matched (target 9)
- **Missing functions:** `size_hint`, `next_back`, `into_iter`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Item`, `IntoIter`

### 31. duplicates_impl

- **Target:** `itertools.DuplicatesImpl [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 42210.0
- **Functions:** 9/11 matched (target 27)
- **Missing functions:** `new`, `next_back`
- **Types:** 9/11 matched (target 10)
- **Missing types:** `Item`, `Container`

### 32. unique_impl

- **Target:** `itertools.UniqueImpl [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 41010.0
- **Functions:** 4/7 matched (target 15)
- **Missing functions:** `count_new_keys`, `count`, `next_back`
- **Types:** 2/3 matched
- **Missing types:** `Item`

### 33. intersperse

- **Target:** `itertools.Intersperse`
- **Similarity:** 0.50
- **Dependents:** 0
- **Priority Score:** 31105.0
- **Functions:** 6/6 matched (target 25)
- **Missing functions:** _none_
- **Types:** 2/5 matched (target 6)
- **Missing types:** `IntersperseElement`, `Intersperse`, `Item`

### 34. exactly_one_err

- **Target:** `itertools.ExactlyOneErr`
- **Similarity:** 0.24
- **Dependents:** 0
- **Priority Score:** 30807.6
- **Functions:** 4/6 matched (target 17)
- **Missing functions:** `new`, `fmt`
- **Types:** 1/2 matched (target 5)
- **Missing types:** `Item`

### 35. pad_tail

- **Target:** `itertools.PadTail`
- **Similarity:** 0.39
- **Dependents:** 0
- **Priority Score:** 30806.1
- **Functions:** 4/6 matched (target 13)
- **Missing functions:** `next_back`, `rfold`
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 36. free

- **Target:** `itertools.Free`
- **Similarity:** 0.69
- **Dependents:** 0
- **Priority Score:** 21603.1
- **Functions:** 14/15 matched (target 46)
- **Missing functions:** `intersperse`
- **Types:** 0/1 matched
- **Missing types:** `VecIntoIter`

### 37. multipeek_impl

- **Target:** `itertools.MultiPeekImpl`
- **Similarity:** 0.59
- **Dependents:** 0
- **Priority Score:** 20904.1
- **Functions:** 6/7 matched (target 11)
- **Missing functions:** `fold`
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 38. iter_index

- **Target:** `itertools.IterIndex`
- **Similarity:** 0.48
- **Dependents:** 0
- **Priority Score:** 20505.2
- **Functions:** 2/2 matched (target 22)
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 8)
- **Missing types:** `Sealed`, `Output`

### 39. put_back_n_impl

- **Target:** `itertools.PutBackNImpl`
- **Similarity:** 0.51
- **Dependents:** 0
- **Priority Score:** 10704.9
- **Functions:** 5/5 matched (target 15)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 40. sources

- **Target:** `itertools.Sources`
- **Similarity:** 0.57
- **Dependents:** 0
- **Priority Score:** 10704.3
- **Functions:** 4/4 matched (target 12)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 5)
- **Missing types:** `Item`

### 41. zip_eq_impl

- **Target:** `itertools.ZipEqImpl`
- **Similarity:** 0.29
- **Dependents:** 0
- **Priority Score:** 10507.1
- **Functions:** 3/3 matched (target 14)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`

### 42. cons_tuples_impl

- **Target:** `itertools.ConsTuplesImpl`
- **Similarity:** 0.71
- **Dependents:** 0
- **Priority Score:** 10302.9
- **Functions:** 1/1 matched (target 5)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `ConsTuplesFn`

### 43. unziptuple

- **Target:** `itertools.UnzipTuple`
- **Similarity:** 0.18
- **Dependents:** 0
- **Priority Score:** 10208.2
- **Functions:** 1/1 matched (target 2)
- **Missing functions:** _none_
- **Types:** 0/1 matched (target 0)
- **Missing types:** `MultiUnzip`

### 44. ziptuple

- **Target:** `itertools.Ziptuple`
- **Similarity:** 0.20
- **Dependents:** 0
- **Priority Score:** 10208.0
- **Functions:** 1/1 matched (target 18)
- **Missing functions:** _none_
- **Types:** 0/1 matched (target 4)
- **Missing types:** `Zip`

### 45. k_smallest

- **Target:** `itertools.KSmallest`
- **Similarity:** 0.78
- **Dependents:** 0
- **Priority Score:** 502.2
- **Functions:** 5/5 matched (target 20)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 46. minmax

- **Target:** `itertools.MinMax`
- **Similarity:** 0.54
- **Dependents:** 0
- **Priority Score:** 304.6
- **Functions:** 2/2 matched (target 20)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_

### 47. group_map

- **Target:** `itertools.GroupMap`
- **Similarity:** 0.71
- **Dependents:** 0
- **Priority Score:** 202.9
- **Functions:** 2/2 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 48. extrema_set

- **Target:** `itertools.ExtremaSet`
- **Similarity:** 0.88
- **Dependents:** 0
- **Priority Score:** 201.2
- **Functions:** 2/2 matched (target 14)
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 49. concat_impl

- **Target:** `itertools.ConcatImpl`
- **Similarity:** 0.64
- **Dependents:** 0
- **Priority Score:** 103.6
- **Functions:** 1/1 matched (target 5)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 50. impl_macros

- **Target:** `itertools.ImplMacros [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 3)
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

