# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 50/50 (100.0%)
- **Function parity:** 415/504 matched (target 1086) — 82.3%
- **Class/type parity:** 102/177 matched (target 210) — 57.6%
- **Combined symbol parity:** 517/681 matched (target 1296) — 75.9%
- **Average inline-code cosine:** 0.44 (function body across 49 matched files)
- **Average documentation cosine:** 0.62 (doc text across 49 matched files)
- **Cheat-zeroed Files:** 2
- **Critical Issues:** 40 files with <0.60 function similarity

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

### 6. permutations

- **Target:** `itertools.Permutations`
- **Similarity:** 0.27
- **Dependents:** 1
- **Priority Score:** 1040907.2
- **Functions:** 3/6 matched (target 10)
- **Missing functions:** `count`, `size_hint`, `size_hint_for`
- **Types:** 2/3 matched (target 7)
- **Missing types:** `Item`

### 7. combinations_with_replacement

- **Target:** `itertools.CombinationsWithReplacement`
- **Similarity:** 0.30
- **Dependents:** 1
- **Priority Score:** 1030907.1
- **Functions:** 5/7 matched (target 10)
- **Missing functions:** `nth`, `remaining_for`
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 8. flatten_ok

- **Target:** `itertools.FlattenOk`
- **Similarity:** 0.33
- **Dependents:** 1
- **Priority Score:** 1030806.7
- **Functions:** 4/6 matched (target 11)
- **Missing functions:** `next_back`, `rfold`
- **Types:** 1/2 matched (target 5)
- **Missing types:** `Item`

### 9. powerset

- **Target:** `itertools.Powerset`
- **Similarity:** 0.60
- **Dependents:** 1
- **Priority Score:** 1021004.0
- **Functions:** 7/8 matched (target 13)
- **Missing functions:** `remaining_for`
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 10. zip_longest

- **Target:** `itertools.ZipLongest`
- **Similarity:** 0.33
- **Dependents:** 1
- **Priority Score:** 1020806.8
- **Functions:** 5/6 matched (target 13)
- **Missing functions:** `next_back`
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 11. peeking_take_while

- **Target:** `itertools.PeekingTakeWhile`
- **Similarity:** 0.28
- **Dependents:** 1
- **Priority Score:** 1020707.2
- **Functions:** 3/4 matched (target 16)
- **Missing functions:** `size_hint`
- **Types:** 2/3 matched (target 4)
- **Missing types:** `Item`

### 12. tee

- **Target:** `itertools.Tee`
- **Similarity:** 0.45
- **Dependents:** 1
- **Priority Score:** 1020605.4
- **Functions:** 2/3 matched (target 13)
- **Missing functions:** `new`
- **Types:** 2/3 matched
- **Missing types:** `Item`

### 13. diff

- **Target:** `itertools.Diff`
- **Similarity:** 0.29
- **Dependents:** 1
- **Priority Score:** 1020407.1
- **Functions:** 1/3 matched (target 13)
- **Missing functions:** `fmt`, `clone`
- **Types:** 1/1 matched (target 6)
- **Missing types:** _none_

### 14. adaptors.multi_product

- **Target:** `adaptors.MultiProduct`
- **Similarity:** 0.55
- **Dependents:** 1
- **Priority Score:** 1011004.5
- **Functions:** 6/6 matched (target 22)
- **Missing functions:** _none_
- **Types:** 3/4 matched
- **Missing types:** `Item`

### 15. with_position

- **Target:** `itertools.WithPosition`
- **Similarity:** 0.34
- **Dependents:** 1
- **Priority Score:** 1010706.6
- **Functions:** 4/4 matched (target 17)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 5)
- **Missing types:** `Item`

### 16. take_while_inclusive

