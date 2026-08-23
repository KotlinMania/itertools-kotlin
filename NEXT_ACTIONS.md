# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 48/50 (96.0%)
- **Function parity:** 238/339 matched (target 663) — 70.2%
- **Class/type parity:** 61/156 matched (target 152) — 39.1%
- **Combined symbol parity:** 299/495 matched (target 815) — 60.4%
- **Average inline-code cosine:** 0.25 (function body across 47 matched files)
- **Average documentation cosine:** 0.63 (doc text across 47 matched files)
- **Cheat-zeroed Files:** 16
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

- **Target:** `itertools.LazyBuffer [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 3
- **Priority Score:** 3011110.0
- **Functions:** 9/9 matched (target 20)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Output`

### 4. peek_nth

- **Target:** `itertools.PeekNth`
- **Similarity:** 0.57
- **Dependents:** 2
- **Priority Score:** 2031204.4
- **Functions:** 8/10 matched (target 12)
- **Missing functions:** `peek_mut`, `peek_nth_mut`
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 5. repeatn

- **Target:** `itertools.RepeatN [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 2
- **Priority Score:** 2020810.0
- **Functions:** 5/6 matched
- **Missing functions:** `rfold`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Item`

### 6. adaptors.multi_product

- **Target:** `adaptors.MultiProduct`
- **Similarity:** 0.13
- **Dependents:** 1
- **Priority Score:** 1071008.8
- **Functions:** 2/6 matched
- **Missing functions:** `new`, `count`, `size_hint`, `last`
- **Types:** 1/4 matched (target 2)
- **Missing types:** `MultiProductInner`, `MultiProductIter`, `Item`

### 7. flatten_ok

- **Target:** `itertools.FlattenOk`
- **Similarity:** 0.13
- **Dependents:** 1
- **Priority Score:** 1050808.8
- **Functions:** 2/6 matched (target 9)
- **Missing functions:** `fold`, `size_hint`, `next_back`, `rfold`
- **Types:** 1/2 matched (target 5)
- **Missing types:** `Item`

### 8. permutations

- **Target:** `itertools.Permutations`
- **Similarity:** 0.27
- **Dependents:** 1
- **Priority Score:** 1040907.2
- **Functions:** 3/6 matched (target 10)
- **Missing functions:** `count`, `size_hint`, `size_hint_for`
- **Types:** 2/3 matched (target 7)
- **Missing types:** `Item`

### 9. zip_longest

- **Target:** `itertools.ZipLongest`
- **Similarity:** 0.13
- **Dependents:** 1
- **Priority Score:** 1040808.7
- **Functions:** 3/6 matched (target 11)
- **Missing functions:** `fold`, `next_back`, `rfold`
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 10. combinations_with_replacement

- **Target:** `itertools.CombinationsWithReplacement`
- **Similarity:** 0.30
- **Dependents:** 1
- **Priority Score:** 1030907.1
- **Functions:** 5/7 matched (target 10)
- **Missing functions:** `nth`, `remaining_for`
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 11. powerset

- **Target:** `itertools.Powerset`
- **Similarity:** 0.60
- **Dependents:** 1
- **Priority Score:** 1021004.0
- **Functions:** 7/8 matched (target 13)
- **Missing functions:** `remaining_for`
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 12. peeking_take_while

- **Target:** `itertools.PeekingTakeWhile`
- **Similarity:** 0.28
- **Dependents:** 1
- **Priority Score:** 1020707.2
- **Functions:** 3/4 matched (target 16)
- **Missing functions:** `size_hint`
- **Types:** 2/3 matched (target 4)
- **Missing types:** `Item`

### 13. tee

