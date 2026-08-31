# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 50/50 (100.0%)
- **Function parity:** 494/504 matched (target 1318) — 98.0%
- **Class/type parity:** 126/177 matched (target 251) — 71.2%
- **Combined symbol parity:** 620/681 matched (target 1569) — 91.0%
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

- **Target:** `itertools.SizeHint`
- **Similarity:** 0.75
- **Dependents:** 15
- **Priority Score:** 15000903.0
- **Functions:** 8/8 matched (target 15)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 2. either_or_both

- **Target:** `itertools.EitherOrBoth`
- **Similarity:** 0.57
- **Dependents:** 3
- **Priority Score:** 3003604.2
- **Functions:** 35/35 matched (target 45)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_

### 3. lazy_buffer

- **Target:** `itertools.LazyBuffer`
- **Similarity:** 0.50
- **Dependents:** 3
- **Priority Score:** 3001105.0
- **Functions:** 9/9 matched (target 19)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_

### 4. peek_nth

- **Target:** `itertools.PeekNth`
- **Similarity:** 0.61
- **Dependents:** 2
- **Priority Score:** 2011203.9
- **Functions:** 10/10 matched (target 14)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`

### 5. repeatn

- **Target:** `itertools.RepeatN`
- **Similarity:** 0.41
- **Dependents:** 2
- **Priority Score:** 2010805.9
- **Functions:** 6/6 matched (target 8)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 6. adaptors.multi_product

- **Target:** `adaptors.MultiProduct`
- **Similarity:** 0.55
- **Dependents:** 1
- **Priority Score:** 1011004.5
- **Functions:** 6/6 matched (target 22)
- **Missing functions:** _none_
- **Types:** 3/4 matched
- **Missing types:** `Item`

### 7. powerset

- **Target:** `itertools.Powerset`
- **Similarity:** 0.69
- **Dependents:** 1
- **Priority Score:** 1011003.1
- **Functions:** 8/8 matched (target 13)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 8. combinations_with_replacement

- **Target:** `itertools.CombinationsWithReplacement`
- **Similarity:** 0.50
- **Dependents:** 1
- **Priority Score:** 1010905.1
- **Functions:** 7/7 matched (target 14)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 9. permutations

- **Target:** `itertools.Permutations`
- **Similarity:** 0.54
- **Dependents:** 1
- **Priority Score:** 1010904.6
- **Functions:** 6/6 matched (target 15)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 7)
- **Missing types:** `Item`

### 10. zip_longest

- **Target:** `itertools.ZipLongest`
- **Similarity:** 0.40
- **Dependents:** 1
- **Priority Score:** 1010806.0
- **Functions:** 6/6 matched (target 16)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 11. flatten_ok

- **Target:** `itertools.FlattenOk`
- **Similarity:** 0.53
- **Dependents:** 1
- **Priority Score:** 1010804.8
- **Functions:** 6/6 matched (target 18)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 5)
- **Missing types:** `Item`

### 12. peeking_take_while

- **Target:** `itertools.PeekingTakeWhile`
- **Similarity:** 0.37
- **Dependents:** 1
- **Priority Score:** 1010706.3
- **Functions:** 4/4 matched (target 18)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 4)
- **Missing types:** `Item`

### 13. with_position

- **Target:** `itertools.WithPosition`
- **Similarity:** 0.39
- **Dependents:** 1
- **Priority Score:** 1010706.1
- **Functions:** 4/4 matched (target 17)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 5)
- **Missing types:** `Item`

### 14. take_while_inclusive

- **Target:** `itertools.TakeWhileInclusive`
- **Similarity:** 0.53
- **Dependents:** 1
- **Priority Score:** 1010604.7
- **Functions:** 4/4 matched (target 16)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`

### 15. tee

- **Target:** `itertools.Tee`
- **Similarity:** 0.57
- **Dependents:** 1
- **Priority Score:** 1010604.4
- **Functions:** 3/3 matched (target 15)
- **Missing functions:** _none_
- **Types:** 2/3 matched
- **Missing types:** `Item`

### 16. diff

- **Target:** `itertools.Diff`
- **Similarity:** 0.49
- **Dependents:** 1
- **Priority Score:** 1000405.1
- **Functions:** 3/3 matched (target 15)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 6)
- **Missing types:** _none_

### 17. groupbylazy

