(ns ^{:doc "Functions grabbed from newer versions of Clojure, 
            so we can maintain backwards compatibility."}
  midje.clojure.backwards-compatibility)

(letfn [(missing? [sym] (not (ns-resolve 'clojure.core sym)))]

  (defmacro defn-once [sym & rest]
    (when (missing? sym)
      `(defn ~sym ~@rest)))

  (defmacro defn-once-in-core [sym & rest]
    (when (missing? sym)
      `(intern 'clojure.core '~sym (fn ~@rest))))

  (defmacro move-once-to-core [source-namespace syms]
    (when (missing? (first syms))
      `(do
         (require '~source-namespace)
         (doseq [sym# '~syms]
           (intern 'clojure.core sym# (ns-resolve '~source-namespace sym#)))))))


(move-once-to-core slingshot.ex-info [ex-info ex-data])
(move-once-to-core midje.clojure.extra-core [cond-> cond->> as-> some-> some->>])
