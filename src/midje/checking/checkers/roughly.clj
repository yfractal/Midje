(ns midje.checking.checkers.roughly
  "Prepackaged functions that perform common checks."
  (:use commons.clojure.core
        midje.checking.core
        [midje.checking.checkers.defining :only [as-checker checker defchecker]]))


(letfn [(abs [n]
          (if (pos? n)
            n
            (-' n)))] ;; -' not strictly necessary, but...

  (defchecker roughly
    "With two arguments, accepts a value within delta of the
     expected value. With one argument, the delta is 1/1000th
     of the expected value."
    ([expected delta]
       (checker [actual]
         (and (number? actual)
              (>= expected (-' actual delta))
              (<= expected (+' actual delta)))))
    ([expected]
       (roughly expected (abs (*' 0.001 expected))))))