- **Target:** `itertools.Groupbylazy`
- **Similarity:** 0.35
- **Dependents:** 0
- **Priority Score:** 52706.5
- **Functions:** 12/14 matched (target 24)
- **Missing functions:** `into_iter`, `drop`
- **Types:** 10/13 matched (target 11)
- **Missing types:** `Key`, `Item`, `IntoIter`

### 18. adaptors.coalesce

- **Target:** `adaptors.Coalesce`
- **Similarity:** 0.52
- **Dependents:** 0
- **Priority Score:** 52704.8
- **Functions:** 11/11 matched (target 36)
- **Missing functions:** _none_
- **Types:** 11/16 matched (target 15)
- **Missing types:** `CoalescePredicate`, `Item`, `CountItem`, `CItem`, `DedupPredicate`

### 19. next_array

- **Target:** `itertools.NextArray`
- **Similarity:** 0.41
- **Dependents:** 0
- **Priority Score:** 51305.9
- **Functions:** 7/11 matched (target 7)
- **Missing functions:** `new`, `drop`, `slice_assume_init_mut`, `tracked_drop`
- **Types:** 1/2 matched
- **Missing types:** `TrackedDrop`
- **Tests:** 3/4 matched

### 20. lib

- **Target:** `itertools.Itertools`
- **Similarity:** 0.53
- **Dependents:** 0
- **Priority Score:** 44204.7
- **Functions:** 137/137 matched (target 254)
- **Missing functions:** _none_
- **Types:** 2/5 matched (target 14)
- **Missing types:** `VecDequeIntoIter`, `VecIntoIter`, `Itertools`

### 21. duplicates_impl

- **Target:** `itertools.DuplicatesImpl`
- **Similarity:** 0.61
- **Dependents:** 0
- **Priority Score:** 42203.9
- **Functions:** 9/11 matched (target 27)
- **Missing functions:** `new`, `next_back`
- **Types:** 9/11 matched (target 10)
- **Missing types:** `Item`, `Container`

### 22. merge_join

- **Target:** `itertools.MergeJoin`
- **Similarity:** 0.23
- **Dependents:** 0
- **Priority Score:** 31907.7
- **Functions:** 9/9 matched (target 33)
- **Missing functions:** _none_
- **Types:** 7/10 matched (target 8)
- **Missing types:** `T`, `MergeResult`, `Item`

### 23. adaptors.map

- **Target:** `adaptors.Map`
- **Similarity:** 0.39
- **Dependents:** 0
- **Priority Score:** 31706.1
- **Functions:** 8/9 matched (target 14)
- **Missing functions:** `clone`
- **Types:** 6/8 matched (target 7)
- **Missing types:** `Item`, `Out`

### 24. kmerge_impl

- **Target:** `itertools.KMergeImpl`
- **Similarity:** 0.54
- **Dependents:** 0
- **Priority Score:** 21404.6
- **Functions:** 8/8 matched (target 18)
- **Missing functions:** _none_
- **Types:** 4/6 matched (target 5)
- **Missing types:** `KMergePredicate`, `Item`

### 25. intersperse

- **Target:** `itertools.Intersperse`
- **Similarity:** 0.50
- **Dependents:** 0
- **Priority Score:** 21105.0
- **Functions:** 6/6 matched (target 24)
- **Missing functions:** _none_
- **Types:** 3/5 matched (target 7)
- **Missing types:** `IntersperseElement`, `Item`

### 26. exactly_one_err

- **Target:** `itertools.ExactlyOneErr`
- **Similarity:** 0.33
- **Dependents:** 0
- **Priority Score:** 20806.7
- **Functions:** 5/6 matched (target 18)
- **Missing functions:** `fmt`
- **Types:** 1/2 matched (target 5)
- **Missing types:** `Item`

### 27. rciter_impl

