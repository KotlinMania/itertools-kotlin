# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 48/50 (96.0%)
- **Function parity:** 372/543 matched (target 907) — 68.5%
- **Class/type parity:** 113/161 matched (target 209) — 70.2%
- **Combined symbol parity:** 485/704 matched (target 1116) — 68.9%
- **Average inline-code cosine:** 0.16 (function body across 48 matched files)
- **Average documentation cosine:** 0.66 (doc text across 48 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 47 files with <0.60 function similarity
- **Needs Review:** 0 files with 0.60-0.84 function similarity
- **Excellent:** 1 files with >=0.85 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

### 1. size_hint
- **Similarity:** 0.45 (needs 40% improvement)
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
- **Similarity:** 0.45
- **Dependents:** 15
- **Priority Score:** 15000905.0
- **Functions:** 8/8 matched (target 15)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 2. lazy_buffer

- **Target:** `itertools.LazyBuffer`
- **Similarity:** 0.17
- **Dependents:** 3
- **Priority Score:** 3011108.2
- **Functions:** 9/9 matched (target 19)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Output`

### 3. either_or_both

- **Target:** `itertools.EitherOrBoth`
- **Similarity:** 0.15
- **Dependents:** 3
- **Priority Score:** 3003708.5
- **Functions:** 36/36 matched (target 45)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_

### 4. peek_nth

- **Target:** `itertools.PeekNth`
- **Similarity:** 0.13
- **Dependents:** 2
- **Priority Score:** 2011308.6
- **Functions:** 11/11 matched (target 14)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 5. repeatn

- **Target:** `itertools.RepeatN`
- **Similarity:** 0.24
- **Dependents:** 2
- **Priority Score:** 2010807.6
- **Functions:** 6/6 matched (target 8)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Item`

### 6. peeking_take_while

- **Target:** `itertools.PeekingTakeWhile`
- **Similarity:** 0.07
- **Dependents:** 1
- **Priority Score:** 1051209.2
- **Functions:** 5/9 matched (target 12)
- **Missing functions:** `peeking_next`, `peeking_next`, `peeking_next`, `peeking_next`
- **Types:** 2/3 matched
- **Missing types:** `Item`

### 7. adaptors.multi_product

- **Target:** `adaptors.MultiProduct`
- **Similarity:** 0.09
- **Dependents:** 1
- **Priority Score:** 1011009.1
- **Functions:** 6/6 matched (target 22)
- **Missing functions:** _none_
- **Types:** 3/4 matched
- **Missing types:** `Item`

### 8. powerset

- **Target:** `itertools.Powerset`
- **Similarity:** 0.29
- **Dependents:** 1
- **Priority Score:** 1011007.1
- **Functions:** 8/8 matched (target 13)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 9. combinations_with_replacement

- **Target:** `itertools.CombinationsWithReplacement`
- **Similarity:** 0.10
- **Dependents:** 1
- **Priority Score:** 1010909.0
- **Functions:** 7/7 matched (target 14)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 10. permutations

- **Target:** `itertools.Permutations`
- **Similarity:** 0.18
- **Dependents:** 1
- **Priority Score:** 1010908.2
- **Functions:** 6/6 matched (target 15)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 7)
- **Missing types:** `Item`

### 11. flatten_ok

- **Target:** `itertools.FlattenOk`
- **Similarity:** 0.06
- **Dependents:** 1
- **Priority Score:** 1010809.4
- **Functions:** 6/6 matched (target 15)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 4)
- **Missing types:** `Item`

### 12. zip_longest

- **Target:** `itertools.ZipLongest`
- **Similarity:** 0.06
- **Dependents:** 1
- **Priority Score:** 1010809.4
- **Functions:** 6/6 matched (target 16)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 13. with_position

- **Target:** `itertools.WithPosition`
- **Similarity:** 0.05
- **Dependents:** 1
- **Priority Score:** 1010709.4
- **Functions:** 4/4 matched (target 17)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 5)
- **Missing types:** `Item`

### 14. take_while_inclusive

