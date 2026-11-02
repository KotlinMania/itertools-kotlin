#[cfg(test)]
mod tests {
    use itertools::{Itertools, EitherOrBoth};
    use std::{rc::Rc,cell::Cell};

    fn expected() -> Vec<EitherOrBoth<Option<i32>,Option<&'static str>>> {
        vec![EitherOrBoth::Both(None,None), EitherOrBoth::Both(Some(7),Some("x")), EitherOrBoth::Left(None)]
    }

    #[test]
    fn forward_preserves_present_none_items() {
        let actual: Vec<_> = [None,Some(7),None].into_iter().zip_longest([None,Some("x")]).collect();
        assert_eq!(actual,expected());
        assert_eq!([None::<i32>;0].into_iter().zip_longest([None::<&str>]).collect::<Vec<_>>(),vec![EitherOrBoth::Right(None)]);
    }

    #[test]
    fn peeked_none_payload_remains_a_present_item() {
        let mut iter = [None,Some(7)].into_iter().zip_longest([None::<&str>]).peekable();
        assert_eq!(iter.peek(),Some(&EitherOrBoth::Both(None,None)));
        assert_eq!(iter.peek(),Some(&EitherOrBoth::Both(None,None)));
        assert_eq!(iter.next(),Some(EitherOrBoth::Both(None,None)));
        assert_eq!(iter.next(),Some(EitherOrBoth::Left(Some(7))));
        assert_eq!(iter.next(),None);
        assert_eq!(iter.next(),None);
    }

    #[test]
    fn exhausted_sources_are_fused() {
        let polls = Rc::new(Cell::new(0));
        let counter = polls.clone();
        let reviving = std::iter::from_fn(move || {
            counter.set(counter.get()+1);
            if counter.get()==1 { None } else { Some(Some(7)) }
        });
        let output: Vec<_> = reviving.zip_longest([None,Some("x")]).collect();
        assert_eq!(output,vec![EitherOrBoth::Right(None),EitherOrBoth::Right(Some("x"))]);
        assert_eq!(polls.get(),1);
    }

    #[test]
    fn presence_is_independent_of_inner_option() {
        assert!(EitherOrBoth::<Option<i32>,Option<&str>>::Left(None).has_left());
        assert!(!EitherOrBoth::<Option<i32>,Option<&str>>::Left(None).has_right());
        assert!(EitherOrBoth::<Option<i32>,Option<&str>>::Right(None).has_right());
        assert!(!EitherOrBoth::<Option<i32>,Option<&str>>::Right(None).has_left());
        let both = EitherOrBoth::<Option<i32>,Option<&str>>::Both(None,None);
        assert!(both.has_left());assert!(both.has_right());
    }

    #[test]
    fn folds_keep_all_tags_and_payloads() {
        let folded = [None,Some(7),None].into_iter().zip_longest([None,Some("x")]).fold(Vec::new(),|mut acc,item| {acc.push(item);acc});
        assert_eq!(folded,expected());
        let reversed = [None,Some(7),None].into_iter().zip_longest([None,Some("x")]).rfold(Vec::new(),|mut acc,item| {acc.push(item);acc});
        assert_eq!(reversed,expected().into_iter().rev().collect::<Vec<_>>());
    }
}

#[cfg(test)]
mod peek_predicate_tests {
    use itertools::{peek_nth, PeekingNext};
    use std::panic::{catch_unwind,AssertUnwindSafe};

    #[test]
    fn next_if_preserves_present_none_and_predicate_calls() {
        let mut iterator = peek_nth([None,Some(1)].into_iter());
        let mut calls = 0;
        assert_eq!(iterator.next_if(|item| {calls+=1;item.is_none()}),Some(None));
        assert_eq!(calls,1);
        assert_eq!(iterator.next(),Some(Some(1)));
        assert_eq!(iterator.next(),None);
        let mut iterator = peek_nth([None,Some(1)].into_iter());
        let mut seen = Vec::new();
        assert_eq!(iterator.next_if(|item| {seen.push(*item);false}),None);
        assert_eq!(seen,vec![None]);
        assert_eq!(iterator.next(),Some(None));
        assert_eq!(iterator.next(),Some(Some(1)));
        let mut iterator = peek_nth([None,Some(1)].into_iter());
        assert_eq!(iterator.next_if_eq(&None),Some(None));
        assert_eq!(iterator.next(),Some(Some(1)));
    }

    #[test]
    fn next_if_panic_consumes_the_buffered_item() {
        let mut iterator = peek_nth([1,2,3].into_iter());
        assert_eq!(iterator.peek_nth(1),Some(&2));
        assert!(catch_unwind(AssertUnwindSafe(|| iterator.next_if(|_| panic!("predicate failure")))).is_err());
        assert_eq!(iterator.next(),Some(2));
        assert_eq!(iterator.next(),Some(3));
    }