- **Target:** `itertools.TakeWhileInclusive`
- **Similarity:** 0.53
- **Dependents:** 1
- **Priority Score:** 1010604.7
- **Functions:** 4/4 matched (target 17)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`

### 17. lib

- **Target:** `itertools.Itertools`
- **Similarity:** 0.37
- **Dependents:** 0
- **Priority Score:** 424206.3
- **Functions:** 100/137 matched (target 183)
- **Missing functions:** `get`, `zip_longest`, `zip_eq`, `tuple_windows`, `circular_tuple_windows`, `tuples`, `tee`, `map_into`, `process_results`, `duplicates`, `duplicates_by`, `unique`, `unique_by`, `take_while_inclusive`, `tuple_combinations`, `array_combinations`, `pad_using`, `with_position`, `next_array`, `collect_array`, `next_tuple`, `collect_tuple`, `format`, `format_with`, `into_group_map`, `into_group_map_by`, `into_grouping_map`, `into_grouping_map_by`, `min_set`, `min_set_by`, `min_set_by_key`, `max_set`, `max_set_by`, `max_set_by_key`, `minmax`, `minmax_by_key`, `minmax_by`
- **Types:** 1/5 matched (target 13)
- **Missing types:** `VecDequeIntoIter`, `VecIntoIter`, `Itertools`, `State`

### 18. adaptors.coalesce

- **Target:** `adaptors.Coalesce`
- **Similarity:** 0.24
- **Dependents:** 0
- **Priority Score:** 182707.6
- **Functions:** 6/11 matched (target 19)
- **Missing functions:** `size_hint`, `fold`, `new`, `coalesce_pair`, `dedup_pair`
- **Types:** 3/16 matched (target 7)
- **Missing types:** `CoalescePredicate`, `Item`, `NoCount`, `WithCount`, `CountItem`, `CItem`, `Coalesce`, `DedupPred2CoalescePred`, `DedupPredicate`, `DedupEq`, `Dedup`, `DedupPredWithCount2CoalescePred`, `DedupWithCount`

### 19. adaptors.mod

- **Target:** `adaptors.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 114410.0
- **Functions:** 22/28 matched (target 59)
- **Missing functions:** `tuple_combinations`, `from`, `test_checked_binomial`, `collect`, `next_back`, `rfold`
- **Types:** 11/16 matched (target 11)
- **Missing types:** `Item`, `TupleCombinations`, `HasCombination`, `Tuple1Combination`, `Combination`
- **Tests:** 0/1 matched

### 20. combinations

- **Target:** `itertools.Combinations`
- **Similarity:** 0.46
- **Dependents:** 0
- **Priority Score:** 82305.4
- **Functions:** 14/18 matched (target 23)
- **Missing functions:** `array_combinations`, `len`, `extract_item`, `new`
- **Types:** 1/5 matched (target 2)
- **Missing types:** `ArrayCombinations`, `CombinationsGeneric`, `PoolIndex`, `Item`

### 21. merge_join

- **Target:** `itertools.MergeJoin`
- **Similarity:** 0.15
- **Dependents:** 0
- **Priority Score:** 61908.5
- **Functions:** 7/9 matched (target 31)
- **Missing functions:** `left`, `right`
- **Types:** 6/10 matched (target 7)
- **Missing types:** `FuncLR`, `T`, `MergeResult`, `Item`

### 22. groupbylazy

- **Target:** `itertools.Groupbylazy`
- **Similarity:** 0.35
- **Dependents:** 0
- **Priority Score:** 52706.5
- **Functions:** 12/14 matched (target 24)
- **Missing functions:** `into_iter`, `drop`
- **Types:** 10/13 matched (target 11)
- **Missing types:** `Key`, `Item`, `IntoIter`
- **Lint issues:** 1

### 23. next_array

- **Target:** `itertools.NextArray`
- **Similarity:** 0.41
- **Dependents:** 0
- **Priority Score:** 51305.9
- **Functions:** 7/11 matched (target 7)
- **Missing functions:** `new`, `drop`, `slice_assume_init_mut`, `tracked_drop`
- **Types:** 1/2 matched
- **Missing types:** `TrackedDrop`
- **Tests:** 3/4 matched

### 24. rciter_impl