- **Target:** `itertools.TakeWhileInclusive`
- **Similarity:** 0.10
- **Dependents:** 1
- **Priority Score:** 1010609.0
- **Functions:** 4/4 matched (target 16)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`

### 15. tee

- **Target:** `itertools.Tee`
- **Similarity:** 0.12
- **Dependents:** 1
- **Priority Score:** 1010608.8
- **Functions:** 3/3 matched (target 15)
- **Missing functions:** _none_
- **Types:** 2/3 matched
- **Missing types:** `Item`

### 16. diff

- **Target:** `itertools.Diff`
- **Similarity:** 0.17
- **Dependents:** 1
- **Priority Score:** 1000408.3
- **Functions:** 3/3 matched (target 15)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 6)
- **Missing types:** _none_

### 17. merge_join

- **Target:** `itertools.MergeJoin`
- **Similarity:** 0.10
- **Dependents:** 0
- **Priority Score:** 153309.0
- **Functions:** 11/23 matched (target 22)
- **Missing functions:** `left`, `right`, `merge`, `left`, `right`, `merge`, `size_hint`, `left`, `right`, `merge`, `size_hint`, `size_hint`
- **Types:** 7/10 matched (target 7)
- **Missing types:** `T`, `MergeResult`, `Item`

### 18. groupbylazy

- **Target:** `itertools.Groupbylazy`
- **Similarity:** 0.11
- **Dependents:** 0
- **Priority Score:** 113808.9
- **Functions:** 18/25 matched (target 28)
- **Missing functions:** `call_mut`, `step`, `drop_group`, `step`, `drop_group`, `next`, `next`
- **Types:** 9/13 matched (target 10)
- **Missing types:** `Key`, `Item`, `IntoIter`, `Chunks`

### 19. adaptors.coalesce

- **Target:** `adaptors.Coalesce`
- **Similarity:** 0.18
- **Dependents:** 0
- **Priority Score:** 53108.2
- **Functions:** 13/15 matched (target 36)
- **Missing functions:** `dedup_pair`, `coalesce_pair`
- **Types:** 13/16 matched (target 17)
- **Missing types:** `Item`, `CountItem`, `CItem`

### 20. unique_impl

- **Target:** `itertools.UniqueImpl`
- **Similarity:** 0.05
- **Dependents:** 0
- **Priority Score:** 51409.5
- **Functions:** 7/11 matched (target 19)
- **Missing functions:** `next`, `size_hint`, `count`, `next_back`
- **Types:** 2/3 matched
- **Missing types:** `Item`

### 21. adaptors.map

- **Target:** `adaptors.Map`
- **Similarity:** 0.08
- **Dependents:** 0
- **Priority Score:** 21809.2
- **Functions:** 10/10 matched (target 15)
- **Missing functions:** _none_
- **Types:** 6/8 matched (target 7)
- **Missing types:** `Item`, `Out`

### 22. kmerge_impl

- **Target:** `itertools.KMergeImpl`
- **Similarity:** 0.15
- **Dependents:** 0
- **Priority Score:** 21708.5
- **Functions:** 10/11 matched (target 18)
- **Missing functions:** `kmerge_pred`
- **Types:** 5/6 matched
- **Missing types:** `Item`

### 23. next_array

- **Target:** `itertools.NextArray`
- **Similarity:** 0.13
- **Dependents:** 0
- **Priority Score:** 21408.7
- **Functions:** 10/12 matched (target 11)
- **Missing functions:** `tracked_drop`, `drop`
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 3/5 matched

### 24. intersperse

- **Target:** `itertools.Intersperse`
- **Similarity:** 0.15
- **Dependents:** 0
- **Priority Score:** 21208.5
- **Functions:** 6/7 matched (target 24)
- **Missing functions:** `generate`
- **Types:** 4/5 matched (target 8)
- **Missing types:** `Item`

### 25. iter_index

- **Target:** `itertools.IterIndex`
- **Similarity:** 0.16
- **Dependents:** 0
- **Priority Score:** 21008.4
- **Functions:** 7/7 matched (target 22)
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 8)
- **Missing types:** `Sealed`, `Output`

### 26. exactly_one_err

- **Target:** `itertools.ExactlyOneErr`
- **Similarity:** 0.08
- **Dependents:** 0
- **Priority Score:** 20909.2
- **Functions:** 6/7 matched (target 19)
- **Missing functions:** `fmt`
- **Types:** 1/2 matched (target 5)
- **Missing types:** `Item`

### 27. rciter_impl

- **Target:** `itertools.RcIterImpl`
- **Similarity:** 0.13
- **Dependents:** 0
- **Priority Score:** 20808.8
- **Functions:** 5/5 matched (target 13)
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Item`, `IntoIter`

### 28. duplicates_impl

- **Target:** `itertools.DuplicatesImpl`
- **Similarity:** 0.08
- **Dependents:** 0
- **Priority Score:** 12609.2
- **Functions:** 15/15 matched (target 30)
- **Missing functions:** _none_
- **Types:** 10/11 matched (target 12)
- **Missing types:** `Item`

### 29. grouping_map

