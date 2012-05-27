(ns orm-untangled.test.validations
  (:use [orm-untangled.validation-types]
        [orm-untangled.validations]
        [clojure.test]))

(def creation-validations
  (chain
    (present "Name" :name)
    (length_in 2 6 "Name" :name)))

(deftest no-errors
  (are [act exp] (= act exp)
    (validate {:name "Logan"} creation-validations) '()
    (validate {:age "24"} creation-validations)     '("Name is required.")))