- **Target:** `itertools.Tee [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1020610.0
- **Functions:** 2/3 matched (target 12)
- **Missing functions:** `new`
- **Types:** 2/3 matched
- **Missing types:** `Item`

### 14. take_while_inclusive

- **Target:** `itertools.TakeWhileInclusive [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1020610.0
- **Functions:** 3/4 matched (target 14)
- **Missing functions:** `new`
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`

### 15. diff

- **Target:** `itertools.Diff`
- **Similarity:** 0.29
- **Dependents:** 1
- **Priority Score:** 1020407.1
- **Functions:** 1/3 matched (target 13)
- **Missing functions:** `fmt`, `clone`
- **Types:** 1/1 matched (target 6)
- **Missing types:** _none_

### 16. with_position

- **Target:** `itertools.WithPosition [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1010710.0
- **Functions:** 4/4 matched (target 15)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 5)
- **Missing types:** `Item`

### 17. groupbylazy

- **Target:** `itertools.Groupbylazy`
- **Similarity:** 0.03
- **Dependents:** 0
- **Priority Score:** 242709.8
- **Functions:** 1/14 matched (target 12)
- **Missing functions:** `call_mut`, `new`, `step`, `lookup_buffer`, `next_element`, `step_buffering`, `push_next_group`, `step_current`, `group_key`, `drop_group`, `into_iter`, `drop`, `new_chunks`
- **Types:** 2/13 matched (target 3)
- **Missing types:** `KeyFunction`, `Key`, `ChunkIndex`, `GroupInner`, `GroupBy`, `Item`, `IntoIter`, `Groups`, `Group`, `Chunks`, `Chunk`

### 18. adaptors.coalesce

- **Target:** `adaptors.Coalesce`
- **Similarity:** 0.24
- **Dependents:** 0
- **Priority Score:** 182707.6
- **Functions:** 6/11 matched (target 19)
- **Missing functions:** `size_hint`, `fold`, `new`, `coalesce_pair`, `dedup_pair`
- **Types:** 3/16 matched (target 7)
- **Missing types:** `CoalescePredicate`, `Item`, `NoCount`, `WithCount`, `CountItem`, `CItem`, `Coalesce`, `DedupPred2CoalescePred`, `DedupPredicate`, `DedupEq`, `Dedup`, `DedupPredWithCount2CoalescePred`, `DedupWithCount`

### 19. tuple_impl

- **Target:** `itertools.TupleImpl`
- **Similarity:** 0.07
- **Dependents:** 0
- **Priority Score:** 141609.3
- **Functions:** 2/9 matched (target 14)
- **Missing functions:** `new`, `size_hint`, `tuples`, `add_then_div`, `tuple_windows`, `circular_tuple_windows`, `buffer_len`
- **Types:** 0/7 matched (target 4)
- **Missing types:** `HomogeneousTuple`, `TupleBuffer`, `Item`, `Tuples`, `TupleWindows`, `CircularTupleWindows`, `TupleCollect`

### 20. merge_join

- **Target:** `itertools.MergeJoin`
- **Similarity:** 0.09
- **Dependents:** 0
- **Priority Score:** 131909.1
- **Functions:** 4/9 matched (target 25)
- **Missing functions:** `merge_by_new`, `left`, `right`, `fold`, `nth`
- **Types:** 2/10 matched (target 3)
- **Missing types:** `MergeLte`, `Merge`, `MergeFuncLR`, `FuncLR`, `T`, `OrderingOrBool`, `MergeResult`, `Item`

### 21. adaptors.map

- **Target:** `adaptors.Map`
- **Similarity:** 0.17
- **Dependents:** 0
- **Priority Score:** 121708.3
- **Functions:** 3/9 matched (target 10)
- **Missing functions:** `size_hint`, `fold`, `collect`, `next_back`, `call`, `clone`
- **Types:** 2/8 matched (target 3)
- **Missing types:** `MapSpecialCase`, `MapSpecialCaseFn`, `Item`, `Out`, `MapSpecialCaseFnOk`, `MapSpecialCaseFnInto`

### 22. combinations

- **Target:** `itertools.Combinations`
- **Similarity:** 0.46
- **Dependents:** 0
- **Priority Score:** 82305.4
- **Functions:** 14/18 matched (target 22)
- **Missing functions:** `array_combinations`, `len`, `extract_item`, `new`
- **Types:** 1/5 matched (target 2)
- **Missing types:** `ArrayCombinations`, `CombinationsGeneric`, `PoolIndex`, `Item`

### 23. grouping_map

- **Target:** `itertools.GroupingMap`
- **Similarity:** 0.55
- **Dependents:** 0
- **Priority Score:** 72504.5
- **Functions:** 17/20 matched (target 27)
- **Missing functions:** `call`, `new_map_for_grouping`, `new`
- **Types:** 1/5 matched (target 2)
- **Missing types:** `MapForGrouping`, `GroupingMapFn`, `Out`, `GroupingMapBy`

### 24. kmerge_impl

- **Target:** `itertools.KMergeImpl`
- **Similarity:** 0.40
- **Dependents:** 0
- **Priority Score:** 71406.0
- **Functions:** 5/8 matched (target 14)
- **Missing functions:** `new`, `size_hint`, `kmerge_pred`
- **Types:** 2/6 matched (target 3)
- **Missing types:** `KMerge`, `KMergePredicate`, `KMergeByLt`, `Item`

### 25. process_results_impl

- **Target:** `itertools.ProcessResultsImpl`
- **Similarity:** 0.10
- **Dependents:** 0
- **Priority Score:** 60909.0
- **Functions:** 2/7 matched
- **Missing functions:** `next_body`, `size_hint`, `fold`, `next_back`, `rfold`
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`

### 26. next_array

- **Target:** `itertools.NextArray [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 51310.0
- **Functions:** 7/11 matched (target 7)
- **Missing functions:** `new`, `drop`, `slice_assume_init_mut`, `tracked_drop`
- **Types:** 1/2 matched
- **Missing types:** `TrackedDrop`
- **Tests:** 3/4 matched

### 27. format

- **Target:** `itertools.Format [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 50910.0
- **Functions:** 2/6 matched (target 14)
- **Missing functions:** `fmt`, `format`, `clone`, `drop`
- **Types:** 2/3 matched (target 4)
- **Missing types:** `PutBackOnDrop`

### 28. rciter_impl

- **Target:** `itertools.RcIterImpl`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 50808.1
- **Functions:** 2/5 matched (target 8)
- **Missing functions:** `size_hint`, `next_back`, `into_iter`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Item`, `IntoIter`

### 29. duplicates_impl

- **Target:** `itertools.DuplicatesImpl [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 42210.0
- **Functions:** 9/11 matched (target 27)
- **Missing functions:** `new`, `next_back`
- **Types:** 9/11 matched (target 10)
- **Missing types:** `Item`, `Container`

### 30. unique_impl

- **Target:** `itertools.UniqueImpl [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 41010.0
- **Functions:** 4/7 matched (target 15)
- **Missing functions:** `count_new_keys`, `count`, `next_back`
- **Types:** 2/3 matched
- **Missing types:** `Item`

### 31. free

- **Target:** `itertools.Free`
- **Similarity:** 0.64
- **Dependents:** 0
- **Priority Score:** 31603.6
- **Functions:** 13/15 matched (target 35)
- **Missing functions:** `intersperse`, `intersperse_with`
- **Types:** 0/1 matched
- **Missing types:** `VecIntoIter`

### 32. intersperse

- **Target:** `itertools.Intersperse`
- **Similarity:** 0.50
- **Dependents:** 0
- **Priority Score:** 31105.0
- **Functions:** 6/6 matched (target 25)
- **Missing functions:** _none_
- **Types:** 2/5 matched (target 6)
- **Missing types:** `IntersperseElement`, `Intersperse`, `Item`

### 33. exactly_one_err

- **Target:** `itertools.ExactlyOneErr [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 30810.0
- **Functions:** 4/6 matched (target 13)
- **Missing functions:** `new`, `fmt`
- **Types:** 1/2 matched (target 5)
- **Missing types:** `Item`

### 34. pad_tail

- **Target:** `itertools.PadTail [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 30810.0
- **Functions:** 4/6 matched (target 13)
- **Missing functions:** `next_back`, `rfold`
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 35. multipeek_impl

- **Target:** `itertools.MultiPeekImpl`
- **Similarity:** 0.59
- **Dependents:** 0
- **Priority Score:** 20904.1
- **Functions:** 6/7 matched (target 11)
- **Missing functions:** `fold`
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 36. iter_index

- **Target:** `itertools.IterIndex [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 20510.0
- **Functions:** 2/2 matched (target 22)
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 8)
- **Missing types:** `Sealed`, `Output`

### 37. sources

- **Target:** `itertools.Sources [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10710.0
- **Functions:** 4/4 matched (target 12)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 5)
- **Missing types:** `Item`

### 38. put_back_n_impl

- **Target:** `itertools.PutBackNImpl [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10710.0
- **Functions:** 5/5 matched (target 14)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 39. zip_eq_impl

- **Target:** `itertools.ZipEqImpl [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10510.0
- **Functions:** 3/3 matched (target 13)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`

### 40. minmax

- **Target:** `itertools.MinMax`
- **Similarity:** 0.44
- **Dependents:** 0
- **Priority Score:** 10305.6
- **Functions:** 1/2 matched (target 13)
- **Missing functions:** `into_option`
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_

### 41. cons_tuples_impl

- **Target:** `itertools.ConsTuplesImpl`
- **Similarity:** 0.71
- **Dependents:** 0
- **Priority Score:** 10302.9
- **Functions:** 1/1 matched (target 5)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `ConsTuplesFn`

### 42. unziptuple

- **Target:** `itertools.UnzipTuple`
- **Similarity:** 0.18
- **Dependents:** 0
- **Priority Score:** 10208.2
- **Functions:** 1/1 matched (target 2)
- **Missing functions:** _none_
- **Types:** 0/1 matched (target 0)
- **Missing types:** `MultiUnzip`

### 43. ziptuple

- **Target:** `itertools.Ziptuple`
- **Similarity:** 0.20
- **Dependents:** 0
- **Priority Score:** 10208.0
- **Functions:** 1/1 matched (target 12)
- **Missing functions:** _none_
- **Types:** 0/1 matched (target 4)
- **Missing types:** `Zip`

### 44. k_smallest

- **Target:** `itertools.KSmallest [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 510.0
- **Functions:** 5/5 matched (target 20)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 45. group_map

- **Target:** `itertools.GroupMap`
- **Similarity:** 0.71
- **Dependents:** 0
- **Priority Score:** 202.9
- **Functions:** 2/2 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 46. extrema_set

- **Target:** `itertools.ExtremaSet`
- **Similarity:** 0.88
- **Dependents:** 0
- **Priority Score:** 201.2
- **Functions:** 2/2 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 47. concat_impl

- **Target:** `itertools.ConcatImpl`
- **Similarity:** 0.64
- **Dependents:** 0
- **Priority Score:** 103.6
- **Functions:** 1/1 matched (target 5)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 48. impl_macros

- **Target:** `itertools.ImplMacros [STUB]`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

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