- **Target:** `itertools.GroupingMap`
- **Similarity:** 0.13
- **Dependents:** 0
- **Priority Score:** 12508.7
- **Functions:** 20/20 matched (target 30)
- **Missing functions:** _none_
- **Types:** 4/5 matched
- **Missing types:** `Out`

### 30. combinations

- **Target:** `itertools.Combinations`
- **Similarity:** 0.20
- **Dependents:** 0
- **Priority Score:** 12408.0
- **Functions:** 19/19 matched (target 40)
- **Missing functions:** _none_
- **Types:** 4/5 matched (target 7)
- **Missing types:** `Item`

### 31. tuple_impl

- **Target:** `itertools.TupleImpl`
- **Similarity:** 0.11
- **Dependents:** 0
- **Priority Score:** 12208.9
- **Functions:** 15/15 matched (target 70)
- **Missing functions:** _none_
- **Types:** 6/7 matched (target 15)
- **Missing types:** `Item`

### 32. process_results_impl

- **Target:** `itertools.ProcessResultsImpl`
- **Similarity:** 0.09
- **Dependents:** 0
- **Priority Score:** 10909.1
- **Functions:** 7/7 matched (target 12)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`

### 33. multipeek_impl

- **Target:** `itertools.MultiPeekImpl`
- **Similarity:** 0.18
- **Dependents:** 0
- **Priority Score:** 10908.2
- **Functions:** 7/7 matched (target 12)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 34. pad_tail

- **Target:** `itertools.PadTail`
- **Similarity:** 0.11
- **Dependents:** 0
- **Priority Score:** 10808.9
- **Functions:** 6/6 matched (target 17)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 35. sources

- **Target:** `itertools.Sources`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 10808.1
- **Functions:** 5/5 matched (target 12)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 5)
- **Missing types:** `Item`

### 36. put_back_n_impl

- **Target:** `itertools.PutBackNImpl`
- **Similarity:** 0.11
- **Dependents:** 0
- **Priority Score:** 10708.9
- **Functions:** 5/5 matched (target 16)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Item`

### 37. zip_eq_impl

- **Target:** `itertools.ZipEqImpl`
- **Similarity:** 0.09
- **Dependents:** 0
- **Priority Score:** 10509.1
- **Functions:** 3/3 matched (target 14)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Item`

### 38. free

- **Target:** `itertools.Free`
- **Similarity:** 0.30
- **Dependents:** 0
- **Priority Score:** 1607.0
- **Functions:** 15/15 matched (target 50)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_

### 39. format

- **Target:** `itertools.Format`
- **Similarity:** 0.09
- **Dependents:** 0
- **Priority Score:** 1209.1
- **Functions:** 9/9 matched (target 20)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 5)
- **Missing types:** _none_

### 40. k_smallest

- **Target:** `itertools.KSmallest`
- **Similarity:** 0.40
- **Dependents:** 0
- **Priority Score:** 506.0
- **Functions:** 5/5 matched (target 20)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 41. cons_tuples_impl

- **Target:** `itertools.ConsTuplesImpl`
- **Similarity:** 0.09
- **Dependents:** 0
- **Priority Score:** 309.1
- **Functions:** 1/1 matched (target 8)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_

### 42. minmax

- **Target:** `itertools.MinMax`
- **Similarity:** 0.15
- **Dependents:** 0
- **Priority Score:** 308.5
- **Functions:** 2/2 matched (target 14)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_

### 43. ziptuple

- **Target:** `itertools.Ziptuple`
- **Similarity:** 0.03
- **Dependents:** 0
- **Priority Score:** 209.7
- **Functions:** 1/1 matched (target 26)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_

### 44. unziptuple

- **Target:** `itertools.UnzipTuple`
- **Similarity:** 0.04
- **Dependents:** 0
- **Priority Score:** 209.6
- **Functions:** 1/1 matched (target 8)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_

### 45. group_map

- **Target:** `itertools.GroupMap`
- **Similarity:** 0.29
- **Dependents:** 0
- **Priority Score:** 207.1
- **Functions:** 2/2 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 46. extrema_set

- **Target:** `itertools.ExtremaSet`
- **Similarity:** 0.36
- **Dependents:** 0
- **Priority Score:** 206.4
- **Functions:** 2/2 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 47. concat_impl

- **Target:** `itertools.ConcatImpl`
- **Similarity:** 0.05
- **Dependents:** 0
- **Priority Score:** 109.5
- **Functions:** 1/1 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 48. impl_macros

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

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `adaptors.mod` | `adaptors.Mod` | 0 | `adaptors/mod.rs` | `adaptors/Mod.kt` |