- **Target:** `itertools.RcIterImpl`
- **Similarity:** 0.55
- **Dependents:** 0
- **Priority Score:** 20804.5
- **Functions:** 5/5 matched (target 13)
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Item`, `IntoIter`

### 28. iter_index

- **Target:** `itertools.IterIndex`
- **Similarity:** 0.48
- **Dependents:** 0
- **Priority Score:** 20505.2
- **Functions:** 2/2 matched (target 22)
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 8)
- **Missing types:** `Sealed`, `Output`

### 29. grouping_map

- **Target:** `itertools.GroupingMap`
- **Similarity:** 0.64
- **Dependents:** 0
- **Priority Score:** 12503.6
- **Functions:** 20/20 matched (target 30)
- **Missing functions:** _none_
- **Types:** 4/5 matched
- **Missing types:** `Out`

### 30. combinations

- **Target:** `itertools.Combinations`
- **Similarity:** 0.67
- **Dependents:** 0
- **Priority Score:** 12303.3
- **Functions:** 18/18 matched (target 40)
- **Missing functions:** _none_
- **Types:** 4/5 matched (target 7)
- **Missing types:** `Item`

### 31. tuple_impl

- **Target:** `itertools.TupleImpl`
- **Similarity:** 0.53
- **Dependents:** 0
- **Priority Score:** 11604.7
- **Functions:** 9/9 matched (target 70)
- **Missing functions:** _none_
- **Types:** 6/7 matched (target 15)
- **Missing types:** `Item`

### 32. free

- **Target:** `itertools.Free`
- **Similarity:** 0.75
- **Dependents:** 0
- **Priority Score:** 11602.5
- **Functions:** 15/15 matched (target 50)
- **Missing functions:** _none_
- **Types:** 0/1 matched
- **Missing types:** `VecIntoIter`

### 33. unique_impl

- **Target:** `itertools.UniqueImpl`
- **Similarity:** 0.30
- **Dependents:** 0
- **Priority Score:** 11007.0
- **Functions:** 7/7 matched (target 19)
- **Missing functions:** _none_
- **Types:** 2/3 matched
- **Missing types:** `Item`

### 34. process_results_impl

- **Target:** `itertools.ProcessResultsImpl`
- **Similarity:** 0.47
- **Dependents:** 0
- **Priority Score:** 10905.3
- **Functions:** 7/7 matched (target 12)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`

### 35. multipeek_impl

- **Target:** `itertools.MultiPeekImpl`
- **Similarity:** 0.63
- **Dependents:** 0
- **Priority Score:** 10903.7
- **Functions:** 7/7 matched (target 12)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 36. pad_tail

- **Target:** `itertools.PadTail`
- **Similarity:** 0.59
- **Dependents:** 0
- **Priority Score:** 10804.1
- **Functions:** 6/6 matched (target 17)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 37. put_back_n_impl

- **Target:** `itertools.PutBackNImpl`
- **Similarity:** 0.51
- **Dependents:** 0
- **Priority Score:** 10704.9
- **Functions:** 5/5 matched (target 15)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 38. sources

- **Target:** `itertools.Sources`
- **Similarity:** 0.57
- **Dependents:** 0
- **Priority Score:** 10704.3
- **Functions:** 4/4 matched (target 12)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 5)
- **Missing types:** `Item`

### 39. zip_eq_impl

- **Target:** `itertools.ZipEqImpl`
- **Similarity:** 0.29
- **Dependents:** 0
- **Priority Score:** 10507.1
- **Functions:** 3/3 matched (target 14)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`

### 40. adaptors.mod

- **Target:** `adaptors.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 4410.0
- **Functions:** 28/28 matched (target 152)
- **Missing functions:** _none_
- **Types:** 16/16 matched (target 29)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 41. format

- **Target:** `itertools.Format`
- **Similarity:** 0.53
- **Dependents:** 0
- **Priority Score:** 904.7
- **Functions:** 6/6 matched (target 20)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 5)
- **Missing types:** _none_

### 42. k_smallest

- **Target:** `itertools.KSmallest`
- **Similarity:** 0.78
- **Dependents:** 0
- **Priority Score:** 502.2
- **Functions:** 5/5 matched (target 20)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 43. minmax

- **Target:** `itertools.MinMax`
- **Similarity:** 0.54
- **Dependents:** 0
- **Priority Score:** 304.6
- **Functions:** 2/2 matched (target 14)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_

### 44. cons_tuples_impl

- **Target:** `itertools.ConsTuplesImpl`
- **Similarity:** 0.71
- **Dependents:** 0
- **Priority Score:** 302.9
- **Functions:** 1/1 matched (target 8)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_

### 45. unziptuple

- **Target:** `itertools.UnzipTuple`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 208.1
- **Functions:** 1/1 matched (target 8)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_

### 46. ziptuple

- **Target:** `itertools.Ziptuple`
- **Similarity:** 0.20
- **Dependents:** 0
- **Priority Score:** 208.0
- **Functions:** 1/1 matched (target 25)
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
- **Functions:** 2/2 matched
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

- **Target:** `itertools.ImplMacros`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
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

