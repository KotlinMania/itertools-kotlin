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