- **Target:** `itertools.RcIterImpl`
- **Similarity:** 0.18
- **Dependents:** 0
- **Priority Score:** 50808.2
- **Functions:** 2/5 matched (target 9)
- **Missing functions:** `size_hint`, `next_back`, `into_iter`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Item`, `IntoIter`

### 25. duplicates_impl

- **Target:** `itertools.DuplicatesImpl`
- **Similarity:** 0.61
- **Dependents:** 0
- **Priority Score:** 42203.9
- **Functions:** 9/11 matched (target 27)
- **Missing functions:** `new`, `next_back`
- **Types:** 9/11 matched (target 10)
- **Missing types:** `Item`, `Container`

### 26. format

- **Target:** `itertools.Format`
- **Similarity:** 0.17
- **Dependents:** 0
- **Priority Score:** 40908.3
- **Functions:** 3/6 matched (target 18)
- **Missing functions:** `fmt`, `clone`, `drop`
- **Types:** 2/3 matched (target 4)
- **Missing types:** `PutBackOnDrop`

### 27. adaptors.map

- **Target:** `adaptors.Map`
- **Similarity:** 0.39
- **Dependents:** 0
- **Priority Score:** 31706.1
- **Functions:** 8/9 matched (target 14)
- **Missing functions:** `clone`
- **Types:** 6/8 matched (target 7)
- **Missing types:** `Item`, `Out`

### 28. intersperse

- **Target:** `itertools.Intersperse`
- **Similarity:** 0.50
- **Dependents:** 0
- **Priority Score:** 31105.0
- **Functions:** 6/6 matched (target 24)
- **Missing functions:** _none_
- **Types:** 2/5 matched (target 6)
- **Missing types:** `IntersperseElement`, `Intersperse`, `Item`

### 29. exactly_one_err

- **Target:** `itertools.ExactlyOneErr`
- **Similarity:** 0.24
- **Dependents:** 0
- **Priority Score:** 30807.6
- **Functions:** 4/6 matched (target 17)
- **Missing functions:** `new`, `fmt`
- **Types:** 1/2 matched (target 5)
- **Missing types:** `Item`

### 30. pad_tail

- **Target:** `itertools.PadTail`
- **Similarity:** 0.45
- **Dependents:** 0
- **Priority Score:** 30805.5
- **Functions:** 4/6 matched (target 16)
- **Missing functions:** `next_back`, `rfold`
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 31. kmerge_impl

- **Target:** `itertools.KMergeImpl`
- **Similarity:** 0.56
- **Dependents:** 0
- **Priority Score:** 21404.4
- **Functions:** 8/8 matched (target 18)
- **Missing functions:** _none_
- **Types:** 4/6 matched (target 5)
- **Missing types:** `KMergePredicate`, `Item`

### 32. unique_impl

- **Target:** `itertools.UniqueImpl`
- **Similarity:** 0.29
- **Dependents:** 0
- **Priority Score:** 21007.1
- **Functions:** 6/7 matched (target 17)
- **Missing functions:** `next_back`
- **Types:** 2/3 matched
- **Missing types:** `Item`

### 33. multipeek_impl

- **Target:** `itertools.MultiPeekImpl`
- **Similarity:** 0.59
- **Dependents:** 0
- **Priority Score:** 20904.1
- **Functions:** 6/7 matched (target 11)
- **Missing functions:** `fold`
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 34. iter_index

- **Target:** `itertools.IterIndex`
- **Similarity:** 0.48
- **Dependents:** 0
- **Priority Score:** 20505.2
- **Functions:** 2/2 matched (target 22)
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 8)
- **Missing types:** `Sealed`, `Output`

### 35. grouping_map

- **Target:** `itertools.GroupingMap`
- **Similarity:** 0.64
- **Dependents:** 0
- **Priority Score:** 12503.6
- **Functions:** 20/20 matched (target 34)
- **Missing functions:** _none_
- **Types:** 4/5 matched
- **Missing types:** `Out`

### 36. tuple_impl

- **Target:** `itertools.TupleImpl`
- **Similarity:** 0.53
- **Dependents:** 0
- **Priority Score:** 11604.7
- **Functions:** 9/9 matched (target 70)
- **Missing functions:** _none_
- **Types:** 6/7 matched (target 15)
- **Missing types:** `Item`

### 37. free

- **Target:** `itertools.Free`
- **Similarity:** 0.75
- **Dependents:** 0
- **Priority Score:** 11602.5
- **Functions:** 15/15 matched (target 47)
- **Missing functions:** _none_
- **Types:** 0/1 matched
- **Missing types:** `VecIntoIter`

### 38. process_results_impl

- **Target:** `itertools.ProcessResultsImpl`
- **Similarity:** 0.47
- **Dependents:** 0
- **Priority Score:** 10905.3
- **Functions:** 7/7 matched (target 12)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`

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
- **Similarity:** 0.67
- **Dependents:** 0
- **Priority Score:** 103.3
- **Functions:** 1/1 matched (target 6)
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
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

