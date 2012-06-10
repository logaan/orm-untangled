(ns orm_untangled.test.validation_tree_map
  (:use [orm_untangled.validation_tree_map]
        [clojure.test]))

(def fail (simple [:foo] (fn [d] true)  "fail"))
(def other-fail (simple [:foo] (fn [d] true)  "other-fail"))
(def pass (simple [:foo] (fn [d] false) "pass"))
(def data {:foo true})

(deftest simples
  (are [errors validator] (= errors (validate validator data))
    ["fail"]               fail
    []                     pass

    ["fail" "fail"]        (join fail fail)
    ["fail" "fail" "fail"] (join (join fail fail) fail)

    ["fail"]               (chain fail other-fail)
    ["fail"]               (chain (chain fail other-fail) other-fail)

    ["fail" "other-fail"]  (join (chain pass fail) other-fail)
    ["fail"]               (chain (join fail other-fail) other-fail)))

