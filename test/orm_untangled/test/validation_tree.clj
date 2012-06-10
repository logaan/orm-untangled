(ns orm_untangled.test.validation_tree
  (:use orm_untangled.validation_tree)
  (:use     [clojure.test])
  (:import  [orm_untangled.validation_tree Join Chain Simple]))

(def fail (Simple. [:foo] (fn [d] true)  "fail"))
(def other-fail (Simple. [:foo] (fn [d] true)  "other-fail"))
(def pass (Simple. [:foo] (fn [d] false) "pass"))
(def data {:foo true})

(deftest simples
  (are [errors validator] (= errors (validate validator data))
    ["fail"]               fail
    []                     pass

    ["fail" "fail"]        (Join. fail fail)
    ["fail" "fail" "fail"] (Join. (Join. fail fail) fail)

    ["fail"]               (Chain. fail other-fail)
    ["fail"]               (Chain. (Chain. fail other-fail) other-fail)

    ["fail" "other-fail"]  (Join. (Chain. pass fail) other-fail)
    ["fail"]               (Chain. (Join. fail other-fail) other-fail)))