    #[test]
    fn peeking_next_checks_present_none_without_consuming_rejection() {
        let mut iterator = peek_nth([None,Some(7)].into_iter());
        let mut seen = Vec::new();
        assert_eq!(iterator.peeking_next(|item| {seen.push(*item);false}),None);
        assert_eq!(seen,vec![None]);
        assert_eq!(iterator.next(),Some(None));
        assert_eq!(iterator.next(),Some(Some(7)));
    }

    #[test]
    fn peeking_next_panic_retains_the_buffered_item() {
        let mut iterator = peek_nth([1,2].into_iter());
        assert!(catch_unwind(AssertUnwindSafe(|| iterator.peeking_next(|_| panic!("predicate failure")))).is_err());
        assert_eq!(iterator.next(),Some(1));
        assert_eq!(iterator.next(),Some(2));
    }

    #[test]
    fn exhausted_input_skips_predicates() {
        let mut iterator = peek_nth([None::<i32>;0].into_iter());
        assert_eq!(iterator.next_if(|_| panic!("no item")),None);
        assert_eq!(iterator.peeking_next(|_| panic!("no item")),None);
        assert_eq!(iterator.next(),None);
    }
}

#[cfg(test)]
mod zip_cache_tests {
    use itertools::{Itertools,EitherOrBoth};

    #[test]
    fn prefetched_singleton_remains_available_from_back() {
        let mut iter = [None::<i32>].into_iter().zip_longest([None::<&str>]).peekable();
        assert_eq!(iter.peek(),Some(&EitherOrBoth::Both(None,None)));
        assert_eq!(iter.next_back(),Some(EitherOrBoth::Both(None,None)));
        assert_eq!(iter.peek(),None);
        assert_eq!(iter.next_back(),None);
    }

    #[test]
    fn exact_hints_include_pending_front() {
        let mut iter = [1,2,3].into_iter().zip_longest(["x"]).peekable();
        assert_eq!(iter.size_hint(),(3,Some(3)));
        assert!(iter.peek().is_some());assert!(iter.peek().is_some());
        assert_eq!(iter.size_hint(),(3,Some(3)));
        assert_eq!(iter.next_back(),Some(EitherOrBoth::Left(3)));
        assert_eq!(iter.size_hint(),(2,Some(2)));
        assert_eq!(iter.next(),Some(EitherOrBoth::Both(1,"x")));
        assert_eq!(iter.size_hint(),(1,Some(1)));
        assert!(iter.peek().is_some());
        assert_eq!(iter.size_hint(),(1,Some(1)));
        assert_eq!(iter.next_back(),Some(EitherOrBoth::Left(2)));
        assert_eq!(iter.size_hint(),(0,Some(0)));
        assert_eq!(iter.next(),None);
    }

    #[test]
    fn reverse_fold_includes_prefetched_front() {
        let mut iter = [1,2].into_iter().zip_longest(["x","y","z"]).peekable();
        assert!(iter.peek().is_some());
        let reversed = iter.by_ref().rfold(Vec::new(),|mut acc,item| {acc.push(item);acc});
        assert_eq!(reversed,vec![EitherOrBoth::Right("z"),EitherOrBoth::Both(2,"y"),EitherOrBoth::Both(1,"x")]);
        assert_eq!(iter.peek(),None);
    }

    #[test]
    fn mixed_ends_preserve_nullable_tags() {
        let mut iter = [None,Some(2),None].into_iter().zip_longest([None,Some("x")]).peekable();
        assert!(iter.peek().is_some());
        assert_eq!(iter.next_back(),Some(EitherOrBoth::Left(None)));
        assert_eq!(iter.next(),Some(EitherOrBoth::Both(None,None)));
        assert!(iter.peek().is_some());
        assert_eq!(iter.next_back(),Some(EitherOrBoth::Both(Some(2),Some("x"))));
        assert_eq!(iter.next(),None);assert_eq!(iter.next_back(),None);
    }

    #[test]
    fn unprefetched_null_tail_and_empty_input() {
        let mut iter = [None::<i32>].into_iter().zip_longest([None::<&str>;0]);
        assert_eq!(iter.next_back(),Some(EitherOrBoth::Left(None)));
        assert_eq!(iter.next_back(),None);
        let mut empty = [None::<i32>;0].into_iter().zip_longest([None::<&str>;0]).peekable();
        assert_eq!(empty.peek(),None);assert_eq!(empty.size_hint(),(0,Some(0)));assert_eq!(empty.next_back(),None);
    }
}
