(ns midje.checking.checkers.roughly
  "Prepackaged functions that perform common checks."
  (:use commons.clojure.core
        midje.checking.core
        [midje.checking.checkers.defining :only [as-checker checker defchecker]]))

(def roughly-equal?)

(defn number-roughly-equal?
  [expected actual tolerance]
  (and (>= expected (-' actual tolerance))
       (<= expected (+' actual tolerance))))

(defn coll-roughly-equal?
  [compare-val compared-val tolerance]
  (if (not= (count compare-val) (count compared-val))
    false
    (loop [remain-compare-val compare-val
         remain-compared-val compared-val
         eq true]
    (cond (not eq) false
          (empty? remain-compare-val) true
          :else (recur
                 (rest remain-compare-val)
                 (rest remain-compared-val)
                 (roughly-equal? (first remain-compare-val) (first remain-compared-val) tolerance))))))

(defn comparable?
  [a1 a2]
  (or (=  (type a1) (type a2))
      (and (number? a1) (number? a2))))

(defn roughly-equal?
  [expected actual tolerance]
  (cond (not (comparable? expected actual)) false
        (number? expected) (number-roughly-equal? expected actual tolerance)
        (coll? expected) (coll-roughly-equal? expected actual tolerance)))

(letfn [(abs [n]
          (if (pos? n)
            n
            (-' n)))] ;; -' not strictly necessary, but...
  (defchecker roughly
    ([expected tolerance]
       (checker [actual]
                (roughly-equal? expected actual tolerance)))
    ([expected]
       (roughly expected (abs (*' 0.001 expected))))))
