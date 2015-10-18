(ns midje.checking.checkers.t-roughly
  (:use midje.sweet
        [midje.checking.checkers.defining :only [checker?]]
        midje.test-util))

(facts "roughly"
  (fact "is a checker that produces checkers"
    #'roughly => checker?
    roughly => checker?
    (roughly 3) => checker?
    (roughly 3 1) => checker?)

  (fact "allows an explicit range"
    0.99 =not=> (roughly 2.0 1.0)
    3.01 =not=> (roughly 2.0 1.0)

    0.00 => (roughly 1.0 1.0)
    2.00 => (roughly 1.0 1.0))

  (fact "provides an implicit range if needed"
    998.999 =not=> (roughly 1000)
    999.001 => (roughly 1000)
    1000.990 => (roughly 1000)
    1001.001 =not=> (roughly 1000))

  (fact "works with negative numbers"
    -1 => (roughly -1)
    -1.00001 => (roughly -1)
    -0.99999 => (roughly -1)

    -1 => (roughly -1 0.1)
    -0.90001 => (roughly -1 0.1)
    -1.00001 => (roughly -1 0.1))

  (fact "no longer has an old bug to do with collections"
    [-0.16227766016837952 6.16227766016838]
    => (just (roughly -0.1622) (roughly 6.1622)))

  (fact "non-numbers produces a falsey result"
    ((roughly 12) nil) => false
    ((roughly 12) "ba") => false))
